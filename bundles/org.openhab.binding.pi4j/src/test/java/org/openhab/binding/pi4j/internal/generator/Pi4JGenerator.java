/**
 * Copyright (c) 2010-2022 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.pi4j.internal.generator;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.reflections.Reflections;

import com.hubspot.jinjava.Jinjava;
import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinPullResistance;

/**
 * @author Sascha Volkenandt - Initial contribution
 */
@NonNullByDefault
public class Pi4JGenerator {

    private static final Set<PinMode> SUPPORTED_PIN_MODES = Set.of(PinMode.DIGITAL_INPUT, PinMode.DIGITAL_OUTPUT);

    private static final Jinjava jinjava = new Jinjava();

    public static void main(String[] args) {
        generate();
    }

    private static void generate() {
        var devices = scanClasspathForDevices().collect(Collectors.toUnmodifiableList());
        devices.forEach(Pi4JGenerator::generateDevice);
        generateBinding(devices);
    }

    private static void generateDevice(Device device) {
        var bindings = DeviceBindingsBuilder.build(device);
        generateFile("template-config.xml.j2", "src/main/resources/OH-INF/config/" + device.getId() + "-config.xml",
                bindings);
        generateFile("template-thing.xml.j2", "src/main/resources/OH-INF/thing/" + device.getId() + "-thing.xml",
                bindings);
        generateFile("TemplateGpioProviderHandler.java.j2", "src/main/java/org/openhab/binding/pi4j/internal/handler/"
                + device.getJavaId() + "GpioProviderHandler.java", bindings);
    }

    private static void generateBinding(List<Device> devices) {
        var bindings = Map.of("devices", devices);
        generateFile("TemplatePi4JHandlerFactory.java.j2",
                "src/main/java/org/openhab/binding/pi4j/internal/Pi4JHandlerFactory.java", bindings);
    }

    static void generateFile(String inputTemplate, String outputFile, Map<String, ?> bindings) {
        var jinjaTemplate = loadJinjaTemplate(inputTemplate);

        var path = Path.of(outputFile);
        try {
            Files.createDirectories(path.getParent());
            try (var out = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
                out.write(jinjava.render(jinjaTemplate, bindings));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error generating " + path, e);
        }
    }

    private static String loadJinjaTemplate(String resource) {
        try (var input = Pi4JGenerator.class.getResourceAsStream(resource)) {
            if (input == null) {
                throw new IOException("Resource does not exist");
            }
            return new String(input.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Error reading template " + resource, e);
        }
    }

    static Stream<PinMode> findSupportedPinModes(Device device) {
        return Arrays.stream(device.getPins()).flatMap(pin -> pin.getSupportedPinModes().stream())
                .filter(SUPPORTED_PIN_MODES::contains).distinct().sorted();
    }

    static Stream<PinPullResistance> findSupportedPullResistances(Device device) {
        return Arrays.stream(device.getPins()).flatMap(pin -> pin.getSupportedPinPullResistance().stream()).distinct()
                .sorted();
    }

    static String typeId(PinMode pinMode) {
        switch (pinMode) {
            case DIGITAL_INPUT:
                return "input";
            case DIGITAL_OUTPUT:
                return "output";
            default:
                throw new UnsupportedOperationException("Unsupported pin mode " + pinMode);
        }
    }

    private static Stream<Device> scanClasspathForDevices() {
        var reflections = new Reflections("com.pi4j");
        return reflections.getSubTypesOf(GpioProvider.class).stream()
                .filter(clazz -> !clazz.isInterface() && !Modifier.isAbstract(clazz.getModifiers()))
                .map(Pi4JGenerator::scanDevice).filter(Optional::isPresent).map(Optional::get);
    }

    private static Optional<Device> scanDevice(Class<? extends GpioProvider> clazz) {
        if (!clazz.getName().endsWith("GpioProvider")) {
            throw new UnsupportedOperationException("Invalid GpioProvider class name: " + clazz.getName());
        }
        return Device.Type.fromGpioProviderClass(clazz).map(type -> scanDevice(clazz, type));
    }

    private static Device scanDevice(Class<? extends GpioProvider> clazz, Device.Type type) {
        var prefix = clazz.getName().substring(0, clazz.getName().length() - 12);
        var id = clazz.getSimpleName().substring(0, clazz.getSimpleName().length() - 12).toLowerCase(Locale.ROOT);
        var name = scanDeviceName(clazz);
        var pinsField = findAllPinsField(prefix);
        var pins = introspectField(pinsField, Pin[].class);
        return new Device(clazz, type, id, name, pinsField, pins);
    }

    private static String scanDeviceName(Class<? extends GpioProvider> clazz) {
        var description = introspectField(clazz, "DESCRIPTION", String.class);
        if (description.endsWith(" GPIO Provider")) {
            return description.substring(0, description.length() - 14);
        }
        if (description.endsWith(" PWM Provider")) {
            return description.substring(0, description.length() - 13);
        }
        throw new UnsupportedOperationException("Invalid GpioProvider DESCRIPTION: " + description);
    }

    private static Class<?> findClass(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            throw new UnsupportedOperationException("Missing class " + name);
        }
    }

    private static Field findAllPinsField(String prefix) {
        var clazz = findClass(prefix + "Pin");
        return Arrays.stream(clazz.getFields())
                .filter(field -> "ALL".equals(field.getName()) || "ALL_PINS".equals(field.getName())).findFirst()
                .orElseThrow(() -> new UnsupportedOperationException(
                        "Neither ALL nor ALL_PINS in Pin class " + clazz.getSimpleName()));
    }

    private static <T> T introspectField(Class<?> clazz, String name, Class<T> resultClass) {
        try {
            return introspectField(clazz.getField(name), resultClass);
        } catch (NoSuchFieldException e) {
            throw new UnsupportedOperationException("Missing field " + name + " in class " + clazz.getSimpleName());
        }
    }

    private static <T> T introspectField(Field field, Class<T> resultClass) {
        try {
            return resultClass.cast(field.get(null));
        } catch (IllegalAccessException | ClassCastException e) {
            throw new UnsupportedOperationException(
                    "Invalid field " + field.getName() + " in class " + field.getDeclaringClass().getSimpleName());
        }
    }
}

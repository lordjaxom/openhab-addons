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

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.i2c.I2CBus;

/**
 * @author Sascha Volkenandt - Initial contribution
 */
@NonNullByDefault
public class Device {

    private final Class<? extends GpioProvider> provider;
    private final Type type;
    private final String id;
    private final String name;
    private final Field pinsField;
    private final Pin[] pins;

    public Device(Class<? extends GpioProvider> provider, Type type, String id, String name, Field pinsField,
            Pin[] pins) {
        this.provider = provider;
        this.type = type;
        this.id = id;
        this.name = name;
        this.pinsField = pinsField;
        this.pins = pins;
    }

    public String getProviderName() {
        return provider.getName();
    }

    public String getProviderSimpleName() {
        return provider.getSimpleName();
    }

    public Type getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public String getJavaId() {
        return id.toUpperCase(Locale.ROOT);
    }

    public String getName() {
        return name;
    }

    public String getPinClassName() {
        return pinsField.getDeclaringClass().getName();
    }

    public String getPinsFieldName() {
        return pinsField.getDeclaringClass().getSimpleName() + "." + pinsField.getName();
    }

    public Pin[] getPins() {
        return pins;
    }

    @NonNullByDefault
    public enum Type {
        I2C("I²C") {
            @Override
            boolean isOfType(Class<? extends GpioProvider> clazz) {
                return Arrays.stream(clazz.getConstructors())
                        .anyMatch(constructor -> constructor.getParameterTypes().length > 0
                                && constructor.getParameterTypes()[0] == I2CBus.class);
            }
        };

        public static Optional<Type> fromGpioProviderClass(Class<? extends GpioProvider> clazz) {
            return Arrays.stream(values()).filter(value -> value.isOfType(clazz)).findFirst();
        }

        private final String name;

        Type(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        abstract boolean isOfType(Class<? extends GpioProvider> clazz);
    }
}

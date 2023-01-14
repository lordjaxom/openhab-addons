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

import static org.openhab.binding.pi4j.internal.generator.Pi4JGenerator.findSupportedPinModes;
import static org.openhab.binding.pi4j.internal.generator.Pi4JGenerator.findSupportedPullResistances;
import static org.openhab.binding.pi4j.internal.generator.Pi4JGenerator.typeId;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinPullResistance;

/**
 * @author Sascha Volkenandt - Initial contribution
 */
@NonNullByDefault
public class DeviceBindingsBuilder {

    public static Map<String, ?> build(Device device) {
        return Map.of("device", device, "channels", buildChannels(device));
    }

    private static List<Map<String, ?>> buildChannels(Device device) {
        return findSupportedPinModes(device).map(pinMode -> buildChannel(device, pinMode))
                .collect(Collectors.toUnmodifiableList());
    }

    private static Map<String, ?> buildChannel(Device device, PinMode pinMode) {
        return Map.of("type", typeId(pinMode), "name", device.getId() + "_" + typeId(pinMode), "pins",
                buildChannelPins(device, pinMode), "pullModes", buildChannelPullModes(device, pinMode));
    }

    private static List<Pin> buildChannelPins(Device device, PinMode pinMode) {
        return Arrays.stream(device.getPins()).filter(pin -> pin.getSupportedPinModes().contains(pinMode))
                .collect(Collectors.toUnmodifiableList());
    }

    private static List<PinPullResistance> buildChannelPullModes(Device device, PinMode pinMode) {
        if (pinMode == PinMode.DIGITAL_INPUT) {
            return findSupportedPullResistances(device).collect(Collectors.toUnmodifiableList());
        }
        return List.of();
    }
}

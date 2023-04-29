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
package org.openhab.binding.pi4j.internal.config;

import java.util.Objects;
import java.util.OptionalDouble;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

/**
 * The {@link ChannelConfig} class contains fields mapping thing configuration parameters.
 *
 * @author Sascha Volkenandt - Initial contribution
 */
@NonNullByDefault
public class ChannelConfig {

    int pin = 0;
    @Nullable
    Double defaultValue;
    boolean invert = false;

    public int getPin() {
        return pin;
    }

    public OptionalDouble getDefaultValue() {
        return defaultValue != null ? OptionalDouble.of(Objects.requireNonNull(defaultValue)) : OptionalDouble.empty();
    }

    public boolean isInvert() {
        return invert;
    }

    @Override
    public String toString() {
        return "{"
                + Stream.of(Stream.of("pin=" + pin),
                        getDefaultValue().stream().mapToObj(value -> "defaultValue=" + value),
                        Stream.of("invert=" + invert)).flatMap(Function.identity()).collect(Collectors.joining(", "))
                + "}";
    }
}

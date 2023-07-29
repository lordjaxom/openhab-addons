/**
 * Copyright (c) 2010-2023 Contributors to the openHAB project
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
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

/**
 * The {@link GpioProviderConfig} class contains fields mapping thing configuration parameters.
 *
 * @author Sascha Volkenandt - Initial contribution
 */
@NonNullByDefault
public class GpioProviderConfig {

    @Nullable
    String address;
    @Nullable
    Integer busNumber;

    public OptionalInt getAddress() {
        return Optional.ofNullable(address).stream().mapToInt(value -> Integer.parseInt(value, 16)).findFirst();
    }

    public OptionalInt getBusNumber() {
        return busNumber != null ? OptionalInt.of(Objects.requireNonNull(busNumber)) : OptionalInt.empty();
    }

    @Override
    public String toString() {
        return "{" + Stream
                .of(Stream.ofNullable(address).map(value -> "address=" + value),
                        getBusNumber().stream().mapToObj(value -> "busNumber=" + value))
                .flatMap(Function.identity()).collect(Collectors.joining(", ")) + "}";
    }
}

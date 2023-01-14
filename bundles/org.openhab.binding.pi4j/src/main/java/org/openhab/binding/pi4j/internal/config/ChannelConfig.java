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

import java.util.Optional;
import java.util.OptionalDouble;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;

/**
 * The {@link ChannelConfig} class contains fields mapping thing configuration parameters.
 *
 * @author Sascha Volkenandt - Initial contribution
 */
@NonNullByDefault
public class ChannelConfig {

    int pin = 0;
    @Nullable
    PinPullResistance pullMode;
    @Nullable
    PinState initialState;
    @Nullable
    Double defaultValue;
    boolean invert = false;

    public int getPin() {
        return pin;
    }

    public Optional<PinPullResistance> getPullMode() {
        return Optional.ofNullable(pullMode);
    }

    public Optional<PinState> getInitialState() {
        return Optional.ofNullable(initialState);
    }

    public OptionalDouble getDefaultValue() {
        return defaultValue != null ? OptionalDouble.of(defaultValue) : OptionalDouble.empty();
    }

    public boolean isInvert() {
        return invert;
    }

    @Override
    public String toString() {
        return "{pin=" + pin + (pullMode != null ? ", pullMode=" + pullMode.name() : "")
                + (initialState != null ? ", initialState=" + initialState.name() : "") + ", invert=" + invert + "}";
    }
}

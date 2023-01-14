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

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.pi4j.io.gpio.PinPullResistance;

/**
 * The {@link InputChannelConfig} class contains fields mapping thing configuration parameters.
 *
 * @author Sascha Volkenandt - Initial contribution
 */
@NonNullByDefault
public class InputChannelConfig {

    int pin = 0;
    String pullMode = "";
    boolean invert = false;

    public int getPin() {
        return pin;
    }

    public Optional<PinPullResistance> getPinPullResistance() {
        return Optional.ofNullable(!pullMode.isEmpty() ? PinPullResistance.valueOf(pullMode) : null);
    }

    public boolean isInvert() {
        return invert;
    }

    @Override
    public String toString() {
        return "{" + "pin=" + pin + ", pullMode='" + pullMode + '\'' + ", invert=" + invert + '}';
    }
}

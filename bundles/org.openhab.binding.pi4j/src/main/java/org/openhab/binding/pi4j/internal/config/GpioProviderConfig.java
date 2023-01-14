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

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * The {@link GpioProviderConfig} class contains fields mapping thing configuration parameters.
 *
 * @author Sascha Volkenandt - Initial contribution
 */
@NonNullByDefault
public class GpioProviderConfig {

    String address = "";
    int busNumber = 0;

    public int getAddress() {
        return Integer.parseInt(address, 16);
    }

    public int getBusNumber() {
        return busNumber;
    }

    @Override
    public String toString() {
        return "{address='" + address + "', busNumber=" + busNumber + "}";
    }
}

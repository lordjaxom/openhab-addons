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
package org.openhab.binding.pi4j.internal.device;

import java.io.IOException;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.pi4j.internal.config.GpioProviderConfig;

import com.pi4j.gpio.extension.ads.ADS1015GpioProvider;
import com.pi4j.gpio.extension.ads.ADS1015Pin;
import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.i2c.I2CFactory;

/**
 * The {@link ADS1015GpioProviderDevice}.
 *
 * @author Sascha Volkenandt - Initial contribution
 */
@NonNullByDefault
public class ADS1015GpioProviderDevice implements GpioProviderDevice {

    @Override
    public String getName() {
        return "ADS1015";
    }

    @Override
    public Pin getPin(int index) {
        return ADS1015Pin.ALL[index];
    }

    @Override
    public GpioProvider newGpioProvider(GpioProviderConfig config)
            throws IOException, I2CFactory.UnsupportedBusNumberException {
        return new ADS1015GpioProvider(config.getBusNumber().orElseThrow(), config.getAddress().orElseThrow());
    }
}

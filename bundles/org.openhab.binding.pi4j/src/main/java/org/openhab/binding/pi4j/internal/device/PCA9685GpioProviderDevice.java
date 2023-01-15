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

import com.pi4j.gpio.extension.pca.PCA9685GpioProvider;
import com.pi4j.gpio.extension.pca.PCA9685Pin;
import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.i2c.I2CFactory;

/**
 * The {@link PCA9685GpioProviderDevice}.
 *
 * @author Sascha Volkenandt - Initial contribution
 */
@NonNullByDefault
public class PCA9685GpioProviderDevice implements GpioProviderDevice {

    @Override
    public String getName() {
        return "PCA9685";
    }

    @Override
    public Pin getPin(int index) {
        return PCA9685Pin.ALL[index];
    }

    @Override
    public GpioProvider newGpioProvider(GpioProviderConfig config)
            throws IOException, I2CFactory.UnsupportedBusNumberException {
        return new PCA9685GpioProvider(config.getBusNumber().orElseThrow(), config.getAddress().orElseThrow());
    }
}

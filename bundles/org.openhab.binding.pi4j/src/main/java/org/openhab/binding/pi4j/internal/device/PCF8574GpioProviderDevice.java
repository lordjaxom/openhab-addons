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
package org.openhab.binding.pi4j.internal.device;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.pi4j.internal.config.GpioProviderConfig;
import org.openhab.binding.pi4j.internal.legacy.GpioProvider;
import org.openhab.binding.pi4j.internal.legacy.PCF8574GpioProvider;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.i2c.I2C;
import com.pi4j.io.i2c.I2CConfig;
import com.pi4j.io.i2c.I2CProvider;
import com.pi4j.plugin.linuxfs.provider.i2c.LinuxFsI2CProvider;

/**
 * The {@link PCF8574GpioProviderDevice}.
 *
 * @author Sascha Volkenandt - Initial contribution
 */
@NonNullByDefault
public class PCF8574GpioProviderDevice implements GpioProviderDevice {

    @Override
    public String getName() {
        return "PCF8574";
    }

    @Override
    public GpioProvider newGpioProvider(GpioProviderConfig config) {
        Context pi4j = Pi4J.newContext();
        I2CProvider i2cProvider = LinuxFsI2CProvider.newInstance();
        I2CConfig i2cConfig = I2C.newConfigBuilder(pi4j).id(getName()) //
                .bus(config.getBusNumber().orElseThrow()) //
                .device(config.getAddress().orElseThrow()) //
                .build();
        return new PCF8574GpioProvider(i2cProvider.create(i2cConfig));
    }
}

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
package org.openhab.binding.pi4j.internal.handler;

import java.io.IOException;

import com.pi4j.gpio.extension.pcf.PCF8574Pin;
import com.pi4j.io.gpio.Pin;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.pi4j.internal.config.GpioProviderConfig;
import org.openhab.core.thing.Thing;

import com.pi4j.gpio.extension.pcf.PCF8574GpioProvider;
import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.i2c.I2CFactory;

/**
 * The {@link Pcf8574GpioProviderHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Sascha Volkenandt - Initial contribution
 */
@NonNullByDefault
public class Pcf8574GpioProviderHandler extends BaseGpioProviderHandler {

    public Pcf8574GpioProviderHandler(Thing thing) {
        super(thing, "PCF8574");
    }

    @Override
    public Pin getPin(int index) {
        return PCF8574Pin.ALL[index];
    }

    @Override
    protected GpioProvider newGpioProvider(GpioProviderConfig config)
            throws IOException, I2CFactory.UnsupportedBusNumberException {
        return new PCF8574GpioProvider(config.getBusNumber(), config.getAddress());
    }
}

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
package org.openhab.binding.pi4j.internal.legacy;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.pi4j.io.i2c.I2C;

/**
 * The {@link PCF8574GpioProvider}.
 *
 * @author Sascha Volkenandt - Initial contribution
 */
@NonNullByDefault
public class PCF8574GpioProvider implements GpioProvider {

    private final I2C device;
    private final @Nullable GpioPinDigitalInput[] inputPins = new GpioPinDigitalInput[8];

    private @Nullable ScheduledFuture<?> future;

    public PCF8574GpioProvider(I2C device) {
        this.device = device;
    }

    @Override
    public GpioPinDigitalInput provisionDigitalInputPin(int pin, String id) {
        var gpioPin = new GpioPinDigitalInput(pin);
        inputPins[pin] = gpioPin;
        return gpioPin;
    }

    @Override
    public void unprovisionPin(GpioPinDigitalInput gpioPin) {
        inputPins[gpioPin.getPin()] = null;
    }

    @Override
    public synchronized void start(ScheduledExecutorService scheduler) {
        if (future == null) {
            device.write(0xff);
            future = scheduler.scheduleWithFixedDelay(this::poll, 1000, 100, TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public synchronized void stop() {
        if (future != null) {
            future.cancel(false);
            future = null;
        }
    }

    private void poll() {
        byte data = device.readByte();
        for (int i = 0; i < 8; ++i) {
            if (inputPins[i] != null) {
                boolean value = (data & (1 << i)) != 0;
                inputPins[i].update(value);
            }
        }
    }
}

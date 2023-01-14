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
package org.openhab.binding.pi4j.internal.channel;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.pi4j.internal.device.GpioProviderDevice;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.thing.Channel;
import org.openhab.core.types.Command;
import org.openhab.core.types.RefreshType;

import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinAnalogOutput;
import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.gpio.event.GpioPinListenerAnalog;

/**
 * The {@link AnalogOutputChannelState}.
 *
 * @author Sascha Volkenandt - Initial contribution
 */
@NonNullByDefault
public class AnalogOutputChannelState extends BaseChannelState {

    private final GpioPinAnalogOutput gpioPin;

    public AnalogOutputChannelState(GpioProviderDevice device, Channel channel, GpioProvider provider) {
        super(device, channel);

        gpioPin = GpioFactory.getInstance().provisionAnalogOutputPin(provider, device.getPin(config.getPin()),
                channel.getUID().getId(), config.getDefaultValue().orElseThrow());
        gpioPin.addListener((GpioPinListenerAnalog) event -> updateChannel());
        updateChannel();
    }

    @Override
    public void dispose() {
        GpioFactory.getInstance().unprovisionPin(gpioPin);
    }

    @Override
    public void handleCommand(Command command) {
        if (command instanceof RefreshType) {
            updateChannel();
        }
        if (command instanceof DecimalType) {
            updateOutput((DecimalType) command);
        }
    }

    private void updateChannel() {
        updateStateListener.accept(channelUID, new DecimalType(gpioPin.getValue()));
    }

    private void updateOutput(DecimalType command) {
        gpioPin.setValue(command.doubleValue());
    }
}

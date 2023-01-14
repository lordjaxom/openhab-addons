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
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.thing.Channel;
import org.openhab.core.types.Command;
import org.openhab.core.types.RefreshType;

import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

/**
 * The {@link DigitalOutputChannelState}.
 *
 * @author Sascha Volkenandt - Initial contribution
 */
@NonNullByDefault
class DigitalOutputChannelState extends BaseChannelState {

    private final GpioPinDigitalOutput gpioPin;

    public DigitalOutputChannelState(GpioProviderDevice handler, Channel channel, GpioProvider gpioProvider) {
        super(handler, channel);

        gpioPin = GpioFactory.getInstance().provisionDigitalOutputPin(gpioProvider, handler.getPin(config.getPin()),
                channel.getUID().getId(), config.getInitialState().orElseThrow());
        gpioPin.addListener((GpioPinListenerDigital) event -> updateChannel());
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
        } else if (command instanceof OnOffType) {
            updateOutput((OnOffType) command);
        }
    }

    private void updateChannel() {
        var state = (config.isInvert() ^ gpioPin.isHigh()) ? OnOffType.ON : OnOffType.OFF;
        updateStateListener.accept(channelUID, state);
    }

    private void updateOutput(OnOffType value) {
        gpioPin.setState(config.isInvert() ^ value == OnOffType.ON);
    }
}

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
import org.openhab.binding.pi4j.internal.legacy.GpioPinDigitalInput;
import org.openhab.binding.pi4j.internal.legacy.GpioProvider;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.thing.Channel;

/**
 * The {@link DigitalInputChannelState}.
 *
 * @author Sascha Volkenandt - Initial contribution
 */
@NonNullByDefault
class DigitalInputChannelState extends BaseChannelState {

    private final GpioPinDigitalInput gpioPin;
    private final GpioProvider provider;

    DigitalInputChannelState(GpioProviderDevice device, Channel channel, GpioProvider provider) {
        super(device, channel);
        this.provider = provider;

        gpioPin = provider.provisionDigitalInputPin(config.getPin(), channel.getUID().getId());
        gpioPin.setListener(this::updateChannel);
        updateChannel();
    }

    @Override
    public void dispose() {
        provider.unprovisionPin(gpioPin);
    }

    @Override
    protected void updateChannel() {
        var state = (config.isInvert() ^ gpioPin.isHigh()) ? OpenClosedType.OPEN : OpenClosedType.CLOSED;
        updateStateListener.accept(channelUID, state);
    }
}

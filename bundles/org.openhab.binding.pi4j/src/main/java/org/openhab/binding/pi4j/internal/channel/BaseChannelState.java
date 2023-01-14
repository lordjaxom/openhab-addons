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

import java.util.Optional;

import com.pi4j.io.gpio.GpioProvider;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.pi4j.internal.Pi4JBindingConstants;
import org.openhab.binding.pi4j.internal.config.ChannelConfig;
import org.openhab.binding.pi4j.internal.handler.BaseGpioProviderHandler;
import org.openhab.core.thing.Channel;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link BaseChannelState}.
 *
 * @author Sascha Volkenandt - Initial contribution
 */
@NonNullByDefault
public abstract class BaseChannelState {

    public static Optional<BaseChannelState> newInstance(BaseGpioProviderHandler handler, Channel channel,
            GpioProvider gpioProvider) {
        var channelTypeUID = channel.getChannelTypeUID();
        if (channelTypeUID == null) {
            LoggerFactory.getLogger(BaseChannelState.class).warn("Channel {} has no type", channel.getUID());
            return Optional.empty();
        }

        switch (channelTypeUID.getId()) {
            case Pi4JBindingConstants.CHANNEL_PCF8574_INPUT:
                return Optional.of(new DigitalInputChannelState(handler, channel, gpioProvider));
            case Pi4JBindingConstants.CHANNEL_PCF8574_OUTPUT:
                return Optional.of(new DigitalOutputChannelState(handler, channel, gpioProvider));
            default:
                throw new IllegalStateException("Unexpected value: " + channelTypeUID.getId());
        }
    }

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected final BaseGpioProviderHandler handler;
    protected final ChannelUID channelUID;
    protected final String type;
    protected final ChannelConfig config;

    public BaseChannelState(BaseGpioProviderHandler handler, Channel channel, String type) {
        this.handler = handler;
        this.channelUID = channel.getUID();
        this.type = type;

        config = channel.getConfiguration().as(ChannelConfig.class);

        logger.debug("Initializing {} channel {} with config {}", type, channelUID, config);
    }

    public ChannelUID getUID() {
        return channelUID;
    }

    public abstract void dispose();

    public abstract void handleCommand(Command command);
}

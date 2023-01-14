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
import java.util.function.BiConsumer;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.pi4j.internal.config.ChannelConfig;
import org.openhab.binding.pi4j.internal.device.GpioProviderDevice;
import org.openhab.core.thing.Channel;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.type.ChannelTypeUID;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pi4j.io.gpio.GpioProvider;

/**
 * The {@link BaseChannelState}.
 *
 * @author Sascha Volkenandt - Initial contribution
 */
@NonNullByDefault
public abstract class BaseChannelState {

    public static Optional<BaseChannelState> newInstance(GpioProviderDevice device, Channel channel,
            GpioProvider provider) {
        var channelTypeUID = channel.getChannelTypeUID();
        if (channelTypeUID == null) {
            LoggerFactory.getLogger(BaseChannelState.class).warn("Channel {} has no type", channel.getUID());
            return Optional.empty();
        }
        return Optional.of(newInstance(device, channel, provider, channelTypeUID));
    }

    private static BaseChannelState newInstance(GpioProviderDevice device, Channel channel, GpioProvider provider,
            ChannelTypeUID channelTypeUID) {
        if (channelTypeUID.getId().endsWith("_digital_input")) {
            return new DigitalInputChannelState(device, channel, provider);
        }
        if (channelTypeUID.getId().endsWith("_digital_output")) {
            return new DigitalOutputChannelState(device, channel, provider);
        }
        if (channelTypeUID.getId().endsWith("_analog_input")) {
            return new AnalogInputChannelState(device, channel, provider);
        }
        if (channelTypeUID.getId().endsWith("_analog_output")) {
            return new AnalogOutputChannelState(device, channel, provider);
        }
        throw new IllegalStateException("Unexpected value: " + channelTypeUID.getId());
    }

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected final GpioProviderDevice device;
    protected final ChannelUID channelUID;
    protected final ChannelConfig config;

    protected BiConsumer<ChannelUID, State> updateStateListener = (channelUID, state) -> {
    };

    public BaseChannelState(GpioProviderDevice device, Channel channel) {
        this.device = device;
        this.channelUID = channel.getUID();

        config = channel.getConfiguration().as(ChannelConfig.class);

        logger.debug("Initializing channel {} with config {}", channelUID, config);
    }

    public ChannelUID getUID() {
        return channelUID;
    }

    public void setUpdateStateListener(BiConsumer<ChannelUID, State> updateStateListener) {
        this.updateStateListener = updateStateListener;
    }

    public abstract void dispose();

    public abstract void handleCommand(Command command);
}

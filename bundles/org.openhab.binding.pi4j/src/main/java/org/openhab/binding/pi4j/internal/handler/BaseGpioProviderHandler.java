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
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.pi4j.io.gpio.Pin;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.pi4j.internal.channel.ChannelState;
import org.openhab.binding.pi4j.internal.config.GpioProviderConfig;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.ThingStatusDetail;
import org.openhab.core.thing.binding.BaseThingHandler;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.i2c.I2CFactory;

/**
 * The {@link BaseGpioProviderHandler}.
 *
 * @author Sascha Volkenandt - Initial contribution
 */
@NonNullByDefault
public abstract class BaseGpioProviderHandler extends BaseThingHandler {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    final String name;

    HandlerState handlerState = new UnknownHandlerState();

    public BaseGpioProviderHandler(Thing thing, String name) {
        super(thing);
        this.name = name;
    }

    public void updateChannel(ChannelUID channelUID, State state) {
        logger.debug("Updating channel {} to {}", channelUID, state);
        updateState(channelUID, state);
    }

    @Override
    public void initialize() {
        try {
            handlerState.initialize();
        } catch (ThingStatusException e) {
            logger.error("{}", e.getMessage(), e); // TODO
            updateStatus(e.getStatus(), e.getStatusDetail(), e.getMessage());
        }
    }

    @Override
    public void dispose() {
        handlerState.dispose();
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        handlerState.handleCommand(channelUID, command);
    }

    public abstract Pin getPin(int index);

    protected abstract GpioProvider newGpioProvider(GpioProviderConfig config)
            throws IOException, I2CFactory.UnsupportedBusNumberException;

    private abstract class HandlerState {

        void initialize() throws ThingStatusException {
            logger.error("Unexpected call: {}#initialize", getClass());
        }

        void dispose() {
            logger.error("Unexpected call: {}#dispose", getClass());
        }

        void handleCommand(ChannelUID channelUID, Command command) {
            logger.error("Unexpected call: {}#handleCommand", getClass());
        }
    }

    private class UnknownHandlerState extends HandlerState {

        @Override
        public void initialize() throws ThingStatusException {
            updateStatus(ThingStatus.UNKNOWN);

            logger.debug("Initializing thing {}", thing.getUID());

            var gpioProvider = newGpioProvider();
            var channelStates = thing.getChannels().stream()
                    .map(channel -> ChannelState.newInstance(BaseGpioProviderHandler.this, channel, gpioProvider))
                    .filter(Optional::isPresent).map(Optional::get)
                    .collect(Collectors.toUnmodifiableMap(ChannelState::getUID, Function.identity()));

            updateStatus(ThingStatus.ONLINE);

            handlerState = new OnlineHandlerState(channelStates);
        }

        private GpioProvider newGpioProvider() throws ThingStatusException {
            var config = getConfigAs(GpioProviderConfig.class);
            // TODO: validate

            try {
                logger.debug("Initializing {} provider with config {}", name, config);
                return BaseGpioProviderHandler.this.newGpioProvider(config);
            } catch (I2CFactory.UnsupportedBusNumberException | IOException e) {
                throw new ThingStatusException(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR,
                        "I2C device not accessible: " + e.getMessage(), e);
            }
        }
    }

    private class OnlineHandlerState extends HandlerState {

        private final Map<ChannelUID, ChannelState> channelStates;

        public OnlineHandlerState(Map<ChannelUID, ChannelState> channelStates) {
            this.channelStates = channelStates;
        }

        @Override
        void dispose() {
            logger.debug("Disposing thing {}", thing.getUID());

            channelStates.values().forEach(ChannelState::dispose);

            handlerState = new UnknownHandlerState();
        }

        @Override
        void handleCommand(ChannelUID channelUID, Command command) {
            var channelState = channelStates.get(channelUID);
            if (channelState == null) {
                logger.warn("Received command for unknown channel {}", channelUID);
                return;
            }

            logger.debug("Handling command {} for channel {}", command, channelUID);

            channelState.handleCommand(command);
        }
    }
}

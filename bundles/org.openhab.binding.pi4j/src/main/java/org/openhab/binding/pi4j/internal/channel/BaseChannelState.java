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
import org.openhab.binding.pi4j.internal.handler.BaseGpioProviderHandler;
import org.openhab.core.thing.Channel;
import org.openhab.core.thing.ChannelUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link BaseChannelState}.
 *
 * @author Sascha Volkenandt - Initial contribution
 */
@NonNullByDefault
public abstract class BaseChannelState<C> implements ChannelState {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected final BaseGpioProviderHandler handler;
    protected final ChannelUID channelUID;
    protected final String name;
    protected final C config;

    public BaseChannelState(BaseGpioProviderHandler handler, Channel channel, String name, Class<C> configClass) {
        this.handler = handler;
        this.channelUID = channel.getUID();
        this.name = name;

        config = channel.getConfiguration().as(configClass);

        logger.debug("Initializing {} channel {} with config {}", name, channelUID, config);
    }

    @Override
    public ChannelUID getUID() {
        return channelUID;
    }
}

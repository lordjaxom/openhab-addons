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
package org.openhab.binding.pi4j.internal.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.junit.jupiter.api.Test;
import org.openhab.core.config.core.ConfigParser;

import com.pi4j.io.gpio.PinPullResistance;

/**
 * @author Sascha Volkenandt - Initial contribution
 */
@NonNullByDefault
class ChannelConfigTest {

    @Test
    void parsesCorrectly() {
        var config = ConfigParser.configurationAs(Map.of("pullMode", "PULL_UP"), ChannelConfig.class);
        assertNotNull(config);
        assertEquals(PinPullResistance.PULL_UP, config.pullMode);
    }

    @Test
    void handlesNull() {
        var config = ConfigParser.configurationAs(Map.of(), ChannelConfig.class);
        assertNotNull(config);
        assertNull(config.pullMode);
    }
}

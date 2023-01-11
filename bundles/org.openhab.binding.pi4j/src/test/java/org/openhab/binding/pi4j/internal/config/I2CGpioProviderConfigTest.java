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

import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.junit.jupiter.api.Test;
import org.openhab.core.config.core.ConfigParser;

/**
 * The {@link I2CGpioProviderConfigTest}.
 *
 * @author Sascha Volkenandt - Initial contribution
 */
@NonNullByDefault
class I2CGpioProviderConfigTest {

    @Test
    void parsesCorrectly() {
        var config = ConfigParser.configurationAs(Map.of("address", "24", "busNumber", 2), I2CGpioProviderConfig.class);
        assertNotNull(config);
        assertEquals(0x24, config.getAddress());
        assertEquals(2, config.getBusNumber());
    }
}

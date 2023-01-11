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
package org.openhab.binding.pi4j.internal;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.core.thing.ThingTypeUID;

/**
 * The {@link Pi4JBindingConstants} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Sascha Volkenandt - Initial contribution
 */
@NonNullByDefault
public class Pi4JBindingConstants {

    private static final String BINDING_ID = "pi4j";

    // List of all Thing Type UIDs
    public static final ThingTypeUID THING_TYPE_PCF8574 = new ThingTypeUID(BINDING_ID, "pcf8574");
    public static final ThingTypeUID THING_TYPE_MCP23017 = new ThingTypeUID(BINDING_ID, "mcp23017");

    // List of all Channel ids
    public static final String CHANNEL_PCF8574_INPUT = "pcf8574_input";
    public static final String CHANNEL_PCF8574_OUTPUT = "pcf8574_output";
}

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

import static org.openhab.binding.pi4j.internal.Pi4JBindingConstants.BINDING_ID;

import java.util.Set;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.pi4j.internal.device.MCP23008GpioProviderDevice;
import org.openhab.binding.pi4j.internal.device.MCP23017GpioProviderDevice;
import org.openhab.binding.pi4j.internal.device.PCF8574GpioProviderDevice;
import org.openhab.binding.pi4j.internal.handler.GpioProviderHandler;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingTypeUID;
import org.openhab.core.thing.binding.BaseThingHandlerFactory;
import org.openhab.core.thing.binding.ThingHandler;
import org.openhab.core.thing.binding.ThingHandlerFactory;
import org.osgi.service.component.annotations.Component;

/**
 * The {@link Pi4JHandlerFactory} is responsible for creating things and thing
 * handlers.
 *
 * @author Sascha Volkenandt - Initial contribution
 */
@NonNullByDefault
@Component(configurationPid = "binding.pi4j", service = ThingHandlerFactory.class)
public class Pi4JHandlerFactory extends BaseThingHandlerFactory {
    private static final ThingTypeUID THING_TYPE_MCP23017 = new ThingTypeUID(BINDING_ID, "mcp23017");
    private static final ThingTypeUID THING_TYPE_MCP23008 = new ThingTypeUID(BINDING_ID, "mcp23008");
    private static final ThingTypeUID THING_TYPE_PCF8574 = new ThingTypeUID(BINDING_ID, "pcf8574");

    private static final Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = Set.of(THING_TYPE_MCP23017, THING_TYPE_MCP23008,
            THING_TYPE_PCF8574);

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return SUPPORTED_THING_TYPES_UIDS.contains(thingTypeUID);
    }

    @Override
    protected @Nullable ThingHandler createHandler(Thing thing) {
        ThingTypeUID thingTypeUID = thing.getThingTypeUID();
        if (THING_TYPE_MCP23017.equals(thingTypeUID)) {
            return new GpioProviderHandler(thing, new MCP23017GpioProviderDevice());
        }
        if (THING_TYPE_MCP23008.equals(thingTypeUID)) {
            return new GpioProviderHandler(thing, new MCP23008GpioProviderDevice());
        }
        if (THING_TYPE_PCF8574.equals(thingTypeUID)) {
            return new GpioProviderHandler(thing, new PCF8574GpioProviderDevice());
        }
        return null;
    }
}

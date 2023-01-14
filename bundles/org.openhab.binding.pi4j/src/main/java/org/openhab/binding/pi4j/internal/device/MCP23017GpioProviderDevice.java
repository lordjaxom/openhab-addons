package org.openhab.binding.pi4j.internal.device;

import java.io.IOException;

import com.pi4j.gpio.extension.mcp.MCP23017GpioProvider;
import com.pi4j.gpio.extension.mcp.MCP23017Pin;
import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.i2c.I2CFactory;
import org.openhab.binding.pi4j.internal.config.GpioProviderConfig;

public class MCP23017GpioProviderDevice implements GpioProviderDevice {

    @Override
    public String getName() {
        return "MCP23017";
    }

    @Override
    public Pin getPin(int index) {
        return MCP23017Pin.ALL[index];
    }

    @Override
    public GpioProvider newGpioProvider(GpioProviderConfig config)
            throws IOException, I2CFactory.UnsupportedBusNumberException {
        return new MCP23017GpioProvider(config.getBusNumber(), config.getAddress());
    }
}

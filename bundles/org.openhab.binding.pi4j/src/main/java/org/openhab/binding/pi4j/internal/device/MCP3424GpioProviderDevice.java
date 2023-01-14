package org.openhab.binding.pi4j.internal.device;

import java.io.IOException;

import com.pi4j.gpio.extension.mcp.MCP3424GpioProvider;
import com.pi4j.gpio.extension.mcp.MCP3424Pin;
import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.i2c.I2CFactory;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.pi4j.internal.config.GpioProviderConfig;

@NonNullByDefault
public class MCP3424GpioProviderDevice implements GpioProviderDevice{
    @Override
    public String getName() {
        return "MCP3424";
    }

    @Override
    public Pin getPin(int index) {
        return MCP3424Pin.ALL_PINS[index];
    }

    @Override
    public GpioProvider newGpioProvider(GpioProviderConfig config) throws IOException, I2CFactory.UnsupportedBusNumberException {
        return new MCP3424GpioProvider(config.getBusNumber(), config.getAddress());
    }
}

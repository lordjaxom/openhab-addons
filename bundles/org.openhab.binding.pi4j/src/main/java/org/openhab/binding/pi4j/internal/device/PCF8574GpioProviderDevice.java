package org.openhab.binding.pi4j.internal.device;

import java.io.IOException;

import com.pi4j.gpio.extension.pcf.PCF8574GpioProvider;
import com.pi4j.gpio.extension.pcf.PCF8574Pin;
import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.i2c.I2CFactory;
import org.openhab.binding.pi4j.internal.config.GpioProviderConfig;

public class PCF8574GpioProviderDevice implements GpioProviderDevice {

    @Override
    public String getName() {
        return "PCF8574";
    }

    @Override
    public Pin getPin(int index) {
        return PCF8574Pin.ALL[index];
    }

    @Override
    public GpioProvider newGpioProvider(GpioProviderConfig config)
            throws IOException, I2CFactory.UnsupportedBusNumberException {
        return new PCF8574GpioProvider(config.getBusNumber(), config.getAddress());
    }
}

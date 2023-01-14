package org.openhab.binding.pi4j.internal.device;

import java.io.IOException;

import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.i2c.I2CFactory;
import org.openhab.binding.pi4j.internal.config.GpioProviderConfig;

public interface GpioProviderDevice {

    String getName();

    Pin getPin(int index);

    GpioProvider newGpioProvider(GpioProviderConfig config)
            throws IOException, I2CFactory.UnsupportedBusNumberException;
}

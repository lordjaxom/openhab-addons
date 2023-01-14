package org.openhab.binding.pi4j.internal.channel;

import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinAnalogInput;
import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.gpio.event.GpioPinListenerAnalog;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.pi4j.internal.device.GpioProviderDevice;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.thing.Channel;
import org.openhab.core.types.Command;
import org.openhab.core.types.RefreshType;

@NonNullByDefault
public class AnalogInputChannelState extends BaseChannelState {

    private final GpioPinAnalogInput gpioPin;

    public AnalogInputChannelState(GpioProviderDevice device, Channel channel, GpioProvider provider) {
        super(device, channel);

        gpioPin = GpioFactory.getInstance().provisionAnalogInputPin(provider, device.getPin(config.getPin()),
                channel.getUID().getId());
        gpioPin.addListener((GpioPinListenerAnalog) event -> updateChannel());
        updateChannel();
    }

    @Override
    public void dispose() {
        GpioFactory.getInstance().unprovisionPin(gpioPin);
    }

    @Override
    public void handleCommand(Command command) {
        if (command instanceof RefreshType) {
            updateChannel();
        }
    }

    private void updateChannel() {
        updateStateListener.accept(channelUID, new DecimalType(gpioPin.getValue()));
    }
}

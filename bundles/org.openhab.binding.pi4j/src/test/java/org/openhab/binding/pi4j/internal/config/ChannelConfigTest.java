package org.openhab.binding.pi4j.internal.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.junit.jupiter.api.Test;
import org.openhab.core.config.core.ConfigParser;

import com.pi4j.io.gpio.PinPullResistance;

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

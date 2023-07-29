/**
 * Copyright (c) 2010-2023 Contributors to the openHAB project
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
package org.openhab.binding.pi4j.internal.legacy;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

/**
 * The {@link PCF8574GpioProvider}.
 *
 * @author Sascha Volkenandt - Initial contribution
 */
@NonNullByDefault
public class GpioPinDigitalInput {

    private final int pin;

    private boolean value = false;
    private @Nullable Runnable listener;

    public GpioPinDigitalInput(int pin) {
        this.pin = pin;
    }

    public int getPin() {
        return pin;
    }

    public boolean isHigh() {
        return value;
    }

    public void setListener(Runnable listener) {
        this.listener = listener;
        fireListener();
    }

    void update(boolean value) {
        if (value != this.value) {
            this.value = value;
            fireListener();
        }
    }

    private void fireListener() {
        if (listener != null) {
            listener.run();
        }
    }
}

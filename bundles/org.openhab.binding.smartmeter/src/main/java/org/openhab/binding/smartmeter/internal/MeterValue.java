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
package org.openhab.binding.smartmeter.internal;

import javax.measure.Quantity;
import javax.measure.Unit;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

/**
 * Represents one value of the meter device.
 *
 * @author Matthias Steigenberger - Initial contribution
 *
 */
@NonNullByDefault
public class MeterValue<Q extends Quantity<Q>> {

    private String obis;
    private String value;
    @Nullable
    private Unit<? extends Q> unit;
    @Nullable
    private String status;

    public MeterValue(String obis, String value, @Nullable Unit<? extends Q> unit, @Nullable String status) {
        this.obis = obis;
        this.unit = unit;
        this.value = value;
        this.status = status;
    }

    public MeterValue(String obis, String value, @Nullable Unit<Q> unit) {
        this(obis, value, unit, null);
    }

    /**
     * Gets the values unit.
     *
     * @return the string representation of the values unit - otherwise null.
     */
    public @Nullable Unit<? extends Q> getUnit() {
        return unit;
    }

    /**
     * Gets the value
     *
     * @return the value as String if available - otherwise null.
     */
    public String getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((obis == null) ? 0 : obis.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        result = prime * result + ((unit == null) ? 0 : unit.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        MeterValue<?> other = (MeterValue<?>) obj;
        if (!obis.equals(other.obis)) {
            return false;
        }
        if (status == null) {
            if (other.status != null) {
                return false;
            }
        } else if (!status.equals(other.status)) {
            return false;
        }
        if (unit == null) {
            if (other.unit != null) {
                return false;
            }
        } else if (!unit.equals(other.unit)) {
            return false;
        }
        if (!value.equals(other.value)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "MeterValue [obis=" + obis + ", value=" + value + ", unit=" + unit + "]";
    }

    public String getObisCode() {
        return this.obis;
    }

    public @Nullable String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

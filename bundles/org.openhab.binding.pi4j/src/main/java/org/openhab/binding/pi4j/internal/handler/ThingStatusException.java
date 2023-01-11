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
package org.openhab.binding.pi4j.internal.handler;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.ThingStatusDetail;

/**
 * The {@link ThingStatusException}.
 *
 * @author Sascha Volkenandt - Initial contribution
 */
@NonNullByDefault
public class ThingStatusException extends Exception {

    private final ThingStatus status;
    private final ThingStatusDetail statusDetail;

    public ThingStatusException(ThingStatus status, ThingStatusDetail statusDetail, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
        this.statusDetail = statusDetail;
    }

    public ThingStatusException(ThingStatus status, ThingStatusDetail statusDetail, String message) {
        super(message);
        this.status = status;
        this.statusDetail = statusDetail;
    }

    public ThingStatus getStatus() {
        return status;
    }

    public ThingStatusDetail getStatusDetail() {
        return statusDetail;
    }
}

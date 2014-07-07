/*
 * Copyright (c) 2014 Chris Welty.
 *
 * This is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License, version 3,
 * as published by the Free Software Foundation.
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * For the license, see <http://www.gnu.org/licenses/gpl.html>.
 */

package com.welty.othello.protocol;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.Nullable;

@ToString @EqualsAndHashCode
public class ErrorResponse implements NBoardResponse {
    /**
     * The line of text, received from the engine, that caused the error
     */
    public final String message;

    /**
     * Comment on why the line was in error, or null if no comment
     */
    public final @Nullable String comment;

    public ErrorResponse(String message, @Nullable String comment) {
        this.message = message;
        this.comment = comment;
    }
}

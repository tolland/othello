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

package com.welty.othello.gdk;

import com.orbanova.common.misc.Require;
import com.welty.othello.c.CReader;

import static com.welty.othello.core.Utils.Col;
import static com.welty.othello.core.Utils.Row;
import static com.welty.othello.core.Utils.Square;

/**
 * A GGF-format move
 */
public class OsMove {
    public static final OsMove PASS = new OsMove(-1, -1, true);

    private final boolean fPass;
    private final int row, col;

    public OsMove(int row, int col) {
        this(row, col, false);
    }

    private OsMove(int row, int col, boolean pass) {
        this.row = row;
        this.col = col;
        this.fPass = pass;
    }

    public OsMove(final String text) {
        this(new CReader(text));
    }

    public OsMove(CReader in) {
        final char cCol = Character.toUpperCase(in.read());
        fPass = cCol == 'P';
        if (fPass) {
            while (true) {
                final char c = in.read();
                if (!Character.isLetter(c)) {
                    in.unread(c);
                    break;
                }
            }
            row = 0;
            col = 0;
        } else {
            col = cCol - 'A';
            row = in.read() - '1';
        }
    }

    /**
     * Create a move from an IOS move code
     *
     * @param iosMove integer from 11-88; positive if a black move, negative if a white move
     * @return the move
     */
    public static OsMove ofIos(int iosMove) {
        if (iosMove < 0) {
            iosMove = -iosMove;
        }
        final int row = (iosMove % 10) - 1;
        final int col = (iosMove / 10) - 1;
        return new OsMove(row, col);
    }

    /**
     * @return row number, from 0-7. 0 = top row, 7 = bottom row
     * @throws IllegalStateException if the move is a pass
     */
    public int row() {
        requireOnBoard();
        return row;
    }

    /**
     * @return col number, from 0-7. 0 = left col (col 'A'), 7 = right col (col 'H')
     * @throws IllegalStateException if the move is a pass
     */
    public int col() {
        requireOnBoard();
        return col;
    }

    private void requireOnBoard() {
        if (fPass) {
            throw new IllegalStateException("must not be a pass");
        }
        Require.inRange("Row must be 0-7", row, "row", 0, 7);
        Require.inRange("Col must be 0-7", col, "col", 0, 7);
    }

    /**
     * @return true if the move is a pass
     */
    public boolean isPass() {
        return fPass;
    }

    @Override public String toString() {
        if (fPass)
            return "PA";
        else
            return "" + (char) (col + 'A') + (row + 1);
    }

    @Override public boolean equals(Object obj) {
        if (obj instanceof OsMove) {
            OsMove b = (OsMove) obj;

            if (fPass || b.fPass)
                return fPass && b.fPass;
            else
                return row == b.row && col == b.col;
        }
        return false;
    }

    /**
     * Return a reflection of the move
     * <p/>
     * Passes are returned as passes.
     *
     * @param iReflection reflection index, 0..7. 0 leaves the square unchanged.
     * @return reflected move
     */
    public OsMove reflect(int iReflection) {
        if (fPass || iReflection == 0)
            return this;

        int col = col();
        int row = row();

        if ((iReflection & 4) != 0) {
            int temp = row;
            row = col;
            col = temp;
        }

        if ((iReflection & 2) != 0) {
            col ^= 7;
        }

        if ((iReflection & 1) != 0) {
            row ^= 7;
        }

        return new OsMove(row, col);

    }
}

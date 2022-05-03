/*
 * Copyright 2022 Creek Contributors (https://github.com/creek-service)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.creekservice.api.base.type.json;

/**
 * JSON helper methods.
 *
 * <p>Consider this class private.
 */
public final class Json {

    private static final int ISO_CONTROL_ESCAPE_COUNT = 5;

    private Json() {}

    /**
     * Do an in-place replacement to escape characters in {@code toEscape} starting from {@code
     * startPos}.
     *
     * @param toEscape the SB holding the text to escape
     * @param startPos the position within {@code toEscape} to start the escaping from.
     */
    public static void escapeJson(final StringBuilder toEscape, final int startPos) {
        final int escapeCount = escapeCount(toEscape, startPos);
        if (escapeCount == 0) {
            return;
        }

        final int endPos = toEscape.length() - 1;
        toEscape.setLength(toEscape.length() + escapeCount);

        escape(toEscape, endPos);
    }

    private static int escapeCount(final StringBuilder toEscape, final int startPos) {
        int escapeCount = 0;
        for (int i = startPos; i < toEscape.length(); i++) {
            final char c = toEscape.charAt(i);
            switch (c) {
                case '"':
                case '\\':
                case '\t':
                case '\n':
                case '\r':
                case '\b':
                case '\f':
                    escapeCount++;
                    break;
                default:
                    if (Character.isISOControl(c)) {
                        escapeCount += ISO_CONTROL_ESCAPE_COUNT;
                    }
            }
        }
        return escapeCount;
    }

    @SuppressWarnings("OverflowingLoopIndex")
    private static void escape(final StringBuilder toEscape, final int endPos) {
        int lastPos = toEscape.length() - 1;
        for (int i = endPos; lastPos > i; i--) {
            final char c = toEscape.charAt(i);
            switch (c) {
                case '"':
                case '\\':
                    lastPos = escapeAndDecrement(toEscape, lastPos, c);
                    break;

                case '\t':
                    lastPos = escapeAndDecrement(toEscape, lastPos, 't');
                    break;

                case '\n':
                    lastPos = escapeAndDecrement(toEscape, lastPos, 'n');
                    break;

                case '\r':
                    lastPos = escapeAndDecrement(toEscape, lastPos, 'r');
                    break;

                case '\b':
                    lastPos = escapeAndDecrement(toEscape, lastPos, 'b');
                    break;

                case '\f':
                    lastPos = escapeAndDecrement(toEscape, lastPos, 'f');
                    break;

                default:
                    if (Character.isISOControl(c)) {
                        // ISO control characters are U+00xx, while JSON output format is "\\u00XX"
                        toEscape.setCharAt(lastPos--, upperCaseHex(c & 0xF));
                        toEscape.setCharAt(lastPos--, upperCaseHex((c & 0xF0) >> 4));
                        toEscape.setCharAt(lastPos--, '0');
                        toEscape.setCharAt(lastPos--, '0');
                        toEscape.setCharAt(lastPos--, 'u');
                        toEscape.setCharAt(lastPos--, '\\');
                    } else {
                        toEscape.setCharAt(lastPos, c);
                        lastPos--;
                    }
            }
        }
    }

    private static int escapeAndDecrement(
            final StringBuilder toAppendTo, final int lastPos, final char c) {
        int pos = lastPos;
        toAppendTo.setCharAt(pos--, c);
        toAppendTo.setCharAt(pos--, '\\');
        return pos;
    }

    private static char upperCaseHex(final int digit) {
        if (digit < 0 || digit >= 16) {
            throw new IllegalArgumentException("Invalid hex digit in ISO control character");
        }

        if (digit < 10) {
            return (char) ('0' + digit);
        } else {
            return (char) ('A' + digit - 10);
        }
    }
}

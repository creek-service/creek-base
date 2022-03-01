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

package org.creek.api.base.type.json;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;

import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class JsonTest {

    @Test
    void shouldHandleStringWithNothingToEscape() {
        assertEscaped("hello world", "hello world");
    }

    @Test
    void shouldEscapeQuote() {
        assertEscaped("hello\"world", "hello\\\"world");
        assertEscaped("\"hello\"world\"", "\\\"hello\\\"world\\\"");
    }

    @Test
    void shouldEscapeBackSlash() {
        assertEscaped("hello\\world", "hello\\\\world");
        assertEscaped("\\hello\\world\\", "\\\\hello\\\\world\\\\");
    }

    @Test
    void shouldEscapeTab() {
        assertEscaped("hello\tworld", "hello\\tworld");
        assertEscaped("\thello\tworld\t", "\\thello\\tworld\\t");
    }

    @Test
    void shouldEscapeNewLine() {
        assertEscaped("hello\nworld", "hello\\nworld");
        assertEscaped("\nhello\nworld\n", "\\nhello\\nworld\\n");
    }

    @Test
    void shouldEscapeCarriageReturn() {
        assertEscaped("hello\rworld", "hello\\rworld");
        assertEscaped("\rhello\rworld\r", "\\rhello\\rworld\\r");
    }

    @Test
    void shouldEscapeBreak() {
        assertEscaped("hello\bworld", "hello\\bworld");
        assertEscaped("\bhello\bworld\b", "\\bhello\\bworld\\b");
    }

    @Test
    void shouldEscapeFeed() {
        assertEscaped("hello\fworld", "hello\\fworld");
        assertEscaped("\fhello\fworld\f", "\\fhello\\fworld\\f");
    }

    @Test
    void shouldEscapeIsoControlChars() {
        assertEscaped("hello\u000bworld", "hello\\u000Bworld");
        assertEscaped("\u0000hello\u007fworld\u009f", "\\u0000hello\\u007Fworld\\u009F");
    }

    @ParameterizedTest
    @MethodSource("isoControlChars")
    void shouldEscapeIsoControlChars(final char c) {
        assertEscaped(new StringBuilder(), "" + c, startsWith("\\u00"));
    }

    @ParameterizedTest
    @MethodSource("nonEscapedChars")
    void shouldNotEscapeOtherChars(final char c) {
        assertEscaped("" + c, "" + c);
    }

    public static Stream<Character> nonEscapedChars() {
        return IntStream.range(Character.MIN_VALUE, Character.MAX_VALUE)
                .filter(JsonTest::notEscaped)
                .mapToObj(i -> (Character) (char) i);
    }

    public static Stream<Character> isoControlChars() {
        return IntStream.range(Character.MIN_VALUE, Character.MAX_VALUE)
                .filter(Character::isISOControl)
                .filter(JsonTest::notExplicitlyEscaped)
                .mapToObj(i -> (Character) (char) i);
    }

    private static boolean notEscaped(final int c) {
        return notExplicitlyEscaped(c) && !Character.isISOControl(c);
    }

    private static boolean notExplicitlyEscaped(final int c) {
        switch (c) {
            case '"':
            case '\\':
            case '\t':
            case '\n':
            case '\r':
            case '\b':
            case '\f':
                return false;
            default:
                return true;
        }
    }

    private void assertEscaped(final String input, final String expected) {
        assertEscaped(new StringBuilder(), input, is(expected));
        assertEscaped(new StringBuilder(input), input, is(input + expected));
    }

    private void assertEscaped(
            final StringBuilder sb, final String input, final Matcher<String> matcher) {
        final int startPos = sb.length();
        sb.append(input);
        Json.escapeJson(sb, startPos);
        assertThat(sb.toString(), matcher);
    }
}

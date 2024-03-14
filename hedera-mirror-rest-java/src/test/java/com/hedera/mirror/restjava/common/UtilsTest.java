/*
 * Copyright (C) 2024 Hedera Hashgraph, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hedera.mirror.restjava.common;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.hedera.mirror.restjava.exception.InvalidParametersException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

public class UtilsTest {

    @ParameterizedTest
    @ValueSource(strings = {"3", "65535.000000001", "1.2.3", "0.2.3", "2814792716779530", "" + (Long.MAX_VALUE)})
    @DisplayName("EntityId isValidEntityId tests, positive cases")
    void isValidEntityIdPatternSuccess(String inputId) {
        assertTrue(Utils.isValidEntityIdPattern(inputId));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(
            strings = {
                "",
                "0.1.2.3",
                "-1.0.1",
                "0.-1.1",
                "0.0.-1",
                "100000.65535.000000001",
                "100000.000000001",
                "-1",
                "" + Long.MAX_VALUE + 1
            })
    @DisplayName("EntityId isValidEntityId tests, negative cases")
    void isValidEntityIdPatternFailure(String inputId) {
        assertFalse(Utils.isValidEntityIdPattern(inputId));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(
            strings = {
                "0.1.x",
                "0.1.2.3",
                "a",
                "a.b.c",
                "-1.-1.-1",
                "-1",
                "0.0.-1",
                "0.0.4294967296",
                "32768.65536.4294967296",
                "100000.65535.000000001",
                "100000.000000001",
                "0x",
                "0x00000001000000000000000200000000000000034",
                "0x2540be3f6001fffffffffffff001fffffffffffff",
                "0x10000000000000000000000000000000000000000",
                "2.3.0000000100000000000000020000000000000007"
            })
    @DisplayName("EntityId parse from string tests, negative cases")
    void entityParseFromStringFailure(String inputId) {
        assertThrows(InvalidParametersException.class, () -> Utils.parseId(inputId));
    }

    @Test
    @DisplayName("EntityId parse from string tests")
    void entityParseFromString() {
        assertArrayEquals(Utils.parseId("0.0.0"), new long[] {0, 0, 0});
        assertArrayEquals(Utils.parseId("0"), new long[] {0, 0, 0});
        assertArrayEquals(Utils.parseId("0.0.4294967295"), new long[] {0, 0, 4294967295L});
        assertArrayEquals(Utils.parseId("32767.65535.4294967295"), new long[] {32767, 65535, 4294967295L});
        assertArrayEquals(Utils.parseId("4294967295"), new long[] {0, 0, 4294967295L});
        assertArrayEquals(Utils.parseId("24294967295"), new long[] {0, 5, 2820130815L});
        assertArrayEquals(Utils.parseId("0.1"), new long[] {0, 0, 1});
        assertArrayEquals(Utils.parseId("0x0000000000000000000000000000000000000001"), new long[] {0, 0, 1});
        assertArrayEquals(Utils.parseId("0000000000000000000000000000000000000001"), new long[] {0, 0, 1});
        assertArrayEquals(Utils.parseId("0x0000000100000000000000020000000000000003"), new long[] {1, 2, 3});
        assertArrayEquals(
                Utils.parseId("0x00007fff000000000000ffff00000000ffffffff"), new long[] {32767, 65535, 4294967295L});
        assertArrayEquals(Utils.parseId("0.0.000000000000000000000000000000000186Fb1b"), new long[] {0, 0, 25623323});
        assertArrayEquals(Utils.parseId("0.000000000000000000000000000000000186Fb1b"), new long[] {0, 0, 25623323});
        assertArrayEquals(Utils.parseId("000000000000000000000000000000000186Fb1b"), new long[] {0, 0, 25623323});
        assertArrayEquals(Utils.parseId("0x000000000000000000000000000000000186Fb1b"), new long[] {0, 0, 25623323});
        assertArrayEquals(Utils.parseId("0000000100000000000000020000000000000007"), new long[] {1, 2, 7});
        assertArrayEquals(Utils.parseId("0x0000000100000000000000020000000000000007"), new long[] {1, 2, 7});
        assertArrayEquals(Utils.parseId("1.2.0000000100000000000000020000000000000007"), new long[] {1, 2, 7});
        // Handle null and evm address cases
    }
}

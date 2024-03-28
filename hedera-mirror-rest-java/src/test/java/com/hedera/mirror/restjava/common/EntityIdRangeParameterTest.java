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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.hedera.mirror.common.domain.entity.EntityId;
import com.hedera.mirror.common.exception.InvalidEntityException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class EntityIdRangeParameterTest {

    @Test
    void testConversion() {
        assertThat(new EntityIdRangeParameter(RangeOperator.GTE, EntityId.of("0.0.2000")))
                .isEqualTo(EntityIdRangeParameter.valueOf("gte:0.0.2000"));
        assertThat(new EntityIdRangeParameter(RangeOperator.EQ, EntityId.of("0.0.2000")))
                .isEqualTo(EntityIdRangeParameter.valueOf("0.0.2000"));
        assertThat(EntityIdRangeParameter.EMPTY)
                .isEqualTo(EntityIdRangeParameter.valueOf(""))
                .isEqualTo(EntityIdRangeParameter.valueOf(null));
    }

    @ParameterizedTest
    @ValueSource(
            strings = {
                "0.1.x",
                "0.1.2.3",
                "a",
                "a.b.c",
                "-1.-1.-1",
                "-1",
                "0.0.-1",
                "100000.000000001",
                "eq:0.0.1:someinvalidstring",
                "BLAH:0.0.1",
                "0.0.1:someinvalidstring"
            })
    @DisplayName("EntityIdRangeParameter parse from string tests, negative cases")
    void testInvalidParam(String input) {
        assertThrows(IllegalArgumentException.class, () -> EntityIdRangeParameter.valueOf(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"0.0.4294967296", "32768.65536.4294967296", "100000.65535.000000001"})
    @DisplayName("EntityIdRangeParameter parse from string tests, negative cases for ID having valid format")
    void testInvalidEntity(String input) {
        assertThrows(InvalidEntityException.class, () -> EntityIdRangeParameter.valueOf(input));
    }
}

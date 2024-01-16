/*
 * Copyright (C) 2019-2024 Hedera Hashgraph, LLC
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

package com.hedera.mirror.common.converter;

import static com.hedera.mirror.common.converter.EntityIdSerializer.INSTANCE;

import com.fasterxml.jackson.core.JsonGenerator;
import com.hedera.mirror.common.domain.entity.EntityId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EntityIdSerializerTest {
    @Mock
    JsonGenerator jsonGenerator;

    @Test
    void testNull() throws Exception {
        // when
        INSTANCE.serialize(null, jsonGenerator, null);

        // then
        Mockito.verify(jsonGenerator).writeNull();
    }

    @Test
    void testEmpty() throws Exception {
        // when
        INSTANCE.serialize(EntityId.EMPTY, jsonGenerator, null);

        // then
        Mockito.verify(jsonGenerator).writeNull();
    }

    @Test
    void testEntity() throws Exception {
        // when
        var entity = EntityId.of(10L, 20L, 30L);
        INSTANCE.serialize(entity, jsonGenerator, null);

        // then
        Mockito.verify(jsonGenerator).writeNumber(entity.getId());
    }
}

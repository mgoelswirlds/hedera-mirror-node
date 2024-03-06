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

public class Constants {

    public static final Integer MAX_LIMIT = 100;
    public static final Integer DEFAULT_LIMIT = 25;

    public static final Integer MIN_LIMIT = 1;

    enum EvmAddressType {
        // evm address without shard and realm and with 0x prefix
        NO_SHARD_REALM,
        // evm address with shard and realm as optionals
        OPTIONAL_SHARD_REALM,
        // can be either a NO_SHARD_REALM or OPTIONAL_SHARD_REALM
        ANY,
    };
}

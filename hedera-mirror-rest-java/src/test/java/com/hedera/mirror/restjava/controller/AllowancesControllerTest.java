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

package com.hedera.mirror.restjava.controller;

import com.hedera.mirror.restjava.RestJavaIntegrationTest;
import com.hedera.mirror.restjava.mapper.NftAllowanceMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@RequiredArgsConstructor
class AllowancesControllerTest extends RestJavaIntegrationTest {

    private final AllowancesController allowancesController;
    private final NftAllowanceMapper mapper;

    /*    @SneakyThrows
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void testWithNoQueryParams(boolean owner) {
        var allowance = domainBuilder.nftAllowance().persist();
        var ownerId = String.valueOf(allowance.getOwner());
        var spenderId = String.valueOf(allowance.getSpender());
        NftAllowancesResponse response;

        if (owner) {
            response = allowancesController.getNftAllowancesByAccountId(
                    ownerId, Optional.empty(), Optional.of(owner), Optional.empty(), Optional.of(2), Optional.empty());
        } else {
            response = allowancesController.getNftAllowancesByAccountId(
                    spenderId,
                    Optional.empty(),
                    Optional.of(owner),
                    Optional.empty(),
                    Optional.of(2),
                    Optional.empty());
        }
        assertThat(response.getAllowances()).containsExactlyInAnyOrderElementsOf(mapper.map(List.of(allowance)));
    }

    @SneakyThrows
    @ParameterizedTest
    @ValueSource(strings = {"gte", "gt"})
    void testWithSpenderAndTokenQueryParam(String operator) {
        var allowance = domainBuilder.nftAllowance().persist();
        var allowance1 = domainBuilder
                .nftAllowance()
                .customize(e -> e.owner(allowance.getOwner()))
                .persist();
        Optional<String> accountIdQueryParam = Optional.of(operator + ":" + (allowance.getSpender() - 1));
        Optional<String> tokenIdQueryParam = Optional.of(operator + ":" + (allowance.getTokenId() - 1));

        var response = allowancesController.getNftAllowancesByAccountId(
                String.valueOf(allowance.getOwner()),
                accountIdQueryParam,
                Optional.of(true),
                tokenIdQueryParam,
                Optional.of(2),
                Optional.empty());
        assertThat(response.getAllowances())
                .containsExactlyInAnyOrderElementsOf(mapper.map(List.of(allowance, allowance1)));
    }*/
}

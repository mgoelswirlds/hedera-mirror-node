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

import static org.assertj.core.api.Assertions.assertThat;

import com.hedera.mirror.common.domain.DomainBuilder;
import com.hedera.mirror.common.domain.entity.EntityId;
import com.hedera.mirror.common.domain.entity.NftAllowance;
import com.hedera.mirror.rest.model.NftAllowancesResponse;
import com.hedera.mirror.restjava.RestJavaIntegrationTest;
import com.hedera.mirror.restjava.mapper.NftAllowanceMapper;
import jakarta.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestClient;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AllowancesControllerIntegrationTest extends RestJavaIntegrationTest {

    private static final String CALL_URI = "http://localhost:8084/api/v1/accounts/{id}/allowances/nfts";

    @Resource
    private DomainBuilder domainBuilder;

    @Resource
    private NftAllowanceMapper mapper;

    private static final String ACCOUNT_ID = "accountId";
    private static final String LIMIT = "limit";
    private static final String TOKEN_ID = "tokenId";

    private static final String ORDER = "order";
    private static final String OWNER = "owner";

    @Test
    void successWithNoQueryParams() {
        var allowance = domainBuilder.nftAllowance().persist();
        var allowance1 = domainBuilder
                .nftAllowance()
                .customize(nfta -> nfta.owner(allowance.getOwner()))
                .persist();
        Collection<NftAllowance> collection = List.of(allowance, allowance1);

        RestClient restClient = RestClient.create();
        var result = restClient
                .get()
                .uri(CALL_URI, allowance.getOwner())
                .accept(MediaType.ALL)
                .retrieve()
                .body(NftAllowancesResponse.class);
        assertThat(result.getAllowances()).isEqualTo(mapper.map(collection));
        assertThat(result.getLinks().getNext()).isEqualTo(null);
    }

    @Test
    void successWithAllQueryParams() {
        // Creating nft allowances
        var allowance = domainBuilder.nftAllowance().persist();
        domainBuilder
                .nftAllowance()
                .customize(nfta -> nfta.owner(allowance.getOwner()))
                .persist();

        // Setting up the url params
        Map<String, String> uriVariables = Map.of(
                "id",
                "" + allowance.getOwner(),
                ACCOUNT_ID,
                "gte:1000",
                OWNER,
                "true",
                TOKEN_ID,
                "gt:1000",
                LIMIT,
                "1",
                ORDER,
                "ASC");
        var params = "?" + "account.id={accountId}&" + "owner={owner}&" + "token.id={tokenId}&" + "limit={limit}&"
                + "order={order}";

        // Creating the rest client with the uri variables
        RestClient restClient = RestClient.builder()
                .baseUrl(CALL_URI)
                .defaultUriVariables(uriVariables)
                .build();

        // Performing the GET operation
        var result = restClient.get().uri(params).retrieve().body(NftAllowancesResponse.class);

        assertThat(result.getAllowances()).isEqualTo(mapper.map(List.of(allowance)));
        assertThat(result.getLinks().getNext())
                .isEqualTo("/api/v1/accounts/{id}/allowances/nfts?limit=1&order=ASC&account.id=gte:"
                        + EntityId.of(allowance.getSpender()) + "&token.id=gt:" + EntityId.of(allowance.getTokenId()));
    }
}

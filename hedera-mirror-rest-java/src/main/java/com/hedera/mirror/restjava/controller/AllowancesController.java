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

import static com.hedera.mirror.restjava.common.Constants.MAX_LIMIT;
import static com.hedera.mirror.restjava.common.Constants.MIN_LIMIT;

import com.hedera.mirror.common.domain.entity.EntityId;
import com.hedera.mirror.rest.model.NftAllowancesResponse;
import com.hedera.mirror.restjava.common.RangeParameter;
import com.hedera.mirror.restjava.mapper.NftAllowanceMapper;
import com.hedera.mirror.restjava.service.NftAllowanceRequest;
import com.hedera.mirror.restjava.service.NftAllowanceRequest.NftAllowanceRequestBuilder;
import com.hedera.mirror.restjava.service.NftAllowanceService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CustomLog
@RequiredArgsConstructor
@RequestMapping("/api/v1/accounts/{id}/allowances")
@RestController
public class AllowancesController {

    private final NftAllowanceService service;
    private final NftAllowanceMapper accountMapper;

    @GetMapping(value = "/nfts")
    NftAllowancesResponse getNftAllowancesByAccountId(
            @PathVariable
                    @NotBlank
                    @Pattern(regexp = "/^(\\d{1,5}\\.){1,2}\\d{1,10}$/", message = "The format for ID is invalid")
                    String id,
            @RequestParam("account.id") RangeParameter<EntityId> accountId,
            @RequestParam(defaultValue = "true") boolean owner,
            @RequestParam("token.id") RangeParameter<EntityId> tokenId,
            @RequestParam(defaultValue = "${DEFAULT_LIMIT}") @Min(MIN_LIMIT) @Max(MAX_LIMIT) int limit,
            @RequestParam(defaultValue = "ASC") Sort.Direction order) {

        long accountPathParam = EntityId.of(id).getNum();

        NftAllowanceRequestBuilder requestBuilder =
                NftAllowanceRequest.builder().limit(limit).order(order).isOwner(owner);

        // Setting both owner and spender Id to the account.id query parameter value.
        requestBuilder
                .spenderId(accountId.value().getId())
                .ownerId(accountId.value().getId())
                .accountIdOperator(accountId.operator());

        // Owner value decides if owner or spender should be set to the accountId.
        requestBuilder = owner ? requestBuilder.ownerId(accountPathParam) : requestBuilder.spenderId(accountPathParam);

        requestBuilder.tokenId(tokenId.value().getId()).tokenIdOperator(tokenId.operator());

        var serviceResponse = service.getNftAllowances(requestBuilder.build());

        NftAllowancesResponse response = new NftAllowancesResponse();
        response.setAllowances(accountMapper.map(serviceResponse));
        return response;
    }
}

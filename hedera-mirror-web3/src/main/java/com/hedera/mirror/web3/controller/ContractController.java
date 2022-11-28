package com.hedera.mirror.web3.controller;

/*-
 * ‌
 * Hedera Mirror Node
 * ​
 * Copyright (C) 2019 - 2022 Hedera Hashgraph, LLC
 * ​
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
 * ‍
 */

import static com.hedera.mirror.web3.controller.ValidationErrorParser.parseValidationError;
import static com.hederahashgraph.api.proto.java.ResponseCodeEnum.INVALID_TRANSFER_ACCOUNT_ID;
import static org.apache.tuweni.bytes.Bytes.EMPTY;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_IMPLEMENTED;

import javax.validation.Valid;
import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.datatypes.Address;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import com.hedera.mirror.web3.exception.InvalidTransactionException;
import com.hedera.mirror.web3.service.eth.ContractCallService;
import com.hedera.mirror.web3.service.model.CallBody;
import com.hedera.mirror.web3.viewmodel.ContractCallRequest;
import com.hedera.mirror.web3.viewmodel.ContractCallResponse;
import com.hedera.mirror.web3.viewmodel.GenericErrorResponse;
import com.hedera.services.evm.store.models.HederaEvmAccount;

@CustomLog
@RequestMapping("/api/v1/contracts")
@RequiredArgsConstructor
@RestController
class ContractController {

    static final String NOT_IMPLEMENTED_ERROR = "Operation not supported yet!";
    private final ContractCallService contractCallService;

    @PostMapping(value = "/call")
    Mono<ContractCallResponse> call(@RequestBody @Valid ContractCallRequest request) {
        if (request.isEstimate()) {
            throw new UnsupportedOperationException(NOT_IMPLEMENTED_ERROR);
        }
        //make sure there is a valid sender for value transfers
        if (request.getValue() > 0 && request.getFrom() == null) {
            throw new InvalidTransactionException(INVALID_TRANSFER_ACCOUNT_ID);
        }

        final var params = constructServiceParameters(request);
        final var callResponse =
                ContractCallResponse.of(
                        contractCallService.processCall(params));

        return Mono.just(callResponse);
    }

    private CallBody constructServiceParameters(ContractCallRequest request) {
        final var fromAddress =
                request.getFrom() != null
                        ? Address.fromHexString(request.getFrom())
                        : Address.ZERO;
        final var sender = new HederaEvmAccount(fromAddress);

        final var receiver = Address.fromHexString(request.getTo());
        final var data =
                request.getData() != null
                        ? Bytes.fromHexString(request.getData())
                        : EMPTY;

        return CallBody.builder()
                .sender(sender)
                .receiver(receiver)
                .callData(data)
                .providedGasLimit(request.getGas())
                .value(request.getValue())
                .build();
    }

    //This is temporary method till estimate_gas business logic got impl.
    @ExceptionHandler
    @ResponseStatus(NOT_IMPLEMENTED)
    private Mono<GenericErrorResponse> unsupportedOpResponse(UnsupportedOperationException e) {
        return errorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    private Mono<GenericErrorResponse> validationError(WebExchangeBindException e) {
        log.warn("Validation error: {}", e.getMessage());
        return errorResponse(parseValidationError(e));
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    private Mono<GenericErrorResponse> invalidTxnError(InvalidTransactionException e) {
        log.warn("Transaction error: {}", e.getMessage());
        return errorResponse(e.getMessage());
    }

    @ExceptionHandler()
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    private Mono<GenericErrorResponse> genericError(Exception e) {
        log.warn("Generic error: {}", e.getMessage());
        return errorResponse(INTERNAL_SERVER_ERROR.getReasonPhrase());
    }

    private Mono<GenericErrorResponse> errorResponse(String errorMessage) {
        return Mono.just(new GenericErrorResponse(errorMessage));
    }
}

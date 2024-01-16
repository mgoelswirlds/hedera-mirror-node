/*
 * Copyright (C) 2023-2024 Hedera Hashgraph, LLC
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

package com.hedera.services.fees;

import com.hedera.mirror.web3.evm.store.Store;
import com.hedera.services.hapi.utils.fees.FeeObject;
import com.hedera.services.jproto.JKey;
import com.hedera.services.utils.accessors.TxnAccessor;
import com.hederahashgraph.api.proto.java.*;

/**
 *  Copied Logic type from hedera-services. Differences with the original:
 *  1. Use abstraction for the state by introducing {@link Store} interface
 *  2. Remove unused methods: init, estimatedNonFeePayerAdjustments, estimateFee, computePayment, assessCryptoAutoRenewal
 */
public interface FeeCalculator {

    FeeObject estimatePayment(Query query, FeeData usagePrices, Timestamp at, ResponseType type);

    long estimatedGasPriceInTinybars(HederaFunctionality function, Timestamp at);

    FeeObject computeFee(TxnAccessor accessor, JKey payerKey, Timestamp at);
}

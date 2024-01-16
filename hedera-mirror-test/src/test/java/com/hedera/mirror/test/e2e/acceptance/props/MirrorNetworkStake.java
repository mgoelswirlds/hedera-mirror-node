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

package com.hedera.mirror.test.e2e.acceptance.props;

import lombok.Data;

@Data
public class MirrorNetworkStake {
    private long maxStakeRewarded;
    private long maxStakingRewardRatePerHbar;
    private long maxTotalReward;
    private float nodeRewardFeeFraction;
    private long reservedStakingRewards;
    private long rewardBalanceThreshold;
    private long stakeTotal;
    private TimestampRange stakingPeriod;
    private long stakingPeriodDuration;
    private long stakingPeriodsStored;
    private float stakingRewardFeeFraction;
    private long stakingRewardRate;
    private long stakingStartThreshold;
    private long unreservedStakingRewardBalance;
}

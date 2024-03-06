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

import com.hedera.mirror.common.domain.entity.EntityId;
import java.lang.reflect.ParameterizedType;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class RangeParameterMethodResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return RangeParameter.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public RangeParameter resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory)
            throws IllegalArgumentException {

        Class<?> genericClass =
                (Class<?>) ((ParameterizedType) parameter.getGenericParameterType()).getActualTypeArguments()[0];
        if (EntityId.class.isAssignableFrom(genericClass)) {
            String op = webRequest.getParameter("account.id");
            if (op == null) {
                return RangeParameter.EMPTY;
            }
            String[] splitVal = op.split(":");

            if (splitVal.length == 1) {
                // No operator specified. Just use "eq:"
                return new RangeParameter<>(RangeOperator.EQ, splitVal[0]);
            }

            return getEntityIdRangeParameter(splitVal[0], splitVal[1]);
        }
        throw new IllegalArgumentException("Generic type " + genericClass + " doesn't support");
    }

    @NotNull
    private static RangeParameter<EntityId> getEntityIdRangeParameter(String op, String value) {
        RangeOperator operator;
        EntityId entityId;

        if (op == null) {
            operator = RangeOperator.GT;
        } else {
            operator = RangeOperator.valueOf(op);
        }

        if (value == null) {
            entityId = EntityId.EMPTY;
        } else {
            entityId = EntityId.of(value);
        }

        return new RangeParameter<>(operator, entityId);
    }
}

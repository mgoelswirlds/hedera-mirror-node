/*
 * Copyright (C) 2020-2024 Hedera Hashgraph, LLC
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

package com.hedera.mirror.importer.exception;

@SuppressWarnings("java:S110")
public class InvalidEventFileException extends ImporterException {

    private static final long serialVersionUID = -2645790051583402799L;

    public InvalidEventFileException(String message) {
        super(message);
    }

    public InvalidEventFileException(String message, Throwable throwable) {
        super(message, throwable);
    }
}

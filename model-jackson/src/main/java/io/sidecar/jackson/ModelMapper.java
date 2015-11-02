/*
 * Copyright 2015 QSense, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.sidecar.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.joda.JodaModule;

import io.sidecar.ErrorModule;
import io.sidecar.access.AccessModule;
import io.sidecar.event.EventModule;
import io.sidecar.notification.NotificationModule;
import io.sidecar.org.OrgModule;
import io.sidecar.query.QueryModule;

import java.io.IOException;

import static com.google.common.base.Throwables.propagate;

public class ModelMapper extends ObjectMapper {

    public ModelMapper() {
        registerSidecarModules();
        register3rdPartyModules();
    }

    private void register3rdPartyModules() {
        registerModule(new GuavaModule());
        registerModule(new JodaModule());
    }

    private void registerSidecarModules() {
        registerModule(new EventModule());
        registerModule(new QueryModule());
        registerModule(new OrgModule());
        registerModule(new ErrorModule());
        registerModule(new AccessModule());
        registerModule(new NotificationModule());
    }

    /**
     * Used to serialize an object that can serialize properly in to a byte[] without use of
     * checked exceptions.
     */
    @SuppressWarnings("unused")
    public byte[] writeValueAsBytesUnchecked(Object o) {
        try {
            return writeValueAsBytes(o);
        } catch (JsonProcessingException e) {
            throw propagate(e);
        }
    }


    /**
     * Used to deserialize an object that can deserialize properly from a byte[], but without
     * checked exceptions.
     */
    @SuppressWarnings("unused")
    public <T> T readValueUnchecked(byte[] bytes, Class<T> cl) {
        try {
            return readValue(bytes, cl);
        } catch (IOException e) {
            throw propagate(e);
        }
    }

    /**
     * Used to deserialize an object that can deserialize properly from a String, but without
     * checked exceptions.
     */
    @SuppressWarnings("unused")
    public <T> T readValueUnchecked(String s, Class<T> cl) {
        try {
            return readValue(s, cl);
        } catch (IOException e) {
            throw propagate(e);
        }
    }
}

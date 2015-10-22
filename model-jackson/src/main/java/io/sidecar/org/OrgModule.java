/*
 * Copyright 2015 Sidecar.io
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

package io.sidecar.org;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;

import io.sidecar.credential.Credential;

public class OrgModule extends SimpleModule {

    public OrgModule() {
        super("Organization Module", new Version(1, 0, 0, "SNAPSHOT", "io.sidecar", "model"));
    }

    @Override
    public void setupModule(SetupContext context) {
        context.setMixInAnnotations(Organization.class, OrganizationMixin.class);
        context.setMixInAnnotations(Application.class, ApplicationMixin.class);
        context.setMixInAnnotations(Credential.class, CredentialMixin.class);
        context.setMixInAnnotations(UserGroup.class, UserGroupMixin.class);
        context.setMixInAnnotations(UserGroupMember.class, UserGroupMemberMixin.class);
        context.setMixInAnnotations(PlatformDeviceToken.class, PlatformDeviceTokenMixin.class);

    }

}

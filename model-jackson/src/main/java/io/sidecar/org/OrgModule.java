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

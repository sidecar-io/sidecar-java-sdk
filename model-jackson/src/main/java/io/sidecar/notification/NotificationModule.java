package io.sidecar.notification;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class NotificationModule extends SimpleModule {

    public NotificationModule() {
        super("Notification Module", new Version(1, 0, 0, "SNAPSHOT", "io.sidecar", "model"));

    }

    @Override
    public void setupModule(SetupContext context) {
        context.setMixInAnnotations(EventNotification.class, EventNotificationMixin.class);
        context.setMixInAnnotations(NotificationRule.class, NotificationRuleMixin.class);
    }


}

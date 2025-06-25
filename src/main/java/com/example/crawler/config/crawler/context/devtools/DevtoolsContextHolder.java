package com.example.crawler.config.crawler.context.devtools;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Getter
@Setter
public class DevtoolsContextHolder {

    private static final ThreadLocal<DevtoolsContext> contextManager = new ThreadLocal<>();

    public static DevtoolsContext getContext() {
        DevtoolsContext devtoolsContext = contextManager.get();
        if (devtoolsContext == null) {
            devtoolsContext = new DevtoolsContext();
            contextManager.set(devtoolsContext);
        }
        return devtoolsContext;
    }

    public static void clearContext() {
        log.debug("[DevtoolsContextHolder] Clear context");
        contextManager.remove();
    }

}

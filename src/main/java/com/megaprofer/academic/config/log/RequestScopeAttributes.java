package com.megaprofer.academic.config.log;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;
import java.util.UUID;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RequestScopeAttributes {

    private StopWatch stopWatch;
    private String requestId;

    @PostConstruct
    public void init() {
        stopWatch = new StopWatch();
        requestId = UUID.randomUUID().toString();
    }

    public StopWatch getStopWatch() {
        return stopWatch;
    }

    public String getRequestId() {
        return requestId;
    }

}

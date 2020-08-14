package com.megaprofer.academic.config.log;

//import ec.com.alquimiasoft.logbook.Logbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class EndPointInterceptor implements HandlerInterceptor {

    public static final String REQUESTED_URL_KEY = "requestedUrl";
    public static final String REQUEST_METHOD = "requestMethod";
    public static final String REQUEST_ID = "requestId";
    public static final String ENDPOINT_INTERCEPTOR = "endpointInterceptor";

    @Autowired
    private RequestScopeAttributes requestScopeAttributes;

//    private Logbook logbook;

    @PostConstruct
    public void init() {
        /*logbook = Logbook.instance(EndPointInterceptor.class);*/
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        requestScopeAttributes.getStopWatch().start();
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        StopWatch stopWatch = requestScopeAttributes.getStopWatch();
        stopWatch.stop();
        long duration = stopWatch.getLastTaskTimeMillis();
        /*logbook.duration(duration)
                .status(response.getStatus() + "")
                .add(REQUESTED_URL_KEY, request.getRequestURI())
                .add(REQUEST_METHOD, request.getMethod())
                .add(REQUEST_ID, requestScopeAttributes.getRequestId())
                .type(ENDPOINT_INTERCEPTOR)
                .info();*/
    }
}

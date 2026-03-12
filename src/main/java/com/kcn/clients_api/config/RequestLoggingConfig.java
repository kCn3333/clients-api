package com.kcn.clients_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration
public class RequestLoggingConfig {

    @Bean
    public CommonsRequestLoggingFilter requestLoggingFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        filter.setIncludeClientInfo(true);    // IP klienta
        filter.setIncludeQueryString(true);   // ?param=value
        filter.setIncludePayload(false);      // nie loguj body (dane wrażliwe)
        filter.setIncludeHeaders(false);      // nie loguj headerów (tokeny auth)
        return filter;
    }
}
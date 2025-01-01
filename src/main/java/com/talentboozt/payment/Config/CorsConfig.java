package com.talentboozt.payment.Config;

import com.talentboozt.payment.Utils.ConfigUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Autowired
    private ConfigUtility configUtility;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(configUtility.getProperty("ALLOWED_ORIGIN_1"), configUtility.getProperty("ALLOWED_ORIGIN_2"), configUtility.getProperty("ALLOWED_ORIGIN_3"), configUtility.getProperty("ALLOWED_ORIGIN_4"), configUtility.getProperty("ALLOWED_ORIGIN_5"), configUtility.getProperty("ALLOWED_ORIGIN_6"))
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("Stripe-Signature")
                .allowCredentials(true);
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Arrays.asList(configUtility.getProperty("ALLOWED_ORIGIN_1"), configUtility.getProperty("ALLOWED_ORIGIN_2"), configUtility.getProperty("ALLOWED_ORIGIN_3"), configUtility.getProperty("ALLOWED_ORIGIN_4"), configUtility.getProperty("ALLOWED_ORIGIN_5"), configUtility.getProperty("ALLOWED_ORIGIN_6")));
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}

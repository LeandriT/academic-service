package com.megaprofer.academic.config.app;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.megaprofer.academic.authentication.service.UserService;
import com.megaprofer.academic.authentication.service.impl.UserServiceImpl;
import com.megaprofer.academic.config.auth.JWTAuthenticationFilter;
import com.megaprofer.academic.config.auth.MegaPosAuthenticationManager;
import com.megaprofer.academic.config.auth.MegaPosUserDetails;
import com.megaprofer.academic.config.validation.MethodValidationAspect;
import com.megaprofer.academic.config.app.CustomDateDeserializer;
import org.apache.commons.io.IOUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.client.RestTemplate;


import javax.sql.DataSource;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@EnableWebSecurity
@EnableScheduling
@Configuration
@EnableConfigurationProperties
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ComponentScan(basePackages = {"com.megaprofer.academic"})
public class ConfigurationMegaPOS extends WebSecurityConfigurerAdapter {

    private static final String DB_PASSWORD_FILE_ENV_NAME = "DB_PASSWORD_FILE";

    @Bean
    @Primary
    public DataSource dataSource(@Value("${spring.datasource.url}") String jdbUrl,
                                 @Value("${spring.datasource.username}") String userName,
                                 @Value("${spring.datasource.password}") String password) throws IOException {
        if (System.getenv().get(DB_PASSWORD_FILE_ENV_NAME) != null) {
            password = readPasswordFromFile(System.getenv().get(DB_PASSWORD_FILE_ENV_NAME));
        }
        return DataSourceBuilder.create()
                .url(jdbUrl)
                .username(userName)
                .password(password)
                .build();
    }

    private String readPasswordFromFile(String pathFile) throws IOException {
        return IOUtils.toString(new FileReader(pathFile)).replace("\n", "");
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests().antMatchers("/api/login", "/actuator/health/**")
                .permitAll().anyRequest().authenticated().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf().disable()
                .cors().configure(httpSecurity);
    }

    @Bean
    JWTAuthenticationFilter jwtAuthenticationFilter() {
        return new JWTAuthenticationFilter(this.megaPosAuthenticationManager());
    }

    @Bean
    MegaPosAuthenticationManager megaPosAuthenticationManager() {
        return new MegaPosAuthenticationManager();
    }

    @Bean
    MegaPosUserDetails megaPosUserDetails() {
        return new MegaPosUserDetails();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }

    @Bean
    public Validator validator() {
        return new LocalValidatorFactoryBean();
    }

    @Bean
    public MethodValidationAspect methodValidationAspect() {
        return new MethodValidationAspect();
    }

    @Bean
    public UserService userService() {
        return new UserServiceImpl();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        SimpleModule module = new SimpleModule()
                .addDeserializer(Date.class, new CustomDateDeserializer());
        objectMapper.registerModule(module);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        objectMapper.setDateFormat(df);
        return objectMapper;
    }

    @Bean
    public RestTemplate restTemplate() {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(3000);
        return new RestTemplate(clientHttpRequestFactory);
    }

    @Bean
    public ResourceBundleMessageSource emailMessageSource() {
        final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("mail/MailMessages");
        return messageSource;
    }
}


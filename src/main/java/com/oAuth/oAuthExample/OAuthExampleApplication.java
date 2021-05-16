package com.oAuth.oAuthExample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@SpringBootApplication
@RestController
public class OAuthExampleApplication extends WebSecurityConfigurerAdapter {
    //this extend will avoid automatically redirect github

    @GetMapping("/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        return Collections.singletonMap("name", principal.getAttribute("name"));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
                .authorizeRequests(a -> a
                        .antMatchers("/", "/error", "/webjars/**").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                )
                .oauth2Login();
        // @formatter:on

		/*The above configuration indicates a whitelist of permitted endpoints, with every other endpoint requiring authentication
        You want to allow:
            /   since that’s the page you just made dynamic, with some of its content visible to unauthenticated users
            /error since that’s a Spring Boot endpoint for displaying errors, and
            /webjars/** since you’ll want your JavaScript to run for all visitors, authenticated or not*/
    }

    public static void main(String[] args) {
        SpringApplication.run(OAuthExampleApplication.class, args);
    }

}

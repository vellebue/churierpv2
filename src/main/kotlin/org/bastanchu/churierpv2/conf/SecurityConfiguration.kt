package org.bastanchu.churierpv2.conf

import com.vaadin.flow.spring.security.VaadinAwareSecurityContextHolderStrategyConfiguration
import com.vaadin.flow.spring.security.VaadinSecurityConfigurer
import org.bastanchu.churierpv2.LoginView
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain

@EnableWebSecurity
@Configuration
@Import(VaadinAwareSecurityContextHolderStrategyConfiguration::class)
open class SecurityConfiguration {

    @Bean
    open fun userDetailsService(): InMemoryUserDetailsManager {
        val user = User.withUsername("angel")
                       .password("{noop}angel8")
                       .roles("USER").build()
        return InMemoryUserDetailsManager(user)
    }

    @Bean
    open fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { csrf -> csrf.disable() }

            .with(VaadinSecurityConfigurer.vaadin(), {configurer->
                configurer.loginView(LoginView::class.java)
            })
        return http.build()
    }
}

/*
            .authorizeHttpRequests { http ->
                                           http.requestMatchers("/login",
                                                                            "/VAADIN/**"//,
                                                                            //"/offline-stub.html"
                                               ).permitAll()
                                               .anyRequest().authenticated()
                                  }
            */
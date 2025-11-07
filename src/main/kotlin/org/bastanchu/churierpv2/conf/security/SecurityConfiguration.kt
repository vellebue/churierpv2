package org.bastanchu.churierpv2.conf.security

import com.vaadin.flow.spring.security.VaadinAwareSecurityContextHolderStrategyConfiguration
import com.vaadin.flow.spring.security.VaadinSecurityConfigurer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.core.annotation.Order
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.UrlAuthorizationConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
@Import(VaadinAwareSecurityContextHolderStrategyConfiguration::class)
open class SecurityConfiguration(@Autowired val jwtRequestFilter: JwtRequestFilter) {

    @Order(1)
    @Bean
    open fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { csrf -> csrf.disable() }
            .sessionManagement {sessionManagementConfigurer ->
                sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .oauth2ResourceServer {
                it.jwt(Customizer.withDefaults())
            }
            .with(VaadinSecurityConfigurer.vaadin(), {configurer->
                configurer.loginView("/login")
            })
            .addFilterBefore(jwtRequestFilter,
                org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }

    @Order(0)
    @Bean
    open fun publicFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.securityMatcher("/public/**").authorizeHttpRequests { auth ->
            auth.anyRequest().permitAll()
        }
        return http.build()
    }
}
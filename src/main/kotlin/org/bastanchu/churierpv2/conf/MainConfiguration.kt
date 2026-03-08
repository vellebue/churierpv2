package org.bastanchu.churierpv2.conf

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.servlet.config.annotation.EnableWebMvc

@Configuration
@EnableWebMvc
open class MainConfiguration() {

    @Bean
    open fun messageSource(): MessageSource {
        val resourceBundleMessageSource = ResourceBundleMessageSource()
        resourceBundleMessageSource.setBasename("messages")
        return resourceBundleMessageSource
    }

    @Bean
    open fun validatorFactoryBean(): LocalValidatorFactoryBean {
        return LocalValidatorFactoryBean()
    }

    @Bean("ollamaWebClient")
    open fun ollamaWebClient(@Value("\${ia.ollama.url}") baseUrl: String): WebClient {
        return WebClient.builder()
            .baseUrl(baseUrl)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();
    }
}
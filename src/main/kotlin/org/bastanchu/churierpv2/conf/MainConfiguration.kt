package org.bastanchu.churierpv2.conf

import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
import org.springframework.web.servlet.config.annotation.EnableWebMvc

@Configuration
@EnableWebMvc
open class MainConfiguration {

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
}
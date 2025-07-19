package org.bastanchu.churierpv2.conf

import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ResourceBundleMessageSource

@Configuration
open class MainConfiguration {

    @Bean
    open fun messageSource(): MessageSource {
        val resourceBundleMessageSource = ResourceBundleMessageSource()
        resourceBundleMessageSource.setBasename("messages")
        return resourceBundleMessageSource
    }
}
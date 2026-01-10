package org.bastanchu.churierpv2.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class SpringContextHolder(@Autowired val applicationContext: org.springframework.context.ApplicationContext) {

    companion object instance {

        var appContext : org.springframework.context.ApplicationContext? = null

        fun getCurrentApplicationContext(): org.springframework.context.ApplicationContext {
            return appContext!!;
        }
    }


    init {
        instance.appContext = this.applicationContext
        //println("Active context holder")
    }

}
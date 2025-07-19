package org.bastanchu.churierpv2.view.administration.users

import org.bastanchu.churierpv2.view.common.view.BaseView
import org.springframework.context.ApplicationContext
import org.springframework.context.MessageSource

class UsersView(messages: MessageSource, applicationContext: ApplicationContext) : BaseView(messages, applicationContext) {

    override fun getTitleKey(): String {
        return "users.title"
    }

    override fun onStart() {

    }

    override fun onStop() {

    }
}
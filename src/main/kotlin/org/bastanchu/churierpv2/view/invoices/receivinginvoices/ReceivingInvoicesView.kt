package org.bastanchu.churierpv2.view.invoices.receivinginvoices

import org.bastanchu.churierpv2.view.common.view.BaseView
import org.springframework.context.ApplicationContext
import org.springframework.context.MessageSource

class ReceivingInvoicesView(messages : MessageSource, applicationContext: ApplicationContext) :
                                BaseView(messages, applicationContext) {

    override fun getTitleKey(): String {
        return "receivingInvoices.title"
    }

    override fun onStart() {

    }

    override fun onStop() {

    }
}
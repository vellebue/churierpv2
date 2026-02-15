package org.bastanchu.churierpv2.view.administration.accountancy.icominginvoices

import org.bastanchu.churierpv2.dto.administration.accountancy.incominginvoices.IncomingInvoiceDto
import org.bastanchu.churierpv2.view.common.view.BaseListView
import org.springframework.context.ApplicationContext
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder

class IncomingInvoicesListView(messages : MessageSource, applicationContext: ApplicationContext, parent: IncomingInvoicesView)
      : BaseListView<IncomingInvoiceDto>(messages, applicationContext, parent) {

    override fun getDefaultPageSize(): Int {
        val INCOMING_INVOICES_LIST_PAGE_SIZE = 10
        return INCOMING_INVOICES_LIST_PAGE_SIZE
    }

    override fun getNewItemButtonText(): String {
        return  messages.getMessage("receivingInvoices.listView.newitem.button.text", null, LocaleContextHolder.getLocale())
    }
}
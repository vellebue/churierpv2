package org.bastanchu.churierpv2.view.administration.accountancy.icominginvoices

import org.bastanchu.churierpv2.dto.administration.accountancy.incominginvoices.IncomingInvoiceDto
import org.bastanchu.churierpv2.dto.administration.accountancy.incominginvoices.IncomingInvoiceFilterDto
import org.bastanchu.churierpv2.view.common.view.BaseFilterView
import org.springframework.context.ApplicationContext
import org.springframework.context.MessageSource

class IncomingInvoicesFilterView(messages : MessageSource, applicationContext: ApplicationContext, parent: IncomingInvoicesView)
    : BaseFilterView<IncomingInvoiceFilterDto, IncomingInvoiceDto>(messages, applicationContext, parent) {

    override fun getFormTitleKey(): String {
        return "receivingInvoices.filterView.title"
    }

    override fun doFilter(filterDto: IncomingInvoiceFilterDto): List<IncomingInvoiceDto> {
        return ArrayList()
    }
}
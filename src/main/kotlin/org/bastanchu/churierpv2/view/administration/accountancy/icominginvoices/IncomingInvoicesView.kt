package org.bastanchu.churierpv2.view.administration.accountancy.icominginvoices

import org.bastanchu.churierpv2.dto.administration.accountancy.incominginvoices.IncomingInvoiceDto
import org.bastanchu.churierpv2.dto.administration.accountancy.incominginvoices.IncomingInvoiceFilterDto
import org.bastanchu.churierpv2.view.common.view.BaseCRUDView
import org.bastanchu.churierpv2.view.common.view.BaseDetailView
import org.bastanchu.churierpv2.view.common.view.BaseFilterView
import org.bastanchu.churierpv2.view.common.view.BaseListView
import org.springframework.context.ApplicationContext
import org.springframework.context.MessageSource

class IncomingInvoicesView(messages : MessageSource, applicationContext: ApplicationContext)
    : BaseCRUDView<IncomingInvoiceFilterDto, IncomingInvoiceDto>(messages, applicationContext,
                                                                    IncomingInvoicesFilterView::class.java as Class<in BaseFilterView<IncomingInvoiceFilterDto, IncomingInvoiceDto>>,
                                                                     IncomingInvoicesListView::class.java as Class<in BaseListView<IncomingInvoiceDto>>,
                                                                     IncomingInvoicesDetailView::class.java as Class<in BaseDetailView<IncomingInvoiceDto>>
) {
    override fun getTitleKey(): String {
        return "receivingInvoices.title"
    }

    override fun onStart() {

    }

    override fun onStop() {

    }
}
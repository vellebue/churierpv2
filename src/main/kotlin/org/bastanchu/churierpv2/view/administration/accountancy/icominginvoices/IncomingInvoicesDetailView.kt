package org.bastanchu.churierpv2.view.administration.accountancy.icominginvoices

import org.bastanchu.churierpv2.dto.administration.accountancy.incominginvoices.IncomingInvoiceDto
import org.bastanchu.churierpv2.service.accountancy.IncomingInvoicesIAService
import org.bastanchu.churierpv2.service.administration.adresses.CountryService
import org.bastanchu.churierpv2.service.administration.adresses.RegionService
import org.bastanchu.churierpv2.service.administration.societies.SocietiesService
import org.bastanchu.churierpv2.view.common.view.BaseDetailView
import org.bastanchu.churierpv2.view.common.view.annotation.Injected
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContext
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder

class IncomingInvoicesDetailView(messages: MessageSource,
                                 applicationContext: ApplicationContext,
                                 parent: IncomingInvoicesView,
                                 insertMode: Boolean): BaseDetailView<IncomingInvoiceDto>(messages, applicationContext, parent, insertMode) {

    val logger = LoggerFactory.getLogger(IncomingInvoicesDetailView::class.java)

    @Injected
    lateinit var societiesService: SocietiesService
    @Injected
    lateinit var countryService: CountryService
    @Injected
    lateinit var regionService: RegionService
    @Injected
    lateinit var incomingInvoicesIAService: IncomingInvoicesIAService

    override fun getFormTitleKey(): String {
        return "receivingInvoices.detailView.title"
    }

    override fun getDeleteDialogTextKey(): String {
        return "receivingInvoices.detailView.deleteitem.dialog.text"
    }

    override fun getCreateButtonText(): String {
        return messages.getMessage("receivingInvoices.detailView.newitem.button.text", null, LocaleContextHolder.getLocale())
    }

    override fun getUpdateButtonText(): String {
        return messages.getMessage("receivingInvoices.detailView.updateitem.button.text", null, LocaleContextHolder.getLocale())
    }

    override fun getDeleteButtonText(): String {
        return messages.getMessage("receivingInvoices.detailView.deleteitem.button.text", null, LocaleContextHolder.getLocale())
    }

    override fun buildNewItemModel(): IncomingInvoiceDto {
        return IncomingInvoiceDto()
    }

    override fun completeItemModel(item: IncomingInvoiceDto): IncomingInvoiceDto {
        item.countriesMap = countryService.retieveCountriesMap()
        item.regionsMap = regionService.retrieveRegionsMap()
        item.societiesMap = societiesService.retrieveSocietiesMap()
        return item
    }

    override fun onCreateItem(item: IncomingInvoiceDto) {
        val classLoader = Thread.currentThread().contextClassLoader
        val stream = classLoader.getResourceAsStream("TicketElCorteIngles.jpeg")
        val response = incomingInvoicesIAService.parseInvoice(stream)
        logger.info("Reponse: ${response}")
    }

    override fun onUpdateItem(item: IncomingInvoiceDto) {

    }

    override fun onDeleteItem(item: IncomingInvoiceDto) {

    }
}
package org.bastanchu.churierpv2.view.administration.societies

import org.bastanchu.churierpv2.dto.administration.societies.SocietyDto
import org.bastanchu.churierpv2.service.administration.adresses.CountryService
import org.bastanchu.churierpv2.service.administration.adresses.RegionService
import org.bastanchu.churierpv2.service.administration.societies.SocietiesService
import org.bastanchu.churierpv2.view.common.view.BaseDetailView
import org.bastanchu.churierpv2.view.common.view.annotation.Injected
import org.springframework.context.ApplicationContext

import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder

class SocietiesDetailView(messages: MessageSource,
                          applicationContext: ApplicationContext,
                          parent: SocietiesView,
                          insertMode: Boolean) : BaseDetailView<SocietyDto>(messages, applicationContext, parent, insertMode) {

    @Injected
    lateinit var societiesService: SocietiesService
    @Injected
    lateinit var countryService: CountryService
    @Injected
    lateinit var regionService: RegionService

    override fun getFormTitleKey(): String {
        return "societies.detailView.title"
    }

    override fun getDeleteDialogTextKey(): String {
        return "societies.detailView.deleteitem.dialog.text"
    }

    override fun getCreateButtonText(): String {
        return messages.getMessage("societies.detailView.newitem.button.text", null, LocaleContextHolder.getLocale())
    }

    override fun getUpdateButtonText(): String {
        return messages.getMessage("societies.detailView.updateitem.button.text", null, LocaleContextHolder.getLocale())
    }

    override fun getDeleteButtonText(): String {
        return messages.getMessage("societies.detailView.deleteitem.button.text", null, LocaleContextHolder.getLocale())
    }

    override fun buildNewItemModel(): SocietyDto {
        return SocietyDto()
    }

    override fun completeItemModel(item: SocietyDto): SocietyDto {
        val countriesMap = countryService.retieveCountriesMap()
        item.countriesMap = countriesMap
        val regionsMap = regionService.retrieveRegionsMap()
        item.regionsMap = regionsMap
        return item
    }

    override fun onCreateItem(item: SocietyDto) {
        societiesService.createSociety(item)
    }

    override fun onUpdateItem(item: SocietyDto) {
        societiesService.updateSociety(item)
    }

    override fun onDeleteItem(item: SocietyDto) {
        societiesService.deleteSociety(item)
    }
}
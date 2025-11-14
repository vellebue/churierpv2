package org.bastanchu.churierpv2.view.administration.societies

import org.bastanchu.churierpv2.dto.administration.societies.SocietyDto
import org.bastanchu.churierpv2.view.common.view.BaseDetailView
import org.springframework.context.ApplicationContext

import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder

class SocietiesDetailView(messages: MessageSource,
                          applicationContext: ApplicationContext,
                          parent: SocietiesView,
                          insertMode: Boolean) : BaseDetailView<SocietyDto>(messages, applicationContext, parent, insertMode) {

    override fun getFormTitleKey(): String {
        return "societies.detailView.title"
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
        return item
    }

    override fun onCreateItem(item: SocietyDto) {

    }
}
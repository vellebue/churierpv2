package org.bastanchu.churierpv2.view.administration.societies

import org.bastanchu.churierpv2.dto.administration.societies.SocietyDto
import org.bastanchu.churierpv2.view.common.view.BaseListView
import org.springframework.context.ApplicationContext
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder

class SocietiesListView(messages : MessageSource, applicationContext: ApplicationContext, parent: SocietiesView) :
    BaseListView<SocietyDto>(messages, applicationContext, parent) {

    override fun getDefaultPageSize(): Int {
        return 6
    }

    override fun getNewItemButtonText(): String {
        return messages.getMessage("societies.listView.newitem.button.text", null, LocaleContextHolder.getLocale())
    }


}
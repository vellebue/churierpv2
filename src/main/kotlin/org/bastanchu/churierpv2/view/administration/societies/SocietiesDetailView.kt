package org.bastanchu.churierpv2.view.administration.societies

import org.bastanchu.churierpv2.dto.administration.societies.SocietyDto
import org.bastanchu.churierpv2.view.common.view.BaseDetailView
import org.springframework.context.ApplicationContext

import org.springframework.context.MessageSource

class SocietiesDetailView(messages: MessageSource,
                          applicationContext: ApplicationContext,
                          parent: SocietiesView) : BaseDetailView<SocietyDto>(messages, applicationContext, parent) {

    override fun getFormTitleKey(): String {
        return "societies.detailView.title"
    }
}
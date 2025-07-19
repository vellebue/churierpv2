package org.bastanchu.churierpv2.view.administration.societies

import org.bastanchu.churierpv2.dto.administration.societies.SocietyDto
import org.bastanchu.churierpv2.view.common.view.BaseListView
import org.springframework.context.ApplicationContext
import org.springframework.context.MessageSource

class SocietiesListView(messages : MessageSource, applicationContext: ApplicationContext, parent: SocietiesView) :
    BaseListView<SocietyDto>(messages, applicationContext, parent) {
}
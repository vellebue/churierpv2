package org.bastanchu.churierpv2.view.administration.societies

import org.bastanchu.churierpv2.dto.administration.societies.SocietiesFilterDto
import org.bastanchu.churierpv2.dto.administration.societies.SocietyDto
import org.bastanchu.churierpv2.view.common.view.BaseCRUDView
import org.bastanchu.churierpv2.view.common.view.BaseFilterView
import org.bastanchu.churierpv2.view.common.view.BaseListView
import org.springframework.context.ApplicationContext

import org.springframework.context.MessageSource

class SocietiesView(messages : MessageSource, applicationContext: ApplicationContext) :
            BaseCRUDView<SocietiesFilterDto, SocietyDto>(messages, applicationContext,
                SocietiesFilterView::class.java as Class<in BaseFilterView<SocietiesFilterDto, SocietyDto>>,
                SocietiesListView::class.java as Class<in BaseListView<SocietyDto>>
            ) {

    override fun getTitleKey(): String {
        return "societies.title"
    }

    override fun onStart() {

    }

    override fun onStop() {

    }
}
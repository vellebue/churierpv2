package org.bastanchu.churierpv2.view.administration.societies

import org.bastanchu.churierpv2.dto.administration.societies.SocietiesFilterDto
import org.bastanchu.churierpv2.dto.administration.societies.SocietyDto
import org.bastanchu.churierpv2.service.administration.societies.SocietiesService
import org.bastanchu.churierpv2.view.common.view.BaseFilterView
import org.bastanchu.churierpv2.view.common.view.annotation.Injected
import org.springframework.context.ApplicationContext
import org.springframework.context.MessageSource

class SocietiesFilterView(messages : MessageSource, applicationContext: ApplicationContext, parent: SocietiesView) :
    BaseFilterView<SocietiesFilterDto, SocietyDto>(messages, applicationContext, parent) {

    @Injected
    lateinit var societiesService: SocietiesService


    override fun getFormTitleKey(): String {
        return "societies.filterView.title"
    }

    override fun doFilter(filterDto: SocietiesFilterDto): List<SocietyDto> {
        return societiesService.filterSocieties(filterDto)
    }
}
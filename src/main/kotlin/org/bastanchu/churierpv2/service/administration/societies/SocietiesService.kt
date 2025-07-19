package org.bastanchu.churierpv2.service.administration.societies

import org.bastanchu.churierpv2.dto.administration.societies.SocietiesFilterDto
import org.bastanchu.churierpv2.dto.administration.societies.SocietyDto

interface SocietiesService {

    public fun filterSocieties(filterDto: SocietiesFilterDto): List<SocietyDto>
}
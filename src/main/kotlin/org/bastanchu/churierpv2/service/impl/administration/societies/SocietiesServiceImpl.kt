package org.bastanchu.churierpv2.service.impl.administration.societies

import org.bastanchu.churierpv2.dto.administration.societies.SocietiesFilterDto
import org.bastanchu.churierpv2.dto.administration.societies.SocietyDto
import org.bastanchu.churierpv2.service.administration.societies.SocietiesService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
open class SocietiesServiceImpl : SocietiesService {

    override fun filterSocieties(filterDto: SocietiesFilterDto): List<SocietyDto> {
        val society1 = SocietyDto(0, "Grupo Varma", "Grupo Varma SL",
                                  "46931298M", 0, "SOC", "C/ La Granja 15",
                                   "28108", "Madrid", "ES", "28")
        val society2 = SocietyDto(1, "Bastanchur", "Bastanchur CORP",
            "53108437B", 1, "SOC", "C/ Jabonería 18 1 B",
            "28921", "Alcorcón", "ES", "28")
        val result = ArrayList<SocietyDto>()
        result.add(society1)
        result.add(society2)
        result.add(society1)
        result.add(society2)
        result.add(society1)
        result.add(society2)
        result.add(society1)
        result.add(society2)
        result.add(society1)
        result.add(society2)
        result.add(society1)
        result.add(society2)
        result.add(society1)
        result.add(society2)
        return result
    }
}
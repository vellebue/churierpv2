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
        val society3 = SocietyDto(2, "Grupo Varma", "Grupo Varma SL",
            "46931298M", 0, "SOC", "C/ La Granja 15",
            "28108", "Madrid", "ES", "28")
        val society4 = SocietyDto(3, "Bastanchur", "Bastanchur CORP",
            "53108437B", 1, "SOC", "C/ Jabonería 18 1 B",
            "28921", "Alcorcón", "ES", "28")
        val society5 = SocietyDto(4, "Grupo Varma", "Grupo Varma SL",
            "46931298M", 0, "SOC", "C/ La Granja 15",
            "28108", "Madrid", "ES", "28")
        val society6 = SocietyDto(5, "Bastanchur", "Bastanchur CORP",
            "53108437B", 1, "SOC", "C/ Jabonería 18 1 B",
            "28921", "Alcorcón", "ES", "28")
        val society7 = SocietyDto(6, "Grupo Varma", "Grupo Varma SL",
            "46931298M", 0, "SOC", "C/ La Granja 15",
            "28108", "Madrid", "ES", "28")
        val society8 = SocietyDto(7, "Bastanchur", "Bastanchur CORP",
            "53108437B", 1, "SOC", "C/ Jabonería 18 1 B",
            "28921", "Alcorcón", "ES", "28")
        val society9 = SocietyDto(8, "Grupo Varma", "Grupo Varma SL",
            "46931298M", 0, "SOC", "C/ La Granja 15",
            "28108", "Madrid", "ES", "28")
        val society10 = SocietyDto(9, "Bastanchur", "Bastanchur CORP",
            "53108437B", 1, "SOC", "C/ Jabonería 18 1 B",
            "28921", "Alcorcón", "ES", "28")
        val society11 = SocietyDto(10, "Grupo Varma", "Grupo Varma SL",
            "46931298M", 0, "SOC", "C/ La Granja 15",
            "28108", "Madrid", "ES", "28")
        val society12 = SocietyDto(11, "Bastanchur", "Bastanchur CORP",
            "53108437B", 1, "SOC", "C/ Jabonería 18 1 B",
            "28921", "Alcorcón", "ES", "28")
        val society13 = SocietyDto(12, "Grupo Varma", "Grupo Varma SL",
            "46931298M", 0, "SOC", "C/ La Granja 15",
            "28108", "Madrid", "ES", "28")
        val society14 = SocietyDto(13, "Bastanchur", "Bastanchur CORP",
            "53108437B", 1, "SOC", "C/ Jabonería 18 1 B",
            "28921", "Alcorcón", "ES", "28")
        val result = listOf(society1, society2, society3, society4, society5, society6, society7, society8, society9, society10, society11, society12, society13, society14)
        return result
    }
}
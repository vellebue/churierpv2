package org.bastanchu.churierpv2.dao.administration.societies

import org.bastanchu.churierpv2.dao.BaseValueObjectDao
import org.bastanchu.churierpv2.dto.administration.adresses.AddressDto
import org.bastanchu.churierpv2.dto.administration.societies.SocietyDto
import org.bastanchu.churierpv2.entity.administration.adresses.Address
import org.bastanchu.churierpv2.entity.administration.societies.Society

interface SocietiesDao: BaseValueObjectDao<Int, Society, SocietyDto> {

    public fun buildNewAdressFromSocietyDto(societyDto: SocietyDto): Address

    public fun buildNewAdressDtoFromSocietyDto(societyDto: SocietyDto): AddressDto

}
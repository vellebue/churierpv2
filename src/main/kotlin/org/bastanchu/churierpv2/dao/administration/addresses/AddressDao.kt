package org.bastanchu.churierpv2.dao.administration.addresses

import org.bastanchu.churierpv2.dao.BaseValueObjectDao
import org.bastanchu.churierpv2.dto.administration.adresses.AddressDto
import org.bastanchu.churierpv2.entity.administration.adresses.Address
import org.bastanchu.churierpv2.entity.administration.adresses.AddressType
import org.bastanchu.churierpv2.entity.administration.adresses.AddressTypeEnum

interface AddressDao: BaseValueObjectDao<Int, Address, AddressDto> {

    fun loadAddressType(type: AddressTypeEnum): AddressType

}
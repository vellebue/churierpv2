package org.bastanchu.churierpv2.dao.impl.administration.societies

import org.bastanchu.churierpv2.dao.administration.societies.SocietiesDao
import org.bastanchu.churierpv2.dao.impl.BaseValueObjectDaoImpl
import org.bastanchu.churierpv2.dto.administration.societies.SocietyDto
import org.bastanchu.churierpv2.entity.administration.societies.Society

import jakarta.persistence.*
import org.bastanchu.churierpv2.dao.impl.BaseValueObjectEntityMapperDefaultImpl
import org.bastanchu.churierpv2.dto.administration.adresses.AddressDto
import org.bastanchu.churierpv2.entity.administration.adresses.Address
import org.bastanchu.churierpv2.entity.administration.adresses.AddressType
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
open class SocietiesDaoImpl(@PersistenceContext(unitName = "entityManagerFactory") override val entityManager: EntityManager):
      BaseValueObjectDaoImpl<Int, Society, SocietyDto>(entityManager, SocietiesMapper()),
      SocietiesDao {

    val societiesAddressMapper = SocietiesAddressMapper()
    val societiesAddressDtoMapper = SocietiesAddressDtoMapper()


    class SocietiesMapper : BaseValueObjectEntityMapperDefaultImpl<SocietyDto, Society>() {
    }

    class SocietiesAddressMapper : BaseValueObjectEntityMapperDefaultImpl<SocietyDto, Address>() {
    }

    class SocietiesAddressDtoMapper : BaseValueObjectEntityMapperDefaultImpl<SocietyDto, AddressDto>() {
    }

    override fun buildNewAdressFromSocietyDto(societyDto: SocietyDto): Address {
        val newAddress = Address()
        societiesAddressMapper.toEntity(societyDto, newAddress)
        return newAddress
    }

    override fun buildNewAdressDtoFromSocietyDto(societyDto: SocietyDto): AddressDto {
        val newAddressDto = AddressDto()
        societiesAddressDtoMapper.toEntity(societyDto, newAddressDto)
        return newAddressDto
    }

    override fun toValueObject(society: Society, societyDto: SocietyDto) {
        super.toValueObject(society, societyDto)
        societiesAddressMapper.toValueObject(society.address!!, societyDto)
        societyDto.countryId = society?.address?.country?.countryId
        societyDto.regionId = society?.address?.region?.regionId
    }
}
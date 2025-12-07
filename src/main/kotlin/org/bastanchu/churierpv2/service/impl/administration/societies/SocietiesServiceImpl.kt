package org.bastanchu.churierpv2.service.impl.administration.societies

import org.bastanchu.churierpv2.dao.administration.addresses.AddressDao
import org.bastanchu.churierpv2.dao.administration.addresses.CountriesDao
import org.bastanchu.churierpv2.dao.administration.addresses.RegionsDao
import org.bastanchu.churierpv2.dao.administration.societies.SocietiesDao
import org.bastanchu.churierpv2.dto.administration.societies.SocietiesFilterDto
import org.bastanchu.churierpv2.dto.administration.societies.SocietyDto
import org.bastanchu.churierpv2.entity.administration.adresses.AddressTypeEnum
import org.bastanchu.churierpv2.entity.administration.adresses.RegionPk
import org.bastanchu.churierpv2.entity.administration.societies.Society
import org.bastanchu.churierpv2.service.administration.societies.SocietiesService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
open class SocietiesServiceImpl(@Autowired val societiesDao : SocietiesDao,
                                @Autowired val addressDao : AddressDao,
                                @Autowired val countriesDao: CountriesDao,
                                @Autowired val regionsDao : RegionsDao) :
           SocietiesService {

    override fun filterSocieties(filterDto: SocietiesFilterDto): List<SocietyDto> {
        var societyFilter = Society()
        societyFilter.commercialName = filterDto.name
        societyFilter.socialName= filterDto.socialName
        val societies = societiesDao.filter(societyFilter)
        return societiesDao.toValueObjectList(societies)
    }

    override fun createSociety(societyDto: SocietyDto) {
        val society = societiesDao.fromValueObjectToEntity(societyDto)
        val address = societiesDao.buildNewAdressFromSocietyDto(societyDto)
        val addressType = addressDao.loadAddressType(AddressTypeEnum.SOCIETY_ADDRESS)
        address.type = addressType.typeId
        val country = countriesDao.getById(societyDto.countryId!!)
        address.country = country
        val regionPk = RegionPk(societyDto.countryId!!, societyDto.regionId!!)
        val region = regionsDao.getById(regionPk)
        address.region = region
        society.address = address
        societiesDao.create(society)
        societyDto.societyId = society.societyId
    }

    override fun updateSociety(societyDto: SocietyDto) {
        val society = societiesDao.fromValueObjectToEntity(societyDto)
        val addressDto = societiesDao.buildNewAdressDtoFromSocietyDto(societyDto)
        val address = society.address!!
        addressDto.addressId = address.addressId
        addressDao.fromValueObjectToEntity(addressDto, address)
        val country = countriesDao.getById(societyDto.countryId!!)
        society.address?.country = country
        val regionPk = RegionPk(societyDto.countryId!!, societyDto.regionId!!)
        val region = regionsDao.getById(regionPk)
        address.region = region
    }

    override fun deleteSociety(societyDto: SocietyDto) {
        societiesDao.deleteById(societyDto.societyId!!)
    }
}
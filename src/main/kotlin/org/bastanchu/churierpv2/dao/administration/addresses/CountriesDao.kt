package org.bastanchu.churierpv2.dao.administration.addresses

import org.bastanchu.churierpv2.dao.BaseValueObjectDao
import org.bastanchu.churierpv2.dto.administration.adresses.CountryDto
import org.bastanchu.churierpv2.entity.administration.adresses.Country

interface CountriesDao: BaseValueObjectDao<String, Country, CountryDto> {
}
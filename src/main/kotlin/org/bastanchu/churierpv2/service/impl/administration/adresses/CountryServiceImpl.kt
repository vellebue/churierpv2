package org.bastanchu.churierpv2.service.impl.administration.adresses

import org.bastanchu.churierpv2.dao.administration.addresses.CountriesDao
import org.bastanchu.churierpv2.service.administration.adresses.CountryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
open class CountryServiceImpl(@param:Autowired val countriesDao: CountriesDao,
                              @param:Autowired val messages: MessageSource) : CountryService {

    override fun retieveCountriesMap(): Map<String, String> {
        val countries = countriesDao.listAll()
        val map = mutableMapOf<String, String>()
        for (country in countries) {
            map[country.countryId!!] = "${country.countryId} - ${messages.getMessage(country.key!!, null,
                LocaleContextHolder.getLocale())}"
        }
        map[""] = ""
        return map
    }

}
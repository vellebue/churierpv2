package org.bastanchu.churierpv2.dao.impl.administration.addresses

import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.bastanchu.churierpv2.dao.administration.addresses.CountriesDao
import org.bastanchu.churierpv2.dao.impl.BaseValueObjectDaoImpl
import org.bastanchu.churierpv2.dao.impl.BaseValueObjectEntityMapperDefaultImpl
import org.bastanchu.churierpv2.dto.administration.adresses.CountryDto
import org.bastanchu.churierpv2.entity.administration.adresses.Country

import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
open class CountriesDaoImpl(@PersistenceContext(unitName = "entityManagerFactory") override val entityManager: EntityManager) :
      BaseValueObjectDaoImpl<String, Country, CountryDto>(entityManager, CountriesMapper()),
      CountriesDao {

      class CountriesMapper : BaseValueObjectEntityMapperDefaultImpl<CountryDto, Country>() {

      }
}
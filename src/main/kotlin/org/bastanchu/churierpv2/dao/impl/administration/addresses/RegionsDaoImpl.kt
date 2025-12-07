package org.bastanchu.churierpv2.dao.impl.administration.addresses

import jakarta.persistence.*

import org.bastanchu.churierpv2.dao.administration.addresses.RegionsDao
import org.bastanchu.churierpv2.dao.impl.BaseValueObjectDaoImpl
import org.bastanchu.churierpv2.dao.impl.BaseValueObjectEntityMapperDefaultImpl
import org.bastanchu.churierpv2.dto.administration.adresses.RegionDto
import org.bastanchu.churierpv2.entity.administration.adresses.Region
import org.bastanchu.churierpv2.entity.administration.adresses.RegionPk

import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
open class RegionsDaoImpl(@PersistenceContext(unitName = "entityManagerFactory") override val entityManager: EntityManager):
      BaseValueObjectDaoImpl<RegionPk, Region, RegionDto>(entityManager, RegionsMapper()),
      RegionsDao {

      class RegionsMapper: BaseValueObjectEntityMapperDefaultImpl<RegionDto, Region>() {

      }
}
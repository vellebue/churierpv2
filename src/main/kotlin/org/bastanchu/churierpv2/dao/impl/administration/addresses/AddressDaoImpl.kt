package org.bastanchu.churierpv2.dao.impl.administration.addresses

import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.bastanchu.churierpv2.dao.administration.addresses.AddressDao
import org.bastanchu.churierpv2.dao.impl.BaseValueObjectDaoImpl
import org.bastanchu.churierpv2.dao.impl.BaseValueObjectEntityMapperDefaultImpl
import org.bastanchu.churierpv2.dto.administration.adresses.AddressDto
import org.bastanchu.churierpv2.entity.administration.adresses.Address
import org.bastanchu.churierpv2.entity.administration.adresses.AddressType
import org.bastanchu.churierpv2.entity.administration.adresses.AddressTypeEnum
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
open class AddressDaoImpl(@PersistenceContext(unitName = "entityManagerFactory") override val entityManager: EntityManager) :
      BaseValueObjectDaoImpl<Int, Address, AddressDto>(entityManager, AddressMapper()),
      AddressDao {

    override fun loadAddressType(type: AddressTypeEnum): AddressType {
        val addressType = entityManager.find(AddressType::class.java, type.toString())
        return addressType
    }

    class AddressMapper : BaseValueObjectEntityMapperDefaultImpl<AddressDto, Address>() {
      }
}
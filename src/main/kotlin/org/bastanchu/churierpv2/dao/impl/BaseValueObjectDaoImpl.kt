package org.bastanchu.churierpv2.dao.impl

import jakarta.persistence.EntityManager
import org.bastanchu.churierpv2.dao.BaseValueObjectDao
import org.bastanchu.churierpv2.dao.ValueObjectEntityMapper
import org.slf4j.LoggerFactory
import java.lang.reflect.ParameterizedType

abstract class BaseValueObjectDaoImpl<K,E,V>(override val entityManager: EntityManager,
                                             val valueObjectEntityMapper : ValueObjectEntityMapper<V, E>
) :
    BaseDaoImpl<K,E>(entityManager) ,
    BaseValueObjectDao<K, E, V> {
    lateinit var valueObjectClassTypeClass : Class<V>
    val logger = LoggerFactory.getLogger(BaseValueObjectDaoImpl::class.java)

    init {
        val genericSuperclass = this::class.java.genericSuperclass;
        if (genericSuperclass is ParameterizedType) {
            val parameterizedType : ParameterizedType = genericSuperclass
            valueObjectClassTypeClass = parameterizedType.actualTypeArguments[2] as Class<V>
        }
    }

    override fun toValueObject(entity: E): V {
        val emptyConstructor = valueObjectClassTypeClass!!.getDeclaredConstructor()
        val valueObjectInstance = emptyConstructor.newInstance()
        //valueObjectEntityMapper.toValueObject(entity, valueObjectInstance)
        toValueObject(entity, valueObjectInstance)
        return valueObjectInstance
    }

    override fun toValueObjectList(entityList: List<E>): List<V> {
        return entityList.map { toValueObject(it) }
    }

    override fun toValueObject(entity: E, valueObject : V) {
        valueObjectEntityMapper.toValueObject(entity, valueObject)
    }

    override fun fromValueObjectToEntity(valueObject : V): E {
        val unboundEntityInstance = valueObjectEntityMapper.buildInstance(entityClassTypeClass as Class<*>) as E
        valueObjectEntityMapper.toEntity(valueObject, unboundEntityInstance)
        val entityKey = entityManager.entityManagerFactory.persistenceUnitUtil.getIdentifier(unboundEntityInstance) as K
        if (entityKey != null) {
            val boundEntity = getById(entityKey)
            if (boundEntity != null) {
                valueObjectEntityMapper.toEntity(valueObject, boundEntity)
                return boundEntity
            } else {
                return unboundEntityInstance
            }
        } else {
            return unboundEntityInstance
        }
    }

    override fun fromValueObjectToEntity(valueObject : V, entity: E) {
        valueObjectEntityMapper.toEntity(valueObject, entity)
    }

    override fun fromValueObjectToEntityList(valueObjectList : List<V>) : List<E> {
        return valueObjectEntityMapper.toEntityList(valueObjectList)
    }

}
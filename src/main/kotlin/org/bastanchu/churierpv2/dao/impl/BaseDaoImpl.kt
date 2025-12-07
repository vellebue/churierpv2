package org.bastanchu.churierpv2.dao.impl

import jakarta.persistence.criteria.*

import jakarta.persistence.EntityManager
import org.bastanchu.churierpv2.dao.BaseDao
import java.lang.reflect.Field
import java.lang.reflect.ParameterizedType

abstract class BaseDaoImpl<K,E>(open val entityManager: EntityManager) : BaseDao<K, E> {

    lateinit var keyClassTypeClass : Class<K>
    lateinit var entityClassTypeClass : Class<E>

    init {
        val genericSuperclass = this::class.java.genericSuperclass;
        if (genericSuperclass is ParameterizedType) {
            val parameterizedType : ParameterizedType = genericSuperclass
            keyClassTypeClass = parameterizedType.actualTypeArguments[0]!! as Class<K>
            entityClassTypeClass = parameterizedType.actualTypeArguments[1]!! as Class<E>
        }
    }

    override fun getById(id: K): E? {
        val entity = entityManager.find(entityClassTypeClass, id)
        return entity
    }

    override fun filter(filter: E): List<E> {
        val declaredFields = getEntityClassDeclaredFields()
        val criteriaBuilder = entityManager.criteriaBuilder
        val criteriaQuery = criteriaBuilder.createQuery(entityClassTypeClass)
        val rootQuery = criteriaQuery.from(entityClassTypeClass)
        var mainPredicate : Predicate? = null
        for (field in declaredFields) {
            field.trySetAccessible()
            val value = field.get(filter)
            if (!isEmptyValue(value)) {
                var predicate: Predicate? = null
                if ((value is String) && (value.contains("*"))) {
                    val likeValue = value.replace("*", "%")
                    predicate = criteriaBuilder.like(rootQuery.get(field.name), likeValue )
                } else {
                    predicate = criteriaBuilder.equal(rootQuery.get<Any>(field.name), value)
                }
                if (mainPredicate == null) {
                    mainPredicate = predicate
                } else {
                    mainPredicate = criteriaBuilder.and(mainPredicate, predicate)
                }
            }
        }
        val query = if (mainPredicate != null) {
            criteriaQuery.select(rootQuery).where(mainPredicate)
        } else {
            criteriaQuery.select(rootQuery)
        }
        val entityManagerQuery = entityManager.createQuery(query)
        val resultList = entityManagerQuery.resultList
        return resultList
    }

    override fun listAll(): List<E> {
        val criteriaBuilder = entityManager.criteriaBuilder
        val criteriaQuery = criteriaBuilder.createQuery(entityClassTypeClass)
        val rootQuery = criteriaQuery.from(entityClassTypeClass)
        val query = criteriaQuery.select(rootQuery)
        val entityManagerQuery = entityManager.createQuery(query)
        val resultList = entityManagerQuery.resultList
        return resultList
    }

    override fun flush() {
        entityManager.flush()
    }

    override fun delete(entity: E) {
        entityManager.remove(entity)
    }

    override fun deleteById(id: K) {
        val entity = entityManager.find(entityClassTypeClass, id)
        if (entity != null) {
            entityManager.remove(entity)
        }
    }

    override fun create(entity: E) {
        entityManager.persist(entity)
    }

    private fun getEntityClassDeclaredFields() : List<Field> {
        val fields = entityClassTypeClass.declaredFields.filter {
            !it.name.startsWith("$")
        }
        return fields
    }

    private fun isEmptyValue(value: Any?): Boolean {
        when (value) {
            null -> return true
            is String -> return (value == "")
            else -> return true
        }
    }
}
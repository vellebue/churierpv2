package org.bastanchu.churierpv2.dao.impl

import org.bastanchu.churierpv2.dao.ValueObjectEntityMapper
import java.lang.reflect.Field
import java.lang.reflect.ParameterizedType
import java.math.BigDecimal
import kotlin.jvm.kotlin
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor

abstract class BaseValueObjectEntityMapperDefaultImpl<V,E> : ValueObjectEntityMapper<V, E> {

    val BASIC_TYPES = arrayOf(Int::class.java, Integer::class.java, String::class.java, BigDecimal::class.java, Boolean::class.java)

    val valueObjectClassType = getParameterizedType<V>(0)
    val entityClassType = getParameterizedType<E>(1)

    override fun toValueObject(entity: E, valueObject : V) {
        assign(entity, entityClassType, valueObject, valueObjectClassType)
    }

    override fun toEntity(valueObject: V, entity : E) {
        assign(valueObject, valueObjectClassType, entity, entityClassType)
    }

    override fun toValueObjectList(entityList: List<E>): List<V> {
        return entityList.map {
            val valueObject = buildInstance(valueObjectClassType)
            toValueObject(it, valueObject)
            valueObject
        }
    }

    override fun toEntityList(valueObjectList: List<V>): List<E> {
        return valueObjectList.map {
            val entity = buildInstance(entityClassType)
            toEntity(it, entity)
            entity
        }
    }

    override fun<T> buildInstance(classInstance : Class<T>) : T {
        /*
        val emptyConstructor = classInstance.getDeclaredConstructor()
        val instance = emptyConstructor.newInstance()
        return instance
         */
        val kotlinClassIntance  = (classInstance as Class<*>).kotlin
        val primaryConstructor = kotlinClassIntance.primaryConstructor
        val paramenters = primaryConstructor!!.parameters
        val parametersList = ArrayList<Any?>()
        paramenters.forEach { param ->
            parametersList.add(null)
        }
        val instance = primaryConstructor.call(*parametersList.toTypedArray())
        return instance as T
    }

    private fun <S, T> assign(source : S, sourceClass : Class<S>,
                              target : T, targetClass : Class<T>) {
        val targetFields = targetClass.declaredFields
        for (targetField in targetFields) {
            val candidateSourceField = getMatchingField(targetField, sourceClass)
            if (candidateSourceField != null)
                if ((BASIC_TYPES.filter { candidateSourceField.type.isAssignableFrom(it) }.size > 0) ||
                    candidateSourceField.type.isEnum) {
                    if (candidateSourceField.type.isEnum && targetField.type.isAssignableFrom(String::class.java)) {
                        // Turn enum into String
                        candidateSourceField.trySetAccessible()
                        val value = candidateSourceField.get(source)
                        targetField.trySetAccessible()
                        targetField.set(target, fromEnumToEstring(value))
                    } else if (candidateSourceField.type.isAssignableFrom(String::class.java) && targetField.type.isEnum) {
                        // Turn String into enum
                        candidateSourceField.trySetAccessible()
                        val value = candidateSourceField.get(source) as String
                        targetField.trySetAccessible()
                        targetField.set(target, fromStringToEnumValue(value, targetField.type))
                    } else {
                        //Is basic type or enum source and enum target are the same type
                        candidateSourceField.trySetAccessible()
                        val value = candidateSourceField.get(source)
                        targetField.trySetAccessible()
                        targetField.set(target, value)
                    }
                }
        }
    }

    fun getMatchingField(sourceField : Field, targetClass : Class<*>) : Field? {
        val targetFieldList = targetClass.declaredFields.filter {
            (it.name == sourceField.name) && (
                    it.type.isAssignableFrom(sourceField.type) ||
                            ( (it.type.isAssignableFrom(String::class.java) && sourceField.type.isEnum) ||
                                    (it.type.isEnum && sourceField.type.isAssignableFrom(String::class.java))
                                    )
                    )
        }
        if (targetFieldList.size > 0) {
            return targetFieldList[0]
        } else {
            return null
        }
    }

    fun <T> getParameterizedType(order : Int) : Class<T> {
        val genericSuperclass = this::class.java.genericSuperclass;
        if (genericSuperclass is ParameterizedType) {
            val parameterizedType : ParameterizedType = genericSuperclass
            return parameterizedType.actualTypeArguments[order] as Class<T>
        } else {
            throw ClassNotFoundException("No parameterized class defined (this may not happen)")
        }
    }

    fun <E> fromEnumToEstring(enumValue : E) : String {
        return enumValue.toString()
    }

    fun <E> fromStringToEnumValue(value : String, enumClass : Class<E>) : E? {
        if (enumClass.isEnum) {
            val enumValues = enumClass.enumConstants
            try {
                val enumValue = enumValues.filter { it.toString().equals(value) }.first()
                return enumValue
            } catch (e: NoSuchElementException) {
                return null
            }
        } else {
            return null;
        }
    }
}
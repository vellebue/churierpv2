package org.bastanchu.churierpv2.dao

interface ValueObjectEntityMapper<V,E> {

    fun toValueObject(entity : E, valueObject : V)

    fun toEntity(valueObject : V, entity :E)

    fun toValueObjectList(entityList : List<E>) : List<V>

    fun toEntityList(valueObjectList : List<V>) : List<E>

    fun <T> buildInstance(classInstance: Class<T>) : T
}
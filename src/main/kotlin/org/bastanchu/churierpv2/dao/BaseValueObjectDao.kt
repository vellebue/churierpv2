package org.bastanchu.churierpv2.dao

interface BaseValueObjectDao<K,E,V> : BaseDao<K,E> {

    public fun toValueObject(entity:E) : V

    public fun toValueObjectList(entityList : List<E>) : List<V>

    public fun toValueObject(entity : E, valueObject : V)

    public fun fromValueObjectToEntity(valueObject : V) : E

    public fun fromValueObjectToEntity(valueObject : V, entity : E)

    public fun fromValueObjectToEntityList(valueObjectList : List<V>) : List<E>
}
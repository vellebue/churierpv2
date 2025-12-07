package org.bastanchu.churierpv2.entity.administration.adresses

import jakarta.persistence.*

enum class AddressTypeEnum(val typeId: String) {

    SOCIETY_ADDRESS("SOC");

    override fun toString(): String {
        return typeId
    }
}

@Entity
@Table(name = "C_ADDRESS_TYPES")
data class AddressType(
    @Id
    @Column(name = "TYPE_ID")
    var typeId: String? = null,
    @Column(name = "DESCRIPTION")
    var description: String? = null,
    @Column(name = "KEY")
    var key: String? = null
)
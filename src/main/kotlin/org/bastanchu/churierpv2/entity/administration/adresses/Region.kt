package org.bastanchu.churierpv2.entity.administration.adresses

import jakarta.persistence.*

@Entity
@Table(name = "C_REGIONS")
@IdClass(RegionPk::class)
data class Region(
    @Id
    @Column(name = "COUNTRY_ID")
    var countryId: String? = null,
    @Column(name = "REGION_ID")
    var regionId: String? = null,
    @Column(name = "NAME")
    var name: String? = null,
    @Column(name = "KEY")
    var key: String? = null
)
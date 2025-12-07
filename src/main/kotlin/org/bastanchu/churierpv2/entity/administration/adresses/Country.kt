package org.bastanchu.churierpv2.entity.administration.adresses

import com.vaadin.flow.component.Key
import jakarta.persistence.*

@Entity
@Table(name = "C_COUNTRIES")
data class Country(
    @Id
    @Column(name = "COUNTRY_ID")
    var countryId: String? = null,
    @Column(name = "NAME")
    var name: String? = null,
    @Column(name = "KEY")
    var key: String? = null
)
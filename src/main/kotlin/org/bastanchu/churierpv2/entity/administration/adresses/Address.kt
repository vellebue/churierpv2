package org.bastanchu.churierpv2.entity.administration.adresses

import jakarta.persistence.*
import org.bastanchu.churierpv2.entity.TraceableEntity

@Entity
@Table(name = "ADDRESSES")
data class Address(
    @Id
    @Column(name = "ADDRESS_ID")
    @GeneratedValue(generator = "seq_addresses", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "seq_addresses", sequenceName = "SEQ_ADRESSES", allocationSize = 1)
    var addressId: Int? = null,
    @Column(name = "TYPE_ID")
    var type: String? = null,
    @Column(name = "ADDRESS")
    var address: String? = null,
    @Column(name = "POSTAL_CODE")
    var postalCode: String? = null,
    @Column(name = "CITY")
    var city: String? = null,
    @ManyToOne
    @JoinColumn(name = "COUNTRY_ID", referencedColumnName = "COUNTRY_ID", insertable = false, updatable = false)
    var country: Country? = null,
    @ManyToOne
    @JoinColumns(
        *arrayOf(JoinColumn(name = "COUNTRY_ID", referencedColumnName = "COUNTRY_ID"),
                          JoinColumn(name = "REGION_ID", referencedColumnName = "REGION_ID"))
    )
    var region: Region? = null
): TraceableEntity()
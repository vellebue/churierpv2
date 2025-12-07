package org.bastanchu.churierpv2.entity.administration.societies

import org.bastanchu.churierpv2.entity.TraceableEntity
import org.bastanchu.churierpv2.entity.administration.adresses.Address

import jakarta.persistence.*


@Entity
@Table(name = "SOCIETIES")
data class Society(
    @Id
    @Column(name = "SOCIETY_ID")
    @GeneratedValue(generator = "seq_societies", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "seq_societies", sequenceName = "SEQ_SOCIETIES", allocationSize = 1)
    val societyId: Int? = null,
    @Column(name = "NAME")
    var name: String? = "",
    @Column(name = "COMMERCIAL_NAME")
    var commercialName: String? = "",
    @Column(name = "SOCIAL_NAME")
    var socialName: String? = "",
    @Column(name = "VAT_NUMBER")
    var vatNumber: String? = "",
    @OneToOne(cascade = [(CascadeType.ALL)])
    @JoinColumn(name = "ADDRESS_ID")
    var address: Address? = null
) : TraceableEntity()

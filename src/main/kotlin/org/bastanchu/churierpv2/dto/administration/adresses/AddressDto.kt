package org.bastanchu.churierpv2.dto.administration.adresses

data class AddressDto(
    var addressId: Int?=null,
    var type: String? = "",
    var address: String? = "",
    var postalCode: String? = "",
    var city: String? = "",
    var countryId: String? = "",
    var regionId: String? = ""
)
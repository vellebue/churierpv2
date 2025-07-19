package org.bastanchu.churierpv2.dto.administration.societies

import org.bastanchu.churierpv2.view.common.annotations.Field
import org.bastanchu.churierpv2.view.common.annotations.ListField

data class SocietyDto(@Field(key = "societies.form.companyId.key")
                      @ListField
                      var companyId: Int?,
                      @Field(key = "societies.form.name.key")
                      @ListField
                      var name: String?,
                      @Field(key = "societies.form.socialName.key")
                      @ListField
                      var socialName: String?,
                      @Field(key = "societies.form.vatNumber.key")
                      @ListField
                      var vatNumber: String?,
                      var addressId: Int?,
                      @Field(key = "societies.form.type.key")
                      @ListField
                      var type: String?,
                      @Field(key = "societies.form.address.key")
                      @ListField
                      var address: String?,
                      @Field(key = "societies.form.postalCode.key")
                      @ListField
                      var postalCode: String?,
                      @Field(key = "societies.form.city.key")
                      @ListField
                      var city: String?,
                      @Field(key = "societies.form.countryId.key")
                      @ListField
                      var countryId: String?,
                      @Field(key = "societies.form.regionId.key")
                      @ListField
                      var regionId: String?) {
}
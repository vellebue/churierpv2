package org.bastanchu.churierpv2.dto.administration.societies

import org.bastanchu.churierpv2.view.common.annotations.Field
import org.bastanchu.churierpv2.view.common.annotations.FormField
import org.bastanchu.churierpv2.view.common.annotations.ListField

data class SocietyDto(@Field(key = "societies.form.companyId.key")
                      @FormField(groupId = 0, indexInGroup = 0)
                      @ListField
                      var companyId: Int? = null,
                      @Field(key = "societies.form.name.key")
                      @FormField(groupId = 0, indexInGroup = 1, colspan = 3)
                      @ListField
                      var name: String? = "",
                      @Field(key = "societies.form.socialName.key")
                      @FormField(groupId = 1, indexInGroup = 2, colspan = 2)
                      @ListField
                      var socialName: String? = "",
                      @Field(key = "societies.form.vatNumber.key")
                      @FormField(groupId = 1, indexInGroup = 0)
                      @ListField
                      var vatNumber: String? = "",
                      var addressId: Int? = null,
                      @Field(key = "societies.form.type.key")
                      @FormField(groupId = 1, indexInGroup = 1)
                      @ListField
                      var type: String? = "",
                      @Field(key = "societies.form.address.key")
                      @FormField(groupId = 2, indexInGroup = 0, colspan= 4)
                      @ListField
                      var address: String? = "",
                      @Field(key = "societies.form.postalCode.key")
                      @FormField(groupId = 3, indexInGroup = 0)
                      @ListField
                      var postalCode: String? = "",
                      @Field(key = "societies.form.city.key")
                      @FormField(groupId = 3, indexInGroup = 1)
                      @ListField
                      var city: String? = "",
                      @Field(key = "societies.form.countryId.key")
                      @FormField(groupId = 3, indexInGroup = 2)
                      @ListField
                      var countryId: String? = "",
                      @Field(key = "societies.form.regionId.key")
                      @FormField(groupId = 3, indexInGroup = 3)
                      @ListField
                      var regionId: String? = "") {
}
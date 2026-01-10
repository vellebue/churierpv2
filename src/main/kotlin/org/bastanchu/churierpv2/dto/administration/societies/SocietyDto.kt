package org.bastanchu.churierpv2.dto.administration.societies

import jakarta.validation.ConstraintViolation
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.bastanchu.churierpv2.dto.ConstraintViolationImpl
import org.bastanchu.churierpv2.dto.Valid
import org.bastanchu.churierpv2.service.SpringContextHolder
import org.bastanchu.churierpv2.service.administration.societies.SocietiesService
import org.bastanchu.churierpv2.view.common.annotations.ComboBoxConfiguration
import org.bastanchu.churierpv2.view.common.annotations.Field
import org.bastanchu.churierpv2.view.common.annotations.FormField
import org.bastanchu.churierpv2.view.common.annotations.ListField


data class SocietyDto(@Field(key = "societies.form.societyId.key")
                      @FormField(groupId = 0, indexInGroup = 0)
                      @ListField(identifier = true)
                      var societyId: Int? = null,
                      @Field(key = "societies.form.name.key")
                      @FormField(groupId = 0, indexInGroup = 1, colspan = 1)
                      @ListField
                      @get:NotBlank
                      @get:Size(max = 100)
                      var name: String? = "",
                      @Field(key = "societies.form.commercialName.key")
                      @FormField(groupId = 0, indexInGroup = 2, colspan = 2)
                      @ListField
                      @get:NotBlank
                      @get:Size(max = 100)
                      var commercialName: String? = "",
                      @Field(key = "societies.form.socialName.key")
                      @FormField(groupId = 1, indexInGroup = 3, colspan = 2)
                      @ListField
                      @get:NotBlank
                      @get:Size(max = 100)
                      var socialName: String? = "",
                      @Field(key = "societies.form.vatNumber.key")
                      @FormField(groupId = 1, indexInGroup = 0)
                      @ListField
                      @get:NotBlank
                      @get:Size(max = 15)
                      var vatNumber: String? = "",
                      var addressId: Int? = null,
                      @Field(key = "societies.form.type.key")
                      @FormField(groupId = 1, indexInGroup = 1)
                      @ListField
                      @get:NotBlank
                      @get:Size(max = 4)
                      var type: String? = "",
                      @Field(key = "societies.form.address.key")
                      @FormField(groupId = 2, indexInGroup = 0, colspan= 4)
                      @ListField
                      @get:NotBlank
                      @get:Size(max = 512)
                      var address: String? = "",
                      @Field(key = "societies.form.postalCode.key")
                      @FormField(groupId = 3, indexInGroup = 0)
                      @ListField
                      @get:NotBlank
                      @get:Size(max = 15)
                      var postalCode: String? = "",
                      @Field(key = "societies.form.city.key")
                      @FormField(groupId = 3, indexInGroup = 1)
                      @ListField
                      @get:NotBlank
                      @get:Size(max = 100)
                      var city: String? = "",
                      @Field(key = "societies.form.countryId.key")
                      @FormField(groupId = 3, indexInGroup = 2, comboBoxConfiguration = ComboBoxConfiguration(mapFieldName = "countriesMap"))
                      @ListField
                      @get:NotBlank
                      @get:Size(max = 2)
                      var countryId: String? = "",
                      var countriesMap: Map<String, String> = mutableMapOf(),
                      @Field(key = "societies.form.regionId.key")
                      @FormField(groupId = 3, indexInGroup = 3, comboBoxConfiguration = ComboBoxConfiguration(mapFieldName = "regionsMap", conditionFieldName = "countryId"))
                      @ListField
                      @get:NotBlank
                      @get:Size(max = 2)
                      var regionId: String? = "",
                      var regionsMap: Map<String, Map<String, String>> = mutableMapOf()) : Valid {

    var societiesService: SocietiesService = SpringContextHolder.getCurrentApplicationContext().getBean(SocietiesService::class.java)

    override fun validate(): List<ConstraintViolation<*>> {
        val constraintViolations = ArrayList<ConstraintViolation<*>>()
        // Check if there is another society with the same VAT Number
        val filter = SocietiesFilterDto(vatNumber = vatNumber)
        val existingSocieties = societiesService.filterSocieties(filter)
        val differentSocieties = existingSocieties.filter { (it.vatNumber == vatNumber) && (it.societyId != societyId)  }
        if (differentSocieties.isNotEmpty()) {
            val constraintViolationImpl = ConstraintViolationImpl<SocietyDto>(this, "vatNumber", "societies.form.error.vatNumber.existingVatNumber",  arrayOf(vatNumber) as Array<Any>, vatNumber as Any)
            constraintViolations.add(constraintViolationImpl)
        }
        return constraintViolations
    }

}
package org.bastanchu.churierpv2.dto.administration.accountancy.incominginvoices

import com.github.mvysny.karibudsl.v10.Strong
import org.bastanchu.churierpv2.view.common.annotations.ComboBoxConfiguration
import org.bastanchu.churierpv2.view.common.annotations.Field
import org.bastanchu.churierpv2.view.common.annotations.FormField
import org.bastanchu.churierpv2.view.common.annotations.ListField
import java.math.BigDecimal
import java.util.Date

data class IncomingInvoiceDto(@Field(key = "receivingInvoices.form.invoiceId.key")
                              @FormField(groupId = 0, indexInGroup = 0)
                              @ListField(identifier = true)
                              var invoiceId : Int? = null,
                              @Field(key = "receivingInvoices.form.invoiceDate.key")
                              @FormField(groupId = 0, indexInGroup = 1)
                              @ListField
                              var invoiceDate: Date? = null,
                              @Field(key = "receivingInvoices.form.operationDate.key")
                              @FormField(groupId = 0, indexInGroup = 2)
                              @ListField
                              var operationDate: Date? = null,
                              @Field(key = "receivingInvoices.form.incomingSocialName.key")
                              @FormField(groupId = 1, indexInGroup = 0, colspan = 2)
                              @ListField
                              var incomingSocialName: String = "",
                              @Field(key = "receivingInvoices.form.incomingVatNumber.key")
                              @FormField(groupId = 1, indexInGroup = 1, colspan = 2)
                              @ListField
                              var incomingVatNumber: String = "",
                              @Field(key = "receivingInvoices.form.incomingAddress.key")
                              @FormField(groupId = 2, indexInGroup = 0, colspan = 4)
                              var incomingAddress: String = "",
                              @Field(key = "receivingInvoices.form.incomingPostalCode.key")
                              @FormField(groupId = 3, indexInGroup = 0)
                              var incomingPostalCode: String = "",
                              @Field(key = "receivingInvoices.form.incomingCity.key")
                              @FormField(groupId = 3, indexInGroup = 1)
                              var incomingCity: String = "",
                              @Field(key = "receivingInvoices.form.incomingCountryId.key")
                              @FormField(groupId = 3, indexInGroup = 2, comboBoxConfiguration = ComboBoxConfiguration(mapFieldName = "countriesMap"))
                              var incomingCountryId: String = "",
                              var countriesMap: Map<String, String> = mutableMapOf(),
                              @Field(key = "receivingInvoices.form.incomingRegionId.key")
                              @FormField(groupId = 3, indexInGroup = 3, comboBoxConfiguration = ComboBoxConfiguration(mapFieldName = "regionsMap", conditionFieldName = "incomingCountryId"))
                              var incomingRegionId: String = "",
                              var regionsMap: Map<String, Map<String, String>> = mutableMapOf(),
                              @Field(key = "receivingInvoices.form.taxRetentionPercentage.key")
                              @FormField(groupId = 4, indexInGroup = 0)
                              @ListField
                              var taxRetentionPercentage: BigDecimal = BigDecimal.ZERO,
                              @Field(key = "receivingInvoices.form.societyId.key")
                              @FormField(groupId = 4, indexInGroup = 1, colspan = 2, comboBoxConfiguration = ComboBoxConfiguration(mapFieldName = "societiesMap"))
                              var societyId: String? = "",
                              var societiesMap: Map<String, String> = mutableMapOf(),
                              var invoiceLines: MutableList<IncomingInvoiceLineDto> = ArrayList(),
                              @Field(key = "receivingInvoices.form.totalBaseValue.key")
                              @FormField(groupId = 5, indexInGroup = 0)
                              @ListField
                              var totalBaseValue: BigDecimal = BigDecimal.ZERO,
                              @Field(key = "receivingInvoices.form.totalVatValue.key")
                              @FormField(groupId = 5, indexInGroup = 1)
                              @ListField
                              var totalVatValue: BigDecimal = BigDecimal.ZERO,
                              @Field(key = "receivingInvoices.form.totalTaxRetentionValue.key")
                              @FormField(groupId = 5, indexInGroup = 2)
                              @ListField
                              var totalTaxRetentionValue: BigDecimal = BigDecimal.ZERO,
                              @Field(key = "receivingInvoices.form.totalValue.key")
                              @FormField(groupId = 5, indexInGroup = 3)
                              @ListField
                              var totalValue: BigDecimal = BigDecimal.ZERO)
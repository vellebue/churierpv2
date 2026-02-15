package org.bastanchu.churierpv2.dto.administration.accountancy.incominginvoices

import org.bastanchu.churierpv2.view.common.annotations.Field
import org.bastanchu.churierpv2.view.common.annotations.FormField
import java.util.Date

data class IncomingInvoiceFilterDto(@Field("receivingInvoices.filter.invoiceDateFrom.key")
                                    @FormField(groupId = 0, indexInGroup = 0)
                                    var invoiceDateFrom: Date? = null,
                                    @Field("receivingInvoices.filter.invoiceDateTo.key")
                                    @FormField(groupId = 0, indexInGroup = 1)
                                    var invoiceDateTo: Date? = null,
                                    @Field("receivingInvoices.filter.incomingSocialName.key")
                                    @FormField(groupId = 1, indexInGroup = 0, colspan = 2)
                                    var incomingSocialName: String? = null,
                                    @Field("receivingInvoices.filter.incomingVatNumber.key")
                                    @FormField(groupId = 1, indexInGroup = 1)
                                    var incomingVatNumber: String? = null,
                                    @Field("receivingInvoices.filter.incomingAddress.key")
                                    @FormField(groupId = 2, indexInGroup = 0, colspan = 3)
                                    var incomingAddress: String? = null,
                                    @Field("receivingInvoices.filter.incomingCity.key")
                                    @FormField(groupId = 3, indexInGroup = 0)
                                    var incomingCity: String? = null,
                                    @Field("receivingInvoices.filter.incomingCountryId.key")
                                    @FormField(groupId = 3, indexInGroup = 1)
                                    var incomingCountryId: String? = null,
                                    @Field("receivingInvoices.filter.incomingRegionId.key")
                                    @FormField(groupId = 3, indexInGroup = 2)
                                    var incomingRegionId: String? = null)

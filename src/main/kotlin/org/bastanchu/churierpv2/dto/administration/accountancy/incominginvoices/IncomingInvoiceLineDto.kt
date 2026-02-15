package org.bastanchu.churierpv2.dto.administration.accountancy.incominginvoices

import java.math.BigDecimal

data class IncomingInvoiceLineDto(var articleDescription: String = "",
                                  var vatTypeId: String = "",
                                  var quantity: BigDecimal = BigDecimal.ZERO,
                                  var quantityUnitId: String = "",
                                  var baseValue: BigDecimal = BigDecimal.ZERO,
                                  var taxValue: BigDecimal = BigDecimal.ZERO,
                                  var totalValue: BigDecimal = BigDecimal.ZERO)
package org.bastanchu.churierpv2.service.accountancy

import java.io.InputStream

interface IncomingInvoicesIAService {

    fun parseInvoice(inputStream: InputStream): String

}
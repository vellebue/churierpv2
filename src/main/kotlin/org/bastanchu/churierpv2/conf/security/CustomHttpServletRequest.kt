package org.bastanchu.churierpv2.conf.security

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletRequestWrapper
import java.util.Collections
import java.util.Enumeration

class CustomHttpServletRequest(request: HttpServletRequest) : HttpServletRequestWrapper(request) {

    val customerHeaderMap: MutableMap<String,String> = HashMap()
    var customerRequestURI:String? = null
    var customMethod: String? = null

    fun addCustomHeader(name: String, value: String) {
        customerHeaderMap[name] = value
    }

    override fun getHeader(name: String?): String? {
        val previousHeader = super.getHeader(name)
        if (previousHeader != null) {
            return previousHeader
        } else {
            return customerHeaderMap[name]
        }
    }

    override fun getHeaderNames(): Enumeration<String?>? {
        val previousEnum = super.getHeaderNames()
        val enumList = previousEnum.toList()
        val targetList = ArrayList<String>()
        targetList.addAll(enumList)
        targetList.addAll(customerHeaderMap.keys)
        return Collections.enumeration(targetList as Collection<String>)
    }

    override fun getHeaders(name: String?): Enumeration<String?>? {
        val headers = super.getHeaders(name)
        if (headers.toList().size > 0) {
            return headers
        } else {
            val value = customerHeaderMap[name]
            if (value != null) {
                return Collections.enumeration(Collections.singletonList(value))
            } else {
                return headers
            }
        }
    }

    fun setRequestURI(uri: String) {
        customerRequestURI = uri
    }

    override fun getRequestURI(): String? {
        if (customerRequestURI != null) {
            return customerRequestURI
        } else {
            return super.getRequestURI()
        }
    }

    override fun getRequestURL(): StringBuffer? {
        return StringBuffer(getRequestURI())
    }

    override fun getMethod(): String? {
        if (customMethod != null) {
            return customMethod
        } else {
            return super.getMethod()
        }
    }

    fun setMethod(method: String) {
        customMethod = method
    }
}

package org.bastanchu.churierpv2.service.impl.administration.users

import org.bastanchu.churierpv2.dto.administration.users.JwtDto
import org.bastanchu.churierpv2.service.administration.users.UsersService
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestClient

@Service
@Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
open class UsersServiceImpl(@Value("\${jwt.url.token}") val tokenUri: String,
                            @Value("\${jwt.app.clientid}") val clientId: String,
                            @Value("\${jwt.app.clientsecret}") val clientSecret: String) : UsersService {

    override fun authenticateUser(username: String, password: String): String {
        val client = RestClient.create()
        val requestMap = LinkedMultiValueMap<String, String>()
        requestMap["grant_type"] = "password"
        requestMap["username"] = username
        requestMap["password"] = password
        requestMap["client_id"] = clientId
        requestMap["client_secret"] = clientSecret
        requestMap["scope"] = "openid"
        val request = client.post().uri(tokenUri)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(requestMap)
            .accept(MediaType.APPLICATION_JSON)
        val response = request.retrieve()
        try {
            val jwt = response.body(JwtDto::class.java)
            return jwt.accessToken
        } catch (e: Exception) {
            return ""
        }
    }
}
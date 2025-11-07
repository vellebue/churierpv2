package org.bastanchu.churierpv2.service.administration.users

interface UsersService {

    fun authenticateUser(username: String, password: String): String

}
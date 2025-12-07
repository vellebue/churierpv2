package org.bastanchu.churierpv2.entity

import org.springframework.security.core.context.SecurityContextHolder
import java.sql.Timestamp

import jakarta.persistence.*
import org.springframework.security.oauth2.jwt.Jwt

@MappedSuperclass
abstract class TraceableEntity {

    @Column(name = "CREATION_USER")
    var creationUser : String? = null

    @Column(name = "CREATION_TIME")
    var creationTime : Timestamp? = null

    @Column(name = "UPDATE_USER")
    var updateUser : String? = null

    @Column(name = "UPDATE_TIME")
    var updateTime : Timestamp? = null

    private fun getCurrentUserLoginInSession(): String {
        val auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth is Jwt) {
            val claims = auth?.claims
            if (claims != null) {
                val login = claims.get("preferred_username") as String
                if (login != null) {
                    return login
                }
            }
            return auth.name
        } else if ((auth != null) && (auth.name != null)) {
            return auth.name
        } else {
            return "root"
        }
    }

    @PrePersist
    private fun onPrePresist() {
        val currCreationUser = getCurrentUserLoginInSession()
        creationUser = currCreationUser
        creationTime = Timestamp(java.util.Date().time)
        updateUser = currCreationUser
        updateTime = Timestamp(java.util.Date().time)
    }

    @PreUpdate
    private fun onPreUpdate() {
        val currUpdateUser = getCurrentUserLoginInSession()
        updateUser = currUpdateUser
        updateTime = Timestamp(java.util.Date().time)
    }

}
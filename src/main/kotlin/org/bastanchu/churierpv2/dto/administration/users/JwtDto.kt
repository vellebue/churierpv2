package org.bastanchu.churierpv2.dto.administration.users

import com.fasterxml.jackson.annotation.JsonProperty

data class JwtDto(@field:JsonProperty("access_token") var accessToken: String = "",
                  @field:JsonProperty("expires_in") var expiresIn: Int = 0,
                  @field:JsonProperty("refresh_expires_in") var refreshExpiresIn: Int = 0,
                  @field:JsonProperty("refresh_token") var refreshToken: String = "",
                  @field:JsonProperty("token_type") var tokenType: String = "",
                  @field:JsonProperty("id_token") var idToken: String = "",
                  @field:JsonProperty("not-before-policy") var notBeforePolicy: Int = 0,
                  @field:JsonProperty("session_state") var sessionState: String = "",
                  @field:JsonProperty("scope") var scope: String = "")
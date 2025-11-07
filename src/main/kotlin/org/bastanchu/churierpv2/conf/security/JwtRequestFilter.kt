package org.bastanchu.churierpv2.conf.security

import com.google.common.reflect.TypeToken
import com.nimbusds.jose.shaded.gson.GsonBuilder

import jakarta.servlet.FilterChain
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

import org.bastanchu.churierpv2.service.administration.users.UsersService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.core.env.Environment
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.filter.OncePerRequestFilter

import java.util.*


@Component
class JwtRequestFilter(@Autowired val usersService: UsersService,
                       @Autowired val env: Environment) : OncePerRequestFilter() {

    val logger = LoggerFactory.getLogger(JwtRequestFilter::class.java)
    val LOGIN_REFERENCE_URI = "/login"
    val LOGIN_REFERRENCE_METHOD = RequestMethod.POST.toString()
    val ROOT_REFERENCE_URI = "/"
    val ROOT_REFERENCE_METHOD = RequestMethod.POST.toString()
    val BEARER_TOKEN_COOKIE_NAME = "bearerToken"

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        var targetRequest: HttpServletRequest? = request
        logger.info("Processing URL: ${request.requestURI} method: ${request.method}")
        val authenticationHeaders = request.getHeaders("Authorization")
        val bearerAuthenticationHeaders = authenticationHeaders.toList().filter { it.startsWith("Bearer") }
        val bearerToken = if (bearerAuthenticationHeaders.size > 1) (bearerAuthenticationHeaders[0]) else ""
        if (bearerToken.isNotEmpty()) {
            logger.info("URL with bearer token")
            filterChain.doFilter(targetRequest, response)
        } else if ((request.requestURI == ROOT_REFERENCE_URI) && (request.method == ROOT_REFERENCE_METHOD)) {
            // Perform user authetication
            val username = request.getParameter("username")
            val password = request.getParameter("password")
            val tokenCookie = request.cookies.filter { it.name == BEARER_TOKEN_COOKIE_NAME }.firstOrNull()
            if ((username != null) && (password != null)) {
                logger.info("Performing authentication for username $username")
                targetRequest = authenticateUser(username, password, targetRequest!!, response)
                if (targetRequest == null) {
                    logger.info("Invalid login, redirecting to login error")
                    response.sendRedirect("/login?error")
                } else {
                    filterChain.doFilter(targetRequest, response)
                }
            } else if (tokenCookie != null) {
                logger.info("Processing authentication based on active cookie")
                targetRequest = authenticateTokenCookie(tokenCookie.value, request)
                filterChain.doFilter(targetRequest, response)
            } else {
                // TODO Try to invalidate authentication only if you are not working with login page
                logger.info("No token active cookie found, set empty token")
                val targetRequest = autenthicateEmptyTokenToFail(request)
                filterChain.doFilter(targetRequest, response)
            }
        } else {
            filterChain.doFilter(targetRequest, response)
        }
    }

    private fun authenticateUser(username: String, password: String, request: HttpServletRequest, response: HttpServletResponse) : HttpServletRequest? {
        // TODO Handle authentication failure
        val token = usersService.authenticateUser(username, password)
        val tokenCookieTimeout = Integer.parseInt(env.getProperty("jwt.tokentimeout"))
        if (token != "") {
            val targetRequest = CustomHttpServletRequest(request)
            targetRequest.addCustomHeader("Authorization", "Bearer $token")
            val usernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken(username, null, ArrayList())
            usernamePasswordAuthenticationToken.details = WebAuthenticationDetailsSource().buildDetails(targetRequest)
            SecurityContextHolder.getContext().authentication = usernamePasswordAuthenticationToken
            // Generate temporary cookie for Bearer token
            val tokenCookie = Cookie(BEARER_TOKEN_COOKIE_NAME, token)
            tokenCookie.path = "/"
            tokenCookie.maxAge = tokenCookieTimeout
            response.addCookie(tokenCookie)
            return targetRequest
        } else {
            return null
        }
    }

    private fun authenticateTokenCookie(token: String, request: HttpServletRequest) : HttpServletRequest {
        val targetRequest = CustomHttpServletRequest(request)
        val base64Decoder = Base64.getUrlDecoder()
        val tokenChunks = token.split(".")
        if (tokenChunks.size > 1) {
            try {
                val headerBytes = base64Decoder.decode(tokenChunks[0])
                val payloadBytes = base64Decoder.decode(tokenChunks[1])
                val signatureBytes = if (tokenChunks.size > 1) base64Decoder.decode(tokenChunks[2]) else ""
                val gson = GsonBuilder().create()
                val gsonType = object : TypeToken<MutableMap<String, Any?>>() {}.getType()
                val parsedToken = gson.fromJson<Map<String, Any?>>(String(payloadBytes), gsonType)
                val username = parsedToken["preferred_username"]
                val usernamePasswordAuthenticationToken =
                    UsernamePasswordAuthenticationToken(username, null, ArrayList())
                usernamePasswordAuthenticationToken.details =
                    WebAuthenticationDetailsSource().buildDetails(targetRequest)
                SecurityContextHolder.getContext().authentication = usernamePasswordAuthenticationToken
                targetRequest.addCustomHeader("Authorization", "Bearer $token")
            } catch (ex: Exception) {
                logger.error("Invalid token $token", ex)
            }
        }
        return targetRequest
    }

    private fun autenthicateEmptyTokenToFail(request: HttpServletRequest) :HttpServletRequest {
        val targetRequest = CustomHttpServletRequest(request)
        targetRequest.addCustomHeader("Authorization", "Bearer ")
        return targetRequest
    }
}
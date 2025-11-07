package org.bastanchu.churierpv2.view.main

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.html.NativeLabel
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.server.VaadinServletRequest
import com.vaadin.flow.server.VaadinServletResponse
import jakarta.servlet.http.Cookie
import org.springframework.context.ApplicationContext
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler


class TitleBar(val messages : MessageSource,
               val applicationContext: ApplicationContext,
               bodyContainer : Component) : VerticalLayout() {

    init {
        val titleComponent = buildTitle(messages)
        add(titleComponent)
        val barContainer = HorizontalLayout()
        barContainer.addClassName("bar-container")
        val menu = buildMenu(messages, applicationContext, bodyContainer)
        val leftBar = buildLeftBar(messages)
        barContainer.add(menu)
        barContainer.add(leftBar)
        add(barContainer)
    }

    companion object {

        const val LOGOUT_SUCCESS_URL = "/"
        const val BEARER_TOKEN_COOKIE_NAME = "bearerToken"

        fun buildTitle(messages : MessageSource) : Component {
            val mainTile = messages.getMessage("main.title", null, LocaleContextHolder.getLocale())
            val horizontalLayout = HorizontalLayout()
            horizontalLayout.addClassName("main-title")
            val label = NativeLabel(mainTile)
            label.addClassName("main-title-label")
            horizontalLayout.add(label)
            return horizontalLayout
        }

        fun buildMenu(messages : MessageSource,
                      applicationContext: ApplicationContext,
                      bodyContainer : Component) : Component {
            return MenuBuilder.buildMenu(messages, applicationContext, bodyContainer)
        }

        fun buildLeftBar(messages : MessageSource) : Component {
            val leftBar = HorizontalLayout()
            leftBar.addClassName("left-bar")
            val logoutButton = Button(messages.getMessage("leftBar.button.logout", null, LocaleContextHolder.getLocale()))
            logoutButton.addClickListener {
                UI.getCurrent().page.setLocation(LOGOUT_SUCCESS_URL)
                val logoutHandler = SecurityContextLogoutHandler()
                val response = VaadinServletResponse.getCurrent().httpServletResponse
                // Expiry token cookie
                val tokenCookie = Cookie(BEARER_TOKEN_COOKIE_NAME, "")
                tokenCookie.path = "/"
                tokenCookie.maxAge = 0
                response.addCookie(tokenCookie)
                logoutHandler.logout(
                    VaadinServletRequest.getCurrent().httpServletRequest, null,
                    null
                )
            }
            leftBar.add(logoutButton)
            return leftBar
        }

    }

}
package org.bastanchu.churierpv2

import com.github.mvysny.karibudsl.v10.KComposite
import com.github.mvysny.karibudsl.v10.verticalLayout

import com.vaadin.flow.component.login.LoginForm
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.Route
import com.vaadin.flow.server.auth.AnonymousAllowed

@Route("/login")
@AnonymousAllowed
class LoginView : KComposite() {

    private val root = ui {

        verticalLayout() {
            setSizeFull()
            val verticalLayout = VerticalLayout()
            verticalLayout.justifyContentMode = FlexComponent.JustifyContentMode.CENTER
            verticalLayout.isSpacing = true
            verticalLayout.setSizeFull()
            val loginContainer = HorizontalLayout()
            loginContainer.addClassName("login-container")
            val loginForm = LoginForm()
            loginForm.action = "login"
            verticalLayout.add(loginContainer)
            loginContainer.add(loginForm)
            add(verticalLayout)
            setLoginOptions(loginForm)
        }

    }

    private fun setLoginOptions(loginForm: LoginForm) {
        loginForm.isForgotPasswordButtonVisible = false
    }
}
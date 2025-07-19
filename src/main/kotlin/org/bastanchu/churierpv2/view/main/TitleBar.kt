package org.bastanchu.churierpv2.view.main

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.html.NativeLabel
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import org.springframework.context.ApplicationContext
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder


class TitleBar(val messages : MessageSource,
               val applicationContext: ApplicationContext,
               bodyContainer : Component) : VerticalLayout() {

    init {
        val titleComponent = buildTitle(messages)
        add(titleComponent)
        val menu = buildMenu(messages, applicationContext, bodyContainer)
        add(menu)
    }

    companion object {

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

    }

}
package org.bastanchu.churierpv2.view.main

import com.github.mvysny.kaributools.hasChildren
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.contextmenu.MenuItem
import com.vaadin.flow.component.menubar.MenuBar
import org.bastanchu.churierpv2.view.common.view.BaseView
import org.springframework.context.ApplicationContext
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.yaml.snakeyaml.LoaderOptions
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor
import java.util.stream.Collectors

class MenuBuilder() {

    companion object instance {

        fun buildMenu(messages: MessageSource,
                      applicationContext: ApplicationContext,
                      bodyContainer : Component) : MenuBar {
            val menuModelList = parseMenu()
            val menuBar = buildMenuBar(menuModelList, messages, applicationContext, bodyContainer)
            return menuBar
        }

        private fun parseMenu() : List<MenuModel> {
            val stream = this.javaClass.classLoader.getResourceAsStream("menu.yaml")
            val yaml = Yaml(Constructor(MenuListModel::class.java, LoaderOptions()))
            val menuModelList : MenuListModel = yaml.load(stream)
            return menuModelList.menuList
        }

        private fun buildMenuBar(menuModelList : List<MenuModel>,
                                 messages : MessageSource,
                                 applicationContext: ApplicationContext,
                                 bodyContainer: Component) : MenuBar {
            val menuBar = MenuBar()
            menuModelList.forEach {
                val currentMenu = menuBar.addItem(messages.getMessage(it.textKey, null, LocaleContextHolder.getLocale()))
                currentMenu.setId("menu.${it.id}")
                buildSubmenus(currentMenu, it, messages, applicationContext,  bodyContainer)
            }
            return menuBar
        }

        private fun buildSubmenus(menuItem : MenuItem, menuModel: MenuModel ,
                                  messages : MessageSource,
                                  applicationContext: ApplicationContext,
                                  bodyContainer: Component) {
            menuModel.children.forEach {
                val currentMenuModel = it
                menuItem
                val subMenu = menuItem.subMenu
                val subMenuItem = subMenu.addItem(messages.getMessage(it.textKey, null, LocaleContextHolder.getLocale()))
                subMenuItem.setId("menu.${it.id}")
                if (currentMenuModel.viewClass.isNotEmpty()) {
                    subMenuItem.addClickListener {
                        menuClickHandler(currentMenuModel.viewClass, messages, applicationContext, bodyContainer)
                    }
                }
                buildSubmenus(subMenuItem, it, messages, applicationContext, bodyContainer)
            }
        }

        private fun menuClickHandler(viewClassName : String,
                                     messages : MessageSource,
                                     applicationContext: ApplicationContext,
                                     bodyContainer: Component) {
            if (viewClassName.isNotEmpty()) {
                val childrenList = bodyContainer.children.collect(Collectors.toList())
                if (childrenList.isNotEmpty()) {
                    val currentView = childrenList.get(0) as BaseView
                    currentView.stop()
                    currentView.removeFromParent()
                }
                val viewClass = Class.forName(viewClassName)
                val constructor = viewClass.getDeclaredConstructor(MessageSource::class.java, ApplicationContext::class.java)
                val view = constructor.newInstance(messages, applicationContext) as BaseView
                view.start()
                (bodyContainer as HasComponents).add(view)
            }
        }

    }
}
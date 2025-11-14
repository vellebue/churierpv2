package org.bastanchu.churierpv2.view.common.view

import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import org.bastanchu.churierpv2.view.common.Form
import org.bastanchu.churierpv2.view.common.button.BlueButton
import org.springframework.context.ApplicationContext
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import java.lang.reflect.ParameterizedType

abstract class BaseFilterView<F, L> (val messages: MessageSource,
                                     val applicationContext: ApplicationContext,
                                     val parent: BaseCRUDView<F,L>) : VerticalLayout(), Autowirable {

    val  filterDtoClass: Class<F> = (this::class.java.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<F>

    init {
        autowireComponents(applicationContext)
        val mainView = VerticalLayout()
        val formModel = filterDtoClass.getDeclaredConstructor().newInstance()
        val form = Form(getFormTitleKey(), formModel, messages)
        mainView.add(form)
        add(mainView)
        val buttonBar = buildButtonBar(form)
        mainView.add(buttonBar)
    }

    private fun buildButtonBar(form: Form<F>): HorizontalLayout {
        val buttonBar = HorizontalLayout()
        buttonBar.addClassName("grid-button-bar")
        val filterButton =
            BlueButton(messages.getMessage("baseFilterView.filterButton.key", null, LocaleContextHolder.getLocale()))
        filterButton.addClickListener {
            val currentFormModel = form.retrieveFormModel()
            val itemsList = filterItems(currentFormModel)
            parent.notifyFilterPerformed(itemsList)
        }
        buttonBar.add(filterButton)
        return buttonBar
    }

    private fun filterItems(filterDto: F): List<L> {
        return doFilter(filterDto)
    }

    abstract fun getFormTitleKey(): String

    abstract fun doFilter(filterDto: F) : List<L>

}
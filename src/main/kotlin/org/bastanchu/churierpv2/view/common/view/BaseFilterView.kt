package org.bastanchu.churierpv2.view.common.view

import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import org.bastanchu.churierpv2.view.common.Form
import org.springframework.context.ApplicationContext
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import java.lang.reflect.ParameterizedType

abstract class BaseFilterView<F, L> (val messages: MessageSource,
                                     val applicationContext: ApplicationContext,
                                     val parent: BaseCRUDView<F,L>) : VerticalLayout(), Autowirable {

    var  filterDtoClass: Class<F>
    var  listItemDtoClass: Class<L>

    init {
        filterDtoClass = (this::class.java.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<F>
        listItemDtoClass = (this::class.java.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<L>
        autowireComponents(applicationContext)
        val mainView = VerticalLayout()
        val formModel = filterDtoClass.getDeclaredConstructor().newInstance()
        val form = Form(getFormTitleKey(), formModel, messages)
        mainView.add(form)
        add(mainView)
        val buttonBar = HorizontalLayout()
        val filterButton =
            Button(messages.getMessage("baseFilterView.filterButton.key", null, LocaleContextHolder.getLocale()))
        filterButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY)
        filterButton.addClickListener {
            val currentFormModel = form.retrieveFormModel()
            val itemsList = doFilter(currentFormModel)
            parent.notifyFilterPerformed(itemsList)
        }
        buttonBar.add(filterButton)
        mainView.add(buttonBar)
    }

    abstract fun getFormTitleKey(): String

    abstract fun doFilter(filterDto: F) : List<L>

}
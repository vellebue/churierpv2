package org.bastanchu.churierpv2.view.common.view

import com.vaadin.flow.component.orderedlayout.VerticalLayout
import org.bastanchu.churierpv2.view.common.Form
import org.springframework.context.ApplicationContext
import org.springframework.context.MessageSource
import java.lang.reflect.ParameterizedType

abstract class BaseDetailView<L>(val messages: MessageSource,
                        val applicationContext: ApplicationContext,
                        val parent: BaseCRUDView<*, L> ): VerticalLayout(), Autowirable {

    val listItemDtoClass: Class<L> = (this::class.java.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<L>
    lateinit var form: Form<L>

    init {
        autowireComponents(applicationContext)
        val mainView = VerticalLayout()
        val formModel = listItemDtoClass.getDeclaredConstructor().newInstance()
        val form = Form(getFormTitleKey(), formModel, messages)
        this.form = form
        mainView.add(form)
        add(mainView)
    }

    fun establishFormModel(formModel: L) {
        form.establishFormModel(formModel)
    }

    abstract fun getFormTitleKey(): String
}

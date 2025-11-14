package org.bastanchu.churierpv2.view.common.view

import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import org.bastanchu.churierpv2.view.common.Form
import org.bastanchu.churierpv2.view.common.button.BlueButton
import org.bastanchu.churierpv2.view.common.button.GreenButton
import org.bastanchu.churierpv2.view.common.button.RedButton
import org.springframework.context.ApplicationContext
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import java.lang.reflect.ParameterizedType

abstract class BaseDetailView<L>(val messages: MessageSource,
                        val applicationContext: ApplicationContext,
                        val parent: BaseCRUDView<*, L>,
                        val insertMode: Boolean = false): VerticalLayout(), Autowirable {

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
        var buttonBar: HorizontalLayout? = null
        if (insertMode) {
            buttonBar = buildInsertButonBar()
        } else {
            buttonBar = buildUpdateButonBar()
        }
        add(buttonBar)
    }

    fun establishFormModel(formModel: L) {
        form.establishFormModel(formModel)
    }

    private fun buildInsertButonBar(): HorizontalLayout {
        val buttonBar = HorizontalLayout()
        buttonBar.addClassName("detail-button-bar")
        val backButton = Button(messages.getMessage("baseDetalView.backButton.key", null, LocaleContextHolder.getLocale()))
        backButton.addClickListener {
            parent.notifyBackFromDetailPerformed()
        }
        buttonBar.add(backButton)
        val createButton = GreenButton(getCreateButtonText())
        createButton.addClickListener {
            // TODO Validate input
            val item = form.retrieveFormModel()
            createItem(item)
            parent.notifyCreateItemPerformed(item)
        }
        buttonBar.add(createButton)
        return buttonBar
    }

    private fun buildUpdateButonBar(): HorizontalLayout {
        val buttonBar = HorizontalLayout()
        buttonBar.addClassName("detail-button-bar")
        val backButton = Button(messages.getMessage("baseDetalView.backButton.key", null, LocaleContextHolder.getLocale()))
        backButton.addClickListener {
            parent.notifyBackFromDetailPerformed()
        }
        buttonBar.add(backButton)
        val updateButton = BlueButton(getUpdateButtonText())
        updateButton.addClickListener {

        }
        buttonBar.add(updateButton)
        val deleteButton = RedButton(getDeleteButtonText())
        deleteButton.addClickListener {

        }
        buttonBar.add(deleteButton)
        return buttonBar
    }

    private fun createItem(item: L) {
        onCreateItem(item)
    }

    abstract fun getFormTitleKey(): String

    abstract fun getCreateButtonText(): String

    abstract fun getUpdateButtonText(): String

    abstract fun getDeleteButtonText(): String

    abstract fun buildNewItemModel() : L

    abstract fun completeItemModel(item: L): L

    abstract fun onCreateItem(item: L)
}

package org.bastanchu.churierpv2.view.common.view

import com.vaadin.flow.component.ComponentEventListener
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.confirmdialog.ConfirmDialog
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
        completeItemModel(formModel)
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
        completeItemModel(formModel)
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
            val item = form.retrieveFormModel()
            updateItem(item)
            parent.notifyUpdateItemPerformed(item)
        }
        buttonBar.add(updateButton)
        val deleteButton = RedButton(getDeleteButtonText())
        deleteButton.addClickListener {
            val item = form.retrieveFormModel()
            buildDeleteDialog(item) {
                deleteItem(item)
            }
        }
        buttonBar.add(deleteButton)
        return buttonBar
    }

    private fun createItem(item: L) {
        onCreateItem(item)
    }

    private fun updateItem(item: L) {
        onUpdateItem(item)
    }

    private fun deleteItem(item: L) {
        onDeleteItem(item)
    }

    private fun buildDeleteDialog(item: L, confirmDeleteListener: ComponentEventListener<ConfirmDialog.ConfirmEvent>) {
        val dialog = ConfirmDialog()
        dialog.setHeader(messages.getMessage("baseDetalView.cancelDialog.header", null, LocaleContextHolder.getLocale()))
        dialog.setText(messages.getMessage(getDeleteDialogTextKey(), null, LocaleContextHolder.getLocale()))
        dialog.setConfirmButton(messages.getMessage("baseDetalView.cancelDialog.deleteButton", null, LocaleContextHolder.getLocale()),
            {event ->
                                confirmDeleteListener.onComponentEvent(event)
                                parent.notifyDeleteItemPerformed(item)
                            })
        dialog.setCancelButton(messages.getMessage("baseDetalView.cancelDialog.cancelButton", null, LocaleContextHolder.getLocale()),
               {})
        dialog.open()
    }

    abstract fun getFormTitleKey(): String

    abstract fun getDeleteDialogTextKey(): String

    abstract fun getCreateButtonText(): String

    abstract fun getUpdateButtonText(): String

    abstract fun getDeleteButtonText(): String

    abstract fun buildNewItemModel() : L

    abstract fun completeItemModel(item: L): L

    abstract fun onCreateItem(item: L)

    abstract fun onUpdateItem(item: L)

    abstract fun onDeleteItem(item: L)
}

package org.bastanchu.churierpv2.view.common.formelements

import com.vaadin.flow.component.textfield.TextField
import org.bastanchu.churierpv2.view.common.annotations.Field
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder

class TextFieldElementWrapper(fieldAnnotation: Field, value: String?, messages: MessageSource)
      : FormComponentElementWrapper<TextField, String>(fieldAnnotation, value, messages) {

    private lateinit var textField: TextField

    init {
        textField = TextField(messages.getMessage(fieldAnnotation.key, null, LocaleContextHolder.getLocale()))
        textField.value = value ?: ""
    }

    override fun getComponent(): TextField {
        return textField
    }

    override fun getValue(): String {
        return textField.value ?: ""
    }

    override fun setValue(value: String?) {
        textField.value = value ?: ""
    }

    override fun setErrorMessage(errorMessage: String) {
        if (errorMessage.isEmpty()) {
            textField.errorMessage = ""
            textField.isInvalid = false
        } else {
            textField.errorMessage = errorMessage
            textField.isInvalid = true
        }
    }

    override fun addClassName(cssClassName: String) {
        textField.addClassName(cssClassName)
    }
}
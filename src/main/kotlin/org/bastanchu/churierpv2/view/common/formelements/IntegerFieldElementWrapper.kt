package org.bastanchu.churierpv2.view.common.formelements

import com.vaadin.flow.component.textfield.IntegerField
import org.bastanchu.churierpv2.view.common.annotations.Field
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder

class IntegerFieldElementWrapper(fieldAnnotation: Field, value: Int?, messages: MessageSource)
      : FormComponentElementWrapper<IntegerField, Int>(fieldAnnotation, value, messages) {

    lateinit var integerField : IntegerField

    init {
        integerField = IntegerField(messages.getMessage(fieldAnnotation.key, null, LocaleContextHolder.getLocale()))
        integerField.value = value ?: 0
    }

    override fun getComponent(): IntegerField {
        return integerField
    }

    override fun getValue(): Int? {
        return integerField.value ?: 0
    }

    override fun setValue(value: Int?) {
        integerField.value = value ?: 0
    }

    override fun setErrorMessage(errorMessage: String) {
        if (errorMessage.isEmpty()) {
            integerField.errorMessage = ""
            integerField.isInvalid = false
        } else {
            integerField.errorMessage = errorMessage
            integerField.isInvalid = true
        }
    }

    override fun addClassName(cssClassName: String) {
        integerField.addClassName(cssClassName)
    }
}
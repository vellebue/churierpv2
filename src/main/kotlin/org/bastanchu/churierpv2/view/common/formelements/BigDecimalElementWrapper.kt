package org.bastanchu.churierpv2.view.common.formelements

import com.vaadin.flow.component.textfield.BigDecimalField
import org.bastanchu.churierpv2.view.common.annotations.Field
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import java.math.BigDecimal

class BigDecimalElementWrapper(fieldAnnotation: Field, value: BigDecimal?, messages: MessageSource)
         : FormComponentElementWrapper<BigDecimalField, BigDecimal>(fieldAnnotation, value, messages) {

    lateinit var bigDecimalField: BigDecimalField

    init {
        bigDecimalField = BigDecimalField(messages.getMessage(fieldAnnotation.key, null, LocaleContextHolder.getLocale()))
        bigDecimalField.value = value ?: BigDecimal.ZERO
    }

    override fun getComponent(): BigDecimalField {
        return bigDecimalField
    }

    override fun getValue(): BigDecimal? {
        return bigDecimalField.value ?: BigDecimal.ZERO
    }

    override fun setValue(value: BigDecimal?) {
        bigDecimalField.value = value ?: BigDecimal.ZERO
    }

    override fun setErrorMessage(errorMessage: String) {
        if (errorMessage.isEmpty()) {
            bigDecimalField.errorMessage = ""
            bigDecimalField.isInvalid = false
        } else {
            bigDecimalField.errorMessage = errorMessage
            bigDecimalField.isInvalid = true
        }
    }

    override fun addClassName(cssClassName: String) {
        bigDecimalField.addClassName(cssClassName)
    }
}
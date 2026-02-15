package org.bastanchu.churierpv2.view.common.formelements

import com.vaadin.flow.component.datepicker.DatePicker
import org.bastanchu.churierpv2.view.common.annotations.Field
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import java.util.Date

class DateFieldElementWrapper(fieldAnnotation: Field, value: Date?, messages: MessageSource)
    : FormComponentElementWrapper<DatePicker, Date>(fieldAnnotation, value, messages) {

    private lateinit var datePicker: DatePicker

    init {
        datePicker = DatePicker(messages.getMessage(fieldAnnotation.key, null, LocaleContextHolder.getLocale()))
        datePicker.value = if (value != null) value.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate() else null
    }

    override fun getComponent(): DatePicker {
        return datePicker
    }

    override fun getValue(): Date? {
        val localDate = datePicker.value
        if (localDate != null)  {
            return Date.from(localDate.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant())
        } else {
            return null
        }
    }

    override fun setValue(value: Date?) {
        if (value != null) {
            datePicker.value = value.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate()
        } else {
            datePicker.value = null
        }
    }

    override fun setErrorMessage(errorMessage: String) {
        if (errorMessage.isEmpty()) {
            datePicker.errorMessage = ""
            datePicker.isInvalid = false
        } else {
            datePicker.errorMessage = errorMessage
            datePicker.isInvalid = true
        }
    }

    override fun addClassName(cssClassName: String) {
        datePicker.addClassName(cssClassName)
    }
}
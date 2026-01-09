package org.bastanchu.churierpv2.view.common.formelements

import com.vaadin.flow.component.combobox.ComboBox
import org.bastanchu.churierpv2.view.common.annotations.Field
import org.bastanchu.churierpv2.view.common.annotations.FormField
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder

class ComboBoxFieldElementWrapper<M>(fieldAnnotation: Field, val formFieldAnnotation: FormField, val formModel: M, value: String?, messages: MessageSource)
      : FormComponentElementWrapper<ComboBox<String>, String>(fieldAnnotation, value, messages) {

    private lateinit var comboBox : ComboBox<String>

    init {
        val mapFieldName = formFieldAnnotation?.comboBoxConfiguration?.mapFieldName
        val mapField = formModel!!::class.java.declaredFields.filter { it.name == mapFieldName }[0]
        mapField.trySetAccessible()
        val mapValue = mapField.get(formModel) as Map<String, String>
        comboBox = ComboBox<String>(messages.getMessage(fieldAnnotation.key, null, LocaleContextHolder.getLocale()))
        val items = mapValue.keys.union(setOf(""))
        comboBox.setItems(items)
        comboBox.setItemLabelGenerator({
            if (it == "") ("") else mapValue[it]
        })
        comboBox.value = value ?: ""
    }

    override fun getComponent(): ComboBox<String> {
        return comboBox
    }

    override fun getValue(): String? {
        return comboBox.value ?: ""
    }

    override fun setValue(value: String?) {
        comboBox.value = value ?: ""
    }

    override fun setErrorMessage(errorMessage: String) {
        if (errorMessage.isEmpty()) {
            comboBox.errorMessage = ""
            comboBox.isInvalid = false
        } else {
            comboBox.errorMessage = errorMessage
            comboBox.isInvalid = true
        }
    }

    override fun addClassName(cssClassName: String) {
        comboBox.addClassName(cssClassName)
    }
}
package org.bastanchu.churierpv2.view.common.formelements

import com.vaadin.flow.component.combobox.ComboBox
import org.bastanchu.churierpv2.view.common.annotations.Field
import org.bastanchu.churierpv2.view.common.annotations.FormField
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder

class SurrogateComboBoxFieldElementWrapper<M>(fieldAnnotation: Field, val formFieldAnnotation: FormField, val formModel: M, value: String?, messages: MessageSource)
      : FormComponentElementWrapper<SurrogateComboBoxFieldElementWrapper.SurrogateComboBox, String>(fieldAnnotation, value, messages) {

    lateinit var surrogateComboBox : SurrogateComboBox

    init {
        val mapFieldName = formFieldAnnotation?.comboBoxConfiguration?.mapFieldName
        val mapField = formModel!!::class.java.declaredFields.filter { it.name == mapFieldName }[0]
        mapField.trySetAccessible()
        val fullValuesMap = mapField.get(formModel) as Map<String, Map<String, String>>
        val parentFieldName = formFieldAnnotation?.comboBoxConfiguration?.conditionFieldName
        val parentField = formModel!!::class.java.declaredFields.filter { it.name == parentFieldName }[0]
        parentField.trySetAccessible()
        val parentValue = parentField.get(formModel) as String
        val fieldLabel = messages.getMessage(fieldAnnotation.key, null, LocaleContextHolder.getLocale())
        surrogateComboBox = SurrogateComboBox(fieldLabel, value, parentValue, fullValuesMap)
    }

    override fun getComponent(): SurrogateComboBox {
        return surrogateComboBox
    }

    override fun getValue(): String? {
        return surrogateComboBox.value ?: ""
    }

    override fun setValue(value: String?) {
        surrogateComboBox.value = value ?: ""
    }

    override fun setErrorMessage(errorMessage: String) {
        if (errorMessage.isEmpty()) {
            surrogateComboBox.setErrorMessage("")
            surrogateComboBox.isInvalid = false
        } else {
            surrogateComboBox.setErrorMessage(errorMessage)
            surrogateComboBox.isInvalid = true
        }
    }

    override fun addClassName(cssClassName: String) {
        surrogateComboBox.addClassName(cssClassName)
    }


    class SurrogateComboBox(label: String, value: String?, parentValue: String?, val fullValuesMap: Map<String, Map<String, String>>) : ComboBox<String>(label) {
        init {
            if (parentValue != null) {
                val valuesMap = fullValuesMap[parentValue]
                setItems(valuesMap?.keys ?: listOf(""))
                setItemLabelGenerator({ it: String -> if (it != "") (valuesMap?.get(it) ?: "") else "" })
            } else {
                setItems(setOf(""))
                setItemLabelGenerator({ "" })
            }
            if (value != null) {
                setValue(value)
            }
        }

        fun assignParentComboBox(parentComboBox: ComboBox<String>) {
            parentComboBox.addValueChangeListener { event ->
                val newValue = event.value
                val newValuesMap : Map<String, String> = fullValuesMap[newValue] ?: mapOf("" to "")
                setItems(newValuesMap?.keys)
                setItemLabelGenerator({it: String -> if (it != "") (newValuesMap?.get(it) ?: "") else "" })
                value = newValuesMap?.keys?.first()
            }
        }
    }
}
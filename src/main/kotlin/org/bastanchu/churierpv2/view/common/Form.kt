package org.bastanchu.churierpv2.view.common

import com.vaadin.flow.component.AbstractField
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.html.NativeLabel
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.IntegerField
import com.vaadin.flow.component.textfield.TextField
import org.bastanchu.churierpv2.view.common.annotations.Field
import org.bastanchu.churierpv2.view.common.annotations.FormField
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import java.util.stream.Collectors

class Form<T>(val titleKey : String, var formModel : T, val messages : MessageSource) : VerticalLayout() {

    init {
        buildfForm()
    }

    var modelMap: Map<String, FieldStructure>? = null

    fun retrieveFormModel(): T {
        val formModelClass = formModel!!::class.java
        val formModelConstructor = formModelClass.getConstructor()
        val formModelInstance = formModelConstructor.newInstance() as T
        modelMap?.keys?.forEach {
            val fieldName = it
            val currentComponent = modelMap?.get(it)?.component
            val currentField = formModelClass.declaredFields.filter { it.name ==  fieldName }[0]
            var currentValue: Any? = null
            // TODO Consider all component types
            currentValue = currentComponent?.value
            // TODO END
            currentField.trySetAccessible()
            currentField.set(formModelInstance, currentValue)
        }
        return formModelInstance
    }

    fun establishFormModel(formModel: T) {
        this.formModel = formModel
        val formModelClass = formModel!!::class.java
        val fields = formModelClass.declaredFields
        fields.forEach { field ->
            val fieldName = field.name
            val fieldStructure = modelMap?.get(fieldName)
            if (fieldStructure != null) {
                val fieldComponent = fieldStructure.component!!
                field.trySetAccessible()
                val value = field.get(formModel)
                fieldComponent.setValue(value)
            }
        }
    }

    private fun buildfForm() {
        clearForm()
        val titleContainer = HorizontalLayout()
        titleContainer.addClassName("form-title")
        titleContainer.add(NativeLabel(messages.getMessage(titleKey, null, LocaleContextHolder.getLocale())))
        add(titleContainer)
        val formLayout = buildFormLayoutComponent()
        val formContainer = HorizontalLayout()
        formContainer.addClassName("form-container")
        formContainer.add(formLayout)
        add(formContainer)
    }

    private fun buildFormLayoutComponent() : FormLayout {
        val formStructure = FormLayoutFactory.buildFormLayout(formModel, messages)
        val formLayout = formStructure.formLayout
        modelMap = formStructure.modelMap
        formLayout.addClassName("form-content")
        return formLayout
    }

    private fun clearForm() {
        val childrenList = this.children.collect(Collectors.toList())
        childrenList.forEach {
            it.removeFromParent()
        }
    }

    class FormLayoutFactory {

        companion object {

            fun <T> buildFormLayout(formModel : T, messages: MessageSource): FormStructure {
                val declaredFields = formModel!!::class.java.declaredFields
                val formFields = declaredFields.filter {
                                        it.getDeclaredAnnotation(FormField::class.java) != null
                                 }.toTypedArray()
                val formLayout = FormLayout()
                formLayout.setAutoResponsive(true)
                formLayout.isExpandFields = true
                val modelMap = HashMap<String, FieldStructure>()
                val declaredFieldsByGroups = splitFormModelFields(formFields)
                val formRowsArray = ArrayList<FormLayout.FormRow>()
                declaredFieldsByGroups.forEach {
                    val currentGroupDeclaredFields = it
                    val currentGroupComponentList = ArrayList<Component>()
                    val formRow = FormLayout.FormRow()//formLayout.addFormRow()
                    formRowsArray.add(formRow)
                    currentGroupDeclaredFields.forEach { field ->
                        val fieldName = field.name
                        val fieldAnnotation = field.getDeclaredAnnotation(Field::class.java)
                        val formFieldAnnotation = field.getDeclaredAnnotation(FormField::class.java)
                        field.trySetAccessible()
                        val fieldValue = field.get(formModel)
                        var component: AbstractField<*,in Any?>? = null
                        if (fieldAnnotation != null) {
                            // TODO More type cases
                            if (field.type.isAssignableFrom(String::class.java)) {
                                if ((formFieldAnnotation?.comboBoxConfiguration?.mapFieldName != "") &&
                                    (formFieldAnnotation?.comboBoxConfiguration?.conditionFieldName != "")) {
                                    // String field rendered with a combo box
                                    component = buildSurrogateComboBoxField(fieldAnnotation, formFieldAnnotation, formModel, fieldValue?.toString(), messages) as AbstractField<*, in Any?>?
                                } else if (formFieldAnnotation?.comboBoxConfiguration?.mapFieldName != "") {
                                    // String field rendered with a combo box (surrogated), that is linked to a main combo box
                                    component = buildComboBoxField(fieldAnnotation, formFieldAnnotation, formModel,fieldValue?.toString(), messages) as AbstractField<*, in Any?>?
                                } else {
                                    // Default: Render as Text Field
                                    component = buildTextField(
                                        fieldAnnotation,
                                        fieldValue?.toString(),
                                        messages
                                    ) as AbstractField<*, in Any?>?
                                }
                            }
                            else if (field.type.isAssignableFrom(Integer::class.java)) {
                                component = buildIntegerField(fieldAnnotation, fieldValue as Int?, messages) as AbstractField<*,in Any?>?
                            }
                            // TODO end more type cases
                            if (component != null) {
                                component.addClassName("form-component")
                                currentGroupComponentList.add(component)
                                modelMap.put(fieldName, FieldStructure(component))
                            }
                        }
                        if ((component != null) and (formFieldAnnotation != null)) {
                            formRow.add(component, formFieldAnnotation.colspan)
                        } else if (component != null) {
                            formRow.add(component, 1)
                        }
                    }
                }
                formLayout.add(*formRowsArray.toTypedArray())
                return FormStructure(formLayout, modelMap, formFields)
            }

            private fun splitFormModelFields(declaredFields: Array<java.lang.reflect.Field>) : List<List<java.lang.reflect.Field>> {
                val declaredFieldsMap = declaredFields.groupBy {
                    val formFieldAnnotation = it.getDeclaredAnnotation(FormField::class.java)
                    if (formFieldAnnotation != null) {
                        formFieldAnnotation.groupId
                    } else {
                        0
                    }
                }
                val declaredFieldsSortedMap = HashMap<Int, MutableSet<java.lang.reflect.Field>>()
                declaredFieldsMap.keys.forEach {
                    declaredFieldsSortedMap.put(it, HashSet())
                    val currentSet = declaredFieldsSortedMap.get(it)
                    val currentList = declaredFieldsMap.get(it)
                    currentList?.forEach {
                        currentSet?.add(it)
                    }
                }
                val finalList = declaredFieldsMap.values.map { it.toList().sortedBy {
                        val declaredAnnotation = it.getDeclaredAnnotation(FormField::class.java)
                        declaredAnnotation.indexInGroup
                    }
                }
                return finalList
            }

            private fun buildTextField(fieldAnnotation: Field, value: String?, messages: MessageSource) : TextField {
                val textField = TextField(messages.getMessage(fieldAnnotation.key, null, LocaleContextHolder.getLocale()))
                textField.value = value ?: ""
                return textField
            }

            private fun<T> buildComboBoxField(fieldAnnotation: Field, formFieldAnnotation: FormField, formModel: T, value: String?, messages: MessageSource) : ComboBox<String> {
                val mapFieldName = formFieldAnnotation?.comboBoxConfiguration?.mapFieldName
                val mapField = formModel!!::class.java.declaredFields.filter { it.name == mapFieldName }[0]
                mapField.trySetAccessible()
                val mapValue = mapField.get(formModel) as Map<String, String>
                val comboBox = ComboBox<String>(messages.getMessage(fieldAnnotation.key, null, LocaleContextHolder.getLocale()))
                val items = mapValue.keys.union(setOf(""))
                comboBox.setItems(items)
                comboBox.setItemLabelGenerator({
                    if (it == "") ("") else mapValue[it]
                })
                comboBox.value = value ?: ""
                return comboBox
            }

            private fun<T> buildSurrogateComboBoxField(fieldAnnotation: Field, formFieldAnnotation: FormField, formModel: T, value: String?, messages: MessageSource) : ComboBox<String> {
                val mapFieldName = formFieldAnnotation?.comboBoxConfiguration?.mapFieldName
                val mapField = formModel!!::class.java.declaredFields.filter { it.name == mapFieldName }[0]
                mapField.trySetAccessible()
                val fullValuesMap = mapField.get(formModel) as Map<String, Map<String, String>>
                val parentFieldName = formFieldAnnotation?.comboBoxConfiguration?.conditionFieldName
                val parentField = formModel!!::class.java.declaredFields.filter { it.name == parentFieldName }[0]
                parentField.trySetAccessible()
                val parentValue = parentField.get(formModel) as String
                val fieldLabel = messages.getMessage(fieldAnnotation.key, null, LocaleContextHolder.getLocale())
                val surrogateComboBox = SurrogateComboBox(fieldLabel, value, parentValue, fullValuesMap)
                return surrogateComboBox
            }

            private fun buildIntegerField(fieldAnnotation: Field, value: Int?, messages: MessageSource) : IntegerField {
                val integerField = IntegerField(messages.getMessage(fieldAnnotation.key, null, LocaleContextHolder.getLocale()))
                if (value != null) {
                    integerField.value = value
                }
                return integerField
            }

        }

    }

    data class FieldStructure(val component: AbstractField<*,in Any?>)

    class FormStructure(val formLayout: FormLayout,
                        val modelMap: Map<String, FieldStructure>,
                        val formFields: Array<java.lang.reflect.Field>) {

        init {
            solveCrossReferences()
        }

        private fun solveCrossReferences() {
            solveComboBoxCrossReferences()
        }

        private fun solveComboBoxCrossReferences() {
            val surrogateComboBoxFields = formFields.filter {
                val formFieldAnnotation = it.getAnnotation(FormField::class.java)
                (formFieldAnnotation != null) && (formFieldAnnotation.comboBoxConfiguration.mapFieldName) != "" && (formFieldAnnotation.comboBoxConfiguration.conditionFieldName != "")}
            surrogateComboBoxFields.forEach {
                val fieldName = it.name
                val formFieldAnnotation = it.getAnnotation(FormField::class.java)
                val fieldStructure = modelMap[fieldName]
                val component = fieldStructure?.component as SurrogateComboBox
                val parentComboBox = modelMap[formFieldAnnotation.comboBoxConfiguration.conditionFieldName]?.component as ComboBox<String>
                component.assignParentComboBox(parentComboBox)
            }
        }
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
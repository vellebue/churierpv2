package org.bastanchu.churierpv2.view.common

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.html.NativeLabel
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout

import jakarta.validation.ConstraintViolation
import jakarta.validation.Validation
import jakarta.validation.Validator
import org.bastanchu.churierpv2.view.common.annotations.Field
import org.bastanchu.churierpv2.view.common.annotations.FormField
import org.bastanchu.churierpv2.view.common.formelements.ComboBoxFieldElementWrapper
import org.bastanchu.churierpv2.view.common.formelements.FormComponentElementWrapper
import org.bastanchu.churierpv2.view.common.formelements.IntegerFieldElementWrapper
import org.bastanchu.churierpv2.view.common.formelements.SurrogateComboBoxFieldElementWrapper
import org.bastanchu.churierpv2.view.common.formelements.TextFieldElementWrapper
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import java.util.stream.Collectors

class Form<T>(val titleKey : String, var formModel : T, val messages : MessageSource) : VerticalLayout() {

    var validator: Validator? = null

    init {
        buildForm()
    }

    var modelMap: Map<String, FieldStructure>? = null

    fun retrieveFormModel(): T {
        val formModelClass = formModel!!::class.java
        val formModelConstructor = formModelClass.getConstructor()
        val formModelInstance = formModelConstructor.newInstance() as T
        modelMap?.keys?.forEach {
            val fieldName = it
            val currentComponentFieldStructure = modelMap?.get(it)
            val currentField = formModelClass.declaredFields.filter { it.name ==  fieldName }[0]
            var currentValue: Any? = null
            currentValue = currentComponentFieldStructure?.elementWrapper?.getValue()
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
                field.trySetAccessible()
                val value = field.get(formModel)
                val fieldWrapper = fieldStructure.elementWrapper
                fieldWrapper.setValue(value)
            }
        }
    }

    fun validate(): Boolean {
        val formModel = retrieveFormModel()
        val validations = validator?.validate(formModel)
        if (validations != null && validations.size > 0) {
            notifyValidationErrors(validations)
        }
        return validations?.isEmpty() ?: true
    }

    private fun buildForm() {
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
        val validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.validator
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

    private fun notifyValidationErrors(validationViolations: Set<ConstraintViolation<T>>) {
        validationViolations.forEach {
            val fieldName = it.propertyPath.toString()
            val fieldStructure = modelMap?.get(fieldName)
            val wrapper = fieldStructure?.elementWrapper
            wrapper?.setErrorMessage(it.message)
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
                        var componentElementWrapper: FormComponentElementWrapper<*, *>? = null
                        if (fieldAnnotation != null) {
                            // TODO More type cases
                            if (field.type.isAssignableFrom(String::class.java)) {
                                if ((formFieldAnnotation?.comboBoxConfiguration?.mapFieldName != "") &&
                                    (formFieldAnnotation?.comboBoxConfiguration?.conditionFieldName != "")) {
                                    // String field rendered with a combo box
                                    componentElementWrapper = SurrogateComboBoxFieldElementWrapper<T>(fieldAnnotation, formFieldAnnotation, formModel, fieldValue?.toString(), messages) //as AbstractField<*, in Any?>?
                                } else if (formFieldAnnotation?.comboBoxConfiguration?.mapFieldName != "") {
                                    // String field rendered with a combo box (surrogated), that is linked to a main combo box
                                    componentElementWrapper = ComboBoxFieldElementWrapper<T>(fieldAnnotation, formFieldAnnotation, formModel,fieldValue?.toString(), messages) //as AbstractField<*, in Any?>?
                                } else {
                                    // Default: Render as Text Field
                                    componentElementWrapper = TextFieldElementWrapper(fieldAnnotation,fieldValue?.toString(), messages) //as AbstractField<*, in Any?>?
                                }
                            }
                            else if (field.type.isAssignableFrom(Integer::class.java)) {
                                componentElementWrapper = IntegerFieldElementWrapper(
                                    fieldAnnotation,
                                    fieldValue as Int?,
                                    messages
                                ) //as AbstractField<*,in Any?>?
                            }
                            // TODO end more type cases
                            if (componentElementWrapper != null) {
                                componentElementWrapper.addClassName("form-component")
                                currentGroupComponentList.add(componentElementWrapper.getComponent())
                                modelMap.put(fieldName, FieldStructure(componentElementWrapper as FormComponentElementWrapper<*, in Any>))
                            }
                        }
                        if ((componentElementWrapper != null) and (formFieldAnnotation != null)) {
                            formRow.add(componentElementWrapper?.getComponent(), formFieldAnnotation.colspan)
                        } else if (componentElementWrapper != null) {
                            formRow.add(componentElementWrapper.getComponent(), 1)
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
        }

    }

    data class FieldStructure(val elementWrapper: FormComponentElementWrapper<*, in Any>)

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
                val component = fieldStructure?.elementWrapper?.getComponent() as SurrogateComboBoxFieldElementWrapper.SurrogateComboBox
                val parentComboBox = modelMap[formFieldAnnotation.comboBoxConfiguration.conditionFieldName]?.elementWrapper?.getComponent() as ComboBox<String>
                component.assignParentComboBox(parentComboBox)
            }
        }
    }
}
package org.bastanchu.churierpv2.view.common

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.html.NativeLabel
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import org.bastanchu.churierpv2.view.common.annotations.Field
import org.bastanchu.churierpv2.view.common.annotations.FormField
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import java.util.TreeSet
import java.util.stream.Collectors

class Form<T>(val titleKey : String, val formModel : T, val messages : MessageSource) : VerticalLayout() {

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
            if (currentComponent is TextField) {
                currentValue = currentComponent.value
            }
            // TODO END
            currentField.trySetAccessible()
            currentField.set(formModelInstance, currentValue)
        }
        return formModelInstance
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
                val formLayout = FormLayout()
                val modelMap = HashMap<String, FieldStructure>()
                val declaredFieldsByGroups = splitFormModelFields(declaredFields)
                declaredFieldsByGroups.forEach {
                    val currentGroupDeclaredFields = it
                    val currentGroupComponentList = ArrayList<Component>()
                    currentGroupDeclaredFields.forEach { field ->
                        val fieldName = field.name
                        val fieldAnnotation = field.getDeclaredAnnotation(Field::class.java)
                        field.trySetAccessible()
                        val fieldValue = field.get(formModel)
                        var component: Component? = null
                        if (fieldAnnotation != null) {
                            if (field.type.isAssignableFrom(String::class.java)) {
                                component = buildTextField(fieldAnnotation, fieldValue?.toString(), messages)
                            }
                            // TODO More type cases
                            if (component != null) {
                                component.addClassName("form-component")
                                currentGroupComponentList.add(component)
                                modelMap.put(fieldName, FieldStructure(component))
                            }
                        }
                    }
                    val formRow = formLayout.addFormRow(*currentGroupComponentList.toTypedArray())
                    formRow.addClassName("form-row")
                }

                return FormStructure(formLayout, modelMap)
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
                    declaredFieldsSortedMap.put(it, TreeSet(compareBy {
                        val declaredAnnotation = it.getDeclaredAnnotation(FormField::class.java)
                        declaredAnnotation.indexInGroup
                    }))
                    val currentSet = declaredFieldsSortedMap.get(it)
                    val currentList = declaredFieldsMap.get(it)
                    currentList?.forEach {
                        currentSet?.add(it)
                    }
                }
                val finalList = declaredFieldsMap.values.map { it.toList() }
                return finalList
            }

            private fun buildTextField(fieldAnnotation: Field, value: String?, messages: MessageSource) : TextField {
                val textField = TextField(messages.getMessage(fieldAnnotation.key, null, LocaleContextHolder.getLocale()))
                textField.value = value ?: ""
                return textField
            }

        }

    }

    data class FieldStructure(val component: Component)

    data class FormStructure(val formLayout: FormLayout,
                             val modelMap: Map<String, FieldStructure>)

}
package org.bastanchu.churierpv2.view.common.view

import org.bastanchu.churierpv2.view.common.view.annotation.Injected
import org.springframework.context.ApplicationContext
import java.lang.reflect.Field

interface Autowirable {

    fun autowireComponents(applicationContext: ApplicationContext) {
        val fields = getFields(this.javaClass)
        fields.forEach {
            val aField = it
            val autowiredAnnotation = aField.getAnnotation(Injected::class.java)
            if (autowiredAnnotation != null) {
                aField.trySetAccessible()
                aField.set(this, applicationContext.getBean(aField.type))
            }
        }
    }

    private fun getFields(currentClass: Class<*>) : Array<Field> {
        if (currentClass.equals(Object::class.java)) {
            return Object::class.java.declaredFields
        } else {
            val parentFields = getFields(currentClass.superclass)
            val thisFields = currentClass.declaredFields
            val resultFields = ArrayList<Field>()
            resultFields.addAll(parentFields)
            resultFields.addAll(thisFields)
            return resultFields.toTypedArray()
        }
    }

}
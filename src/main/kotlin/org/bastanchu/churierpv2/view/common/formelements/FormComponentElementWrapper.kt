package org.bastanchu.churierpv2.view.common.formelements

import com.vaadin.flow.component.Component
import org.bastanchu.churierpv2.view.common.annotations.Field
import org.springframework.context.MessageSource

abstract class FormComponentElementWrapper<out C: Component, V : Any>(val fieldAnnotation: Field, value: V?, val messages: MessageSource) {

    abstract fun getComponent() : C

    abstract fun getValue() : V?

    abstract fun setValue(value: V?)

    abstract fun setErrorMessage(errorMessage : String)

    abstract fun addClassName(cssClassName: String)
}
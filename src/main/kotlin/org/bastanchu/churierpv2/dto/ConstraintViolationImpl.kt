package org.bastanchu.churierpv2.dto

import jakarta.validation.ConstraintViolation
import jakarta.validation.ElementKind
import jakarta.validation.Path
import jakarta.validation.metadata.ConstraintDescriptor
import org.bastanchu.churierpv2.service.SpringContextHolder
import org.hibernate.validator.engine.HibernateConstraintViolation
import org.hibernate.validator.internal.engine.ConstraintViolationImpl
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder

class ConstraintViolationImpl<T>(val bean: T, val propertyPath: String, val messageKeyTemplate: String, val messageArguments: Array<Any>? = null, val invalidFieldValue: Any) :
    ConstraintViolation<T> {

    val messageSource: MessageSource = SpringContextHolder.instance.getCurrentApplicationContext().getBean(MessageSource::class.java)

    override fun getMessage(): String? {
        return messageSource.getMessage(messageKeyTemplate, messageArguments, LocaleContextHolder.getLocale())
    }

    override fun getMessageTemplate(): String? {
        return messageKeyTemplate
    }

    override fun getRootBean(): T? {
        return bean
    }

    override fun getRootBeanClass(): Class<T?>? {
        return (if (bean != null) bean.javaClass else null) as Class<T?>?
    }

    override fun getLeafBean(): Any? {
        return bean
    }

    override fun getExecutableParameters(): Array<out Any?>? {
        return null
    }

    override fun getExecutableReturnValue(): Any? {
        return null
    }

    override fun getPropertyPath(): Path? {
        return PropertyPath(PropertyNode(bean as Any, propertyPath))
    }

    override fun getInvalidValue(): Any? {
        return invalidFieldValue
    }

    override fun getConstraintDescriptor(): ConstraintDescriptor<*>? {
        return null
    }

    override fun <U : Any?> unwrap(type: Class<U?>?): U? {
        if (type?.isAssignableFrom(ConstraintViolation::class.java) ?: false) {
            return type!!.cast(this) as U?
        } else if (type?.isAssignableFrom(HibernateConstraintViolation::class.java) ?: false) {
            return type!!.cast(this) as U?
        } else if (type?.isAssignableFrom(ConstraintViolationImpl::class.java) ?: false) {
            return type!!.cast(this) as U?
        } else {
            throw RuntimeException("Type not supported for unwrap operation ${type}")
        }
    }

    class PropertyNode(val bean: Any, val propertyPath: String) : Path.PropertyNode {

        override fun getContainerClass(): Class<*>? {
            return bean.javaClass
        }

        override fun getTypeArgumentIndex(): Int? {
            return null
        }

        override fun getName(): String? {
            return propertyPath
        }

        override fun isInIterable(): Boolean {
            return true
        }

        override fun getIndex(): Int? {
            return null
        }

        override fun getKey(): Any? {
            return null
        }

        override fun getKind(): ElementKind? {
            return ElementKind.PROPERTY
        }

        override fun <T : Path.Node?> `as`(p0: Class<T?>?): T? {
            return this.javaClass as T?
        }

    }

    class PropertyPath(val propertyNode: PropertyNode) : Path {

        override fun iterator(): MutableIterator<Path.Node?> {
            val propertyNodeArray = ArrayList<PropertyNode>()
            propertyNodeArray.add(propertyNode)
            return propertyNodeArray.iterator()
        }

        override fun toString(): String {
            return propertyNode.propertyPath
        }

    }
}
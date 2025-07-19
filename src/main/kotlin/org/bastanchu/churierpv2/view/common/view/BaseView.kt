package org.bastanchu.churierpv2.view.common.view

import com.vaadin.flow.component.html.NativeLabel
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContext
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder

abstract class BaseView(protected val messages : MessageSource,
                        protected val applicationContext: ApplicationContext
) :  VerticalLayout(), Autowirable {

    val logger: Logger = LoggerFactory.getLogger(BaseView::class.java)
    val declaringClass = this::class.java

    init {
        autowireComponents(applicationContext)
        val headingLayout = HorizontalLayout()
        headingLayout.addClassName("heading-layout")
        val titleLabel = NativeLabel("${messages.getMessage(getTitleKey(), null, LocaleContextHolder.getLocale())}")
        titleLabel.addClassName("title-label")
        headingLayout.add(titleLabel)
        add(headingLayout)
    }

    protected abstract fun getTitleKey() : String

    public fun start() {
        logger.debug("Starting view for ${declaringClass.name}")
        onStart()
        logger.debug("Started view for ${declaringClass.name}")
    }

    public fun stop() {
        logger.debug("Stopping view for ${declaringClass.name}")
        onStop()
        logger.debug("Stopped view for ${declaringClass.name}")
    }

    protected abstract fun onStart()

    protected abstract fun onStop()
}
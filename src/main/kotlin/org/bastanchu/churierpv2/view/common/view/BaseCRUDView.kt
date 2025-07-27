package org.bastanchu.churierpv2.view.common.view

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import org.bastanchu.churierpv2.view.common.event.GridEvent
import org.bastanchu.churierpv2.view.common.listener.GridListener
import org.springframework.context.ApplicationContext
import org.springframework.context.MessageSource
import java.util.stream.Collectors

abstract class BaseCRUDView<F, L>(messages: MessageSource, applicationContext: ApplicationContext,
                                  filterViewClass: Class<in BaseFilterView<F, L>>,
                                  listViewClass: Class<in BaseListView<L>>,
                                  detailViewClass: Class<in BaseDetailView<L>>) : BaseView(messages, applicationContext),
                                                                                  GridListener<L> {
    private lateinit var filterView: BaseFilterView<F, L>
    private lateinit var listView: BaseListView<L>
    private lateinit var detailedView: BaseDetailView<L>

    private val viewport = VerticalLayout()

    init {
        val effectiveBaseCRUDViewClass = this.javaClass
        val filterViewContructor = filterViewClass.getConstructor(MessageSource::class.java,
            ApplicationContext::class.java, effectiveBaseCRUDViewClass)
        filterView = filterViewContructor.newInstance(messages, applicationContext, this) as BaseFilterView<F, L>
        val listViewConstructor = listViewClass.getConstructor(MessageSource::class.java,
            ApplicationContext::class.java, effectiveBaseCRUDViewClass)
        listView = listViewConstructor.newInstance(messages, applicationContext, this) as BaseListView<L>
        val detailedViewConstructor = detailViewClass.getConstructor(MessageSource::class.java,
            ApplicationContext::class.java, effectiveBaseCRUDViewClass)
        detailedView = detailedViewConstructor.newInstance(messages, applicationContext, this) as BaseDetailView<L>
        add(viewport)
        addAndReplace(filterView)
    }

    fun notifyFilterPerformed(itemsList : List<L>) {
        addAndReplace(listView)
        listView.updateListModel(itemsList as MutableList<L>)
    }

    fun notifyBackFromListPerformed() {
        addAndReplace(filterView)
    }

    private fun addAndReplace(component: Component) {
        val childrenList = viewport.children.collect(Collectors.toList())
        if (childrenList.isNotEmpty()) {
            val currentView = childrenList.get(0)
            currentView.removeFromParent()
        }
        viewport.add(component)
    }

    override fun itemSelected(event: GridEvent<L>) {
        logger.debug("Event selected: ${event.item!!::class.java.simpleName}")
        addAndReplace(detailedView)
        detailedView.establishFormModel(event.item!!)
    }
}
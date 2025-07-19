package org.bastanchu.churierpv2.view.common.view

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import org.springframework.context.ApplicationContext
import org.springframework.context.MessageSource
import java.util.stream.Collectors

abstract class BaseCRUDView<F, L>(messages: MessageSource, applicationContext: ApplicationContext,
                                  filterViewClass: Class<in BaseFilterView<F, L>>,
                                  listViewClass: Class<in BaseListView<L>>) : BaseView(messages, applicationContext) {

    lateinit var filterView: BaseFilterView<F, L>
    lateinit var listView: BaseListView<L>

    val viewport = VerticalLayout()

    init {
        val effectiveBaseCRUDViewClass = this.javaClass
        val filterViewContructor = filterViewClass.getConstructor(MessageSource::class.java,
            ApplicationContext::class.java, effectiveBaseCRUDViewClass)
        filterView = filterViewContructor.newInstance(messages, applicationContext, this) as BaseFilterView<F, L>
        val listViewConstructor = listViewClass.getConstructor(MessageSource::class.java,
            ApplicationContext::class.java, effectiveBaseCRUDViewClass)
        listView = listViewConstructor.newInstance(messages, applicationContext, this) as BaseListView<L>
        add(viewport)
        addAndReplace(filterView)
    }

    fun notifyFilterPerformed(itemsList : List<L>) {
        addAndReplace(listView)
        listView.updateListModel(itemsList as MutableList<L>)
    }

    private fun addAndReplace(component: Component) {
        val childrenList = viewport.children.collect(Collectors.toList())
        if (childrenList.isNotEmpty()) {
            val currentView = childrenList.get(0)
            currentView.removeFromParent()
        }
        viewport.add(component)
    }
}
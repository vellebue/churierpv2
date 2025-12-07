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
                                  val detailViewClass: Class<in BaseDetailView<L>>) : BaseView(messages, applicationContext),
                                                                                  GridListener<L> {
    private lateinit var filterView: BaseFilterView<F, L>
    private lateinit var listView: BaseListView<L>
    private lateinit var detailedView: BaseDetailView<L>

    private val viewport = VerticalLayout()

    lateinit var effectiveBaseCRUDViewClass: Class<BaseCRUDView<F,L>>

    init {
        effectiveBaseCRUDViewClass = this.javaClass
        val filterViewContructor = filterViewClass.getConstructor(MessageSource::class.java,
            ApplicationContext::class.java, effectiveBaseCRUDViewClass)
        filterView = filterViewContructor.newInstance(messages, applicationContext, this) as BaseFilterView<F, L>
        val listViewConstructor = listViewClass.getConstructor(MessageSource::class.java,
            ApplicationContext::class.java, effectiveBaseCRUDViewClass)
        listView = listViewConstructor.newInstance(messages, applicationContext, this) as BaseListView<L>
        val detailedViewConstructor = detailViewClass.getConstructor(MessageSource::class.java,
            ApplicationContext::class.java, effectiveBaseCRUDViewClass, Boolean::class.java)
        detailedView = detailedViewConstructor.newInstance(messages, applicationContext, this, false) as BaseDetailView<L>
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

    fun notifyNewItemPerformed() {
        val detailedViewConstructor = detailViewClass.getConstructor(MessageSource::class.java,
            ApplicationContext::class.java, effectiveBaseCRUDViewClass, Boolean::class.java)
        detailedView = detailedViewConstructor.newInstance(messages, applicationContext, this, true) as BaseDetailView<L>
        val newItemFormModel = detailedView.buildNewItemModel()
        detailedView.establishFormModel(newItemFormModel)
        addAndReplace(detailedView)
    }

    fun notifyBackFromDetailPerformed() {
        addAndReplace(listView)
    }

    fun notifyCreateItemPerformed(item: L) {
        val listModel = listView.getListModel()
        listModel.add(item)
        listView.updateListModel(listModel)
        addAndReplace(listView)
    }

    fun notifyUpdateItemPerformed(item: L) {
        listView.addOrUpdateItem(item)
        addAndReplace(listView)
    }

    fun notifyDeleteItemPerformed(item: L) {
        listView.deleteItem(item)
        addAndReplace(listView)
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
        val detailedViewConstructor = detailViewClass.getConstructor(MessageSource::class.java,
            ApplicationContext::class.java, effectiveBaseCRUDViewClass, Boolean::class.java)
        detailedView = detailedViewConstructor.newInstance(messages, applicationContext, this, false) as BaseDetailView<L>
        val newItemFormModel = detailedView.buildNewItemModel()
        detailedView.establishFormModel(newItemFormModel)
        addAndReplace(detailedView)
        detailedView.establishFormModel(event.item!!)
    }
}
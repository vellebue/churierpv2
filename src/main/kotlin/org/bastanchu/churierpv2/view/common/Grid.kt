package org.bastanchu.churierpv2.view.common

import com.vaadin.flow.component.html.NativeLabel
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import org.bastanchu.churierpv2.view.common.annotations.Field
import org.bastanchu.churierpv2.view.common.annotations.ListField
import org.bastanchu.churierpv2.view.common.event.GridEvent
import org.bastanchu.churierpv2.view.common.listener.GridListener
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import java.util.stream.Collectors


class Grid<T>(val titleKey: String, var gridModel: MutableList<T>, val modelClass: Class<T>, val messages: MessageSource) : VerticalLayout() {

    lateinit var grid: com.vaadin.flow.component.grid.Grid<T>
    val gridListeners = mutableListOf<GridListener<T>>()

    init  {
        buildGrid()
    }

    fun addGridListener(listener: GridListener<T>) {
        gridListeners.add(listener)
    }

    fun removeGridListener(listener: GridListener<T>) {
        gridListeners.remove(listener)
    }

    fun updateModel(gridModel: MutableList<T>) {
        this.gridModel = gridModel
        grid.setItems(gridModel)
    }

    private fun buildGrid() {
        clearGrid()
        val titleContainer = HorizontalLayout()
        titleContainer.addClassName("grid-title")
        titleContainer.add(NativeLabel(messages.getMessage(titleKey, null, LocaleContextHolder.getLocale())))
        add(titleContainer)
        val gridLayout = GridFactory.buildGrid(gridModel, modelClass, messages)
        grid = gridLayout
        val thisGrid = this
        grid.addItemDoubleClickListener { event ->
            val item = event.item
            val event = GridEvent(item, thisGrid)
            fireItemSelected(event)
        }
        val gridContainer = HorizontalLayout()
        gridContainer.addClassName("grid-container")
        gridContainer.add(gridLayout)
        add(gridLayout)
    }

    protected fun fireItemSelected(event: GridEvent<T>) {
        gridListeners.forEach { listener ->
            listener.itemSelected(event)
        }
    }

    private fun clearGrid() {
        val childrenList = this.children.collect(Collectors.toList())
        childrenList.forEach {
            it.removeFromParent()
        }
    }

    class GridFactory {

        companion object {

            fun <T> buildGrid(gridModel: MutableList<T>, gridClass: Class<T>, messages: MessageSource): com.vaadin.flow.component.grid.Grid<T>  {
                val grid = com.vaadin.flow.component.grid.Grid(gridClass, false)
                gridClass.declaredFields.forEach { field ->
                    val fieldAnnotation = field.getAnnotation(Field::class.java)
                    val listFieldAnnotation = field.getAnnotation(ListField::class.java)
                    if ((fieldAnnotation != null) && (listFieldAnnotation != null)) {
                        val gridColumn = grid.addColumn {
                            field.trySetAccessible()
                            field.get(it)
                        }.setHeader(messages.getMessage(fieldAnnotation.key, null, LocaleContextHolder.getLocale()))
                        gridColumn.setAutoWidth(true)
                    }
                }
                grid.setItems(gridModel)
                return grid
            }
        }
    }
}
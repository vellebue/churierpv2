package org.bastanchu.churierpv2.view.common

import com.vaadin.flow.component.html.NativeLabel
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import org.bastanchu.churierpv2.view.common.annotations.Field
import org.bastanchu.churierpv2.view.common.annotations.ListField
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import java.util.stream.Collectors


class Grid<T>(val titleKey: String, var gridModel: MutableList<T>, val modelClass: Class<T>, val messages: MessageSource) : VerticalLayout() {

    lateinit var grid: com.vaadin.flow.component.grid.Grid<T>

    init  {
        buildGrid()
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
        val gridContainer = HorizontalLayout()
        gridContainer.addClassName("grid-container")
        gridContainer.add(gridLayout)
        add(gridLayout)
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
package org.bastanchu.churierpv2.view.common

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.html.NativeLabel
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.IntegerField
import org.bastanchu.churierpv2.view.common.annotations.Field
import org.bastanchu.churierpv2.view.common.annotations.ListField
import org.bastanchu.churierpv2.view.common.event.GridEvent
import org.bastanchu.churierpv2.view.common.listener.GridListener
import org.springframework.beans.support.PagedListHolder.DEFAULT_PAGE_SIZE
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import oshi.driver.unix.solaris.kstat.SystemPages
import java.util.stream.Collectors
import kotlin.jvm.Throws


class Grid<T>(val titleKey: String, gridModel: List<T>, val modelClass: Class<T>, val messages: MessageSource, var pageSize:Int = DEFAULT_PAGE_SIZE) : VerticalLayout() {

    lateinit var grid: com.vaadin.flow.component.grid.Grid<T>
    val gridListeners = mutableListOf<GridListener<T>>()
    val DEFAULT_PAGE_SIZE = 5
    lateinit var pageListHandler: PageListHandler<T>
    var currentPage: Int = 1
    var numPages: Int = 1

    init  {
        buildGrid(gridModel)
    }

    fun addGridListener(listener: GridListener<T>) {
        gridListeners.add(listener)
    }

    fun removeGridListener(listener: GridListener<T>) {
        gridListeners.remove(listener)
    }

    fun updateModel(gridModel: MutableList<T>) {
        // TODO Refactor
        //this.gridModel = gridModel
        buildGrid(gridModel)
    }

    private fun buildGrid(gridModel: List<T>) {
        clearGrid()
        val titleContainer = HorizontalLayout()
        titleContainer.addClassName("grid-title")
        titleContainer.add(NativeLabel(messages.getMessage(titleKey, null, LocaleContextHolder.getLocale())))
        add(titleContainer)
        pageListHandler = PageListHandler(gridModel, pageSize)
        currentPage = 1
        numPages = pageListHandler.getNumPages()
        val initialList = pageListHandler.getPageAt(currentPage - 1) as MutableList<T>
        val gridLayout = GridFactory.buildGrid(initialList, modelClass, messages)
        grid = gridLayout
        val thisGrid = this
        grid.addItemDoubleClickListener { event ->
            val item = event.item
            val targetEvent = GridEvent(item, thisGrid)
            fireItemSelected(targetEvent)
        }
        val gridContainer = VerticalLayout()
        gridContainer.addClassName("grid-container")
        gridContainer.add(grid)
        val pageBar = buildPageBar()
        gridContainer.add(pageBar)
        add(gridContainer)
    }

    private fun buildPageBar(): Component {
        val pageBar = HorizontalLayout()
        pageBar.addClassName("grid-page-bar")
        val buttonDoubleLeft = Button("", Icon(VaadinIcon.ANGLE_DOUBLE_LEFT))
        pageBar.add(buttonDoubleLeft)
        val buttonLeft = Button("", Icon(VaadinIcon.ANGLE_LEFT))
        pageBar.add(buttonLeft)
        val currentPageField = IntegerField("")
        currentPageField.value = currentPage
        currentPageField.width = "25pt"
        currentPageField.isReadOnly = true
        pageBar.add(currentPageField)
        val slashLabel = NativeLabel("/")
        slashLabel.addClassName("grid-page-bar-slash-label")
        pageBar.add(slashLabel)
        val numPagesField = IntegerField("")
        numPagesField.value = numPages
        numPagesField.width = "25pt"
        numPagesField.isReadOnly = true
        pageBar.add(numPagesField)
        val buttonRight = Button("", Icon(VaadinIcon.ANGLE_RIGHT))
        pageBar.add(buttonRight)
        val buttonDoubleRight = Button("", Icon(VaadinIcon.ANGLE_DOUBLE_RIGHT))
        pageBar.add(buttonDoubleRight)
        // Event handling
        buttonLeft.addClickListener { event ->
            var currentPage = currentPageField.value
            if (currentPage > 1) {
                currentPage--
                val pageList = pageListHandler.getPageAt(currentPage - 1)
                grid.setItems(pageList)
                currentPageField.value = currentPage
            }
        }
        buttonDoubleLeft.addClickListener { event ->
            val pageList = pageListHandler.getPageAt(0)
            grid.setItems(pageList)
            currentPageField.value = 1
        }
        buttonRight.addClickListener { event ->
            var numPages = numPagesField.value
            var currentPage = currentPageField.value
            if (currentPage < numPages) {
                currentPage++
                val pageList = pageListHandler.getPageAt(currentPage - 1)
                grid.setItems(pageList)
                currentPageField.value = currentPage
            }
        }
        buttonDoubleRight.addClickListener { event ->
            var currentPage = numPagesField.value
            val pageList = pageListHandler.getPageAt(currentPage - 1)
            grid.setItems(pageList)
            currentPageField.value = currentPage
        }
        return pageBar
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

    class PageListHandler<T>(val list: List<T>, val pageSize:Int = DEFAULT_PAGE_SIZE) {

        fun getNumPages(): Int {
            return list.size / pageSize + 1
        }

        @Throws(IndexOutOfBoundsException::class)
        fun getPageAt(pageIndex: Int): List<T>  {
            if (pageIndex < 0 || pageIndex >= getNumPages()) {
                throw IndexOutOfBoundsException("Invalid pageIndex ${pageIndex} for num. pages ${getNumPages()}")
            } else {
                val firstIndex = pageIndex * pageSize
                var lastIndex =  if ((pageIndex + 1) * pageSize <= list.lastIndex) ((pageIndex + 1) * pageSize) else list.lastIndex
                if (lastIndex < 0) lastIndex = 0
                val subList = list.subList(firstIndex, if (list.isNotEmpty()) (lastIndex + 1) else 0)
                return subList
            }
        }
    }
}
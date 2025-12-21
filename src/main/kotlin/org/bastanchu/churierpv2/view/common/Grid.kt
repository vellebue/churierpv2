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
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import java.util.stream.Collectors
import kotlin.Comparator
import kotlin.jvm.Throws
import kotlin.jvm.javaClass


class Grid<T>(private val titleKey: String,
              private var gridModel: MutableList<T>,
              private val modelClass: Class<T>,
              private val messages: MessageSource,
              private var pageSize:Int = DEFAULT_PAGE_SIZE) : VerticalLayout() {

    lateinit var grid: com.vaadin.flow.component.grid.Grid<T>
    val gridListeners = mutableListOf<GridListener<T>>()
    lateinit var pageListHandler: PageListHandler<T>
    lateinit var pageBar: PageBar<T>

    companion object {
        val DEFAULT_PAGE_SIZE = 5
    }

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
        this.gridModel = gridModel
        buildGrid(gridModel)
    }

    fun getCurrentModel(): MutableList<T> {
        val targetModel = ArrayList<T>()
        targetModel.addAll(this.gridModel)
        return targetModel
    }

    fun addOrUpdateItem(item :T) {
        val currentModel = getCurrentModel()
        val itemKeyMap = getItemKeyMap(item)
        for (i in 0..(currentModel.size - 1)) {
            val currentModelKeyMap = getItemKeyMap(currentModel[i])
            if (currentModelKeyMap == itemKeyMap) {
                currentModel[i] = item
            }
        }
        updateModel(currentModel)
    }

    fun deleteItem(item: T) {
        var i = 0
        var itemToDeleteFound = false
        val currentModel = getCurrentModel()
        val itemKeyMap = getItemKeyMap(item)
        while ((i < currentModel.size) && !itemToDeleteFound) {
            val currentModelKeyMap = getItemKeyMap(currentModel[i])
            if (currentModelKeyMap == itemKeyMap) {
                itemToDeleteFound = true
                currentModel.removeAt(i)
            }
            i++
        }
        updateModel(currentModel)
    }

    private fun buildGrid(gridModel: MutableList<T>) {
        clearGrid()
        val titleContainer = HorizontalLayout()
        titleContainer.addClassName("grid-title")
        titleContainer.add(NativeLabel(messages.getMessage(titleKey, null, LocaleContextHolder.getLocale())))
        add(titleContainer)
        pageListHandler = PageListHandler(gridModel, pageSize)
        pageBar = buildPageBar(pageListHandler)
        //currentPage = 1
        //numPages = pageListHandler.getNumPages()
        val initialList = pageListHandler.getPageAt(pageBar.currentPage - 1) as MutableList<T>
        val gridLayout = GridFactory.buildGrid(initialList, modelClass, messages, pageListHandler, pageBar)
        grid = gridLayout
        pageBar.enableGrid(grid)
        val thisGrid = this
        grid.addItemDoubleClickListener { event ->
            val item = event.item
            if (item != null) {
                val targetEvent = GridEvent(item, thisGrid)
                fireItemSelected(targetEvent)
            }
        }
        val gridContainer = VerticalLayout()
        gridContainer.addClassName("grid-container")
        gridContainer.add(grid)
        gridContainer.add(pageBar)
        add(gridContainer)
    }

    private fun buildPageBar(pageListHandler: PageListHandler<T>): PageBar<T> {
        val pageBar = PageBar(pageListHandler)
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

    private fun getItemKeyMap(item: T): Map<String, Any?> {
        val declaredFields = item!!::class.java.declaredFields
        val targetMap = HashMap<String, Any?>()
        declaredFields.forEach { field ->
            field.isAccessible = true
            val listFieldAnnotation = field.getAnnotation(ListField::class.java)
            if ((listFieldAnnotation != null) && (listFieldAnnotation.identifier)) {
                targetMap[field.name] = field.get(item)
            }
        }
        return targetMap;
    }

    class PageBar<T>(val pageListHandler: PageListHandler<T>): HorizontalLayout() {

        var currentPage: Int = 1
        var numPages: Int = pageListHandler.getNumPages()
        val buttonDoubleLeft = Button("", Icon(VaadinIcon.ANGLE_DOUBLE_LEFT))
        val buttonLeft = Button("", Icon(VaadinIcon.ANGLE_LEFT))
        val currentPageField = IntegerField("")
        val numPagesField = IntegerField("")
        val buttonRight = Button("", Icon(VaadinIcon.ANGLE_RIGHT))
        val buttonDoubleRight = Button("", Icon(VaadinIcon.ANGLE_DOUBLE_RIGHT))

        init {
            addClassName("grid-page-bar")
            add(buttonDoubleLeft)
            add(buttonLeft)
            currentPageField.value = currentPage
            currentPageField.width = "25pt"
            currentPageField.isReadOnly = true
            add(currentPageField)
            val slashLabel = NativeLabel("/")
            slashLabel.addClassName("grid-page-bar-slash-label")
            add(slashLabel)
            numPagesField.value = numPages
            numPagesField.width = "25pt"
            numPagesField.isReadOnly = true
            add(numPagesField)
            add(buttonRight)
            add(buttonDoubleRight)
        }

        fun enableGrid(grid: com.vaadin.flow.component.grid.Grid<T>) {
            // Event handling
            buttonLeft.addClickListener { event ->
                currentPage = currentPageField.value
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
                currentPage = 1
            }
            buttonRight.addClickListener { event ->
                numPages = numPagesField.value
                currentPage = currentPageField.value
                if (currentPage < numPages) {
                    currentPage++
                    val pageList = pageListHandler.getPageAt(currentPage - 1)
                    grid.setItems(pageList)
                    currentPageField.value = currentPage
                }
            }
            buttonDoubleRight.addClickListener { event ->
                currentPage = numPagesField.value
                val pageList = pageListHandler.getPageAt(currentPage - 1)
                grid.setItems(pageList)
                currentPageField.value = currentPage
            }
        }

    }

    class GridFactory {

        companion object {

            fun <T> buildGrid(gridModel: MutableList<T>, gridClass: Class<T>, messages: MessageSource, pageListHandler: PageListHandler<T>, pageBar: PageBar<T>): com.vaadin.flow.component.grid.Grid<T>  {
                val grid = com.vaadin.flow.component.grid.Grid(gridClass, false)
                val headerBus = HeaderBus()
                gridClass.declaredFields.forEach { field ->
                    val fieldAnnotation = field.getAnnotation(Field::class.java)
                    val listFieldAnnotation = field.getAnnotation(ListField::class.java)
                    if ((fieldAnnotation != null) && (listFieldAnnotation != null)) {
                        field.trySetAccessible()
                        val headerComponent = HeaderComponent(messages.getMessage(fieldAnnotation.key, null, LocaleContextHolder.getLocale()))
                        val gridColumn = grid.addColumn {
                            field.trySetAccessible()
                            val sourceValue = field.get(it)
                            ValueFormatter.getValue(sourceValue)
                        }.setHeader(headerComponent)
                        gridColumn.setAutoWidth(true)
                        gridColumn.setResizable(true)
                        headerComponent.addHeaderListener({event ->
                            pageListHandler.sortBy(field.name, event.targetStatus)
                            grid.setItems(pageListHandler.getPageAt(pageBar.currentPage - 1))
                        })
                        headerBus.addHeaderComponent(headerComponent)
                    }
                }
                grid.setItems(gridModel)
                return grid
            }
        }
    }

    class ComparatorFactory {

        companion object {

            fun <T> getComparator(field: java.lang.reflect.Field, classType: Class<T>): java.util.Comparator<T>? {
                return java.util.Comparator({i1: T, i2: T ->
                    field.trySetAccessible()
                    val fieldValue1 = field.get(i1)
                    val fieldValue2 = field.get(i2)
                    if (fieldValue1 == null && fieldValue2 == null) {
                        - 1 // Strict order, not equals
                    }
                    if (fieldValue1 == null ) {
                        - 1
                    } else if (fieldValue2 == null) {
                        1
                    } else {
                        val fieldComparator = getFieldComparator(fieldValue1.javaClass)
                        val comparationValue = fieldComparator!!.compare(fieldValue1, fieldValue2)
                        if (comparationValue == 0) {
                            - 1 // Force strict order
                        } else {
                            comparationValue
                        }
                    }
                })
            }

            fun <T> getReverseComparator(comparator: java.util.Comparator<T>): java.util.Comparator<T> {
                val reverseComparator: java.util.Comparator<T> =  java.util.Comparator<T> { o1: T, o2: T  ->
                    - comparator.compare(o1, o2)
                }
                return reverseComparator
            }

            // TODO Add remaning types
            private fun <F> getFieldComparator(comparingClass: Class<F>): Comparator<F>? {
                var targetComparator:Comparator<F>? = null
                if (comparingClass.isAssignableFrom(Int::class.java) || comparingClass.isAssignableFrom(Integer::class.java) ) {
                    targetComparator = Comparator<Int> { o1, o2 -> o1 - o2 } as Comparator<F>
                } else if (comparingClass.isAssignableFrom(String::class.java)) {
                    targetComparator = Comparator<String> { o1, o2 -> o1.compareTo(o2) } as Comparator<F>
                }
                return targetComparator
            }
        }
    }

    class HeaderComponent(val headerTitle: String) : HorizontalLayout() {

        enum class Status {UP,DOWN,NONE}

        private var status: Status = Status.NONE
        private val icon = Icon(VaadinIcon.MINUS)
        var headerListeners: MutableList<HeaderListener> = ArrayList()

        init {
            addClassName("grid-column-header")
            val label =NativeLabel(headerTitle)
            add(label)
            add(icon)
            icon.addClickListener {
                rotateStatus()
                fireOrderStateChanged(HeaderEvent(this, status))
            }
        }

        fun rotateStatus() {
            when (status) {
                Status.NONE -> {
                    status = Status.UP
                    icon.setIcon(VaadinIcon.ANGLE_UP)
                }
                Status.UP -> {
                    status = Status.DOWN
                    icon.setIcon(VaadinIcon.ANGLE_DOWN)
                }
                Status.DOWN -> {
                    status = Status.NONE
                    icon.setIcon(VaadinIcon.MINUS)
                }
            }
        }

        fun reset() {
            status = Status.NONE
            icon.setIcon(VaadinIcon.MINUS)
        }

        fun addHeaderListener(listener: HeaderListener) {
            headerListeners.add(listener)
        }

        fun removeHeaderListener(listener: HeaderListener) {
            headerListeners.remove(listener)
        }

        private fun fireOrderStateChanged(event: HeaderEvent) {
            headerListeners.forEach { listener ->
                listener.onOrderStateChanged(event)
            }
        }

        fun interface HeaderListener {

            fun onOrderStateChanged(event: HeaderEvent)

        }

        data class HeaderEvent(val headerComponent: HeaderComponent, val targetStatus: HeaderComponent.Status)

    }

    class HeaderBus : HeaderComponent.HeaderListener {

        val headerComponentsList = ArrayList<HeaderComponent>()

        fun addHeaderComponent(headerComponent: HeaderComponent) {
            headerComponentsList.add(headerComponent)
            headerComponent.addHeaderListener(this)
        }

        override fun onOrderStateChanged(event: HeaderComponent.HeaderEvent) {
            val headerName = event.headerComponent.headerTitle
            headerComponentsList.forEach {headerComponent ->
                val currentHeaderName = headerComponent.headerTitle
                if (currentHeaderName != headerName) {
                    headerComponent.reset()
                }
            }
        }

    }

    class ValueFormatter {

        companion object {

            // TODO Add remaning types
            fun getValue(value: Any?): Any {
                when (value) {
                    is Int -> return value
                    is Integer -> return value
                    is String -> return value
                    null -> return ""
                    else -> return value.toString()
                }
            }
        }
    }

    class PageListHandler<T>(var list: MutableList<T>, val pageSize:Int = DEFAULT_PAGE_SIZE) {

        lateinit var originalList: MutableList<T>

        init {
            originalList = list
        }

        fun getNumPages(): Int {
            return list.size / pageSize + 1
        }

        @Throws(IndexOutOfBoundsException::class)
        fun getPageAt(pageIndex: Int): List<T>  {
            if (pageIndex < 0 || pageIndex >= getNumPages()) {
                throw IndexOutOfBoundsException("Invalid pageIndex ${pageIndex} for num. pages ${getNumPages()}")
            } else {
                val firstIndex = pageIndex * pageSize
                var lastIndex =  if ((pageIndex + 1) * pageSize - 1 <= list.lastIndex) ((pageIndex + 1) * pageSize - 1) else list.lastIndex
                if (lastIndex < 0) lastIndex = 0
                val subList = list.subList(firstIndex, if (list.isNotEmpty()) (lastIndex + 1) else 0)
                return subList
            }
        }

        fun sortBy(fieldName: String, sortingStatus: HeaderComponent.Status): List<T> {
            if (list.isNotEmpty()) {
                val first = list[0]
                val listClass = (first as Any).javaClass as Class<T>
                val field = listClass.getDeclaredField(fieldName)
                when(sortingStatus) {
                    HeaderComponent.Status.NONE -> {
                        list = originalList
                    }
                    HeaderComponent.Status.UP -> {
                        val comparator = ComparatorFactory.getComparator(field, listClass)
                        val sortedSet = list.toSortedSet(comparator as Comparator<T>)
                        list = ArrayList(sortedSet)
                    }
                    HeaderComponent.Status.DOWN -> {
                        val comparator = ComparatorFactory.getComparator(field, listClass)
                        val reverseComparator = ComparatorFactory.getReverseComparator(comparator as Comparator<T>)
                        val sortedSet = list.toSortedSet(reverseComparator)
                        list = ArrayList(sortedSet)
                    }
                }
            }
            return list
        }
    }

}
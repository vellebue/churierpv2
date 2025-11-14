package org.bastanchu.churierpv2.view.common.view

import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import org.bastanchu.churierpv2.view.common.Grid
import org.bastanchu.churierpv2.view.common.button.GreenButton
import org.springframework.context.ApplicationContext
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import java.lang.reflect.ParameterizedType

abstract class BaseListView<L>(val messages: MessageSource,
                           val applicationContext: ApplicationContext,
                           val parent: BaseCRUDView<*, L>): VerticalLayout(), Autowirable {

    var  listItemDtoClass: Class<L>
    lateinit var grid: Grid<L>

    init {
        listItemDtoClass = (this::class.java.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<L>
        autowireComponents(applicationContext)
        val mainView = VerticalLayout()
        val listModel = ArrayList<L>()
        val grid = Grid("societies.listView.title", listModel, listItemDtoClass, messages, getDefaultPageSize())
        this.grid = grid
        grid.addGridListener(parent)
        mainView.add(grid)
        add(mainView)
        val buttoBar = buildButtonBar()
        mainView.add(buttoBar)
    }

    fun updateListModel(listModel: MutableList<L>) {
        grid.updateModel(listModel)
    }

    fun getListModel(): MutableList<L> {
        return grid.getCurrentModel()
    }

    private fun buildButtonBar(): HorizontalLayout {
        val buttonBar = HorizontalLayout()
        buttonBar.addClassName("grid-button-bar")
        val backButton = Button(messages.getMessage("baseListView.backButton.key", null, LocaleContextHolder.getLocale()))
        backButton.addClickListener {
            parent.notifyBackFromListPerformed()
        }
        buttonBar.add(backButton)
        val greenButton = GreenButton(getNewItemButtonText())
        greenButton.addClickListener {
            parent.notifyNewItemPerformed()
        }
        buttonBar.add(greenButton)
        return buttonBar
    }

    abstract fun getDefaultPageSize(): Int

    abstract fun getNewItemButtonText(): String
}
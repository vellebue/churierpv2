package org.bastanchu.churierpv2.view.common.listener

import org.bastanchu.churierpv2.view.common.event.GridEvent

interface GridListener<T> {

    fun itemSelected(event: GridEvent<T>)

}
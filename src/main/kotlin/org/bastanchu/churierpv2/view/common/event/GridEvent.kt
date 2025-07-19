package org.bastanchu.churierpv2.view.common.event

import org.bastanchu.churierpv2.view.common.Grid

data class GridEvent<T>(val item: T, val sourceGrid: Grid<T>)
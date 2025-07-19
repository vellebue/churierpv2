package org.bastanchu.churierpv2.view.main



data class MenuModel(var id: String = "",
                     var textKey: String = "",
                     var viewClass: String = "",
                     var children: List<MenuModel> = ArrayList<MenuModel>()) {
}
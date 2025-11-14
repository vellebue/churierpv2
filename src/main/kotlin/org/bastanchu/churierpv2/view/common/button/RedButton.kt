package org.bastanchu.churierpv2.view.common.button

import com.vaadin.flow.component.button.Button

class RedButton(text: String) : Button(text) {

    init {
        addClassName("red-button")
    }
}
package org.bastanchu.churierpv2.view.common.button

import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant

class BlueButton(text: String) : Button(text)  {

    init {
        addThemeVariants(ButtonVariant.LUMO_PRIMARY)
    }
}
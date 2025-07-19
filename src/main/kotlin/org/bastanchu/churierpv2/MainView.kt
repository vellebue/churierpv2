package org.bastanchu.churierpv2

import com.github.mvysny.karibudsl.v10.KComposite
import com.github.mvysny.karibudsl.v10.verticalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout

import com.vaadin.flow.router.Route
import org.bastanchu.churierpv2.view.main.TitleBar
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.MessageSource


@Route
class MainView(@Autowired val messages : MessageSource,
               @Autowired val applicationContext: ApplicationContext) : KComposite() {
    /**
     * Construct a new Vaadin view.
     *
     * Build the initial UI state for the user accessing the application.
     */
    private val root = ui {
        // Use custom CSS classes to apply styling. This is defined in
        // styles.css.
        verticalLayout(classNames = "centered-content") {

            val bodyPanel = VerticalLayout()
            val titleBar = TitleBar(messages, applicationContext, bodyPanel)
            add(titleBar)
            add(bodyPanel)

            /*
            // Use TextField for standard text input
            val nameField = textField("Your name") {
                addClassName("bordered")
            }

            // Button click listeners can be defined as lambda expressions
            button("Say hello") {
                // Theme variants give you predefined extra styles for components.
                // Example: Primary button has a more prominent look.
                setPrimary()

                // You can specify keyboard shortcuts for buttons.
                // Example: Pressing enter in this view clicks the Button.
                addClickShortcut(Key.ENTER)

                onLeftClick {
                    //this@verticalLayout.p(service.greet(nameField.value))
                }
            }

            */
        }
    }
}
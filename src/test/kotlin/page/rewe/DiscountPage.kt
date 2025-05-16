package org.example.page.rewe

import io.appium.java_client.AppiumBy
import io.appium.java_client.android.AndroidDriver
import org.openqa.selenium.NoSuchElementException
import org.openqa.selenium.WebElement

data class DiscountedItem(val text: String, val description: String, val price: String)

class DiscountPage(private val driver: AndroidDriver) {

    private val containerLocator = AppiumBy.xpath("//android.widget.TextView[@text='Aktion']/../..")
    private val priceLocatorRelative = AppiumBy.xpath("./*/android.view.View[3]/android.widget.TextView")
    private val textLocatorRelative = AppiumBy.xpath("./*/android.widget.TextView[1]")
    private val descriptionLocatorRelative = AppiumBy.xpath("./*/android.widget.TextView[2]") // not really sure why the /*/ is necessary
    private val scrollViewLocator = AppiumBy.xpath("//androidx.compose.ui.platform.ComposeView/android.view.View/android.view.View/android.view.View[2]")

    fun getAllDiscountedItems(): List<DiscountedItem> {
        val discountedItemsSet = mutableSetOf<DiscountedItem>()
        val maxScrollAttempts = 100 // To prevent infinite loops
        var scrollAttempts = 0
        var previousPageSource = ""

        while (scrollAttempts < maxScrollAttempts) {
            val currentPageSource = driver.pageSource
            if (scrollAttempts > 0 && previousPageSource == currentPageSource) {
                println("Page source unchanged since last scroll, assuming end of list.")
                break
            }
            previousPageSource = currentPageSource // Update for the next iteration's comparison
            val containers = driver.findElements(containerLocator)


            for (container in containers) {
                try {
                    val text = container.findElement(textLocatorRelative).text
                    val description = container.findElement(descriptionLocatorRelative).text
                    val price = container.findElement(priceLocatorRelative).text
                    val item = DiscountedItem(text, description, price)
                    if (discountedItemsSet.add(item)) {
                        println(item.text + " - " + item.price + " - " + item.description)
                    }
                } catch (e: NoSuchElementException) {
                    // Element not found, skip. Parameter e is not used.
                }
            }

            // Removed empty if block that was here

            val scrollViewElement: WebElement
            try {
                scrollViewElement = driver.findElement(scrollViewLocator)
                if (!scrollViewElement.isDisplayed) {
                    break
                }
            } catch (e: NoSuchElementException) {
                // Scrollable element not found, cannot scroll further. Parameter e is not used.
                break
            }
            // Perform scroll action
            try {
                driver.executeScript("mobile: swipeGesture", mapOf(
                    "element" to scrollViewElement,
                    "direction" to "up",
                    "percent" to 0.30,
                    "speed" to 5000
                ))
                Thread.sleep(200)
            } catch (e: Exception) {
                // Exception during scroll. Parameter e is not used.
                break
            }

            scrollAttempts++
        }
        return discountedItemsSet.toList()
    }
}


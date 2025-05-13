package org.example

import io.appium.java_client.AppiumBy
import io.appium.java_client.android.AndroidDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration

class DiscountPage(private val driver: AndroidDriver) {

    private val searchInputLocator = AppiumBy.id("de.hit.android.hitapp:id/et_search")
    private val containerLocator = AppiumBy.id("de.hit.android.hitapp:id/container")
    private val headlineLocator = AppiumBy.id("de.hit.android.hitapp:id/headline")

    fun clickSearchInput() {
        val wait = WebDriverWait(driver, Duration.ofSeconds(10))
        val searchInputElement = wait.until(ExpectedConditions.presenceOfElementLocated(searchInputLocator))
        searchInputElement.click()
    }

    fun enterSearchText(text: String) {
        val searchInputElement = driver.findElement(searchInputLocator)
        searchInputElement.sendKeys(text)
    }

    fun findHeadlineInContainersWithText(text: String): WebElement? {
        val containerElements = driver.findElements(containerLocator)

        for (containerElement in containerElements) {
            try {
                val headlineElement = containerElement.findElement(headlineLocator)
                if (headlineElement.text.contains(text, ignoreCase = true)) {
                    return headlineElement
                }
            } catch (_: org.openqa.selenium.NoSuchElementException) {
                // Headline not found in this container, continue to the next one
            }
        }
        return null
    }
}

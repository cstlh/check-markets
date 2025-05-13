package org.example

import io.appium.java_client.AppiumBy
import io.appium.java_client.android.AndroidDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration

class DiscountPage(private val driver: AndroidDriver) {

    private val searchInputLocator = AppiumBy.id("de.hit.android.hitapp:id/et_search")
    private val containerLocator = AppiumBy.id("de.hit.android.hitapp:id/container")
    private val headlineLocator = AppiumBy.id("de.hit.android.hitapp:id/headline")
    private val productTextLocator = AppiumBy.id("de.hit.android.hitapp:id/text")

    fun clickSearchInput() {
        val wait = WebDriverWait(driver, Duration.ofSeconds(10))
        val searchInputElement = wait.until(ExpectedConditions.presenceOfElementLocated(searchInputLocator))
        searchInputElement.click()
    }

    fun enterSearchText(text: String) {
        val searchInputElement = driver.findElement(searchInputLocator)
        searchInputElement.sendKeys(text)
    }

    fun clearSearchInput() {
        val searchInputElement = driver.findElement(searchInputLocator)
        searchInputElement.clear()
    }

    fun findHeadlineInContainersWithText(text: String): Boolean {
        val containerElements = driver.findElements(containerLocator)

        for (containerElement in containerElements) {
            try {
                val headlineElement = containerElement.findElement(headlineLocator)
                if (headlineElement.text.contains(text, ignoreCase = true)) {
                    return true
                }
            } catch (_: org.openqa.selenium.NoSuchElementException) {
                // Headline not found in this container, continue to the next one
            }
        }
        return false
    }

    fun getProductTextsForMatchingHeadlines(text: String): Set<String>? {
        val containerElements = driver.findElements(containerLocator)
        val matchingProductTexts = mutableSetOf<String>()

        for (containerElement in containerElements) {
            try {
                val headlineElement = containerElement.findElement(headlineLocator)
                if (headlineElement.text.contains(text, ignoreCase = true)) {
                    try {
                        val productTextElement = containerElement.findElement(productTextLocator)
                        matchingProductTexts.add(productTextElement.text)
                    } catch (_: org.openqa.selenium.NoSuchElementException) {
                        // Product text not found in this container
                    }
                }
            } catch (_: org.openqa.selenium.NoSuchElementException) {
                // Headline not found in this container, continue to the next one
            }
        }
        return matchingProductTexts.ifEmpty { null }
    }
}

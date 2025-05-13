package org.example

import io.appium.java_client.AppiumBy
import io.appium.java_client.android.AndroidDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration

class HomePage(private val driver: AndroidDriver) {

    private val discountLinkLocator = AppiumBy.androidUIAutomator("new UiSelector().text(\"Angebote\")")

    fun clickDiscountLink(): WebElement {
        val wait = WebDriverWait(driver, Duration.ofSeconds(10))
        val discountLinkElement = wait.until(ExpectedConditions.presenceOfElementLocated(discountLinkLocator))
        discountLinkElement.click()
        return discountLinkElement
    }

}

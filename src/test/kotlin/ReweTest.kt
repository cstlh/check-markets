package org.example

import io.appium.java_client.android.AndroidDriver
import io.appium.java_client.remote.MobileCapabilityType
import org.example.page.rewe.DiscountPage
import org.example.page.rewe.FooterPage
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.openqa.selenium.remote.DesiredCapabilities
import java.io.File
import java.net.URI

class ReweTest {

    private lateinit var driver: AndroidDriver
    private lateinit var footerPage: FooterPage
    private lateinit var discountPage: DiscountPage

    @BeforeEach
    fun setUp() {
        val capabilities = DesiredCapabilities()
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android")
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Pixel_7")
        capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2")
        capabilities.setCapability("appPackage", "de.rewe.app.mobile")
        capabilities.setCapability("appActivity", "de.rewe.app.app.view.MainActivity")
        capabilities.setCapability(MobileCapabilityType.NO_RESET, true)
        capabilities.setCapability("forceAppLaunch", true)

        val url = URI("http://127.0.0.1:4723/").toURL()
        driver = AndroidDriver(url, capabilities)
        footerPage = FooterPage(driver)
        discountPage = DiscountPage(driver)
    }

    @AfterEach
    fun tearDown() {
        driver.quit()
    }

    @Test
    fun test() {
        footerPage.clickDiscountLink()
        val itemsToTest = listOf("pepsi", "oryza", "ben&Jerry's", "magnum", "Barilla")
        val allDiscountedItems = discountPage.getAllDiscountedItems()
        val filteredItems = allDiscountedItems.filter { discountedItem ->
            itemsToTest.any { searchString ->
                discountedItem.text.contains(searchString, ignoreCase = true)
            }
        }
        println("----------")
        println("Filtered items:")
        // Print the filtered items
        filteredItems.forEach { println(it) }

        val resultsFile = File("src/test/resources/test_results.csv")
        if (!resultsFile.exists() || resultsFile.readText().isEmpty()) {
            resultsFile.writeText("Market,Search Term Used,Product Text\n")
        }


        filteredItems.forEach { item ->
            val matchingSearchTerms = itemsToTest.filter { searchTerm ->
                item.text.contains(searchTerm, ignoreCase = true)
            }.joinToString(" | ")

            resultsFile.appendText("Rewe,\"$matchingSearchTerms\",\"${item.text}\"\n")
        }
    }
}

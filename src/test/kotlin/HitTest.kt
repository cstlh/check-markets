package org.example

import io.appium.java_client.android.AndroidDriver
import io.appium.java_client.remote.MobileCapabilityType
import org.example.page.hit.DiscountPage
import org.example.page.hit.FooterPage
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.openqa.selenium.remote.DesiredCapabilities
import java.io.File

class HitTest {

    private lateinit var driver: AndroidDriver
    private lateinit var footerPage: FooterPage
    private lateinit var discountPage: DiscountPage

    @BeforeEach
    fun setUp() {
        val capabilities = DesiredCapabilities()
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android")
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Pixel_7")
        capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2")
        capabilities.setCapability("appPackage", "de.hit.android.hitapp")
        capabilities.setCapability("appActivity", "de.hit.three.presentation.MainActivity")
        capabilities.setCapability(MobileCapabilityType.NO_RESET, true)
        capabilities.setCapability("forceAppLaunch", true)

        val url = java.net.URI("http://127.0.0.1:4723/").toURL()
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
        val itemsToTest = listOf("pepsi", "ben&Jerry's", "magnum")
        itemsToTest.forEach { item ->
            val hasDiscount = checkDiscount(item)
            if (hasDiscount) {
                println("Discount found for: $item")
            } else {
                println("No discount found for: $item")
            }
        }
    }

    fun checkDiscount(text: String): Boolean {
        discountPage.clickSearchInput()
        discountPage.enterSearchText(text)
        driver.hideKeyboard()
        val found = discountPage.findHeadlineInContainersWithText(text)
        discountPage.clearSearchInput()
        return found
    }

    @Test
    fun test2() {
        footerPage.clickDiscountLink()
        val itemsFile = File("src/test/resources/items_to_test.txt")
        val resultsFile = File("src/test/resources/test_results.csv")
        resultsFile.writeText("Market,Search Term,Product Text\n")

        val itemsToTest = itemsFile.readLines().filter { it.isNotBlank() }

        itemsToTest.forEach { item ->
            val discountTextList = checkDiscount2(item)
            if (discountTextList?.isNotEmpty() == true) {
                val resultString = "Hit,\"$item\",\"${discountTextList.joinToString(", ")}\""
                println("Discount found for: $item - $discountTextList")
                println("---------------------")
                resultsFile.appendText("$resultString\n")
            }
        }
    }

    fun checkDiscount2(text: String): Set<String>? {
        discountPage.clickSearchInput()
        discountPage.enterSearchText(text)
        driver.hideKeyboard()
        val found = discountPage.getProductTextsForMatchingHeadlines(text)
        discountPage.clearSearchInput()
        return found
    }

}


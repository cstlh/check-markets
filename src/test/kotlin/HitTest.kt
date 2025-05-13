package org.example

import io.appium.java_client.android.AndroidDriver
import io.appium.java_client.remote.MobileCapabilityType
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.openqa.selenium.remote.DesiredCapabilities


class HitTest {

    companion object {
        private var driver: AndroidDriver? = null
        private lateinit var homePage: HomePage
        private lateinit var discountPage: DiscountPage // Added DiscountPage

        @BeforeAll
        @JvmStatic
        fun setUp() {
            val capabilities = DesiredCapabilities()
            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android")
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Pixel_7") // Replace with your device name or UDID
            capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2")
            capabilities.setCapability("appPackage", "de.hit.android.hitapp",) // This might vary based on the Android version/ROM
            capabilities.setCapability("appActivity", "de.hit.three.presentation.MainActivity") // This might vary
            capabilities.setCapability(MobileCapabilityType.NO_RESET, true) // Do not reset app state before the session


            // Replace with your Appium server URL
            val url = java.net.URI("http://127.0.0.1:4723/").toURL()
            driver = AndroidDriver(url, capabilities)
            homePage = HomePage(driver!!)
            discountPage = DiscountPage(driver!!) // Initialize DiscountPage
        }

        @AfterAll
        @JvmStatic
        fun tearDown() {
            driver?.quit()
        }
    }

    @Test
    fun test(){
        homePage.clickDiscountLink()
        val itemsToTest = listOf("pepsi", "ben&Jerry's", "magnum") // put this in file later
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
        discountPage.clickSearchInput()
        val found = discountPage.findHeadlineInContainersWithText(text)
        discountPage.clearSearchInput()
        return found
    }

    @Test
    fun test2(){
        homePage.clickDiscountLink()
        val itemsToTest = listOf("pepsi", "ben&Jerry's", "magnum", "true fruits") // put this in file later
        itemsToTest.forEach { item ->
            val discountTextList = checkDiscount2(item)
            if (discountTextList?.isNotEmpty() == true) {
                println("Discount found for: $item - $discountTextList")
            } else {
                println("No discount found for: $item")
            }
            println("---------------------")
        }
    }

    fun checkDiscount2(text: String): Set<String>? {
        discountPage.clickSearchInput()
        discountPage.enterSearchText(text)
        discountPage.clickSearchInput()
        val found = discountPage.getProductTextsForMatchingHeadlines(text)
        discountPage.clearSearchInput()
        return found
    }

}


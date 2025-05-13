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
    fun checkDiscount() {
        homePage.clickDiscountLink()
        discountPage.clickSearchInput()
        discountPage.enterSearchText("pepsi")
        discountPage.clickSearchInput()
        discountPage.findHeadlineInContainersWithText("pepsi")?.click();
    }
}


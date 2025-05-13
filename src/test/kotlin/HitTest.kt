package org.example

import io.appium.java_client.android.AndroidDriver
import io.appium.java_client.remote.MobileCapabilityType
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.openqa.selenium.remote.DesiredCapabilities
import java.io.File

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
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Pixel_7")
            capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2")
            capabilities.setCapability("appPackage", "de.hit.android.hitapp",)
            capabilities.setCapability("appActivity", "de.hit.three.presentation.MainActivity")
            capabilities.setCapability(MobileCapabilityType.NO_RESET, true)
            capabilities.setCapability("forceAppLaunch", true);


            // Replace with your Appium server URL
            val url = java.net.URI("http://127.0.0.1:4723/").toURL()
            driver = AndroidDriver(url, capabilities)
            homePage = HomePage(driver!!)
            discountPage = DiscountPage(driver!!)
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
        //discountPage.clickSearchInput()
        // hide keyboard
        driver?.hideKeyboard();
        val found = discountPage.findHeadlineInContainersWithText(text)
        discountPage.clearSearchInput()
        return found
    }

    @Test
    fun test2(){
        homePage.clickDiscountLink()
        val itemsFile = File("src/test/resources/items_to_test.txt")
        val resultsFile = File("src/test/resources/test_results.csv") // Changed to .csv
        resultsFile.writeText("Market,Search Term,Product Text\n") // Add CSV header

        val itemsToTest = itemsFile.readLines().filter { it.isNotBlank() }

        itemsToTest.forEach { item ->
            val discountTextList = checkDiscount2(item)
            if (discountTextList?.isNotEmpty() == true) {
                val resultString = "Hit,\"$item\",\"${discountTextList.joinToString(", ")}\""
                println("Discount found for: $item - $discountTextList") //temp log
                println("---------------------") //temp log
                resultsFile.appendText("$resultString\n")
            }
        }
    }

    fun checkDiscount2(text: String): Set<String>? {
        discountPage.clickSearchInput()
        discountPage.enterSearchText(text)
        //discountPage.clickSearchInput()
        // hide keyboard
        driver?.hideKeyboard();
        val found = discountPage.getProductTextsForMatchingHeadlines(text)
        discountPage.clearSearchInput()
        return found
    }

}


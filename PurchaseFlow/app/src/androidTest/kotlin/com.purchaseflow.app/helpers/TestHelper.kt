import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.await
import androidx.compose.ui.test.onNodeWithContentDescription

// Holds all helper functions that can be reused by other tests
internal class TestHelper(private val composeTestRule: ComposeTestRule) {
    // Helper function to log the current screen
    internal fun logCurrentScreen() {
        val currentScreen = composeTestRule.onRoot(useUnmergedTree = true)
        println("Current Screen: ${currentScreen.toString()}")
    }

    // Helper function to wait for an element with the specified content description to become visible
    // This will ensure that the element has to be visible first before further actions can be done
    internal fun waitForElementToBeVisible(contentDescription: String, timeout: Long): Boolean {
        var elapsedTime = 0L
        val pollingInterval = 100L // Milliseconds
        while (elapsedTime < timeout) {
            val node = composeTestRule.onNodeWithContentDescription(contentDescription)
            if (node.isDisplayed()) {
                return true
            }
            Thread.sleep(pollingInterval)
            elapsedTime += pollingInterval
        }
        return false
    }
}

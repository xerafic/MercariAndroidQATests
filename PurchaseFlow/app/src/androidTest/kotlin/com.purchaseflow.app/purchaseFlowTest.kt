@LargeTest
@RunWith(AndroidJUnit4::class)
@ExperimentalComposeUiApi
import com.example.myapp.helpers.TestCredentials
import org.junit.Test
import org.junit.Assert.assertTrue
import androidx.compose.ui.test.assertEquals
import androidx.compose.ui.test.assertDoesNotExist
import androidx.compose.ui.test.assertExist

class PurchaseFlowTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var loginHelper: LoginHelper

    // This assumes you have some setup to initialize the starting state of the app.
    @Before
    fun setUp() {
        // e.g. clear cart, initialize user state
        val userData = loadUserDataFromFile(composeTestRule.activity, "users_data.json")
        val userToUse = userData.find { it.username == TestCredentials.standardUser } ?: userData.first()
        loginHelper = LoginHelper(composeTestRule, mapOf(userToUse.username to userToUse.password))
    }

    @Test
    fun testPurchaseFlow() {
        // Initialize testHelper
        val testHelper = TestHelper(composeTestRule)

        // On the Login Screen (starting point), login with a user
        loginHelper.login(TestCredentials.standardUser, TestCredentials.standardPassword)

        // Log the current screen after the transition
        testHelper.logCurrentScreen()

        // Wait for the list (or any other element with content description) to become visible
        val listVisible = testHelper.waitForElementToBeVisible("inventory_list", 5000)

        // Assert that the element is displayed
        assertTrue("Item list is not displayed", listVisible)

        // Wait for the First Item to become visible
        val firstItemVisible = composeTestRule.waitForElementToBeVisible("item_4_title_link", 5000)

        // Assert that the element is displayed
        assertTrue("First item not displayed", firstItemVisible)

        // User is now on Showcase Screen, from a list of items being shown, open the first item from the list
        composeTestRule.onNodeWithTag("item_4_title_link").performClick()

        // Log the current screen after the transition
        testHelper.logCurrentScreen()

        // On the Item Screen, add this item to the cart
        composeTestRule.onNodeWithTag("add-to-cart-sauce-labs-backpack").performClick()

        // Go back to Showcase Screen
        composeTestRule.onNodeWithTag("back-to-products").performClick()

        // Log the current screen after the transition
        testHelper.logCurrentScreen()

        // Wait for the Second Item to become visible
        val secondItemVisible = composeTestRule.waitForElementToBeVisible("item_0_title_link", 5000)

        // Assert that the element is displayed
        assertTrue("Second item not displayed", secondItemVisible)

        // Add the second item of the list into the cart
        composeTestRule.onNodeWithTag("add-to-cart-sauce-labs-bike-light").performClick()

        // Go to the Cart Screen and start the purchase flow
        composeTestRule.onNodeWithTag("shopping_cart_link").performClick()

        // Log the current screen after the transition
        testHelper.logCurrentScreen()

        // This will inform the user uses how many items there are in their cart, this is very important so that the user can keep track what they are going to buy.
        // If number of items in the cart are not visible, the user would have to go back in and out of the cart page to know how many items the user already has
        // I have asserted the badge value should be 2 since we added two items to the cart
        composeTestRule.onNodeWithTag("shopping_cart_badge").assertExists()
        assertEquals(2, composeTestRule.onNodeWithText("shopping_cart_badge").text.toInt())
        composeTestRule.onNodeWithTag("shopping_cart_badge").text

        // On Address Screen, complete the address and continue
        // Saucedemo doesn't have the address screen so I'm just putting AddressFiled
        // For the Address I just put Some address
        // Same as the continue button
        composeTestRule.onNodeWithTag("AddressField").performTextInput("Some Address")
        composeTestRule.onNodeWithTag("continue_btn").performClick()

        // On Confirm Purchase Screen, finish the purchase and verify it was successful
        // Check if user is actually in the Confirm Purchase Screen
        // Click on the checkout button to actually purchase the item
        // Assert that shopping cart is empty after purchase
        composeTestRule.onNodeWithText("Confirm Purchase").assertExists()
        composeTestRule.onNodeWithTag("checkout_btn").performClick()
        composeTestRule.onNodeWithText("Purchase Successful").assertExists()
        assertDoesNotExist(composeTestRule.onNodeWithTag("shopping_cart_badge"))
    }
}

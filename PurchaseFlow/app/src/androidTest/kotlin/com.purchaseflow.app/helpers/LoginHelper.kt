// Login helper to allow other tests to reuse the login function
class LoginHelper(private val composeTestRule: ComposeTestRule) {
    fun login(username: String, password: String) {
        composeTestRule.onNodeWithTag("user-name").performTextInput(username)
        composeTestRule.onNodeWithTag("password").performTextInput(password)
        composeTestRule.onNodeWithTag("login-button").performClick()
    }
}
import components.mainContainer
import helpers.getJson
import model.Configuration
import model.UiState
import react.dom.render
import redux.createReducer
import redux.solveEffect
import store.Store
import kotlin.browser.document

/**
 * Main method
 *
 * This is the application entry point
 * @param args main arguments
 */
fun main(args: Array<String>) {
    getJson<Configuration>("config.json")
        .then { configuration ->
            val logger = console
            if (document.getElementById("mocha") != null) {
                logger.info("Test mode detected")
            } else {
                bootstrapApplication(configuration)
            }
        }
}

/**
 * Bootstrap application
 *
 * @param config the configuration
 */
private fun bootstrapApplication(config: Configuration) {
    val host = document.querySelector(config.host)

    // Initial state
    val uiState = UiState(config)

    // Create the store
    val effect = solveEffect(config.url) { url, body ->
        // POST to url to get a Array<SolveMove>
        TODO("4.3")
    }
    val appStore = Store.create(uiState, createReducer(), effect)

    render(host) {
        mainContainer(config, appStore)
    }
}

package model


/**
 * Application configuration
 */
interface Configuration {
    val minCapacity: Int
    val maxCapacity: Int
    val url: String
    val debug: Boolean
    val host: String
    val initial: String
    val final: String
}

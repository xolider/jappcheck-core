package dev.vicart.jappcheck.core.exceptions

/**
 * Thrown when the JAppCheck library is used without being initialized
 * @author Clément Vicart
 * @since 1.0
 */
class JAppCheckNotInitializedException : RuntimeException("JAppCheck is not initialized. Use JAppCheck.initialize() first") {
}
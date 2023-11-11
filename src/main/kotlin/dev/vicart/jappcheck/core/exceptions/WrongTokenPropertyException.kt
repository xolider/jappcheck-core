package dev.vicart.jappcheck.core.exceptions

/**
 * Thrown when the token does not respect the Google standards
 * @see <a href="https://firebase.google.com/docs/app-check/custom-resource-backend?hl=fr">Google standards</a>
 * @author Clément Vicart
 * @since 1.0
 */
class WrongTokenPropertyException : RuntimeException("The provided token does not respect Google App Check standards") {
}
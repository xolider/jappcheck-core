package dev.vicart.jappcheck.core.exceptions

/**
 * Thrown when the token has an expiration date before today
 * @author Clément Vicart
 * @since 1.0
 */
class TokenExpiredException : RuntimeException("The provided token is expired") {
}
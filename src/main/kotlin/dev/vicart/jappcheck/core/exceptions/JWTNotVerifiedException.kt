package dev.vicart.jappcheck.core.exceptions

/**
 * Thrown when RSA verifiers could not verify the token
 * @author Clément Vicart
 * @since 1.0
 */
class JWTNotVerifiedException : RuntimeException("JWT Token could not be verified") {
}
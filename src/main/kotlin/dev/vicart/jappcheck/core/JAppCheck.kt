package dev.vicart.jappcheck.core

import com.nimbusds.jose.JOSEObjectType
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.crypto.RSASSAVerifier
import com.nimbusds.jose.jwk.JWK
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jwt.SignedJWT
import dev.vicart.jappcheck.core.exceptions.JAppCheckNotInitializedException
import dev.vicart.jappcheck.core.exceptions.JWTNotVerifiedException
import dev.vicart.jappcheck.core.exceptions.TokenExpiredException
import dev.vicart.jappcheck.core.exceptions.WrongTokenPropertyException
import java.net.URL
import java.time.LocalDateTime
import java.time.ZoneOffset

/**
 * Util class that verifies a provided token against Google App Check JWKS
 * @author Clément Vicart
 * @since 1.0
 */
object JAppCheck {

    private lateinit var jwks: JWKSet
    private lateinit var projectId: String

    /**
     * Sets the project ID and load JWKS set from Google
     * @since 1.0
     * @param projectId the firebase project ID
     */
    fun initialize(projectId: String) {
        jwks = JWKSet.load(URL("https://firebaseappcheck.googleapis.com/v1/jwks"))
        this.projectId = projectId
    }

    /**
     * Checks the given token for App Check pass
     * @param token App Check Token
     * @since 1.0
     */
    fun checkAppToken(token: String) {
        if(!this::jwks.isInitialized || !this::projectId.isInitialized) {
            throw JAppCheckNotInitializedException()
        }

        val jws = SignedJWT.parse(token)
        val verifiers = jwks.keys.map(JWK::toRSAKey).map(::RSASSAVerifier)

        val verified = verifiers.any {
            isVerified(jws, it)
        }

        if(!verified) throw JWTNotVerifiedException()

        val header = jws.header
        if(header.algorithm != JWSAlgorithm.RS256 || header.type != JOSEObjectType.JWT) {
            throw WrongTokenPropertyException()
        }

        val payload = jws.payload.toJSONObject()

        if(payload["iss"] != "https://firebaseappcheck.googleapis.com/$projectId") {
            throw WrongTokenPropertyException()
        }

        if((LocalDateTime.ofEpochSecond(payload["exp"] as Long, 0, ZoneOffset.of("+2"))).isBefore(LocalDateTime.now())) {
            throw TokenExpiredException()
        }

        if(!(payload["aud"] as ArrayList<*>).contains("projects/$projectId")) {
            throw WrongTokenPropertyException()
        }
    }

    private fun isVerified(jws: SignedJWT, verifier: RSASSAVerifier): Boolean {
        return jws.verify(verifier)
    }
}
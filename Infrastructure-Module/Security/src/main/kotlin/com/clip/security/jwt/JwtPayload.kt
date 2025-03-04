package com.clip.security.jwt

import com.clip.domain.user.enums.LoginProvider
import com.clip.security.jwt.user.TokenType
import io.jsonwebtoken.Claims
import java.util.*
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor


data class JwtPayload<T: JwtClaims> (
    val issuedAt: Date = Date(),
    val issuer: String,
    val subject: String,
    val expireTime: Long,
    val claims: T
) {
}


interface JwtClaims{

    fun getClaims(): Map<String, Any>{
        val claims = mutableMapOf<String, Any>()
        // JwtClaims 구현체의 프로퍼티를 순회하며 claims에 추가
        this::class.memberProperties.forEach {
            claims[it.name] = it.getter.call(this) as Any
        }
        return claims
    }

    companion object{
        inline fun <reified T: JwtClaims> convertFromClaims(claims: Claims): T {
            val claimsMap = claims.toMap()
            val constructor = T::class.primaryConstructor ?: throw IllegalArgumentException("Primary constructor not found")

            val args = constructor.parameters.associateWith {
                val value = claimsMap[it.name]?: throw IllegalArgumentException("Claim not found")

                when(it.type.classifier){
                    String::class -> value.toString()
                    Long::class -> value.toString().toLong()
                    LoginProvider::class -> LoginProvider.parse(value.toString())
                    TokenType::class -> TokenType.valueOf(value.toString())
                    else -> throw IllegalArgumentException("Unsupported type")
                }
            }
            return constructor.callBy(args)
        }
    }


}
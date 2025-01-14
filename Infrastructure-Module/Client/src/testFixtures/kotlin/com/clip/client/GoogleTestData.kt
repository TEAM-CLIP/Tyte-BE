package com.clip.client

object GoogleTestData {

    val OAUTH_SUCCESS_RESPONSE =
        MockServer.Response(
            responseCode = 200,
            body =
            """
                {
                  "sub": "socialId",
                  "name": "홍길동",
                  "given_name": "길동",
                  "family_name": "홍",
                  "picture": "https://example.com/photo.jpg",
                  "email": "abcd@example.com",
                  "email_verified": true,
                  "locale": "ko"
                }
                """.trimIndent(),
            headers =
            mapOf(
                "Content-Type" to "application/json",
            ),
        )

    val OAUTH_FAIL_RESPONSE_WITH_NON_REGISTERED =
        MockServer.Response(
            responseCode = 200,
            body =
            """
                {
                  "sub": "non-registered",
                  "name": "홍길동",
                  "given_name": "길동",
                  "family_name": "홍",
                  "picture": "https://example.com/photo.jpg",
                  "email": "abcd@example.com",
                  "email_verified": true,
                  "locale": "ko"
                }
                """.trimIndent(),
            headers =
            mapOf(
                "Content-Type" to "application/json",
            ),
        )

    val OAUTH_FAIL_RESPONSE_WITH_INVALID_ACCESS_TOKEN =
        MockServer.Response(
            responseCode = 401,
            body =
            """
                {
                    "error": "invalid_token",
                    "error_description": "An error occurred while attempting to retrieve the access token."
                }
                """.trimIndent(),
            headers =
            mapOf(
                "Content-Type" to "application/json",
            ),
        )
}
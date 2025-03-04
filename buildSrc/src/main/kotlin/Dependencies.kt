object Dependencies {
    object Spring {
        const val BOOT = "org.springframework.boot:spring-boot-starter"
        const val WEB = "org.springframework.boot:spring-boot-starter-web"
        const val DATA_JPA = "org.springframework.boot:spring-boot-starter-data-jpa"
        const val TEST = "org.springframework.boot:spring-boot-starter-test"
        const val WEBFLUX = "org.springframework.boot:spring-boot-starter-webflux"
        const val REACTOR_TEST = "io.projectreactor:reactor-test"
        const val OPEN_API = "org.springdoc:springdoc-openapi-starter-webmvc-ui:${Versions.OPEN_API}"
        const val TRANSACTION = "org.springframework:spring-tx"
        const val ACTUATOR = "org.springframework.boot:spring-boot-starter-actuator"
    }

    object TemplateEngine{
        const val THYMELEAF = "org.springframework.boot:spring-boot-starter-thymeleaf"
    }

    object DATABASE {
        const val MYSQL = "com.mysql:mysql-connector-j"
        const val H2 = "com.h2database:h2"
    }

    object Kotlin {
        const val KOTLIN_REFLECT = "org.jetbrains.kotlin:kotlin-reflect"
        const val KOTLIN_TEST_JUNIT5 = "org.jetbrains.kotlin:kotlin-test-junit5"
    }

    object Kotest {
        const val KOTEST_RUNNER = "io.kotest:kotest-runner-junit5:${Versions.KOTEST_VERSION}"
        const val KOTEST_ASSERTIONS_CORE = "io.kotest:kotest-assertions-core:${Versions.KOTEST_VERSION}"
        const val KOTEST_PROPERTY = "io.kotest:kotest-property:${Versions.KOTEST_VERSION}"
    }

    object Mockk {
        const val MOCKK = "io.mockk:mockk:${Versions.MOCKK_VERSION}"
    }

    object Junit {
        const val JUNIT_LAUNCHER = "org.junit.platform:junit-platform-launcher"
    }

    object Logger {
        const val KOTLIN_OSHAI = "io.github.oshai:kotlin-logging-jvm:${Versions.KOTLIN_OSHAI}"
    }

    object MockServer {
        const val MOCK_SERVER = "com.squareup.okhttp3:mockwebserver"
    }

    object Jwt {
        const val JWT_API = "io.jsonwebtoken:jjwt-api:${Versions.JJWT_VERSION}"
        const val JWT_IMPL = "io.jsonwebtoken:jjwt-impl:${Versions.JJWT_VERSION}"
        const val JWT_JACKSON = "io.jsonwebtoken:jjwt-jackson:${Versions.JJWT_VERSION}"
    }

    object Bcrypt {
        const val BCRYPT = "at.favre.lib:bcrypt:${Versions.BCRYPT}"
    }

    object UUID {
        const val FASTER_XML = "com.fasterxml.uuid:java-uuid-generator:${Versions.UUID_GENERATOR}"
    }

    object FLYWAY {
        const val FLYWAY_CORE = "org.flywaydb:flyway-core"
        const val FLYWAY_MYSQL = "org.flywaydb:flyway-mysql"
    }

    object Jackson {
        const val KOTLIN = "com.fasterxml.jackson.module:jackson-module-kotlin"
        const val JAVA_TIME = "com.fasterxml.jackson.datatype:jackson-datatype-jsr310"
    }
}

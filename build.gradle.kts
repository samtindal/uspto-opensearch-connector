plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.8.21"
}

group = "com.uspto.opensearch"
version = "1.0.0"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21)) // Use Java 21 LTS
    }
}

dependencies {
    // AWS SDK for OpenSearch
    implementation("software.amazon.awssdk:opensearch:2.20.6")

    // Logging with SLF4J and Logback
    implementation("org.slf4j:slf4j-api:1.7.32")
    implementation("ch.qos.logback:logback-classic:1.2.11")

    // Unit Testing with JUnit 5
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.3")

    // Mocking with Mockito
    testImplementation("org.mockito:mockito-core:4.11.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.1.0")
}

tasks {
    test {
        useJUnitPlatform() // Enable JUnit 5
    }

    compileKotlin {
        kotlinOptions.jvmTarget = "21" // Set Kotlin to target Java 21
    }

    compileTestKotlin {
        kotlinOptions.jvmTarget = "21" // Set Kotlin test compilation to target Java 21
    }
}

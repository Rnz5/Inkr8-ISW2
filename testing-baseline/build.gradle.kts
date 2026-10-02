import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version "2.4.20"
}

repositories {
    mavenCentral()
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_1_8)
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

sourceSets {
    main {
        kotlin.setSrcDirs(listOf("../app/src/main/java"))
        kotlin.include(
            "com/inkr8/economy/EconomyConfig.kt",
            "com/inkr8/economy/RankedCostCalculator.kt",
            "com/inkr8/economy/TournamentEconomyCalculator.kt",
            "com/inkr8/economy/TournamentEconomyProjection.kt",
            "com/inkr8/economy/TournamentRewardCalculator.kt",
            "com/inkr8/rating/ReputationManager.kt",
            "com/inkr8/utils/ValidationUtils.kt",
        )
    }
}

dependencies {
    testImplementation(kotlin("test-junit"))
    testImplementation("junit:junit:4.13.2")
}

import org.jetbrains.kotlin.gradle.dsl.JvmTarget

// Versions and dependencies reused from the authentic testing-baseline harness.
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

val generatedWritingSources = layout.buildDirectory.dir("generated/writing")
val extractWritingSources by tasks.registering(Exec::class) {
    inputs.file("../../app/src/main/java/com/inkr8/screens/Writing.kt")
    inputs.file("extract_writing.py")
    inputs.file("../../app/src/main/java/com/inkr8/evaluation/WritingAdmission.kt")
    outputs.dir(generatedWritingSources)
    commandLine("python", file("extract_writing.py").absolutePath,
        generatedWritingSources.get().asFile.absolutePath)
}

sourceSets {
    main {
        kotlin.setSrcDirs(listOf("../../app/src/main/java", "src/fixtures/kotlin", generatedWritingSources))
        kotlin.include(
            "com/inkr8/utils/ValidationUtils.kt",
            "com/inkr8/data/Gamemodes.kt",
            "com/inkr8/data/Theme.kt",
            "com/inkr8/data/Topics.kt",
            "com/inkr8/blocks/WritingSourceProbe.kt",
            "com/inkr8/evaluation/WritingAdmission.kt",
            "com/inkr8/evaluation/AdmissionReference.kt",
            "com/inkr8/data/AdmissionModeFixture.kt",
        )
    }
}

tasks.named("compileKotlin") {
    dependsOn(extractWritingSources)
}

dependencies {
    testImplementation(kotlin("test-junit"))
    testImplementation("junit:junit:4.13.2")
}

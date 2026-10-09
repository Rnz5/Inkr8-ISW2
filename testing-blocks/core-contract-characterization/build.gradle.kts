import org.jetbrains.kotlin.gradle.dsl.JvmTarget

// Isolated harness; versions reused from the authentic testing-baseline.
plugins { kotlin("jvm") version "2.4.20" }
repositories { mavenCentral() }
kotlin { compilerOptions { jvmTarget.set(JvmTarget.JVM_1_8) } }
java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
val generated = layout.buildDirectory.dir("generated/core")
val extractCore by tasks.registering(Exec::class) {
    inputs.files("../../app/src/main/java/com/inkr8/utils/DraftManager.kt",
        "../../app/src/main/java/com/inkr8/viewmodel/AppViewModel.kt", "extract_core.py", "../../app/src/main/java/com/inkr8/viewmodel/ResultWaitPolicy.kt")
    outputs.dir(generated)
    commandLine("python", file("extract_core.py").absolutePath, generated.get().asFile.absolutePath)
}
sourceSets {
    main {
        kotlin.setSrcDirs(listOf("../../app/src/main/java", generated))
        kotlin.include("com/inkr8/data/SubmissionStatus.kt", "com/inkr8/viewmodel/ResultWaitPolicy.kt", "com/inkr8/characterization/CoreSourceProbe.kt")
    }
}
tasks.named("compileKotlin") { dependsOn(extractCore) }
dependencies {
    testImplementation(kotlin("test-junit"))
    testImplementation("junit:junit:4.13.2")
}

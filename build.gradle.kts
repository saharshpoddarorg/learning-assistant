// Root build file — shared conventions for all modules

plugins {
    java
    idea
}

idea {
    module {
        // Exclude legacy directories from IntelliJ indexing
        excludeDirs.addAll(files(
            "temp-atlassian-tools"
        ))
    }
}

allprojects {
    group = "com.learning"
    version = "1.0.0-SNAPSHOT"
}

subprojects {
    apply(plugin = "java")

    java {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
        toolchain {
            languageVersion = JavaLanguageVersion.of(21)
        }
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.compilerArgs.addAll(listOf("-Xlint:all", "-Xlint:-serial"))
    }

    repositories {
        mavenCentral()
    }

    dependencies {
        "testImplementation"(platform("org.junit:junit-bom:6.1.0"))
        "testImplementation"("org.junit.jupiter:junit-jupiter")
        "testRuntimeOnly"("org.junit.platform:junit-platform-launcher")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

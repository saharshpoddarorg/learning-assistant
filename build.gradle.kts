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
        sourceCompatibility = JavaVersion.VERSION_26
        targetCompatibility = JavaVersion.VERSION_26
        toolchain {
            languageVersion = JavaLanguageVersion.of(26)
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
        val catalog = rootProject.extensions.getByType<VersionCatalogsExtension>().named("libs")
        "testImplementation"(platform(catalog.findLibrary("junit-bom").get()))
        "testImplementation"(catalog.findBundle("testing").get())
        "testRuntimeOnly"(catalog.findLibrary("junit-platform-launcher").get())
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

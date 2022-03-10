plugins {
    `java-library`
}

val spotBugsVersion : String by extra

dependencies {
    implementation(project(":annotation"))

    // Do not add any other non-test runtime dependencies

    testImplementation("com.github.spotbugs:spotbugs-annotations:$spotBugsVersion")
}
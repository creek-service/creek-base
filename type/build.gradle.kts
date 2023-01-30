plugins {
    `java-library`
}

val spotBugsVersion : String by extra

dependencies {
    api(project(":annotation"))

    implementation("com.github.spotbugs:spotbugs-annotations:$spotBugsVersion")

    // Do not add any other non-test runtime dependencies
}
import org.javamodularity.moduleplugin.extensions.CompileTestModuleOptions

plugins {
    `java-library`
}

val spotBugsVersion : String by extra

dependencies {
    // Do not add any non-test runtime dependencies

    testImplementation("com.github.spotbugs:spotbugs-annotations:$spotBugsVersion")
}
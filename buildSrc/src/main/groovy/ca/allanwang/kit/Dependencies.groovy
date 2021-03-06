package ca.allanwang.kit

/**
 * Some common dependencies, backed by the supplied versions
 */
class Dependencies {
    static def kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"
    static def kotlinTest = "org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}"
    static def junit = "junit:junit:${Versions.junit}"
}
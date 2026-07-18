plugins {
    application
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation(libs.guava)
    implementation(libs.mysql.connector)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    // FIX: mainClass harus "tubes.App" dan App.java sudah kita buat
    mainClass = "tubes.App"
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

// FIX: Tambahkan agar Swing app bisa fullscreen & anti-aliasing berjalan
tasks.named<JavaExec>("run") {
    jvmArgs = listOf(
        "-Dawt.useSystemAAFontSettings=on",
        "-Dswing.aatext=true"
    )
}

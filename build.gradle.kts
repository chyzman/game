import org.jetbrains.gradle.ext.runConfigurations
import org.jetbrains.gradle.ext.settings
import org.jetbrains.gradle.ext.Application
import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform;

plugins {
    id("java")
    id("org.jetbrains.gradle.plugin.idea-ext") version "1.1.7"
}

group = "com.chyzman"
version = "0.0.0"

repositories {
    mavenCentral()
    mavenLocal()
    maven ("https://oss.sonatype.org/content/repositories/snapshots/")
    maven ("https://oss.sonatype.org/content/repositories/releases/")
    maven ("https://maven.wispforest.io/releases/")
}

fun getPlatform(): String {
    return if (DefaultNativePlatform.getCurrentOperatingSystem().isWindows) "natives-windows" else "natives-linux"
}

dependencies {
    //TODO someone organize this
    testImplementation (platform("org.junit:junit-bom:5.9.1"))
    testImplementation ("org.junit.jupiter:junit-jupiter")

    compileOnly ("org.jetbrains:annotations:24.1.0")

    implementation("it.unimi.dsi:fastutil:8.5.12")
    implementation("de.javagl:obj:0.4.0")

    implementation (platform("org.lwjgl:lwjgl-bom:${project.property("lwjglVersion")}"))

    implementation("com.google.guava:guava:33.1.0-jre")

    implementation("net.minecrell:terminalconsoleappender:1.3.0")
    implementation("org.slf4j:slf4j-api:2.0.9")
    implementation("org.apache.logging.log4j:log4j-api:2.23.1")
    implementation("org.apache.logging.log4j:log4j-core:2.23.1")
    implementation("org.apache.logging.log4j:log4j-slf4j2-impl:2.23.1")

    implementation("org.joml:joml:${project.property("jomlVersion")}")

    implementation("dev.dominion.ecs:dominion-ecs-engine:${project.property("dominionVersion")}")

    implementation("io.wispforest:endec:${project.property("endecVersion")}")
    implementation("io.wispforest.endec:gson:${project.property("endecGsonVersion")}")
    implementation("io.wispforest.endec:netty:${project.property("endecGsonVersion")}")

    implementation("de.articdive:jnoise-pipeline:${project.property("jnoiseVersion")}")

    implementation("org.ode4j:core:${project.property("ode4jVersion")}")

    //region LWJGL
    implementation("org.lwjgl:lwjgl")
    implementation("org.lwjgl:lwjgl-assimp")
    implementation("org.lwjgl:lwjgl-bgfx")
    implementation("org.lwjgl:lwjgl-cuda")
    implementation("org.lwjgl:lwjgl-egl")
    implementation("org.lwjgl:lwjgl-fmod")
    implementation("org.lwjgl:lwjgl-freetype")
    implementation("org.lwjgl:lwjgl-glfw")
    implementation("org.lwjgl:lwjgl-harfbuzz")
    implementation("org.lwjgl:lwjgl-hwloc")
    implementation("org.lwjgl:lwjgl-jawt")
    implementation("org.lwjgl:lwjgl-jemalloc")
    implementation("org.lwjgl:lwjgl-ktx")
    implementation("org.lwjgl:lwjgl-libdivide")
    implementation("org.lwjgl:lwjgl-llvm")
    implementation("org.lwjgl:lwjgl-lmdb")
    implementation("org.lwjgl:lwjgl-lz4")
    implementation("org.lwjgl:lwjgl-meow")
    implementation("org.lwjgl:lwjgl-meshoptimizer")
    implementation("org.lwjgl:lwjgl-nanovg")
    implementation("org.lwjgl:lwjgl-nfd")
    implementation("org.lwjgl:lwjgl-nuklear")
    implementation("org.lwjgl:lwjgl-odbc")
    implementation("org.lwjgl:lwjgl-openal")
    implementation("org.lwjgl:lwjgl-opencl")
    implementation("org.lwjgl:lwjgl-opengl")
    implementation("org.lwjgl:lwjgl-opengles")
    implementation("org.lwjgl:lwjgl-openvr")
    implementation("org.lwjgl:lwjgl-openxr")
    implementation("org.lwjgl:lwjgl-opus")
    implementation("org.lwjgl:lwjgl-par")
    implementation("org.lwjgl:lwjgl-remotery")
    implementation("org.lwjgl:lwjgl-rpmalloc")
    implementation("org.lwjgl:lwjgl-shaderc")
    implementation("org.lwjgl:lwjgl-spvc")
    implementation("org.lwjgl:lwjgl-sse")
    implementation("org.lwjgl:lwjgl-stb")
    implementation("org.lwjgl:lwjgl-tinyexr")
    implementation("org.lwjgl:lwjgl-tinyfd")
    implementation("org.lwjgl:lwjgl-tootle")
    implementation("org.lwjgl:lwjgl-vma")
    implementation("org.lwjgl:lwjgl-vulkan")
    implementation("org.lwjgl:lwjgl-xxhash")
    implementation("org.lwjgl:lwjgl-yoga")
    implementation("org.lwjgl:lwjgl-zstd")
    runtimeOnly("org.lwjgl:lwjgl::${getPlatform()}")
    runtimeOnly("org.lwjgl:lwjgl-assimp::${getPlatform()}")
    runtimeOnly("org.lwjgl:lwjgl-bgfx::${getPlatform()}")
    runtimeOnly("org.lwjgl:lwjgl-freetype::${getPlatform()}")
    runtimeOnly("org.lwjgl:lwjgl-glfw::${getPlatform()}")
    runtimeOnly("org.lwjgl:lwjgl-harfbuzz::${getPlatform()}")
    runtimeOnly("org.lwjgl:lwjgl-hwloc::${getPlatform()}")
    runtimeOnly("org.lwjgl:lwjgl-jemalloc::${getPlatform()}")
    runtimeOnly("org.lwjgl:lwjgl-ktx::${getPlatform()}")
    runtimeOnly("org.lwjgl:lwjgl-libdivide::${getPlatform()}")
    runtimeOnly("org.lwjgl:lwjgl-llvm::${getPlatform()}")
    runtimeOnly("org.lwjgl:lwjgl-lmdb::${getPlatform()}")
    runtimeOnly("org.lwjgl:lwjgl-lz4::${getPlatform()}")
    runtimeOnly("org.lwjgl:lwjgl-meow::${getPlatform()}")
    runtimeOnly("org.lwjgl:lwjgl-meshoptimizer::${getPlatform()}")
    runtimeOnly("org.lwjgl:lwjgl-nanovg::${getPlatform()}")
    runtimeOnly("org.lwjgl:lwjgl-nfd::${getPlatform()}")
    runtimeOnly("org.lwjgl:lwjgl-nuklear::${getPlatform()}")
    runtimeOnly("org.lwjgl:lwjgl-openal::${getPlatform()}")
    runtimeOnly("org.lwjgl:lwjgl-opengl::${getPlatform()}")
    runtimeOnly("org.lwjgl:lwjgl-opengles::${getPlatform()}")
    runtimeOnly("org.lwjgl:lwjgl-openvr::${getPlatform()}")
    runtimeOnly("org.lwjgl:lwjgl-openxr::${getPlatform()}")
    runtimeOnly("org.lwjgl:lwjgl-opus::${getPlatform()}")
    runtimeOnly("org.lwjgl:lwjgl-par::${getPlatform()}")
    runtimeOnly("org.lwjgl:lwjgl-remotery::${getPlatform()}")
    runtimeOnly("org.lwjgl:lwjgl-rpmalloc::${getPlatform()}")
    runtimeOnly("org.lwjgl:lwjgl-shaderc::${getPlatform()}")
    runtimeOnly("org.lwjgl:lwjgl-spvc::${getPlatform()}")
    runtimeOnly("org.lwjgl:lwjgl-sse::${getPlatform()}")
    runtimeOnly("org.lwjgl:lwjgl-stb::${getPlatform()}")
    runtimeOnly("org.lwjgl:lwjgl-tinyexr::${getPlatform()}")
    runtimeOnly("org.lwjgl:lwjgl-tinyfd::${getPlatform()}")
    runtimeOnly("org.lwjgl:lwjgl-tootle::${getPlatform()}")
    runtimeOnly("org.lwjgl:lwjgl-vma::${getPlatform()}")
    runtimeOnly("org.lwjgl:lwjgl-xxhash::${getPlatform()}")
    runtimeOnly("org.lwjgl:lwjgl-yoga::${getPlatform()}")
    runtimeOnly("org.lwjgl:lwjgl-zstd::${getPlatform()}")
    //endregion
}

tasks.test {
    useJUnitPlatform()
}

idea.project.settings {
    runConfigurations {
        create<Application>("Run Game") {
            mainClass = "com.chyzman.Game"
            moduleName = "game.main"
            includeProvidedDependencies = true

            this.jvmArgs = "-Dchyzman.game.disableAnsi=false -Dlog4j2.formatMsgNoLookups=true -Dchyzman.game.log.level=debug"

            this.envs = HashMap<String, String>();

            this.envs.set("chyzman.game.disableAnsi", false.toString())
            this.envs.set("log4j2.formatMsgNoLookups", true.toString())
            this.envs.set("chyzman.game.log.level", "debug")
        }
    }
}
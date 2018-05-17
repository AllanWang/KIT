package ca.allanwang.kit.utils

internal enum class OS {
    WINDOWS, LINUX, MAC, UNKNOWN
}

/**
 * Gets the OS for the current device
 */
internal val os: OS by lazy {
    val os = System.getProperty("os.name").toLowerCase()
    when {
        os.contains("win") -> OS.WINDOWS
        os.contains("nix") || os.contains("nux") || os.contains("aix") -> OS.LINUX
        os.contains("mac") -> OS.MAC
        else -> OS.UNKNOWN
    }
}

fun <T : Any?> os(windows: T, linux: T, mac: T = linux, unknown: T = linux) =
        os({ windows }, { linux }, { mac }, { unknown })

fun <T : Any?> os(windows: () -> T, linux: () -> T, mac: () -> T = linux, unknown: () -> T = linux) = lazy {
    when (os) {
        OS.WINDOWS -> windows()
        OS.LINUX -> linux()
        OS.MAC -> mac()
        OS.UNKNOWN -> unknown()
    }
}

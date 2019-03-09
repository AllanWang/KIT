/*
 * Copyright 2019 Allan Wang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

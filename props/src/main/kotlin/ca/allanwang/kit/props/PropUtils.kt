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
package ca.allanwang.kit.props

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.Properties

object PropUtils {

    /**
     * Attempts to load properties from the provided paths into [props]
     * Returns the file that was loaded, if any
     */
    fun loadProps(props: Properties, vararg path: String): File? {
        val file = path.map(::File).firstOrNull(File::isFile)
        if (file != null)
            FileInputStream(file).use(props::load)
        return file
    }

    /**
     * Saves the provided [props], returning [true] if successful, [false] otherwise
     */
    fun saveProps(props: Properties, path: String, comment: String): Boolean {
        val file = File(path)
        if (!file.exists() && !file.createNewFile())
            return false
        props.store(FileOutputStream(file), comment)
        return true
    }
}

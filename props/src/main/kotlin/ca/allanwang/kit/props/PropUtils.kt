package ca.allanwang.kit.props

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*

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
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
package ca.allanwang.kit.logger

import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.apache.logging.log4j.core.LoggerContext
import kotlin.reflect.full.companionObject

/**
 * Created by Allan Wang on 2017-09-29.
 */

/**
 * We will use the benefits of log4j2,
 * but wrap it in a delegate to make it cleaner
 *
 * Typically, classes should have a companion object extending [WithLogging]
 *
 * See <a href="https://stackoverflow.com/a/34462577/4407321">Stack Overflow</a>
 */
fun <T : Any> T.logger() = logger(this.javaClass)

private fun <T : Any> logger(forClass: Class<T>): Logger = LogManager.getLogger(unwrapCompanionClass(forClass).name)

// unwrap companion class to enclosing class given a Java Class
private fun <T : Any> unwrapCompanionClass(ofClass: Class<T>): Class<*> = if (ofClass.enclosingClass != null && ofClass.enclosingClass.kotlin.companionObject?.java == ofClass)
    ofClass.enclosingClass else ofClass

interface Loggable {
    val log: Logger
}

/**
 * Base implementation of a static final logger
 *
 * Can by used through
 *
 * companion object: WithLogging()
 */
open class WithLogging(name: String? = null) : Loggable {
    override val log: Logger by lazy {
        if (name != null) LogManager.getLogger(name)
        else this.logger()
    }
}

object LogUtils {

    fun setLoggingLevel(log: Logger?, level: Level) {
        val log = log ?: LogManager.getLogger("LogUtils")
        log.info("Updating log level to $level")
        val ctx = LogManager.getContext(false) as? LoggerContext
                ?: return log.warn("Could not change log level")
        val config = ctx.configuration
        val loggerConfig = config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME)
        loggerConfig.level = level
        ctx.updateLoggers()
    }
}

import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.rolling.RollingFileAppender
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy
import ch.qos.logback.core.util.FileSize

def logPath = System.getProperty("LOG_PATH")
def logFile = System.getProperty("LOG_FILE")
def logArchive = "${logPath}/archive"

if (!logPath || !logFile) {
    throw new IllegalStateException("Logging is not configured: make sure LOG_PATH and LOG_FILE properties are set")
}

appender("CONSOLE", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%d{HH:mm:ss} %-4relative [%thread] %-5level %logger{35} - %msg%n"
    }
}

appender("FILE", RollingFileAppender) {
    file = "${logPath}/${logFile}.log"

    rollingPolicy(TimeBasedRollingPolicy) {
        fileNamePattern = "${logArchive}/${logFile}-%d{yyyy-MM-dd}.log"
        maxHistory = 30
        totalSizeCap = FileSize.valueOf("10MB")
    }

    encoder(PatternLayoutEncoder) {
        pattern = "%d{HH:mm:ss} %-4relative [%thread] %-5level %logger{35} - %msg%n"
    }
}

root(DEBUG, ["CONSOLE", "FILE"])
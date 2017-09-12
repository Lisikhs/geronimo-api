import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.rolling.RollingFileAppender
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy
import ch.qos.logback.core.util.FileSize

def LOG_PATH = System.getProperty("LOG_PATH")
def LOG_FILE = System.getProperty("LOG_FILE")
def LOG_ARCHIVE = "${LOG_PATH}/archive"


appender("CONSOLE", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%d{HH:mm:ss} %-4relative [%thread] %-5level %logger{35} - %msg%n"
    }
}

appender("FILE", RollingFileAppender) {
    file = "${LOG_PATH}/${LOG_FILE}"

    rollingPolicy(TimeBasedRollingPolicy) {
        fileNamePattern = "${LOG_ARCHIVE}/rollingfile.log%d{yyyy-MM-dd}.log"
        maxHistory = 30
        totalSizeCap = FileSize.valueOf("10MB")
    }

    encoder(PatternLayoutEncoder) {
        pattern = "%d{HH:mm:ss} %-4relative [%thread] %-5level %logger{35} - %msg%n"
    }
}

root(INFO, ["CONSOLE", "FILE"])
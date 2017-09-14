import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.util.FileSize

def LOG_PATH = System.getProperty("LOG_PATH") ?: 'logs/'
def LOG_FILE = System.getProperty("LOG_FILE") ?: 'geronimo'
def LOG_ARCHIVE = "${LOG_PATH}/archive"


appender("CONSOLE", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%d{HH:mm:ss} %-4relative [%thread] %-5level %logger{35} - %msg%n"
    }
}

appender("FILE", RollingFileAppender) {
    file = "${LOG_PATH}/${LOG_FILE}.log"

    rollingPolicy(TimeBasedRollingPolicy) {
        fileNamePattern = "${LOG_ARCHIVE}/${LOG_FILE}-%d{yyyy-MM-dd}.log"
        maxHistory = 30
        totalSizeCap = FileSize.valueOf("10MB")
    }

    encoder(PatternLayoutEncoder) {
        pattern = "%d{HH:mm:ss} %-4relative [%thread] %-5level %logger{35} - %msg%n"
    }
}

root(INFO, ["CONSOLE", "FILE"])
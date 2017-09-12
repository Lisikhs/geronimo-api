import ch.qos.logback.core.util.FileSize

def LOG_PATH = System.getProperty("LOG_FILE")
def LOG_ARCHIVE = "${LOG_PATH}/archive" as Object


appender("RollingFile-Appender", RollingFileAppender) {
    file = "${LOG_PATH}/geronimo.log"

    rollingPolicy(TimeBasedRollingPolicy) {
        fileNamePattern = "${LOG_ARCHIVE}/rollingfile.log%d{yyyy-MM-dd}.log"
        maxHistory = 30
        totalSizeCap = FileSize.valueOf("10MB")
    }

    encoder(PatternLayoutEncoder) {
        pattern = "%d{HH:mm:ss} %-4relative [%thread] %-5level %logger{35} - %msg%n"
    }
}

root(INFO, ["RollingFile-Appender"])
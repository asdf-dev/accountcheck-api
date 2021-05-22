package accountcheck

import java.util.logging.Logger

class LoggingInterceptor {

    Long startTimer

    LoggingInterceptor(){
        matchAll()
    }

    Logger logger = Logger.getLogger("")
    boolean before() {
        startTimer = new Date().getTime()
        logger.info("Incoming request")
        logger.info("headers: " + request.headerNames)
        logger.info("request getMethod: " + request?.getMethod())
        logger.info("requestURL: " + request.requestURL)
        logger.info("remoteAddr: " + request.remoteAddr)
        logger.info("request: " + request)
        true }

    boolean after() {
        logger.info("respond status: " + response.status)
        true }

    void afterView() {
        Long endTimer = new Date().getTime()
        Integer calc = endTimer-startTimer
        logger.info("response time: ${calc/1000}sek")
        logger.info("request end")

        // no-op
    }
}

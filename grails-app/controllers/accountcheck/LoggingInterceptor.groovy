package accountcheck


class LoggingInterceptor {

    Long startTimer

    LoggingInterceptor(){
        matchAll()
    }


    boolean before() {
        startTimer = new Date().getTime()
        println("Incoming request")
        println("headers: " + request.headerNames)
        println("request getMethod: " + request?.getMethod())
        println("requestURL: " + request.requestURL)
        println("remoteAddr: " + request.remoteAddr)
        println("request: " + request)
        true }

    boolean after() {
        print("respond status: " + response.status)
        true }

    void afterView() {
        Long endTimer = new Date().getTime()
        Integer calc = endTimer-startTimer
        println("response time: ${calc/1000}sek")
        println("request end")

        // no-op
    }
}

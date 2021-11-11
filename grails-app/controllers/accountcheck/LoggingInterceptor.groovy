package accountcheck


class LoggingInterceptor {

    Long startTimer

    LoggingInterceptor(){
        matchAll()
    }


    boolean before() {
        startTimer = new Date().getTime()
        print("Incoming request")
        print("headers: " + request.headerNames)
        print("request getMethod: " + request?.getMethod())
        print("requestURL: " + request.requestURL)
        print("remoteAddr: " + request.remoteAddr)
        print("request: " + request)
        true }

    boolean after() {
        print("respond status: " + response.status)
        true }

    void afterView() {
        Long endTimer = new Date().getTime()
        Integer calc = endTimer-startTimer
        print("response time: ${calc/1000}sek")
        print("request end")

        // no-op
    }
}

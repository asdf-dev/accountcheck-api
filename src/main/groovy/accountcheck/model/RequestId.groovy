package accountcheck.model

import grails.validation.Validateable

class RequestId implements Validateable {
    String  steam64
}


/**
make command obj in controller

 def contraoller(RequestList req)
 bindData(req, request.JSON)
 check for errors

 if ok start calling

 *request like this:
 * {"steam64":[
 76561197967742127,
 76561197967742127,
 76561197967742127,
 76561197967742127
 ]}
 */

package agoda

/**
  * This class acts like configuration that store details of each HTTP Call with specific IP
  * Assume that each API HTTP rate limited (request per 10 second)
  * If the same request(Assume they are same IP) with 10 seconds, that request will be suspend for 5 mins
  * Api key can have different rate limit set, in this case from configuration,
  * and if not present a global rate limit(10 sec) applied.
  * Created by Kwanmuang on 10/12/2016.
  */
class Limit (
              var IP:String,                // IP address for each request
              var updateTime:Long = -1,     // Default updateTime -1, means First Time request
              var rateLimit:Long = 10000,     // Default rate limit 10000 sec = 10 sec, this can be changed/configured to constructor
              var suspendTime:Long = 300000,  // Default suspend time 300,000 = 5 mins, this can be changed/configured to constructor
              var suspended:Boolean = false // Default status of each HTTP whether HTTP in SUSPENED OR RELEASE Status

            ){}



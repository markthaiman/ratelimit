package agoda

import scala.collection.mutable
import scala.io.Source

/**
  * This class is an implementation to provide all services.
  * - CheckRateLimit:
  * - seacrhByHotelId
  * - sortByHotelIdASC
  * - sortByPriceASC
  * - sortByPriceDESC
  * - private binarySearchByHotelId
  * - private popData
  * Created by Kwanmuang on 10/12/2016.
  */
class RateLimit{

  /**
    * Check Rate Limit will verify if request has been request within specific of Rate Limit time
    * Default Rate Limit = 10 sec, Suspend time  = 5 mins
    * @param ip
    * @return
    */
  def checkRateLimit(ip: Limit): Boolean = {

    var currentTime = System.currentTimeMillis();
    var result:Long = 0;
    // If this first time request
    if(ip.updateTime == -1){
      ip.updateTime= currentTime;
      ip.suspended = false;
    }

    else{
      result = currentTime - ip.updateTime;

      // If request status suspended
      if(ip.suspended){
        // if request suspend more than 5 min by default, then release request
        if(result > ip.suspendTime){
          ip.suspended = false;
        }else{
          ip.suspended = true;
        }
      }else{
        ip.updateTime = currentTime ;
        // Check if a request done > rate limit, then request still accepted
        // Otherwise, suspend request
        if (result > ip.rateLimit) {
          ip.suspended =false;
        }
        else {
          ip.suspended =true;
        }
      }
    }
    // Iby default if time different < 10 second per request, the request would be SUSPENDED for  5 min
    // Otherwise RELEASE SUSPENDED(Accepted Request)
    if(!ip.suspended){
      println(ip.IP+ ": RELEASE : Time Different[" +result+"]");
    }else{
      println(ip.IP+ ": SUSPENDED : Time Different[" +result+"]");
    }
    return ip.suspended;
  }

  /**
    * Search Hotel using Binary search algorithm
    * Binary search required data need to be sorted in asc before perform search
    * Binary search divide tree left and right node to perform search tasks
    * Lower value will in left tree, higher value will be in right tree
    * @param hotelId
    * @tparam hotel
    * @return
    */
  def seacrhByHotelId[hotel>: Null](hotelId: Int): Hotel ={
    var list = sortByHotelIdASC();
    var idx = binarySearchByHotelId(list,hotelId);

    // If search not found return index  == -1
    if(idx > -1){
      println("Hotel ID["+hotelId+"] FOUND");
      return list(idx-1); //  Get Hotel object with specific index found and return
    }
    println("Hotel ID["+hotelId+"] NOT FOUND");
    return null;

  }

  /**
    * Perform quick sort Hotel By ID with ASC Order
    * @return
    */
  def sortByHotelIdASC():mutable.MutableList[Hotel] ={
    var list = popData();
    // Sort Hotel By ID using function with ASC Order
    def sortASC = (A:Hotel, B:Hotel) => { A.hotelId < B.hotelId }
    var sortedList =  list.sortWith(sortASC);
    return sortedList;
  }

  /**
    * Perform quick sort Hotel By Price with ASC Order
    * @return
    */
  def sortByPriceASC():mutable.MutableList[Hotel] ={
    var list = popData();
    // Sort Hotel Price By using function by ASC Order
    def sortASC = (A:Hotel, B:Hotel) => { A.price < B.price }
    var sortedList =  list.sortWith(sortASC);
    return sortedList;
  }

  /**
    * Perform quick sort Hotel By Price with DESC Order
    * @return
    */
  def sortByPriceDESC():mutable.MutableList[Hotel] ={
    var list = popData();
    // Sort Hotel Price By using function by DESC Order
    def sortDESC = (A:Hotel, B:Hotel) => { A.price > B.price }
    var sortedList =  list.sortWith(sortDESC);
    return sortedList;
  }

  /**
    * Using Binary search algorithm to perform search
    * Tree divide into left/ right node
    * lower value will in left node, higher value will be in right node
    * @param array
    * @param value
    * @return
    */
  private def binarySearchByHotelId(array: mutable.MutableList[Hotel], value: Int) = {

    def binarySearchRecursive(array: mutable.MutableList[Hotel], start: Int, end: Int, value: Int): Int = {

      // Divide tree in left/ right node
      var i = (start + end) / 2
      if(start > end) {
        return -1;
      }

      array(i) match {
        // If found value in middle tree
        case x if (x.hotelId == value) => x.hotelId;
        // Search value in left tree
        case x if (x.hotelId > value) => binarySearchRecursive(array, start, i - 1, value);
        // Search value in right tree
        case x if (x.hotelId < value) => binarySearchRecursive(array, i + 1, end, value);
      }
    }
    // Perform recursive check and divide into sub tree and node
    binarySearchRecursive(array, 0, array.size - 1, value)
  }

  /**
    * Read csv from resource folder and populate all data into Hotel Object List
    * @return
    */
  private def popData() :mutable.MutableList[Hotel] ={
    var list = mutable.MutableList[Hotel]();
    var idx: Int = 0;
    for (line <- Source.fromURL(getClass.getResource("/resource/hoteldb.csv")).getLines()) {
      if ( idx > 0){
        var row = line.split(",");
        var hotel = new Hotel(row(0), row(1).toInt, row(2), row(3).toInt);

        list += hotel;

      }
      idx += 1;
    }
    return list;
  }


}


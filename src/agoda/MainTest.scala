package agoda

import agoda.util.CMap
import scala.util.control.Breaks._;

/**
  * This main testing for all services
  * Created by Kwanmuang on 11/12/2016.
  */
object MainTest {

  def main(args: Array[String]): Unit = {
    println("####### Main Test Start Module #######");
    monitorRateLimitTest();
    seacrhByHotelIdTest();
    sortByHotelIdASCTest();
    sortByPriceASCTest();
    sortByPriceDESCTest();
    println("####### Main Test Finish All Test #######");
  }


  // This test, we assume that there are 2 request from different IP,
  // Default rateLimit = 10 sec and Default suspended request = 5 min
  // To see test soon, we set rateLimit = 5 sec(5000 milisec) and suspended request = 10 sec (10,000 milisec)
  // Scenario: Since there are same request to API Rate limit check < 5 sec, then request get SUSPENED for 10 sec
  // After suspend for 10 sec, release rate mit check, allow to get new request again
  def monitorRateLimitTest(){
    println("-------Start monitorRateLimitTest-------------");
    val rate = new RateLimit;
    var startTestTime = System.currentTimeMillis();
    var map = new CMap[String, Limit];
    var idx =0;
    val ips: List[String] = List("IP1","IP2");
    breakable {
      while (true) {

        for (ip <- ips) {
          var limit = map.get(ip);
          if (limit == null) {
            limit = new Limit(ip, -1, 5000, 10000);// rateLimit & suspend time can be configured here for individual request
            map.put(ip, limit);
          }
          // Rate Limit Check start here
          rate.checkRateLimit(limit);

          // Sleep  for 1 sec to assume they doing some works
          Thread.sleep(1000);
        }

        var endTestTime = System.currentTimeMillis();
        // Terminate Test if it run more than  20 sec
        if (endTestTime - startTestTime > 20000) {
            break;
        }
      }
    }
  }

  // Test Search Hotel By ID test by using binary search against hoteldb.csv
  // Hotel ID from 1 - 26
  // Return null if search not found
  def seacrhByHotelIdTest(){
    println("-------Start seacrhByHotelIdTest-------------");
    val rate = new RateLimit;

    rate.seacrhByHotelId(0); // Return NULL, Hotel ID Not found
    rate.seacrhByHotelId(1); // Return Hotel Object
  }

  // Test sorting Hotel By HotelID
  def sortByHotelIdASCTest(){
    println("-------Start sortByHotelIdASCTest-------------");
    val rate = new RateLimit;
    var sortedList =  rate.sortByHotelIdASC()
    for(c<-sortedList){
       println("Hotel ID: " +c.hotelId);
    }
  }

  // Test sorting Hotel By Price in Ascending Order
  def sortByPriceASCTest()
  {
    println("-------Start sortByPriceASCTest-------------");
    val rate = new RateLimit;
    var sortedList = rate.sortByPriceASC();
    for(c<-sortedList){
      println("Hotel Price: " +c.price);
    }
  }

  // Test sorting Hotel By Price in Desending Order
  def sortByPriceDESCTest()
  {
    println("-------Start sortByPriceDESC-------------");
    val rate = new RateLimit;
    var sortedList = rate.sortByPriceDESC();
    for(c<-sortedList){
      println("Hotel Price: " +c.price);
    }
  }

}

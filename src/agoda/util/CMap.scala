package agoda.util
import scala.util.control.Breaks._;

/**
  * This is Custom Map that store object as key,value pair. The initialize Hash Bucket size is 1000
  *
  * Created by Kwanmuang on 10/12/2016.
  */
class CMap[K, V >: Null] {
  private var bucketSize = 1000;
  private var mapList:Array[Node[K,V]]= new Array[Node[K,V]](bucketSize);

  /**
    * Get value in Map with spefic key
    * @param key
    * @return value
    */
  def get(key:K) : V ={
    var index = getHash(key);
    var list = mapList(index);

    // Search value in next node within bucket
    while (list != null) {
      if (list.key == key) {
        return list.value;
      }
      list = list.next;
    }

    return null;
  }

  /**
    * Add new object into Map with Key, Value
    * @param key
    * @param value
    */
  def put(key:K, value:V){
    var index = getHash(key);
    storeValue(index, key, value);
  }

  /**
    * Calculate Hash Value
    * @param key
    * @return hashValue
    */
  private def getHash(key:K) : Int ={
    var hash = key.hashCode();
    return hash % bucketSize;
  }

  /**
    * Insert value into Map, if the same HashKey found, then add it into same Bucket for next Collection Node
    * @param index
    * @param key
    * @param value
    */
  private def storeValue(index: Int, key:K, value:V){
    var list = mapList(index);

    if (list == null) {
          mapList(index) = new Node(key, value);
    } else {
      var done = false;
      // Search through next node in list, if key found, then add/update at the end of list
      breakable {
        while(list.next != null) {
           if (list.key == key) {
               list.value = value;
               done = true;
               break;
           }
           list = list.next;
        }
      }
      // Found key, then add new node at the end of list
      if (!done) {
          list.next = new Node(key, value);
      }
    }
  }
}

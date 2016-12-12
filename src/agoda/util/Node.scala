package agoda.util

/**
  * Node class to store in Map, Object with same HashKey will be store in same  bucket
  * Object Node has [Key,Value] pair
  * Created by Kwanmuang on 10/12/2016.
  */
class Node[K,V](var key: K, var value: V, var next:Node[K,V] = null) {}

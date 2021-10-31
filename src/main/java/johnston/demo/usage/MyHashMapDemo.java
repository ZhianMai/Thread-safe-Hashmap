package johnston.demo.usage;

import johnston.hashmap.MyHashMap;
import johnston.hashmap.MyHashMapFactory;
import johnston.hashmap.ThreadSafePolicy;

public class MyHashMapDemo {
  public static void main(String[] args) {
    MyHashMap<Integer, Integer> hashMap = MyHashMapFactory.newMyHashMap(ThreadSafePolicy.NoSync);
    System.out.println("Is Empty: " + hashMap.isEmpty());

    for (int i = 0; i < 10; i++) {
      hashMap.put(i, i * i);
    }

    System.out.println(hashMap);

    for (int i = 0; i < 10; i++) {
      System.out.print( hashMap.get(i) + ",");
    }
    System.out.println();

    System.out.println("Contains 5? :" + hashMap.containsKey(5));
    hashMap.remove(5);
    System.out.println("5 Removed. Contains 5? :" + hashMap.containsKey(5));
  }
}

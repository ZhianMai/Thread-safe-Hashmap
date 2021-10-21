package johnston.demo.factory_pattern;

public class Main {
  public static void main(String[] args) {
    DevHashMapStable mapA1 = DevHashMapFactory.newDevHashMap(ImplType.A);
    mapA1.get(1);
    mapA1.put(1, 1);

    DevHashMapStable mapB1 = DevHashMapFactory.newDevHashMap(ImplType.B, 1);
    mapB1.get(1);
    mapB1.put(1, 1);

    DevHashMapTest mapTA1 = DevHashMapFactory.newDevHashMapTesting(ImplType.A);
    mapTA1.get(1);
    mapTA1.put(1, 1);
    mapTA1.test();
    mapTA1.printVal();

    DevHashMapTest mapTB1 = DevHashMapFactory.newDevHashMapTesting(ImplType.B, 1);
    mapTB1.get(1);
    mapTB1.put(1, 1);
    mapTB1.test();
    mapTB1.printVal();
  }
}

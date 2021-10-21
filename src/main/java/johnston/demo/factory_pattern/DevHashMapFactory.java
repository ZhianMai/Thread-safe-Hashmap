package johnston.demo.factory_pattern;

public class DevHashMapFactory {

  // Create hash map without debug methods
  public static DevHashMapStable newDevHashMap(ImplType implType) {
    return getDevHashMap(implType, false, 0);
  }

  public static DevHashMapStable newDevHashMap(ImplType implType, int val) {
    return getDevHashMap(implType, true, val);
  }

  private static DevHashMapStable getDevHashMap(ImplType implType, boolean hasParam, int val) {
    return getDevHashMapTest(implType, hasParam, val);
  }

  // Create hash map with debug methods
  public static DevHashMapTest newDevHashMapTesting(ImplType implType) {
    return getDevHashMapTest(implType, false, 0);
  }

  public static DevHashMapTest newDevHashMapTesting(ImplType implType, int val) {
    return getDevHashMapTest(implType, true, val);
  }

  private static DevHashMapTest getDevHashMapTest(ImplType implType, boolean hasParam, int val) {
    switch (implType) {
      case A:
        return hasParam ? new DevHashMapImplTestA(val) : new DevHashMapImplTestA();
      case B:
        return hasParam ? new DevHashMapImplTestB(val) : new DevHashMapImplTestB();
      default:
        return null;
    }
  }
}

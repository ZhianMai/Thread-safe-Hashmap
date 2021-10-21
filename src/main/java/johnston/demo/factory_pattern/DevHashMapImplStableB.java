package johnston.demo.factory_pattern;

public class DevHashMapImplStableB implements DevHashMapStable {

  public DevHashMapImplStableB() {
    System.out.println("DevHashMapImplStableB default ctor");
  }

  public DevHashMapImplStableB(int val) {
    System.out.println("DevHashMapImplStableB param ctor");
  }

  @Override
  public int get(int index) {
    System.out.println("DevHashMapImplStableB get()");
    return 1;
  }

  @Override
  public void put(int index, int value) {
    System.out.println("DevHashMapImplStableB put()");
  }
}

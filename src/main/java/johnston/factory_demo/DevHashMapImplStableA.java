package johnston.factory_demo;

public class DevHashMapImplStableA implements DevHashMapStable {

  public DevHashMapImplStableA() {
    System.out.println("DevHashMapImplStableA default ctor");
  }

  public DevHashMapImplStableA(int val) {
    System.out.println("DevHashMapImplStableA param ctor");
  }

  @Override
  public int get(int index) {
    System.out.println("DevHashMapImplStableA get()");
    return 1;
  }

  @Override
  public void put(int index, int value) {
    System.out.println("DevHashMapImplStableA put()");
  }
}

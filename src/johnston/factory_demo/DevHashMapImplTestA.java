package johnston.factory_demo;

public class DevHashMapImplTestA implements DevHashMapTest {

  public DevHashMapImplTestA() {
    System.out.println("DevHashMapImplStableA default ctor");
  }

  public DevHashMapImplTestA(int val) {
    System.out.println("DevHashMapImplStableA param ctor");
  }

  @Override
  public int get(int index) {
    System.out.println("DevHashMapImplTestA get()");
    return 1;
  }

  @Override
  public void put(int index, int value) {
    System.out.println("DevHashMapImplTestA put()");
  }

  @Override
  public boolean test() {
    System.out.println("DevHashMapImplTestA test()");
    return false;
  }

  @Override
  public void printVal() {
    System.out.println("DevHashMapImplTestA printVal()");
  }
}

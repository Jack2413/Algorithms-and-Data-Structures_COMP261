import java.util.*;

class Cat {
  protected String name;
  public Cat(String name) { this.name = name; }
  public void fight(Cat target, List<String> log) {
    log.add(name + " claws " + target.name);
    target.fightBack(this,log);
  }

  public void fightBack(Cat target, List<String> log) {
    log.add(name + " claws back " + target.name);
  }
}

class RoughCat extends Cat {
  public RoughCat(String name) {
    super(name);
  }

  public void fight(Cat target, List<String> log) {
    log.add(name + " bites " + target.name);
    target.fightBack(this,log);
  }

  public void fightBack(Cat target, List<String> log) {
    log.add(name + " bites back " + target.name);
  }
}

public class Exercise {
  public static void main(String[] args) {
    List<String> log = new ArrayList<String>();
    Cat jim = new Cat("Jim");
    Cat bob = new RoughCat("Bob");
    bob.fight(jim,log);
 System.out.println(log.toString());
  }
}
  
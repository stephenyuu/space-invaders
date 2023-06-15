import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import javalib.funworld.*;
import javalib.worldcanvas.*;
import java.awt.Color;


//FUNCTION OBJECTS:

//Used in check expects for list abstractions

//adds 1 to given int
class AddOne implements Function<Integer, Integer> {
  public Integer apply(Integer n) {
    return n + 1;
  }
}

//is given int even?
class IsEven implements Predicate<Integer> {
  public boolean test(Integer n) {
    return n % 2 == 0;
  }
}

//sums all int in given list of int
class SumAll implements BiFunction<Integer, Integer, Integer> {
  public Integer apply(Integer n, Integer sum) {
    return sum + n;
  }
}

//is the given CartPt the same as the current CartPt?
class SamePoint implements Predicate<CartPt> {
  CartPt given;

  SamePoint(CartPt given) {
    this.given = given;
  }

  public boolean test(CartPt firstInList) {
    return this.given.x == firstInList.x &&
        this.given.y == firstInList.y;
  }
}

//is the given CartPt contained in this list?
class InList implements Predicate<CartPt> {
  IList<CartPt> points;

  InList(IList<CartPt> points) {
    this.points = points;
  }

  public boolean test(CartPt x) {
    return this.points.ormap(new SamePoint(x));
  }
} 

//The following are used for the game:

//-------------------CREATE INVADER METHOD-------------------------------------

//given an integer create an Invader
class CreateInvader implements Function<Integer, IGamePiece> {
  //invaders are 60 x 60
  public IGamePiece apply(Integer n) {
    // row and column of the invader
    int r = n / 9; // find the row the invader is on
    int c = n % 9; // find the column the invader is in
    // x and y coordinates
    int x = c * 80 + 80;
    int y = r * 80 + 100;

    CartPt loc = new CartPt(x, y);
    return new Invader(loc);
  }
}

//-------------------DRAW GAMEPIECE METHODS------------------------------------

//draw an invader onto a WorldScene
class DrawGamePiece {
  public WorldScene apply(AGamePiece gp, WorldScene scene) {
    return gp.draw(scene);
  }
}

class DrawInvader extends DrawGamePiece implements BiFunction<Invader, WorldScene, WorldScene> {
  public WorldScene apply(Invader zim, WorldScene scene) { // zim is a reference to Invader Zim
    return super.apply(zim, scene);
  }
}

//draw an invader onto a WorldScene
class DrawPBullet extends DrawGamePiece implements BiFunction<PlayerBullet, WorldScene, WorldScene> {
  public WorldScene apply(PlayerBullet bill, WorldScene scene) { // zim is a reference to Invader Zim
    return super.apply(bill, scene);
  }
}

//draw an invader onto a WorldScene
class DrawIBullet extends DrawGamePiece implements BiFunction<InvaderBullet, WorldScene, WorldScene> {
  public WorldScene apply(InvaderBullet bill, WorldScene scene) { // zim is a reference to Invader Zim
    return super.apply(bill, scene);
  }
}

//-------------------GAMEPIECE-BULLET COLLISION  METHODS-----------------------

//does the Bullet hit the given AGamePiece
class IsPieceHit {
  AGamePiece gp;
  
  IsPieceHit(AGamePiece gp) {
    this.gp = gp;
  }
  
  public boolean test(Bullet bill, int gpWidth, int gpHeight) {
    boolean isHit = bill.loc.x < (gp.loc.x + gpWidth / 2) && bill.loc.x > (gp.loc.x - gpWidth / 2)
        && bill.loc.y < (gp.loc.y + gpHeight / 2) && bill.loc.y > (gp.loc.y - gpHeight / 2);
    if (isHit) {
      bill.commitDie();
    }
    return isHit;
  }
}

//does the PlayerBullet hit the given Invader
class IsInvaderHit extends IsPieceHit implements Predicate<PlayerBullet> {
  
  IsInvaderHit(Invader zim) {
    super(zim);
  }
  
  public boolean test(PlayerBullet bill) {
    return super.test(bill, bill.INVADER_SIZE, bill.INVADER_SIZE);
  }
}

//does the InvaderBullet hit the given Player
class IsPlayerHit extends IsPieceHit implements Predicate<InvaderBullet> {
  
  IsPlayerHit(Spaceship player) {
    super(player);
  }
  
  public boolean test(InvaderBullet bill) {
    return super.test(bill, bill.SPACESHIP_WIDTH, bill.SPACESHIP_HEIGHT);
  }
}

//-------------------IS SAFE METHODS-------------------------------------------

//is given Invader safe from the bullets in the list?
class IsInvaderSafe implements Predicate<Invader> {
  IList<PlayerBullet> bullets;
  
  IsInvaderSafe(IList<PlayerBullet> bullets) {
    this.bullets = bullets;
  }
  
  public boolean test(Invader zim) {
    return !(this.bullets.ormap(new IsInvaderHit(zim)));
  }
}

//is given Spaceship safe from the bullets in the list?
class IsPlayerSafe implements Predicate<Spaceship> {
  IList<InvaderBullet> bullets;
  
  IsPlayerSafe(IList<InvaderBullet> bullets) {
    this.bullets = bullets;
  }
  
  public boolean test(Spaceship player) {
    return !(this.bullets.ormap(new IsPlayerHit(player)));
  }
}

//-------------------LIVE BULLET METHODS---------------------------------------

//is the given InvaderBullet alive
class IsLiveIBullet implements Predicate<InvaderBullet> {
  public boolean test(InvaderBullet bill) {
    return bill.alive;
  }
}
//is the given PlayerBullet alive
class IsLivePBullet implements Predicate<PlayerBullet> {
  public boolean test(PlayerBullet bill) {
    return bill.alive;
  }
}

//-------------------MOVE BULLET METHODS---------------------------------------

//move the given InvaderBullet
class MoveIBullet implements Function<InvaderBullet, InvaderBullet> {
  public InvaderBullet apply(InvaderBullet bill) {
    bill.move();
    return bill;
  }
}
//move the given PlayerBullet
class MovePBullet implements Function<PlayerBullet, PlayerBullet> {
  public PlayerBullet apply(PlayerBullet bill) {
    bill.move();
    return bill;
  }
}

//-------------------BULLET IN BOUNDS METHODS----------------------------------

//is the given InvaderBullet in bounds
class IsBInBounds {
  public boolean test(Bullet gp) {
    return gp.loc.x > 0 && gp.loc.x < gp.GAME_WIDTH
        && gp.loc.y > 0 && gp.loc.y < gp.GAME_HEIGHT;
  }
}

//is the given InvaderBullet in bounds
class IsIBInBounds extends IsBInBounds implements Predicate<InvaderBullet> {
  public boolean test(InvaderBullet gp) {
    return super.test(gp);
  }
}

//is the given PlayerBullet in bounds
class IsPBInBounds extends IsBInBounds implements Predicate<PlayerBullet> {
  public boolean test(PlayerBullet gp) {
    return super.test(gp);
  }
}

//-------------------LOAD INVADER METHOD---------------------------------------

//move the given InvaderBullet
class LoadInvader implements Function<Invader, Invader> {
  public Invader apply(Invader zim) {
    zim.setHasFired(false);
    return zim;
  }
}

//-------------------HASNTFIRED METHODS------------------------------------------

//is the given invader able to fire
class HasntFired implements Predicate<Invader> {
  public boolean test(Invader zim) {
    return !zim.hasFired;
  }
}

//-------------------UTILS CLASS-----------------------------------------------

//Utils class
//Contains useful methods
class Utils<T> {
  //default constructor -- does nothing
  Utils() {}

  //create a list of integers starting from a the first input and ending at the second input
  public IList<Integer> buildAToB(int start, int end) {
    if (start > end) {
      return new MtList<Integer>();
    }
    else {
      return new ConsList<Integer>(start,this.buildAToB(start + 1, end));
    }  
  }

  //create a list of length n with the given function done to each element
  public IList<T> buildList(Integer n, Function<Integer, T> func) {
    return buildAToB(0, n - 1).map(func);
  }
  
  /*
  //draws initial state of the game -- UNUSED
  public void drawInitialGameState() {
    WorldCanvas c = new WorldCanvas(800, 1000);

    Utils utility = new Utils();

    IList<Invader> invaders = utility.buildList(36, new CreateInvader());
    CartPt playerLoc = new CartPt(400, 950);
    Spaceship player = new Spaceship(playerLoc);

    SpaceInvadersGame game = new SpaceInvadersGame(player, invaders);
    WorldScene s = game.makeScene();

    c.show();
    c.drawScene(s);
  }
  */
}
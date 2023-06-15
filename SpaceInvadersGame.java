import tester.Tester;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import javalib.funworld.*;
import javalib.worldimages.*;
import java.awt.Color;
import java.util.Random;

//represents a list of generic type
interface IList<T> {  
  //maps given function onto every member of this list
  public <R> IList<R> map(Function<T, R> func);

  //filters this list the given predicate
  public IList<T> filter(Predicate<T> pred);

  //combines members in this list using the given function
  public <R> R foldr(BiFunction<T, R, R> func, R base);

  //does all the members in this list satisfy the given predicate?
  public boolean andmap(Predicate<T> pred);

  //does at least ONE member in this list satisfy the given predicate?
  public boolean ormap(Predicate<T> pred);
  
  //return the length of this list
  public int length();
  
  //return the n-th element of the list
  public T getNthElement(int n);
}

//represents an empty list of generic type
class MtList<T> implements IList<T> {

  /* TEMPLATE:
   * fields:
   * methods:
   *  this.map ... IList<R>
   *  this.filter ... IList<T>
   *  this.foldr ... R
   *  this.andmap ... boolean
   *  this.ormap ... boolean
   *  this.length ... int
   *  this.getNthElement ... T
   * methods for fields:
   * 
   */

  //maps given function onto every member of this empty list
  public <R> IList<R> map(Function<T, R> func) {
    return new MtList<R>();
  }

  //filters this empty list the given predicate
  public IList<T> filter(Predicate<T> pred) {
    return new MtList<T>();
  }

  //combines members in this list using the given function
  public <R> R foldr(BiFunction<T, R, R> func, R base) {
    return base;
  }

  //does all the members in this empty list satisfy the given predicate?
  public boolean andmap(Predicate<T> pred) {
    return true;
  }

  //does at least ONE member in this empty list satisfy the given predicate?
  public boolean ormap(Predicate<T> pred) {
    return false;
  }
  
  //return the length of this empty list
  public int length() {
    return 0;
  }

  //return the n-th element of the empty list
  public T getNthElement(int n) {
    return null;
  }
}

//represents a nonempty list of generic type
class ConsList<T> implements IList<T> {
  T first;
  IList<T> rest;

  ConsList(T first, IList<T> rest) {
    this.first = first;
    this.rest = rest;
  }

  /* TEMPLATE:
   * fields:
   *  this.first ... T
   *  this.rest ... IList<T>
   * methods:
   *  this.map ... IList<R>
   *  this.filter ... IList<T>
   *  this.foldr ... R
   *  this.andmap ... boolean
   *  this.ormap ... boolean
   *  this.length ... int
   *  this.getNthElement ... T
   * methods for fields:
   *  this.rest.map ... IList<R>
   *  this.rest.filter ... IList<T>
   *  this.rest.foldr ... R
   *  this.rest.andmap ... boolean
   *  this.rest.ormap ... boolean
   *  this.rest.length ... int
   *  this.rest.getNthElement ... T
   */

  //maps given function onto every member of this nonempty list
  public <R> IList<R> map(Function<T, R> func) {
    return new ConsList<R>(func.apply(this.first), this.rest.map(func));
  }

  //filters this nonempty list the given predicate
  public IList<T> filter(Predicate<T> pred) {
    if (pred.test(this.first)) {
      return new ConsList<T>(this.first, this.rest.filter(pred));
    }
    else {
      return this.rest.filter(pred);
    }
  }

  //combines members in this list using the given function
  public <R> R foldr(BiFunction<T, R, R> func, R base) {
    return func.apply(this.first, this.rest.foldr(func, base));
  }

  //does all the members in this nonempty list satisfy the given predicate?
  public boolean andmap(Predicate<T> pred) {
    return pred.test(this.first) && this.rest.andmap(pred);
  }

  //does at least ONE member in this nonempty list satisfy the given predicate?
  public boolean ormap(Predicate<T> pred) {
    return pred.test(this.first) || this.rest.ormap(pred);
  }

  //return the length of this list
  public int length() {
    return 1 + this.rest.length();
  }

  //return the n-th element of the list
  public T getNthElement(int n) {
    if (n == 0) {
      return this.first;
    }
    else {
      return this.rest.getNthElement(n - 1);
    }
  }
}

//represents location on Cartesian plane
class CartPt {
  int x;
  int y;

  CartPt(int x, int y) {
    this.x = x;
    this.y = y;
  }

  /* TEMPLATE:
   * fields:
   *  this.x ... int
   *  this.y ... int
   * methods:
   *  this.moveUp(int n) ... void
   *  this.moveDown(int n) ... void
   *  this.moveLeft(int n) ... void
   *  this.moveRight(int n) ... void
   *  this.setX(int n) ... void
   * methods for fields:
   * 
   */
  
  //EFFECT: move n pixels up
  public void moveUp(int n) {
    this.y -= n;
  }
  
  //EFFECT: move n pixels down
  public void moveDown(int n) {
    this.y += n;
  }
  
  //EFFECT: move n pixels to the left
  public void moveLeft(int n) {
    this.x -= n;
  }
  
  //EFFECT: move n pixels to the right
  public void moveRight(int n) {
    this.x += n;
  }
  
  //EFFECT: sets the x value
  public void setX(int n) {
    this.x = n;
  }
}

//represents a game piece in game
interface IGamePiece {
  //game board
  static int GAME_WIDTH = 800;
  static int GAME_HEIGHT = 800;
  
  //spaceship (rectangle)
  static int SPACESHIP_HEIGHT = 25;
  static int SPACESHIP_WIDTH = 50;
  static int SPACESHIP_SPEED = 5;

  //invaders (squares)
  static int INVADER_SIZE = 60;

  //bullets (circles)
  static int BULLET_SIZE = 5;
  static int BULLET_SPEED = 2;
  
  //draws this IGamePiece onto given World Scene
  WorldScene draw(WorldScene acc);
}

//to share implementations common to all game pieces
abstract class AGamePiece implements IGamePiece {
  CartPt loc;
  Color color;
  
  AGamePiece(CartPt loc, Color color) {
    this.loc = loc;
    this.color = color;
  }
}

//represents a spaceship in game
class Spaceship extends AGamePiece {
  boolean direction; // true if left and false if right

  Spaceship(CartPt loc) {
    super(loc, Color.black);
    this.direction = true;
  }

  /* TEMPLATE:
   * fields:
   *  this.loc ... Location
   *  this.color ... Color
   *  this.direction ... boolean
   * methods:
   *  this.draw(WorldScene acc) ... WorldScene
   *  this.moveLeft() ... void
   *  this.moveRight() ... void
   *  this.move() ... void
   *  this.setDirection(boolean b) ... void
   * methods for fields:
   * 
   */

  //draws this Spaceship onto given world scene
  public WorldScene draw(WorldScene acc) {
    return acc.
        placeImageXY(new RectangleImage(SPACESHIP_WIDTH, SPACESHIP_HEIGHT, 
            OutlineMode.SOLID, this.color), 
        this.loc.x, this.loc.y);
  }
  
  //EFFECT: move this spaceship to the left
  //if this spaceship reaches left border of the game screen,
  //update this spaceship's CartPt stay at the left border
  public void moveLeftS() {
    this.loc.moveLeft(SPACESHIP_SPEED);
    if (this.loc.x - SPACESHIP_WIDTH / 2 < 0) {
      this.loc.setX(SPACESHIP_WIDTH / 2);
    }
  }
  
  //EFFECT: move this spaceship to the right
  //if this spaceship reaches right border of the game screen,
  //update this spaceship's CartPt to stay at the right border
  public void moveRightS() {
    this.loc.moveRight(SPACESHIP_SPEED);
    if (this.loc.x + SPACESHIP_WIDTH / 2 > GAME_WIDTH) {
      this.loc.setX(GAME_WIDTH - SPACESHIP_WIDTH / 2);
    }
  }
  
  //EFFECT: move this spaceship
  public void moveS() {
    if (this.direction) {
      this.moveLeftS();
    }
    else {
      this.moveRightS();
    }
  }
  
  //EFFECT: updates the direction of this spaceship to given boolean (true = right, false = right)
  public void setDirection(boolean b) {
    this.direction = b;
  }
}

//represents an invader in game
class Invader extends AGamePiece {
  boolean hasFired;
  
  Invader(CartPt loc) {
    super(loc, Color.red);
    this.hasFired = false;
  }

  /* TEMPLATE:
   * fields:
   *  this.loc ... Location
   *  this.color ... Color
   *  this.hasFired ... boolean
   * methods:
   *  this.draw(WorldScene acc) ... WorldScene
   *  this.setHasFired(boolean b) ... void
   * methods for fields:
   * 
   */

  //draws this invader onto given world scene
  public WorldScene draw(WorldScene acc) {
    return acc.
        placeImageXY(new RectangleImage(INVADER_SIZE,INVADER_SIZE, 
            OutlineMode.SOLID, this.color), 
        this.loc.x, this.loc.y);
  }

  //EFFECT: set hasFired to given boolean
  public void setHasFired(boolean b) {
    this.hasFired = b;
  }
}


//represents a bullet in game
abstract class Bullet extends AGamePiece {
  boolean alive;

  /* TEMPLATE:
   * fields:
   *  this.loc ... Location
   *  this.color ... Color
   *  this.alive ... boolean
   * methods:
   *  this.draw(WorldScene acc) ... WorldScene
   *  this.commitDie() ... void
   *  this.move() .. void
   * methods for fields:
   */
  
  Bullet(CartPt loc, Color color) {
    super(loc, color);
    this.alive = true;
  }

  //draws this bullet onto given world scene
  public WorldScene draw(WorldScene acc) {
    return acc.placeImageXY(new CircleImage(BULLET_SIZE, OutlineMode.SOLID, this.color),
    this.loc.x, this.loc.y);
  }
  
  //EFFECT: set alive to false
  public void commitDie() {
    this.alive = false;
  }
  
  //EFFECT: move this bullet
  abstract void move();
}

//represents a bullet fired by the player
class PlayerBullet extends Bullet {
  PlayerBullet(CartPt loc) {
    super(loc, Color.black);
  }

  /* TEMPLATE:
   * fields:
   *  this.loc ... Location
   *  this.color ... Color
   *  this.alive ... boolean
   * methods:
   *  this.draw(WorldScene acc) ... WorldScene
   *  this.commitDie() ... void
   *  this.move() .. void
   * methods for fields:
   */
  
  //EFFECT: move this PlayerBullet
  public void move() {
    this.loc.moveUp(BULLET_SPEED);
  }
}

//represents a bullet fired by an invader
class InvaderBullet extends Bullet {
  InvaderBullet(CartPt loc) {
    super(loc, Color.red);
  }
  
  /* TEMPLATE:
   * fields:
   *  this.loc ... Location
   *  this.color ... Color
   *  this.alive ... boolean
   * methods:
   *  this.draw(WorldScene acc) ... WorldScene
   *  this.commitDie() ... void
   *  this.move() .. void
   * methods for fields:
   */
  
  //EFFECT: move this InvaderBullet
  public void move() {
    this.loc.moveDown(BULLET_SPEED);
  }
}

/* tests left to do:
 * commitDie
 * move for bullets (player + invader)
 */

class SpaceInvadersGame extends World {
  Spaceship player;
  IList<Invader> invaders;
  IList<PlayerBullet> pBullets;
  IList<InvaderBullet> iBullets;
  
  //constructor for start of every game
  SpaceInvadersGame() {
    Utils util = new Utils();
    //create player
    CartPt playerLoc = new CartPt(400, 750);
    this.player = new Spaceship(playerLoc);
    //create invaders
    this.invaders = util.buildList(36, new CreateInvader());
    //create bullets
    this.pBullets = new MtList<PlayerBullet>();
    this.iBullets = new MtList<InvaderBullet>();
  }

  SpaceInvadersGame(Spaceship player, IList<Invader> invaders) {
    this.player = player;
    this.invaders = invaders;
    this.pBullets = new MtList<PlayerBullet>();
    this.iBullets = new MtList<InvaderBullet>();
  }

  /* TEMPLATE:
   * fields:
   *  this.player ... Spaceship
   *  this.invaders ... IList<Invader>
   *  this.pBullets ... IList<PlayerBullet>
   *  this.iBullets ... IList<InvaderBullet>
   * methods:
   *  this.makeScene() ... WorldScene
   *  this.fireBullets() ... World
   *  this.onTick() ... World
   *  this.onKeyEvent(String key) ... World
   *  this.lastScene(String msg) ... WorldScene
   * methods for fields:
   */

  //draws the WorldScene with Spaceship and Invaders
  public WorldScene makeScene() {
    return this.player.draw(this.iBullets.foldr(new DrawIBullet(), this.pBullets.foldr(new DrawPBullet(), this.invaders.foldr(new DrawInvader(), new WorldScene(player.GAME_WIDTH, player.GAME_HEIGHT)))));
  }
  
  //fire bullets
  public void fireBullets() {
    //get number of InvaderBullets and Invaders
    int numIBullets = this.iBullets.length();
    int numInvaders = this.invaders.length();
    
    //find the maximum number of bullets that can be fired
    int maxBulletsToFire;
    if (10 - numIBullets < numInvaders) {
      maxBulletsToFire = 10 - numIBullets;
    }
    else {
      maxBulletsToFire = numInvaders;
    }
    
    //determine how many bullets to fire
    Random rand = new Random();
    int bulletsToFire = rand.nextInt(maxBulletsToFire + 1);
    
    //create new list of invaders that can fire named loadedInvaders
    //set numInvaders to the length of loadedInvaders
    IList<Invader> loadedInvaders = this.invaders;
    
    while (bulletsToFire > 0) {
      //get random Invader
      int randIndex = rand.nextInt(numInvaders);
      Invader attacker = loadedInvaders.getNthElement(randIndex);
      
      //fire(create new InvaderBullet) and set hasFired to true
      CartPt attackerLoc = new CartPt(attacker.loc.x, attacker.loc.y);
      this.iBullets = new ConsList<InvaderBullet>(new InvaderBullet(attackerLoc), this.iBullets);
      attacker.setHasFired(true);
      
      //filter loadedInvaders
      loadedInvaders = loadedInvaders.filter(new HasntFired());
      
      //Deincrement numInvaders and bulletsToFire
      numInvaders--;
      bulletsToFire--;
    }
  }
  
  //do all the things that occur each tick
  public World onTick() {
    //test if player safe
    boolean isAlive = new IsPlayerSafe(this.iBullets).test(this.player); // if false, end the game
    
    //test if any hits and delete hit invaders
    this.invaders = this.invaders.filter(new IsInvaderSafe(this.pBullets));
    boolean levelCleared = this.invaders.length() == 0; // if true, end game
    
    //end game if player dead or  level cleared
    if (!isAlive || levelCleared) {
      return this.endOfWorld("A message");
    }
    
    //delete bullets that hit targets
    this.pBullets = this.pBullets.filter(new IsLivePBullet());
    this.iBullets = this.iBullets.filter(new IsLiveIBullet());
    
    //move all bullets
    this.pBullets.map(new MovePBullet());
    this.iBullets.map(new MoveIBullet());
    
    //delete bullets that hit/go beyond the edge
    this.pBullets = this.pBullets.filter(new IsPBInBounds());
    this.iBullets = this.iBullets.filter(new IsIBInBounds());
    
    //fire invader bullets    
    if (this.iBullets.length() < 10) {
      // random firing thing
      this.fireBullets();
    }
    //"reload" all invaders
    this.invaders.map(new LoadInvader());
    
    //move spaceship
    this.player.moveS();
    
    return this;
  }
  
  //handles player inputs
  public World onKeyEvent(String key) {
    switch(key)
    {
      case " " : // on space
        //fire bullet
        if (this.pBullets.length() < 3) {
          CartPt playerLoc = new CartPt(this.player.loc.x, this.player.loc.y);
          this.pBullets = new ConsList<PlayerBullet>(new PlayerBullet(playerLoc), this.pBullets);
        }
        return this;
      case "left" : // on left arrow
        //move ship left
        this.player.setDirection(true);
        return this;
      case "right" : // on right arrow
        //move ship right
        this.player.setDirection(false);
        return this;
      default :
        //do nothing
        return this;
    }
  }
  
  //return the "Game Over" Scene
  public WorldScene lastScene(String msg) {
    return new WorldScene(800, 1000);
  }
}

//represents examples and tests for Space Invader game
class ExamplesSpaceInvaders {
  //represents examples for IList<Integer>
  IList<Integer> mt = new MtList<>();
  IList<Integer> list1 = new ConsList<Integer>(1, this.mt);
  IList<Integer> list2 = new ConsList<Integer>(2, this.list1); 
  IList<Integer> list3 = new ConsList<Integer>(2, this.mt);
  IList<Integer> list4 = new ConsList<Integer>(3, this.list3);

  //represents examples for CartPt
  CartPt loc1;
  CartPt loc2;
  CartPt loc3;
  CartPt loc4;
  CartPt loc5;
  CartPt startPos;

  //represents examples for IList<CartPt>
  IList<CartPt> mtListCP = new MtList<CartPt>();
  IList<CartPt> list1CP;
  IList<CartPt> list2CP;

  //represents examples for IGamePiece (all IGamePiece are AGamePiece as well)
  IGamePiece ship1;
  IGamePiece ship2;
  IGamePiece invader1;
  IGamePiece invader2;
  IGamePiece bullet1;
  IGamePiece bullet2;

  //represents Utils Class for Invaders
  Utils genericUtils;
  
  //represents example for Spaceship
  Spaceship shipStart;
  Spaceship ship3;
  Spaceship ship4;
  Spaceship ship5;

  //represents example for Invader
  Invader invader3;
  Invader invader4;
  
  //represents example for Bullet
  Bullet bulletS1;
  Bullet bulletI1;
  
  //represents example for starting list of Invaders
  IList<Invader> invadersStart;
  
  //represents WorldImage for Spaceship
  WorldImage spaceshipImage;

  //represents WorldImage for Invader
  WorldImage invaderImage;

  //represents game screen dimensions
  WorldScene gameScreen;

  //represents examples for IList<Invader>
  IList<Invader> mtLoInvaders;
  IList<Invader> invadersList1;
  IList<Invader> invadersList2;

  //represents examples for SpaceInvadersGame
  SpaceInvadersGame game1;
  SpaceInvadersGame game2;
  SpaceInvadersGame game3;
  
  //represents examples for InList
  InList inListInstanceMT;
  InList inListInstanceList1;
  InList inListInsranceList2;
  
  void initData() {
    //represents examples for CartPt
    this.loc1 = new CartPt(0,0);
    this.loc2 = new CartPt(150, 250);
    this.loc3 = new CartPt(300, 500);
    this.loc4 = new CartPt(150, 475);
    this.loc5 = new CartPt(150, 25);
    this.startPos = new CartPt(400, 750);
  
    //represents examples for IList<CartPt>
    this.list1CP = new ConsList<CartPt>(loc1,
        new ConsList<CartPt>(loc2,
            new ConsList<CartPt>(loc3, this.mtListCP)));
    this.list2CP = new ConsList<CartPt>(loc5,
        new ConsList<CartPt>(loc4,
            new ConsList<CartPt>(loc3,
                new ConsList<CartPt>(loc2,
                    new ConsList<CartPt>(loc1, this.mtListCP)))));
  
    //represents examples for IGamePiece
    this.ship1 = new Spaceship(this.loc4);
    this.ship2 = new Spaceship(new CartPt(140, 475));
    this.invader1 = new Invader(this.loc5);
    this.invader2 = new Invader(new CartPt(180, 25));
    this.bullet1 = new PlayerBullet(new CartPt(180, 25));
    this.bullet2 = new InvaderBullet(new CartPt(260, 100));
    

    //represents Utils Class for Invaders
    this.genericUtils = new Utils();
    
    //represents example for Spaceship
    this.shipStart = new Spaceship(startPos);
    this.ship3 = new Spaceship(new CartPt(5, 475));
    this.ship4 = new Spaceship(new CartPt(795, 475));
    
    //represents example for Invader
    this.invader3 = new Invader(new CartPt(240, 25));
    this.invader4 = new Invader(new CartPt(320, 25));
    
    //represent example for Bullet
    this.bulletS1 = new PlayerBullet(new CartPt(400, 400));
    this.bulletI1 = new InvaderBullet(new CartPt(400, 400));
    
    //represents example for starting list of Invaders
    this.invadersStart = genericUtils.buildList(36, new CreateInvader());
    
    //represents WorldImage for Spaceship
    this.spaceshipImage = new RectangleImage(50, 25, OutlineMode.SOLID, Color.black);

    //represents WorldImage for Invader
    this.invaderImage = new RectangleImage(60, 60, OutlineMode.SOLID, Color.RED);

    //represents game screen dimensions
    this.gameScreen = new WorldScene(800, 800);

    //represents examples for IList<Invader>
    this.mtLoInvaders = new MtList<Invader>();
    this.invadersList1 = this.genericUtils.buildList(5, new CreateInvader());
    this.invadersList2 = this.genericUtils.buildList(10, new CreateInvader());

    //represents examples for SpaceInvadersGame
    this.game1 = new SpaceInvadersGame(this.shipStart, this.mtLoInvaders);
    this.game2 = new SpaceInvadersGame(this.shipStart, this.invadersList1);
    this.game3 = new SpaceInvadersGame(this.shipStart, this.invadersList2);
    
    //represents examples for InList
    this.inListInstanceMT = new InList(this.mtListCP);
    this.inListInstanceList1 = new InList(this.list1CP);
    this.inListInsranceList2 = new InList(this.list2CP);
    
  }

  //tests for addOne
  AddOne addOneInstance = new AddOne();
  
  boolean testAddOne(Tester t) {
    return t.checkExpect(this.addOneInstance.apply(0), 1) &&
        t.checkExpect(this.addOneInstance.apply(-1), 0) &&
        t.checkExpect(this.addOneInstance.apply(99), 100);
  }

  //tests for isEven
  IsEven isEvenInstance = new IsEven();
  
  boolean testIsEven(Tester t) {
    return t.checkExpect(this.isEvenInstance.test(0), true) &&
        t.checkExpect(this.isEvenInstance.test(1), false) &&
        t.checkExpect(this.isEvenInstance.test(2), true);
  }

  //tests for sumAll
  SumAll sumAllInstance = new SumAll();
  
  boolean testSumAll(Tester t) {
    return t.checkExpect(this.sumAllInstance.apply(0, 0), 0) &&
        t.checkExpect(this.sumAllInstance.apply(1, 3), 4) &&
        t.checkExpect(this.sumAllInstance.apply(-1, 6), 5);
  }

  //tests for map
  boolean testMap(Tester t) {
    return t.checkExpect(this.mt.map(new AddOne()), this.mt)
        && t.checkExpect(this.list1.map(new AddOne()), this.list3)
        && t.checkExpect(this.list2.map(new AddOne()), this.list4);
  }  

  //tests for filter
  boolean testFilter(Tester t) {
    return t.checkExpect(this.mt.filter(new IsEven()), this.mt)
        && t.checkExpect(this.list1.filter(new IsEven()), this.mt)
        && t.checkExpect(this.list2.filter(new IsEven()), this.list3);
  }

  //tests for foldr
  boolean testFoldr(Tester t) {
    return t.checkExpect(this.mt.foldr(new SumAll(), 0), 0)
        && t.checkExpect(this.list1.foldr(new SumAll(), 0), 1)
        && t.checkExpect(this.list2.foldr(new SumAll(), 0), 3);
  }

  //tests for andmap
  boolean testAndmap(Tester t) {
    return t.checkExpect(this.mt.andmap(new IsEven()), true)
        && t.checkExpect(this.list1.andmap(new IsEven()), false)
        && t.checkExpect(this.list3.andmap(new IsEven()), true);
  }

  //tests for ormap
  boolean testOrmap(Tester t) {
    return t.checkExpect(this.mt.ormap(new IsEven()), false)
        && t.checkExpect(this.list1.ormap(new IsEven()), false)
        && t.checkExpect(this.list2.ormap(new IsEven()), true)
        && t.checkExpect(this.list3.ormap(new IsEven()), true);
  }

  //tests for length
  boolean testLength(Tester t) {
    return t.checkExpect(this.mt.length(), 0) &&
        t.checkExpect(this.list1.length(), 1) &&
        t.checkExpect(this.list2.length(), 2);
  }
  
  //tests for getNthElement
  boolean testGetNthElement(Tester t) {
    return t.checkExpect(this.mt.getNthElement(1), null) &&
        t.checkExpect(this.list1.getNthElement(1), null) &&
        t.checkExpect(this.list2.getNthElement(0), 2) &&
        t.checkExpect(this.list2.getNthElement(1), 1) &&
        t.checkExpect(this.list2.getNthElement(100), null);
  }
  
  //tests for samePoint
  SamePoint samePointInstance = new SamePoint(new CartPt(100, 200));
  
  boolean testSamePoint(Tester t) {
    this.initData();
    return t.checkExpect(this.samePointInstance.test(loc1), false) 
        && t.checkExpect(this.samePointInstance.test(loc2), false) 
        && t.checkExpect(this.samePointInstance.test(new CartPt(100, 200)), true);
  }

  //tests for inList 
  boolean testInList(Tester t) {
    this.initData();
    return t.checkExpect(this.inListInstanceMT.test(loc1), false) 
        && t.checkExpect(this.inListInstanceList1.test(loc5), false) 
        && t.checkExpect(this.inListInsranceList2.test(loc3), true);
  }

  //tests for buildAToB
  Utils<Integer> intUtils = new Utils<Integer>();
  
  boolean testBuildAToB(Tester t) {
    this.initData();
    return t.checkExpect(this.intUtils.buildAToB(5, 0), new MtList<Integer>()) 
        && t.checkExpect(this.intUtils.buildAToB(0, 5), new ConsList<Integer>(0,
            new ConsList<Integer>(1,
                new ConsList<Integer>(2,
                    new ConsList<Integer>(3,
                        new ConsList<Integer>(4,
                            new ConsList<Integer>(5,new MtList<Integer>()))))))) 
        && t.checkExpect(this.intUtils.buildAToB(1, 3), new ConsList<Integer>(1,
            new ConsList<Integer>(2,
                new ConsList<Integer>(3, new MtList<Integer>()))));
  }

  //tests for buildList
  boolean testBuildList(Tester t) {
    this.initData();
    return t.checkExpect(this.intUtils.buildList(0, addOneInstance), new MtList<Integer>()) 
        && t.checkExpect(this.intUtils.buildList(5, addOneInstance), new ConsList<Integer>(1,
            new ConsList<Integer>(2,
                new ConsList<Integer>(3,
                    new ConsList<Integer>(4,
                        new ConsList<Integer>(5, new MtList<Integer>())))))) 
        && t.checkExpect(this.intUtils.buildList(3, addOneInstance), new ConsList<Integer>(1,
            new ConsList<Integer>(2,
                new ConsList<Integer>(3, new MtList<Integer>()))));
  }

  //tests for createInvader
  CreateInvader createInvaderInstance = new CreateInvader();
  
  boolean testCreateInvader(Tester t) {
    this.initData();
    return t.checkExpect(this.createInvaderInstance.apply(0), 
        new Invader(new CartPt(80, 100)))
        && t.checkExpect(this.createInvaderInstance.apply(5), 
            new Invader(new CartPt(480, 100)))
        && t.checkExpect(this.createInvaderInstance.apply(35), 
            new Invader(new CartPt(720, 340)));
  }

  //tests for drawInvader
  DrawInvader drawInvaderInstance = new DrawInvader();
  
  boolean testDrawInvader(Tester t) {
    this.initData();
    return t.checkExpect(this.drawInvaderInstance.
        apply(new Invader(new CartPt(80,100)), this.gameScreen),
        this.gameScreen.
        placeImageXY(new RectangleImage(60, 60, OutlineMode.SOLID, Color.RED), 80, 100))
        && t.checkExpect(this.drawInvaderInstance.
            apply(new Invader(new CartPt(720, 340)), this.gameScreen), 
            this.gameScreen.
            placeImageXY(new RectangleImage(60, 60, OutlineMode.SOLID, Color.RED), 720, 340));
  }

  //tests for draw
  boolean testDraw(Tester t) {
    this.initData();
    return t.checkExpect(this.ship1.draw(this.gameScreen),
        this.gameScreen.
        placeImageXY(new RectangleImage(50, 25, OutlineMode.SOLID, Color.black), 150, 475))
        && t.checkExpect(this.ship2.draw(this.gameScreen),
            this.gameScreen.
            placeImageXY(new RectangleImage(50, 25, OutlineMode.SOLID, Color.black), 140, 475))
        && t.checkExpect(this.invader1.draw(this.gameScreen),
            this.gameScreen.
            placeImageXY(new RectangleImage(60, 60, OutlineMode.SOLID, Color.red), 150, 25))
        && t.checkExpect(this.invader2.draw(this.gameScreen),
            this.gameScreen.
            placeImageXY(new RectangleImage(60, 60, OutlineMode.SOLID, Color.red), 180, 25))
        && t.checkExpect(this.bullet1.draw(this.gameScreen),
            this.gameScreen.
            placeImageXY(new CircleImage(5, OutlineMode.SOLID, Color.black), 180, 25))
        && t.checkExpect(this.bullet2.draw(this.gameScreen),
            this.gameScreen.
            placeImageXY(new CircleImage(5, OutlineMode.SOLID, Color.red), 260, 100));
  }

  //tests for makeScene
  boolean testMakeScene(Tester t) {
    this.initData();
    return t.checkExpect(this.game1.makeScene(), 
        this.gameScreen.placeImageXY(spaceshipImage, 400, 750))
        && t.checkExpect(this.game2.makeScene(), 
            this.gameScreen.placeImageXY(spaceshipImage, 400, 750).
            placeImageXY(invaderImage, 80, 100).
            placeImageXY(invaderImage, 160, 100).
            placeImageXY(invaderImage, 240, 100).
            placeImageXY(invaderImage, 320, 100).
            placeImageXY(invaderImage, 400, 100))
        && t.checkExpect(this.game3.makeScene(), 
            this.gameScreen.placeImageXY(spaceshipImage, 400, 750).
            placeImageXY(invaderImage, 80, 100).
            placeImageXY(invaderImage, 160, 100).
            placeImageXY(invaderImage, 240, 100).
            placeImageXY(invaderImage, 320, 100).
            placeImageXY(invaderImage, 400, 100).
            placeImageXY(invaderImage, 480, 100).
            placeImageXY(invaderImage, 560, 100).
            placeImageXY(invaderImage, 640, 100).
            placeImageXY(invaderImage, 720, 100).
            placeImageXY(invaderImage, 80, 180));
  }
    
  //tests for moveUp 
  void testMoveUp(Tester t) {
    this.initData();
    t.checkExpect(this.loc2.y, 250);
    loc2.moveUp(1);
    t.checkExpect(this.loc2.y, 249);
    t.checkExpect(this.loc3.y, 500);
    loc3.moveUp(5);
    t.checkExpect(this.loc3.y, 495);
  }
  
  //tests for moveDown
  void testMoveDown(Tester t) {
    this.initData();
    t.checkExpect(this.loc2.y, 250);
    this.loc2.moveDown(1);
    t.checkExpect(this.loc2.y, 251);
    t.checkExpect(this.loc3.y, 500);
    this.loc3.moveUp(5);
    t.checkExpect(this.loc3.y, 495);
  }
  
  //tests for moveRight (in CartPt class)
  void testMoveRight(Tester t) {
    this.initData();
    t.checkExpect(this.loc1.x, 0);
    this.loc1.moveRight(1);
    t.checkExpect(this.loc1.x, 1);
    t.checkExpect(this.loc4.x, 150);
    this.loc4.moveRight(5);
    t.checkExpect(this.loc4.x, 155);
  }
  
  //tests for moveLeft (in CartPt class)
  void testMoveLeft(Tester t) {
    this.initData();
    t.checkExpect(this.loc1.x, 0);
    this.loc1.moveLeft(1);
    t.checkExpect(this.loc1.x, -1);
    t.checkExpect(this.loc4.x, 150);
    this.loc4.moveLeft(5);
    t.checkExpect(this.loc4.x, 145);
  }
  
  //tests for setX
  void testSetX(Tester t) {
    this.initData();
    t.checkExpect(this.loc1.x, 0);
    this.loc1.setX(100);
    t.checkExpect(this.loc1.x, 100);
    t.checkExpect(this.loc2.x, 150);
    this.loc2.setX(5);
    t.checkExpect(this.loc2.x, 5);
  }
  
  //tests for moveLeftS
  void testMoveLeftS(Tester t) {
    this.initData();
    t.checkExpect(this.shipStart.loc.x, 400);
    this.shipStart.moveLeftS();
    t.checkExpect(this.shipStart.loc.x, 395);
    t.checkExpect(this.ship3.loc.x, 5);
    this.ship3.moveLeftS();
    t.checkExpect(this.ship3.loc.x, 25);
  }
  
  //tests for moveRightS
  void testMoveRightS(Tester t) {
    this.initData();
    t.checkExpect(this.shipStart.loc.x, 400);
    this.shipStart.moveRightS();
    t.checkExpect(this.shipStart.loc.x, 405);
    t.checkExpect(this.ship4.loc.x, 795);
    this.ship4.moveRightS();
    t.checkExpect(this.ship4.loc.x, 775);
  }
  
  //tests for moveS
  void testMoveS(Tester t) {
    this.initData();
    t.checkExpect(this.shipStart.loc.x, 400);
    this.shipStart.moveS();
    t.checkExpect(this.shipStart.loc.x, 395);
    t.checkExpect(this.ship3.loc.x, 5);
    this.ship3.moveS();
    t.checkExpect(this.ship3.loc.x, 25);
    this.ship4.setDirection(false); //change direction to right
    t.checkExpect(this.ship4.loc.x, 795);
    this.ship4.moveS();
    t.checkExpect(this.ship4.loc.x, 775);
  }
  
  //tests for setDirection
  void testSetDirection(Tester t) {
    this.initData();
    t.checkExpect(this.shipStart.direction, true);
    this.shipStart.setDirection(false);
    t.checkExpect(this.shipStart.direction, false);
    this.ship3.setDirection(false);
    t.checkExpect(this.ship3.direction, false);
    this.ship3.setDirection(true);
    t.checkExpect(this.ship3.direction, true);
  }
  
  //tests for setHasFired
  void testSetHasFired(Tester t) {
    this.initData();
    t.checkExpect(this.invader3.hasFired, false);
    this.invader3.setHasFired(true);
    t.checkExpect(this.invader3.hasFired, true);
    this.invader4.setHasFired(true);
    t.checkExpect(this.invader4.hasFired, true);
    this.invader4.setHasFired(false);
    t.checkExpect(this.invader4.hasFired, false);
  }
  
  //
  //tests for bigBang
  boolean testBigBang(Tester t) { 
    SpaceInvadersGame game = new SpaceInvadersGame(); 
    int worldWidth = 800;
    int worldHeight = 800;
    double tickRate = 0.01;
    return game.bigBang(worldWidth, worldHeight, tickRate);
  }
}
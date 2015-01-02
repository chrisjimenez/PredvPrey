import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import ddf.minim.spi.*; 
import ddf.minim.signals.*; 
import ddf.minim.*; 
import ddf.minim.analysis.*; 
import ddf.minim.ugens.*; 
import ddf.minim.effects.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class assignment3 extends PApplet {








Minim minim;
AudioPlayer collisionSound1;
AudioPlayer collisionSound2;

int score;//score
int startTime;
int currentTime;
int displayTime;//time
int count= 120;

int state = 0;//INITIAL state

ColorQuadrillateral predator;
PulsatingCircle prey;
PulsatingSquare redEnemy;

//For the easy,medium,hard buttons
PImage easyButtonImage;
PImage easyButtonPressedImage;
PImage mediumButtonImage;
PImage mediumButtonPressedImage;
PImage hardButtonImage;
PImage hardButtonPressedImage;
ImageButton easyButton;
ImageButton mediumButton;
ImageButton hardButton;

PFont font;

//=============================================================
public void setup() {
  size(500, 500);
  frameRate(30);

  // set up Minim  
  minim = new Minim(this);

  // load in our audio file
  collisionSound1 = minim.loadFile("crash-sound.mp3");
  collisionSound2 = minim.loadFile("thump-sound.mp3");



  //create predator
  predator = new ColorQuadrillateral();

  //create a prey
  prey = new PulsatingCircle(predator.xPos, predator.yPos);

  //create red enemy
  redEnemy = new PulsatingSquare();

  //button images
  easyButtonImage = loadImage("easybutton.png");
  easyButtonPressedImage = loadImage("easybutton-pressed.png");
  mediumButtonImage = loadImage("mediumbutton.png");
  mediumButtonPressedImage = loadImage("mediumbutton-pressed.png");
  hardButtonImage = loadImage("hardbutton.png");
  hardButtonPressedImage = loadImage("hardbutton-pressed.png");

  //create button objects
  easyButton = new ImageButton(easyButtonImage, easyButtonPressedImage);
  mediumButton = new ImageButton(mediumButtonImage, mediumButtonPressedImage);
  hardButton = new ImageButton(hardButtonImage, hardButtonPressedImage);

}

//===============================================================
public void draw() { 
  smooth();
  background(0);
  fill(255);

  //space travelling simulation!
  for (int i = 0; i < 20; i++) {
    ellipse(random(width), random(height), 2, 2);
  }
  //states correspond to different levels
  if (state == 0) {
    startGame();
  }
  else if (state == 1) {
    easyGame();
  }
  else if (state == 2) {
    mediumGame();
  }
  else if (state == 3) {
    hardGame();
  }
}

//==================================================
//method that displays the start screen or title screen
public void startGame() {

  //Place characters in title screen in the background
  prey.display();
  prey.move();
  predator.display();
  predator.move();
  redEnemy.display();
  redEnemy.move();


  //display Button
  textSize(10);
  fill(255);
  easyButton.resizeButton(100, 50);
  easyButton.display(100, 150);
  text("-->for beginners.", 210, 180);
  mediumButton.resizeButton(100, 50);
  mediumButton.display(100, 250);
  text("-->A little bit faster.", 210, 280);
  hardButton.resizeButton(100, 50);
  hardButton.display(100, 350);
  text("-->You're not the only predator.", 210, 380);

  //Display title
  font = loadFont("SynchroLET-48.vlw");
  textFont(font);
  text("Predator v. Prey", 50, 100);

  //commands if either button is pressed
  if (easyButton.isPressed() && mousePressed) {
    reset();
    state = 1;
  }
  else if (mediumButton.isPressed() && mousePressed) {
    reset();
    state = 2;
  }
  else if (hardButton.isPressed() && mousePressed) {
    reset();
    state = 3;
  }
}

//=================================================
//different levels of gaming
public void easyGame() {
  playGame();
}

public void mediumGame() {
  prey.adjustAccel(0.3f);
  predator.adjustAccel(0.8f);
  playGame();
}

public void hardGame() {
  prey.adjustAccel(0.3f);
  predator.adjustAccel(0.8f);
  displayFadingText("DONT TOUCH THE RED SQUARE!", 70, 250);
  playGame();
  checkCollision(redEnemy, predator);
  redEnemy.move();
  redEnemy.display();
}

//=========================================================
//game play function
public void playGame() {
  displayFadingText("CHASE THE CIRCLE!", 130, 200);

  predator.display();
  prey.display();

  displayTimeAndScore();
  displayCommands();

  gameControls();

  checkCollision(prey, predator);
}

//==============================================================
//resets the game
public void reset() {
  //initialize position of the predator & prey
  predator.reset();
  prey.reset();
  prey.adjustPosition(prey.xPos+300, prey.yPos+300);

  //reset start time
  startTime = (int)(millis()/1000);
}


//==============================================================
//display the fading text on the screen
public void displayFadingText(String s, float x, float y) {
  fill(255, 255, 255, count--);
  textSize(25);//slightlty larger text size
  text(s, x, y);
  textSize(15);//return text size back to normal
}

//==============================================================
//displays the time and score on the screen
public void displayTimeAndScore() {
  //time & score
  fill(255);
  currentTime = (int)(millis()/1000);
  displayTime = currentTime - startTime;
  text("TIME: " + displayTime, 0, 20);
  text("SCORE: " + score, 0, 35);
}

//==============================================================
//check if there is a collision between the two characters
public void checkCollision(PulsatingSquare ps, ColorQuadrillateral cq) {
  //when predator and prey collide
  if (cq.detectCollision(ps.xPos, ps.yPos)) {
    collisionSound1.rewind();
    collisionSound1.play();
    score--;
    prey.reset();
    predator.reset();
    prey.adjustPosition(prey.xPos+300, prey.yPos+300);
  }
}

//==============================================================
//check if there is a collision between the two characters
public void checkCollision(PulsatingCircle pc, ColorQuadrillateral cq) {
  //when predator and prey collide
  if (cq.detectCollision(pc.xPos, pc.yPos)) {
    collisionSound2.rewind();
    collisionSound2.play();
    score++;
    prey.reset();
    predator.reset();
    prey.adjustPosition(prey.xPos+300, prey.yPos+300);
  }
}

//==============================================================
//game controls for the game
public void gameControls() {
  //*NOTE: the acceleration idea came from DAY03 lecture
  if (keyPressed && key == CODED) {
    if (keyCode == UP) {
      predator.moveUp();
      prey.moveUp();
    }
    if (keyCode == DOWN) {
      predator.moveDown();
      prey.moveDown();
    }

    if (keyCode == LEFT) {
      predator.moveLeft();
      prey.moveLeft();
    }

    if (keyCode == RIGHT) {
      predator.moveRight();
      prey.moveRight();
    }
  }

  if (keyPressed && key =='r') {//resets the game
    score= 0;
    reset();
    currentTime = 0;
  }

  if (keyPressed && key =='q') {//quits the game
    state = 0;
  }
}

//=====================================================
//Displays the commands to reset or quit
public void displayCommands() {
  fill(255);
  textSize(10);
  text("TO GO BACK TO MENU, PRESS 'Q'", 340, 15);
  text("TO RESET GAME, PRESS 'R'", 340, 30);
}



//this is the class for a quadrillateral that is made using
//four triangles, where each individual triangle has its
//color but all share same vertex points
class ColorQuadrillateral {
  //character globals-position & speed
  float xPos;
  float yPos;
  float xPosMove;
  float yPosMove;
  float accel = .2f;//BY DEFAULT THE ACCELERATION IS 0.5
  float xDest, yDest;

  //chracter-quad with four triangles
  //which creates a quad w/ four corners
  float corner1X;
  float corner1Y;

  float corner2X;
  float corner2Y;

  float corner3X;
  float corner3Y;

  float corner4X;
  float corner4Y;


  //============================================================
  ColorQuadrillateral() {
    randomPlacement();
  }
  //============================================================
  public void drawQuad() {
    corner1X = xPos - random(10, 20);
    corner1Y = yPos - random(10, 20);

    corner2X = xPos + random(10, 20);
    corner2Y = yPos - random(10, 20);

    corner3X = xPos - random(10, 20);
    corner3Y = yPos + random(10, 20);

    corner4X = xPos + random(10, 20);
    corner4Y = yPos + random(10, 20);

    noStroke();  
    //triangle formation
    fill(random(20, 200), random(20, 200), random(20, 200));
    triangle(corner1X, corner1Y, xPos, yPos, corner3X, corner3Y);

    fill(random(20, 200), random(20, 200), random(20, 200));
    triangle(corner1X, corner1Y, xPos, yPos, corner2X, corner2Y);

    fill(random(20, 200), random(20, 200), random(20, 200));
    triangle(corner2X, corner2Y, xPos, yPos, corner4X, corner4Y);

    fill(random(20, 200), random(20, 200), random(20, 200));
    triangle(corner4X, corner4Y, xPos, yPos, corner3X, corner3Y);
  }

  //=============================================================    
  //method that places the character at the edge of the screen location based on chance 
  public void characterPlacement() {
    float chance = random(0, 1);

    if (chance <= .25f) {//bottom
      xPos = width/2;
      yPos = height -10;//not exactly at the edge
    }
    else if (chance > .25f && chance <= .5f) {//top
      xPos = width/2;
      yPos = 10;//not exactly at the edge
    }
    else if (chance > .5f && chance <= .75f) {//left
      xPos = 10;
      yPos = height -10;//not exactly at the edge
    }
    else {
      xPos = width-10;
      yPos = height-10;//not exactly at the edge
    }
  }

  //======================================================  
  //this method displays the chracter on the screen
  public void display() {
    drawQuad();
    xPos += xPosMove;
    yPos += yPosMove;

    permitWrapAround();
  }

  //======================================================
  //The following methods are for character movement
  //*NOTE: the acceleration idea came from DAY03 lecture
  public void moveRight() {
    if (xPosMove < 10) {//to set the speed limit
      xPosMove += accel;
    }
  }

  public void moveLeft() {
    if (xPosMove > -10) {//to set the speed limit
      xPosMove -= accel;
    }
  }

  public void moveUp() {
    if (yPosMove > -10) {//to set the speed limit
      yPosMove -= accel;
    }
  }

  public void moveDown() {
    if (yPosMove < 10) {//to set the speed limit
      yPosMove += accel;
    }
  }

  //=====================================================
  //resets the position of the character
  public void reset() {
    xDest = random(25, width-25);
    yDest = random(25, height-25);
    xPosMove = 0;
    yPosMove = 0;
    randomPlacement();

  }

  //===========================================================
  //method that places the predator at the edge of the screen
  //location based on chance 
  public void randomPlacement() {
    float chance = random(0, 1);

    if (chance <= .25f) {//bottom
      xPos = width/2;
      yPos = height -10;//not exactly at the edge
    }
    else if (chance > .25f && chance <= .5f) {//top
      xPos = width/2;
      yPos = 10;//not exactly at the edge
    }
    else if (chance > .5f && chance <= .75f) {//left
      xPos = 10;
      yPos = height -10;//not exactly at the edge
    }
    else {
      xPos = width-10;
      yPos = height-10;//not exactly at the edge
    }
  }

  //=================================================
  //permits the character to wrap around
  public void permitWrapAround() {
    if (xPos>width) {
      xPos = 0;
    }
    else if (yPos>height) {
      yPos = 0;
    } 
    else if (yPos < 0) {
      yPos = height;
    }
    else if (xPos < 0) {
      xPos = width;
    }
  }

  //=================================================
  //detects collision
  public boolean detectCollision(float x, float y) {
    if (dist(xPos, yPos, x, y) < 30) {
      return true;
    }
    else {
      return false;
    }
  }

  //=================================================
  //adjust the acceleration of the character
  public void adjustAccel(float a) {
    accel = a;
  }


  //=================================================
  //Randomy moving AI
  //*NOTE: method idea came from lecture
  public void move() {
    // have we arrived at this position?
    if (dist(xPos, yPos, xDest, yDest) < 25) {
      // pick a new destination
      xDest = random(0, width-25);  
      yDest = random(0, height-25);
    }

    if (xDest < xPos) {
      xPos -= 5;
    }
    else {
      xPos += 5;
    }

    if (yDest < yPos) {
      yPos -= 3;
    }
    else {
      yPos += 3;
    }
  }
  
}

class ImageButton {
  int yPos;
  int xPos;
  int buttonWidth;
  int buttonHeight;

  PImage button;
  PImage buttonPressed;
  PImage currentButton;
  boolean pressed;

  //=========================================================
  //constructor
  ImageButton(PImage buttonImage, PImage buttonPressedImage) {
    button = buttonImage;
    buttonPressed = buttonPressedImage;
    currentButton = button;
  }

  //========================================================
  //displays button at position x,y
  public void display(int x, int y) {
    xPos = x;
    yPos = y;
    image(currentButton, x, y);
  }

  //=======================================================
  //resize the button using given width(w) & height(h)
  public void resizeButton(int w, int h) { 
    buttonWidth = w;
    buttonHeight = h;
    button.resize(buttonWidth, buttonHeight);
    buttonPressed.resize(buttonWidth, buttonHeight);
  }

  //=======================================================
  //determiness if button is pressed
  public boolean isPressed() {
    if (((mouseX >= xPos) && (mouseX <= xPos+buttonWidth)) &&
      ((mouseY >= yPos) && (mouseY <=yPos+buttonHeight))) {
      currentButton = buttonPressed;
      return true;
    }
    else {
      currentButton = button;
      return false;
    }
  }
  
}

class PulsatingCircle {
  //character globals-position & speed
  float xPos;
  float yPos;
  float xPosMove;
  float yPosMove;
  float xDest, yDest;
  float predXPos, predYPos;
  float accel = .05f;
  float radius = 1.0f;//by default it will start at 1.0
  float radiusIncrease = 0.5f;


  //=================================================
  //constructor
  PulsatingCircle(float x, float y) {
    predXPos = x;
    predYPos = y;
    xPos = x + 200;
    yPos = y + 200;
  }

  //=================================================
  //displays the pullsating circle on canvas
  public void display() {
    //pulsing ellipse
    if (radius >= 1) {
      radius += radiusIncrease;
      fill(255, 255, 255);
      ellipseMode(RADIUS);
      strokeWeight(3);
      stroke(204, 102, 0);
      ellipse(xPos, yPos, radius, radius);
      noStroke();
      if (radius >= 20) {
        radius = 1;//shrink circle
      }
    }
    xPos += xPosMove;
    yPos += yPosMove;
    wrapAround();
  }

  //==================================================
  //the following methods are for character movements
  public void moveRight() {
    if (xPosMove < 10) {//to set the speed limit
      xPosMove += accel;
    }
  }

  public void moveLeft() {
    if (xPosMove > -10) {//to set the speed limit
      xPosMove -= accel;
    }
  }

  public void moveUp() {
    if (yPosMove > -15) {//to set the speed limit
      yPosMove -= accel;
    }
  }

  public void moveDown() {
    if (yPosMove < 15) {//to set the speed limit
      yPosMove += accel;
    }
  }

  //==================================================
  //resets character at different position
  public void reset() {
    xPosMove = 0;
    yPosMove = 0;
  }

  //randomly places character on the canvas
  public void randomPlacement() {
    float chance = random(0, 1);

    if (chance <= .25f) {//bottom
      xPos = width/2;
      yPos = height -10;//not exactly at the edge
    }
    else if (chance > .25f && chance <= .5f) {//top
      xPos = width/2;
      yPos = 10;//not exactly at the edge
    }
    else if (chance > .5f && chance <= .75f) {//left
      xPos = 10;
      yPos = height -10;//not exactly at the edge
    }
    else {
      xPos = width-10;
      yPos = height-10;//not exactly at the edge
    }
  }

  //==============================================
  //permits wrap around for the character
  public void wrapAround() {
    
    if (xPos>width) {
      xPos = 0;
    }
    else if (yPos>height) {
      yPos = 0;
    }
    else if (yPos < 0) {
      yPos = height;
    }
    else if (xPos < 0) {
      xPos = width;
    }
  }

  //=============================================
  //adjusts acceleration of the character
  public void adjustAccel(float a) {
    accel = a;
  }
  
  //=============================================
  //adjusts acceleration of the character
  public void adjustPosition(float x, float y) {
    xPos = x;
    yPos = y;
  }
  
  
  
  //===============================================
  //randomly moves the charcter around the canvas
  public void move() {
    // have we arrived at this position?
    if (dist(xPos, yPos, xDest, yDest) < 25) {
      // pick a new destination
      xDest = random(0, width-25);  
      yDest = random(0, height-25);
    }

    if (xDest < xPos) {
      xPos -= 5;
    }
    else {
      xPos += 5;
    }

    if (yDest < yPos) {
      yPos -= 3;
    }
    else {
      yPos += 3;
    }
  }
}

class PulsatingSquare {
  //character globals-position & speed
  float xPos;
  float yPos;
  float xPosMove;
  float yPosMove;
  float accel = .07f;
  float widthAndHeight = 1.0f;//by default it will start at 1.0
  float increase = 0.5f;
  float xDest, yDest;



  //=================================================
  PulsatingSquare() {
    randomPlacement();
  }

  //=================================================
  //displays the pullsating square on canvas
  public void display() {
    //pulsing ellipse
    if (widthAndHeight >= 1) {
      widthAndHeight += increase;
      fill(190, 20, 20);
      strokeWeight(3);
      stroke(255, 0, 0);
      rect(xPos, yPos, widthAndHeight, widthAndHeight);
      noStroke();
      if (widthAndHeight >= 20) {
        widthAndHeight = 1;//shrink square
      }
    }
    xPos += xPosMove;
    yPos += yPosMove;
    wrapAround();
  }

  //==========================================================
  //The following methods are for character movement
  //*NOTE: the acceleration idea came from DAY03 lecture
  public void moveRight() {
    if (xPosMove < 10) {//to set the speed limit
      xPosMove += accel;
    }
  }

  public void moveLeft() {
    if (xPosMove > -10) {//to set the speed limit
      xPosMove -= accel;
    }
  }

  public void moveUp() {
    if (yPosMove > -15) {//to set the speed limit
      yPosMove -= accel;
    }
  }

  public void moveDown() {
    if (yPosMove < 15) {//to set the speed limit
      yPosMove += accel;
    }
  }

  //==================================================
  //resets the pulsating square
  public void reset() {
    xPosMove = 0;
    yPosMove = 0;
  }

  //=====================================================
  //randomly places character around canvas based on chance
  public void randomPlacement() {
    float chance = random(0, 1);

    if (chance <= .25f) {//bottom
      xPos = width/2;
      yPos = height -10;//not exactly at the edge
    }
    else if (chance > .25f && chance <= .5f) {//top
      xPos = width/2;
      yPos = 10;//not exactly at the edge
    }
    else if (chance > .5f && chance <= .75f) {//left
      xPos = 10;
      yPos = height -10;//not exactly at the edge
    }
    else {
      xPos = width-10;
      yPos = height-10;//not exactly at the edge
    }
  }

  public void wrapAround() {
    //wraparound for predator
    if (xPos>width) {
      xPos = 0;
    }
    else if (yPos>height) {
      yPos = 0;
    }
    else if (yPos < 0) {
      yPos = height;
    }
    else if (xPos < 0) {
      xPos = width;
    }
  }

  //===============================================
  //adjusts the acceleration of the character
  public void adjustAccel(float a) {
    accel = a;
  }

  //=================================================
  //randomly moves character around canvas
  public void move() {
    // have we arrived at this position?
    if (dist(xPos, yPos, xDest, yDest) < 25) {
      // pick a new destination
      xDest = random(0, width-25);  
      yDest = random(0, height-25);
    }

    if (xDest < xPos) {
      xPos -= 5;
    }
    else {
      xPos += 5;
    }

    if (yDest < yPos) {
      yPos -= 3;
    }
    else {
      yPos += 3;
    }
  }
}

  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--full-screen", "--bgcolor=#666666", "--stop-color=#cccccc", "assignment3" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}

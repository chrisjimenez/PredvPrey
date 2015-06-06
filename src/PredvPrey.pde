/**
*  PrevPrey.pde
*  By: Chris Jimenez
*/


import ddf.minim.spi.*;
import ddf.minim.signals.*;
import ddf.minim.*;
import ddf.minim.analysis.*;
import ddf.minim.ugens.*;
import ddf.minim.effects.*;

Minim minim;
AudioPlayer collisionSound1;
AudioPlayer collisionSound2;

//  score
int score;  
int startTime;
int currentTime;
int displayTime;
int count= 120;

//  INITIAL state
int state = 0;

ColorQuadrillateral predator;
PulsatingCircle prey;
PulsatingSquare redEnemy;

//  easy,medium,hard buttons
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

/**
*  Setup the game
*/
void setup() {
  size(500, 500);
  frameRate(30);

  //  set up Minim  
  minim = new Minim(this);

  // load in audio file
  collisionSound1 = minim.loadFile("assets/crash-sound.ogg");
  collisionSound2 = minim.loadFile("assets/thump-sound.ogg");

  //  create predator
  predator = new ColorQuadrillateral();
  
  //  create prey
  prey = new PulsatingCircle(predator.xPos, predator.yPos);
  
  //  create red enemy
  redEnemy = new PulsatingSquare();

  //  load button images
  easyButtonImage = loadImage("assets/easybutton.png");
  easyButtonPressedImage = loadImage("assets/easybutton-pressed.png");
  mediumButtonImage = loadImage("assets/mediumbutton.png");
  mediumButtonPressedImage = loadImage("assets/mediumbutton-pressed.png");
  hardButtonImage = loadImage("assets/hardbutton.png");
  hardButtonPressedImage = loadImage("assets/hardbutton-pressed.png");

  //  create button objects
  easyButton = new ImageButton(easyButtonImage, easyButtonPressedImage);
  mediumButton = new ImageButton(mediumButtonImage, mediumButtonPressedImage);
  hardButton = new ImageButton(hardButtonImage, hardButtonPressedImage);
}

/**
*
*/
void draw() { 
  smooth();
  background(0);
  fill(255);

  //  space travelling simulation!
  for (int i = 0; i < 20; i++) {
    ellipse(random(width), random(height), 2, 2);
  }
  
  //  states correspond to different levels
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

/**
*  Displays the start screen or title screen
*/
void startGame() {

  //  Place characters in the background of start screen
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
  text("--> for beginners.", 210, 180);
  mediumButton.resizeButton(100, 50);
  mediumButton.display(100, 250);
  text("--> A little bit faster.", 210, 280);
  hardButton.resizeButton(100, 50);
  hardButton.display(100, 350);
  text("--> You're not the only predator.", 210, 380);

  //  Display title
  textFont(createFont("Orbitron", 36));
  text("Predator v. Prey", 75, 100);

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

/**
*  Set up easy game, which is the base for the other levels
*/
void easyGame() {
  playGame();
}

/**
*  Set up medium game, adjusting accel of both prey + predator
*/
void mediumGame() {
  prey.adjustAccel(0.3);
  predator.adjustAccel(0.8);
  playGame();
}

/**
*  Set up medium game, adjusting accel of both prey + predator
*/
void hardGame() {
  prey.adjustAccel(0.3);
  predator.adjustAccel(0.8);
  displayFadingText("DONT TOUCH THE RED SQUARE!", 70, 250);
  playGame();
  checkCollision(redEnemy, predator);
  redEnemy.move();
  redEnemy.display();
}

/**
*  Displays everything on to the sketch
*/
void playGame() {
  displayFadingText("CHASE THE CIRCLE!", 130, 200);

  predator.display();
  prey.display();

  displayTimeAndScore();
  displayCommands();

  gameControls();

  checkCollision(prey, predator);
}

/**
*  Resets the game.
*/
void reset() {
  //initialize position of the predator & prey
  predator.reset();
  prey.reset();
  prey.adjustPosition(prey.xPos+300, prey.yPos+300);

  //reset start time
  startTime = (int)(millis()/1000);
}


/**
*  Displays fading text on to the sketch
*  s = Text being displayed
*  x,y = xy position of text
*/
void displayFadingText(String s, float x, float y) {
  fill(255, 255, 255, count--);
  textSize(25);//slightlty larger text size
  text(s, x, y);
  textSize(15);//return text size back to normal
}

/**
*  Set up medium game, adjusting accel of both prey + predator
*/
void displayTimeAndScore() {
  //time & score
  fill(255);
  currentTime = (int)(millis()/1000);
  displayTime = currentTime - startTime;
  text("TIME: " + displayTime, 0, 20);
  text("SCORE: " + score, 0, 35);
}

//==============================================================
//check if there is a collision between the two characters
void checkCollision(PulsatingSquare ps, ColorQuadrillateral cq) {
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
void checkCollision(PulsatingCircle pc, ColorQuadrillateral cq) {
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
void gameControls() {
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
void displayCommands() {
  fill(255);
  textSize(10);
  text("TO GO BACK TO MENU, PRESS 'Q'", 340, 15);
  text("TO RESET GAME, PRESS 'R'", 340, 30);
}




//this is the class for a quadrillateral that is made using
//four triangles, where each individual triangle has its
//color but all share same vertex points
class ColorQuadrillateral {
  //character globals-position & speed
  float xPos;
  float yPos;
  float xPosMove;
  float yPosMove;
  float accel = .2;//BY DEFAULT THE ACCELERATION IS 0.5
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
  void drawQuad() {
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
  void characterPlacement() {
    float chance = random(0, 1);

    if (chance <= .25) {//bottom
      xPos = width/2;
      yPos = height -10;//not exactly at the edge
    }
    else if (chance > .25 && chance <= .5) {//top
      xPos = width/2;
      yPos = 10;//not exactly at the edge
    }
    else if (chance > .5 && chance <= .75) {//left
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
  void display() {
    drawQuad();
    xPos += xPosMove;
    yPos += yPosMove;

    permitWrapAround();
  }

  //======================================================
  //The following methods are for character movement
  //*NOTE: the acceleration idea came from DAY03 lecture
  void moveRight() {
    if (xPosMove < 10) {//to set the speed limit
      xPosMove += accel;
    }
  }

  void moveLeft() {
    if (xPosMove > -10) {//to set the speed limit
      xPosMove -= accel;
    }
  }

  void moveUp() {
    if (yPosMove > -10) {//to set the speed limit
      yPosMove -= accel;
    }
  }

  void moveDown() {
    if (yPosMove < 10) {//to set the speed limit
      yPosMove += accel;
    }
  }

  //=====================================================
  //resets the position of the character
  void reset() {
    xDest = random(25, width-25);
    yDest = random(25, height-25);
    xPosMove = 0;
    yPosMove = 0;
    randomPlacement();

  }

  //===========================================================
  //method that places the predator at the edge of the screen
  //location based on chance 
  void randomPlacement() {
    float chance = random(0, 1);

    if (chance <= .25) {//bottom
      xPos = width/2;
      yPos = height -10;//not exactly at the edge
    }
    else if (chance > .25 && chance <= .5) {//top
      xPos = width/2;
      yPos = 10;//not exactly at the edge
    }
    else if (chance > .5 && chance <= .75) {//left
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
  void permitWrapAround() {
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
  boolean detectCollision(float x, float y) {
    if (dist(xPos, yPos, x, y) < 30) {
      return true;
    }
    else {
      return false;
    }
  }

  //=================================================
  //adjust the acceleration of the character
  void adjustAccel(float a) {
    accel = a;
  }


  //=================================================
  //Randomy moving AI
  //*NOTE: method idea came from lecture
  void move() {
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


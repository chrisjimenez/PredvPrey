class PulsatingCircle {
  //character globals-position & speed
  float xPos;
  float yPos;
  float xPosMove;
  float yPosMove;
  float xDest, yDest;
  float predXPos, predYPos;
  float accel = .05;
  float radius = 1.0;//by default it will start at 1.0
  float radiusIncrease = 0.5;


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
  void display() {
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
    if (yPosMove > -15) {//to set the speed limit
      yPosMove -= accel;
    }
  }

  void moveDown() {
    if (yPosMove < 15) {//to set the speed limit
      yPosMove += accel;
    }
  }

  //==================================================
  //resets character at different position
  void reset() {
    xPosMove = 0;
    yPosMove = 0;
  }

  //randomly places character on the canvas
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

  //==============================================
  //permits wrap around for the character
  void wrapAround() {
    
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
  void adjustAccel(float a) {
    accel = a;
  }
  
  //=============================================
  //adjusts acceleration of the character
  void adjustPosition(float x, float y) {
    xPos = x;
    yPos = y;
  }
  
  
  
  //===============================================
  //randomly moves the charcter around the canvas
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


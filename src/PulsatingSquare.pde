class PulsatingSquare {
  //character globals-position & speed
  float xPos;
  float yPos;
  float xPosMove;
  float yPosMove;
  float accel = .07;
  float widthAndHeight = 1.0;//by default it will start at 1.0
  float increase = 0.5;
  float xDest, yDest;



  //=================================================
  PulsatingSquare() {
    randomPlacement();
  }

  //=================================================
  //displays the pullsating square on canvas
  void display() {
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
  //resets the pulsating square
  void reset() {
    xPosMove = 0;
    yPosMove = 0;
  }

  //=====================================================
  //randomly places character around canvas based on chance
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

  void wrapAround() {
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
  void adjustAccel(float a) {
    accel = a;
  }

  //=================================================
  //randomly moves character around canvas
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


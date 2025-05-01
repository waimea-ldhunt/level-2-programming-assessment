# Plan for Testing the Program

The test plan lays out the actions and data I will use to test the functionality of my program.

Terminology:

- **VALID** data values are those that the program expects
- **BOUNDARY** data values are at the limits of the valid range
- **INVALID** data values are those that the program should reject

---

## Setup Test

Testing that the house is set up correctly, the grid is full and the correct size and checking that the fire seed is
placed correctly/within the specified location.

### Test Data To Use

Running the code multiple times to check for incorrect setup.

### Expected Test Result

The house should appear, conforming to the specified Width and Height, with a few walls that do not block off any areas
and a fire seed that is able to spread the fire.

---

## Fire Spread Test

Checking that the fire spreads correctly and is blocked by walls.
Only intensity 3 fire "INTENSE" should spread the fire, fire should not spread onto walls or tiles that are already on
fire, Fire should also spread to tiles in all directions (onto any SAFE tile touching INTENSE fire).

### Test Data To Use

Running the code multiple times to check that the fire is spreading in the correct direction/pattern.

### Expected Test Result

The fire should spread in all directions, always stopping at walls and not igniting existing fire.

---

## Boundary Testing

Testing to make sure that the players are unable to move outside the playable area (error prevention).

### Test Data To Use

Running multiple times and intentionally attempting to move outside the boundary on all sides.

### Expected Test Result

Player should not move and should instead be told that that movement is invalid (given a reason), moves should also not
be deducted for invalid movements.

---

## Invalid Inputs

The program should account for invalid inputs and handle them in a way that allows the user to try again.

### Test Data To Use

Testing valid and invalid inputs for each section that requires player input.

### Expected Test Result

If the input is invalid, the program should alert the player and allow them to try again, Otherwise the program should
continue as normal.

---

## Win Conditions

There are three win conditions:
One of the players is engulfed by fire, in that case, the other player wins.
Both of the players are engulfed by fire at the same time, no players win.
The fire can no longer spread, both players win.

### Test Data To Use

Play testing for each scenario (running the program multiple times).

### Expected Test Result

Each win condition/scenario should end the game, reveal the winning player/s and show the number of rounds survived.

---

## Player Actions / Turns

Player should be able to perform actions correctly and turns should be organised and shown correctly.

### Test Data To Use

Running and play testing the program multiple times to investigate weather or not the program handles player turns and
actions correctly.

### Expected Test Result

Player should have correctly ordered turns:
The players can move a number of times specified by the MOVES value
At the end of Player 2's turn, the fire should spread a number of tiles specified by the fireSpeed variable.
Between these actions, the house should be printed to show progress.

---

## Player Movement and Obstacle Checking

Players should be able to move one tile in any direction that is not blocked by a wall or the fire.

### Test Data To Use

Running the program multiple times to test movement and blocking.
Testing attempting to move into walls from multiple directions and moving into fire.

### Expected Test Result

Player should move exactly one tile in the desired direction unless they are blocked by a wall or fire.

---

## Fire Extinguisher

The fire extinguisher should work as intended and extinguish the fire correctly.

### Test Data To Use

Running the program multiple times in order to test whether the fire extinguisher works correctly.

### Expected Test Result

The fire extinguisher should convert all tiles within a specified distance of the player into SAFE tiles,
removing all fire.

---
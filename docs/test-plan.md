# Plan for Testing the Program

The test plan lays out the actions and data I will use to test the functionality of my program.

Terminology:

- **VALID** data values are those that the program expects
- **BOUNDARY** data values are at the limits of the valid range
- **INVALID** data values are those that the program should reject

---

## Setup Test

Testing that the house is set up correctly, the grid is full and the correct size and checking that the fire seed is
placed correctly/in a valid location.

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

The program should account for invalid inputs.

### Test Data To Use

Testing valid and invalid inputs for each section that requires player input.

### Expected Test Result

If the input is invalid, the program should alert the player and allow them to try again, Otherwise the program should
continue as normal.

---

## Example Test Name

Example test description. Example test description. Example test description. Example test description. Example test
description. Example test description.

### Test Data To Use

Details of test data and reasons for selection. Details of test data and reasons for selection. Details of test data and
reasons for selection.

### Expected Test Result

Statement detailing what should happen. Statement detailing what should happen. Statement detailing what should happen.
Statement detailing what should happen.

---

## Example Test Name

Example test description. Example test description. Example test description. Example test description. Example test
description. Example test description.

### Test Data To Use

Details of test data and reasons for selection. Details of test data and reasons for selection. Details of test data and
reasons for selection.

### Expected Test Result

Statement detailing what should happen. Statement detailing what should happen. Statement detailing what should happen.
Statement detailing what should happen.

---

## Example Test Name

Example test description. Example test description. Example test description. Example test description. Example test
description. Example test description.

### Test Data To Use

Details of test data and reasons for selection. Details of test data and reasons for selection. Details of test data and
reasons for selection.

### Expected Test Result

Statement detailing what should happen. Statement detailing what should happen. Statement detailing what should happen.
Statement detailing what should happen.

---
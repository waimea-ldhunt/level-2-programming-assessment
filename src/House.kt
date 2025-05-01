import kotlin.random.Random
import kotlin.system.exitProcess

/**
 * House.kt
 * This file is where all the house/fire functions are.
 * These files all alter or use the House or Fire lists.
 *
 * This file contains the following functions:
 *
 * setupHouse
 * placeFireSeed
 * placeFireExtinguisher
 * showHouse
 *
 * stepFire
 * checkAdjacent
 * extinguishFire
 * checkEmpty
 *
 * SETUP FUNCTIONS-----------------------------------------------------------------------------------------------------|
 *
 * House Setup:
 * Fills the house and fire lists with empty tiles, builds the walls and places both players.
 */
fun setupHouse(
    house: MutableList<MutableList<String>>,
    fire: MutableList<MutableList<Int>>,
) {

    //builds two mutable lists of desired size (height * width), one containing empty tiles (string) and the other containing safe tiles (int)
    repeat(HOUSE_HEIGHT) {
        val buildHouseRow = mutableListOf<String>() //creates a row variable of strings
        val buildfireRow = mutableListOf<Int>() //creates a row variable of integers
        repeat(HOUSE_WIDTH) {
            buildHouseRow.add(EMPTY) //adds an "EMPTY" tile to the row
            buildfireRow.add(SAFE) //adds a "SAFE" tile to the row
        }
        house.add(buildHouseRow) //adds the house row as an item to the house list
        fire.add(buildfireRow) //adds the fire row as an item to the fire list
    }

    var recentWall = false
    for (row in 1..<house.size) {
        for (tile in 1..<house[row].size) {

            if (recentWall) { //if a wall was placed previously use wall continue chance (more likely)
                if (Random.nextInt(1, WALL_CONTINUE_CHANCE) == 1) {
                    house[row][tile] = WALL
                    recentWall = true
                } else recentWall = false
            } else if (Random.nextInt(
                    1,
                    WALL_SEED_CHANCE
                ) == 1
            ) { //if a wall was not placed previously use wall start chance (less likely)
                house[row][tile] = WALL
                recentWall = true
            }
        }
    }

    //place players in their designated start positions
    house[0][1] = PLAYER1
    house[1][0] = PLAYER2
}

/**
 * Fire Seed:
 * Places the start of the fire.
 * gets a random tile, within a specified area and removes any walls that might be blocking it.
 */
fun placeFireSeed(house: MutableList<MutableList<String>>, fire: MutableList<MutableList<Int>>) {
    val fireSeedLocation = mutableListOf( //creates a list of (X, Y)
        Random.nextInt(HOUSE_WIDTH - 5, HOUSE_WIDTH - 1), //random X value
        Random.nextInt(HOUSE_HEIGHT - 5, HOUSE_HEIGHT - 1) //random Y value
    )

    //removes walls if there are any
    house[fireSeedLocation[1]][fireSeedLocation[0]] = EMPTY
    house[fireSeedLocation[1] - 1][fireSeedLocation[0]] = EMPTY
    house[fireSeedLocation[1]][fireSeedLocation[0] - 1] = EMPTY

    fire[fireSeedLocation[1]][fireSeedLocation[0]] = INTENSE //sets fire intensity of tile to spreadable
}

/**
 * Fire Extinguisher:
 * Places the fire extinguisher where valid:
 *
 * Gets a random location within the playable area
 * Checks if the tile is not on fire
 *  else, it tries again.
 * Checks if the tile is empty
 *  else, it tries again.
 * if tile is valid, places fire extinguisher
 *
 */
fun placeFireExtinguisher(house: MutableList<MutableList<String>>, fire: MutableList<MutableList<Int>>) {
    while (true) {
        val fireExtinguisherLocation = mutableListOf( //creates a list of (X, Y)
            Random.nextInt(0, HOUSE_WIDTH), //random X value
            Random.nextInt(0, HOUSE_HEIGHT) //random Y value
        )

        if (fire[fireExtinguisherLocation[1]][fireExtinguisherLocation[0]] == SAFE) { //if tile is not on fire

            if (house[fireExtinguisherLocation[1]][fireExtinguisherLocation[0]] == EMPTY) { //if tile is not player or wall
                house[fireExtinguisherLocation[1]][fireExtinguisherLocation[0]] =
                    FIRE_EXTINGUISHER //place fire extinguisher
                break
            }
        }
    }
}

/**
 * Show House:
 * Prints the house array, colouring using tile types and the fire array.
 * In the format:
 *
 * Top border
 *
 * Side border
 * individual row
 * Side border
 * (repeated a number of times equal to the HOUSE_HEIGHT variable)
 *
 * Bottom Border
 */
fun showHouse(
    house: MutableList<MutableList<String>>,
    fire: MutableList<MutableList<Int>>,
    player1Tile: String,
    player2Tile: String
) {
    println()
    println("$BORDER_WALL ".cyan().repeat(HOUSE_WIDTH + 2)) //prints the top border

    for (row in 0..<house.size) {

        print("$BORDER_WALL ".cyan()) //prints the border at the start of the row

        for (tile in 0..<house[row].size) {

            if (fire[row][tile] > SAFE) { //if the tile is on fire, colour tile by fire intensity
                when (fire[row][tile]) {
                    INTENSE -> print(house[row][tile].col(225, 225, 0))
                    MEDIUM -> print(house[row][tile].col(255, 150, 25))
                    WEAK -> print(house[row][tile].red())
                }
            } else { //otherwise colour tile buy tile type

                when (house[row][tile]) {
                    WALL -> print(house[row][tile].cyan())
                    FIRE_EXTINGUISHER -> print(house[row][tile].magenta())
                    PLAYER1 -> print(player1Tile.blue())
                    PLAYER2 -> print(player2Tile.green())
                    EMPTY -> print(house[row][tile].grey())
                }
            }
        }

        println(BORDER_WALL.cyan()) //prints the border at the end of the row
    }

    print("$BORDER_WALL ".cyan().repeat(HOUSE_WIDTH + 2)) //prints the bottom border
    println()
}

/**
 * FIRE FUNCTIONS -----------------------------------------------------------------------------------------------------|
 *
 * Step Fire:
 * Checks each tile for adjacent fire (using checkAdjacent) and if there is puts them in a list, Steps through each fire stage of existing fire and then
 * sets the tiles in the created list on fire.
 */
fun stepFire(house: MutableList<MutableList<String>>, fire: MutableList<MutableList<Int>>) {
    val burnTiles = mutableListOf<MutableList<Int>>()
    for (row in 0..<fire.size) { //for each row
        for (tile in 0..<fire[row].size) { //for each tile in each row

            if (fire[row][tile] == SAFE && house[row][tile] != WALL && checkAdjacent(
                    row,
                    tile,
                    fire
                )
            ) { //if tile is not wall and is not already on fire, run checkAdjacent and if true,
                burnTiles.add(mutableListOf(row, tile)) //add tile to burn list
                //burnTiles is a list used to make sure tiles are ignited after every tile is checked.
            }

        }
    }

    for (row in 0..<fire.size) { //for each row
        for (tile in 0..<fire[row].size) { //for each tile in each row

            if (fire[row][tile] > SAFE) {
                fire[row][tile] -= 1 //steps each fire tile to the next stage
            }
        }
    }

    for (targetTile in burnTiles) { //for each tile in the list
        fire[targetTile[0]][targetTile[1]] = INTENSE //sets the fire value of the tile to intense

        if (house[targetTile[0]][targetTile[1]] == FIRE_EXTINGUISHER) { //if the tile is a fire extinguisher
            house[targetTile[0]][targetTile[1]] = EMPTY //removes fire extinguisher
            placeFireExtinguisher(
                house,
                fire
            ) //places a new fire extinguisher so that there is always the same number of fire extinguishers in play.
        }
    }
}

/**
 * Check Adjacent:
 * Checks each adjacent and if any have INTENSE fire, returns true.
 * Checks directions in order:
 *
 * Above
 * Below
 * Left
 * Right
 *
 * If none have any INTENSE fire, returns false
 */
fun checkAdjacent(row: Int, tile: Int, fire: MutableList<MutableList<Int>>): Boolean {
    if (row > 0 && fire[row - 1][tile] == INTENSE) //if not outside border, checks tile above for intense fire
        return true

    if (row < HOUSE_HEIGHT - 1 && fire[row + 1][tile] == INTENSE) //if not outside border, checks tile below for intense fire
        return true

    if (tile > 0 && fire[row][tile - 1] == INTENSE) //if not outside border, checks tile to the left for intense fire
        return true

    if (tile < HOUSE_WIDTH - 1 && fire[row][tile + 1] == INTENSE)//if not outside border, checks tile to the right for intense fire
        return true

    return false //otherwise, returns false (tile should not ignite)
}

/**
 * Extinguish Fire:
 * Performs necessary checks and extinguishes fire accordingly.
 * Sets the fire value of every tile within the area specified by the EXTINGUISHER_STRENGTH value to SAFE.
 */
fun extinguishFire(fire: MutableList<MutableList<Int>>, location: MutableList<Int>) {
    for (row in location[0] - EXTINGUISHER_STRENGTH..location[0] + EXTINGUISHER_STRENGTH) { //for each row within two rows of the player (vertical component)
        for (tile in location[1] - EXTINGUISHER_STRENGTH..location[1] + EXTINGUISHER_STRENGTH) { //for each tile within two tiles of the player (horizontal component)
            if (row in 0..<HOUSE_HEIGHT) { //if row is not outside the boundary
                if (tile in 0..<HOUSE_WIDTH) { //if row is not outside the boundary
                    fire[row][tile] = SAFE //set fire value of tile to zero
                }
            }
        }
    }
}

/**
 * Check Empty:
 * Checks if there is no more INTENSE fire in the house, and ends accordingly.
 */
fun checkEmpty(fire: MutableList<MutableList<Int>>, rounds: Int) {

    var intenseFireCount = 0
    for (row in 0..<fire.size) { //checks each row
        for (tile in 0..<fire[row].size) {//checks each tile of each row
            if (fire[row][tile] == INTENSE) { //check if fire on tile is intense/spreadable
                intenseFireCount++ //updates count
            }
        }
    }

    if (intenseFireCount == 0) { //checks if fire cannot continue spreading
        println()
        println("# Fire Extinguished!")
        println("# You escaped after $rounds rounds.")
        exitProcess(0)
    }
}
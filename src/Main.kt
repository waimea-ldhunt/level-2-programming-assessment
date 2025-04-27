import kotlin.random.Random
import kotlin.system.exitProcess

/**
 * ===================================================================================*
 * Programming Project for NCEA Level 2, Standard 91896
 * -----------------------------------------------------------------------------------*
 * Project Name:   NAME
 * Project Author: Lachlan Hunt
 * GitHub Repo:    https://github.com/waimea-ldhunt/level-2-programming-assessment
 * -----------------------------------------------------------------------------------*
 * Notes:
 *
 *
 *
 * ===================================================================================*
 */

const val HOUSE_WIDTH = 14 //Width of the playable area
const val HOUSE_HEIGHT = 10 //Height of the playable area
const val WALL_SEED_CHANCE = 9 //Chance that a tile will become a wall (1 in X)
const val WALL_CONTINUE_CHANCE = 3 //Chance that a wall will extend (1 in X)
const val BORDER_WALL = "#" //Border Character
const val EMPTY = "+ " //Empty tile
const val WALL = "# " //Wall Tile
const val FIRE_EXTINGUISHER = "& " //Fire Extinguisher Tile
const val PLAYER1 = "1 " //Player Tile
const val PLAYER2 = "2 " //Player Tile
const val INTENSE = 3 //Max Fire Level (Spreads fire)
const val MEDIUM = 2 //fire level 2 (does not spread)
const val WEAK = 1 //fire level 1 (does not spread)
const val SAFE = 0 //No fire on the tile
const val MOVES = 2 //Number of movements each player gets per round

/**
 * Main function:
 * Contains game loop and variable setup.
 */
fun main() {

    val house = mutableListOf<MutableList<String>>() //The 2-dimensional grid that is shown in the showGrid() function
    val fire =
        mutableListOf<MutableList<Int>>() //The state of the tile, 0 = Not on fire, >0 = On fire, Numbers show the state of the fire.
    val difficulty: Int //The difficulty of the game
    val player1Location = mutableListOf(0, 1) //Setting starting location of Player 1
    val player2Location = mutableListOf(1, 0) //Setting starting location of Player 2
    val playerFireExtinguishers = mutableListOf(0, 0) //Setting the number of fire extinguishers each player has
    var rounds = 0 //Number of rounds that have passes
    var remainingMoves: Int // Number of

    setupHouse(house, fire) //starts the house setup function
    placeFireSeed(house, fire) //Places the beginning of the fire

    while (true) {
        println()
        println("# Welcome to GAME #")
        println("1: Play")
        println("2: How to Play")
        print("> ")

        when (readlnOrNull()) { //Asks player if they would like to learn how to play or start
            "1" -> break

            "2" -> {
                println()
                println("# Each player will take turns moving twice using [W][A][S][D][skip]".padEnd(68) + "#")
                println("# If you pick up a Fire Extinguisher (&) you may use it with [F]".padEnd(68) + "#")
                println("# Your goal is to be the last player standing".padEnd(68) + "#")
                println("# Each difficulty changes game rules:".padEnd(68) + "#")
                println("# Easy:   Fire is slow".padEnd(68) + "#")
                println("# Normal: Fire is fast".padEnd(68) + "#")
                println("# Hard:   Fire is fast + only one fire extinguisher".padEnd(68) + "#")
                println()
                println("1: Play")
                println("2: Quit")
                print("> ")
                when (readlnOrNull()) { //asks for play or quit
                    "1" -> break //ends loop (continues game)
                    "2" -> exitProcess(0) //ends process
                    else -> {
                        println()
                        println("Invalid Input")
                        Thread.sleep(1000)
                        println()
                        continue //resets if the input is invalid (else)
                    }
                }
            }

            else -> {
                println()
                println("Invalid Input")
                Thread.sleep(1000)
                println()
                continue //resets if the input is invalid (else)
            }
        }
    }

    gameLoop@ while (true) {

        println()
        println("# Choose a Difficulty:".padEnd(23) + "#")
        println("1: Easy")
        println("2: Normal")
        println("3: Hard")
        print("> ")

        difficulty = when (readlnOrNull()) { // asks for difficulty then sets difficulty
            "1" -> 1
            "2" -> 2
            "3" -> 3
            else -> {
                println()
                println("# Invalid input #")
                Thread.sleep(1000)
                println()
                continue //resets if the input is invalid (else)
            }
        }

        val player1Name = getPlayerName(PLAYER1)
        val player2Name = getPlayerName(PLAYER2)
        val player1Tile = player1Name.first().uppercase() + " "
        val player2Tile = player2Name.first().uppercase() + " "

        var fireSpeed = 2
        var fireExtinguisherCount = 2

        when (difficulty) { //Updates values based on difficulty
            1 -> {
                fireSpeed = 1 //reduces number of times the fire moves
            }

            3 -> {
                fireExtinguisherCount = 1 //reduces the number of fire extinguishers that spawn
            }
        }

        repeat(2) {
            stepFire(house, fire) //steps fire once
            Thread.sleep(1000)
            showHouse(house, fire, player1Tile, player2Tile) //shows house
        }

        repeat(fireExtinguisherCount) { placeFireExtinguisher(house, fire) } //places fire extinguishers based on count
        Thread.sleep(1000)
        showHouse(house, fire, player1Tile, player2Tile) //shows house

        roundLoop@ while (true) {

            remainingMoves = MOVES
            repeat(MOVES) { //Player 1's turns (repeats a number of times equal to the move count "MOVES")
                playerTurn(
                    house,
                    fire,
                    PLAYER1,
                    player1Location,
                    playerFireExtinguishers,
                    remainingMoves,
                    player1Name,
                    player1Tile,
                    player2Tile
                )
                showHouse(house, fire, player1Tile, player2Tile)
                remainingMoves--
            }

            remainingMoves = MOVES
            repeat(MOVES) { //Player 2's turns (repeats a number of times equal to the move count "MOVES")
                playerTurn(
                    house,
                    fire,
                    PLAYER2,
                    player2Location,
                    playerFireExtinguishers,
                    remainingMoves,
                    player2Name,
                    player1Tile,
                    player2Tile
                )
                showHouse(house, fire, player1Tile, player2Tile)
                remainingMoves--
            }

            checkEmpty(fire, rounds) //checks if there is no INTENSE fire remaining and then ends game if so

            repeat(fireSpeed) {
                stepFire(house, fire) //steps the fire
                showHouse(house, fire, player1Tile, player2Tile) //shows the house
                Thread.sleep(1000)

                if (fire[player1Location[0]][player1Location[1]] > 0 && fire[player2Location[0]][player2Location[1]] > 0) { //checks if both players are in the fire and ends accordingly
                    println()
                    println("# No Survivors".padEnd(29) + "#")
                    println("# You survived for $rounds rounds".padEnd(29) + "#")
                    exitProcess(0)
                } else {
                    checkPlayer(
                        fire,
                        PLAYER1,
                        player1Name,
                        player2Name,
                        player1Location,
                        rounds
                    ) //checks if Player 1 is in the fire and ends accordingly
                    checkPlayer(
                        fire,
                        PLAYER2,
                        player1Name,
                        player2Name,
                        player2Location,
                        rounds
                    ) //checks if Player 2 is in the fire and ends accordingly
                }
            }

            rounds += 1
        }
    }
}

/**
 * Player Turn:
 * Communicates with player and gives action prompt.
 */
fun playerTurn(
    house: MutableList<MutableList<String>>,
    fire: MutableList<MutableList<Int>>,
    player: String,
    location: MutableList<Int>,
    playerFireExtinguishers: MutableList<Int>,
    remainingMoves: Int,
    playerName: String,
    player1Tile: String,
    player2Tile: String
) {
    turnLoop@ while (true) {
        println()
        if (player == PLAYER1) { //Prints turn text based on player
            println("# It is your turn ${playerName.blue()} ".padEnd(32 - (18 + playerName.length)) + "#")
            println("# You have ${playerFireExtinguishers[0]} Fire Extinguishers".padEnd(32) + "#") //number of fire extinguishers p1 has
        }
        if (player == PLAYER2) { //Prints turn text based on player
            println("# It is your turn ${playerName.green()}" + "".padEnd(7) + "#")
            println("# You have ${playerFireExtinguishers[1]} Fire Extinguishers".padEnd(32) + "#") //number of fire extinguishers p2 has
        }
        println("# You have $remainingMoves moves remaining".padEnd(32) + "#")
        print("> ")
        when (readlnOrNull()?.uppercase()) { //get player input
            "W" -> {
                if (location[0] > 0) { //checks if the player is not on the top edge of the grid and if so, stops it
                    if (movePlayer(house, fire, player, location, -1, 0, playerFireExtinguishers)) break@turnLoop
                    else continue@turnLoop //resets turn
                } else {
                    println()
                    println("# That Movement Is Blocked by a Wall #") //gives a reason for the prevention (edge)
                    Thread.sleep(1000)
                    continue@turnLoop //resets turn
                }
            }

            "A" -> { //checks if the player is not on the left edge of the grid and if so, stops it
                if (location[1] > 0) {
                    if (movePlayer(house, fire, player, location, 0, -1, playerFireExtinguishers)) break@turnLoop
                    else continue@turnLoop //resets turn
                } else {
                    println()
                    println("# That Movement Is Blocked by a Wall #") //gives a reason for the prevention (edge)
                    Thread.sleep(1000)
                    continue@turnLoop //resets turn
                }
            }

            "S" -> { //checks if the player is not on the bottom edge of the grid and if so, stops it
                if (location[0] < HOUSE_HEIGHT - 1) {
                    if (movePlayer(house, fire, player, location, 1, 0, playerFireExtinguishers)) break@turnLoop
                    else continue@turnLoop //resets turn
                } else {
                    println()
                    println("# That Movement Is Blocked by a Wall #") //gives a reason for the prevention (edge)
                    Thread.sleep(1000)
                    continue@turnLoop //resets turn
                }
            }

            "D" -> { //checks if the player is not on the right edge of the grid and if so, stops it
                if (location[1] < HOUSE_WIDTH - 1) {
                    if (movePlayer(house, fire, player, location, 0, 1, playerFireExtinguishers)) break@turnLoop
                    else continue@turnLoop //resets turn
                } else {
                    println()
                    println("# That Movement Is Blocked by a Wall #") //gives a reason for the prevention (edge)
                    Thread.sleep(1000)
                    continue@turnLoop //resets turn
                }
            }

            "F" -> {
                if (player == PLAYER1) {
                    if (playerFireExtinguishers[0] > 0) {
                        extinguishFire(fire, location)
                        playerFireExtinguishers[0] -= 1
                        placeFireExtinguisher(house, fire)
                        showHouse(house, fire, player1Tile, player2Tile)
                        continue@turnLoop //continues to next turn
                    } else {
                        println()
                        println("# You do not have any Fire Extinguishers #") //gives a reason for the prevention (no item)
                        Thread.sleep(1000)
                        continue@turnLoop //resets turn
                    }
                } else if (player == PLAYER2) {
                    if (playerFireExtinguishers[1] > 0) {
                        extinguishFire(fire, location)
                        playerFireExtinguishers[1] -= 1
                        placeFireExtinguisher(house, fire)
                        showHouse(house, fire, player1Tile, player2Tile)
                        continue@turnLoop //resets turn
                    } else {
                        println()
                        println("# You do not have any Fire Extinguishers #") //gives a reason for the prevention (no item)
                        Thread.sleep(1000)
                        continue@turnLoop //resets turn
                    }
                }
            }

            "SKIP" -> break@turnLoop //immediately ends turn without moving the player

            else -> { //if input none of the above (invalid)
                println()
                println("# Invalid input #")
                Thread.sleep(1000)
                println()
                continue@turnLoop //resets turn if input is not valid
            }
        }
    }
}

/**
 * Extinguish Fire:
 * Performs necessary checks and extinguishes fire accordingly.
 */
fun extinguishFire(fire: MutableList<MutableList<Int>>, location: MutableList<Int>) {
    for (row in location[0] - 2..location[0] + 2) { //for each row within two rows of the player (vertical component)
        for (tile in location[1] - 2..location[1] + 2) { //for each tile within two tiles of the player (horizontal component)
            if (row in 0..<HOUSE_HEIGHT) { //if row is not outside the boundary
                if (tile in 0..<HOUSE_WIDTH) { //if row is not outside the boundary
                    fire[row][tile] = SAFE //set fire value of tile to zero
                }
            }
        }
    }
}

/**
 * Check Player:
 * Checks for individual player win scenarios.
 */
fun checkPlayer(
    fire: MutableList<MutableList<Int>>,
    player: String,
    player1Name: String,
    player2Name: String,
    location: MutableList<Int>,
    rounds: Int
) {
    if (fire[location[0]][location[1]] > 0) { //if tile is on fire
        if (player == PLAYER1) { //if player is PLAYER1, player 2 wins
            println()
            println("# ${player2Name.green()} Escapes!".padEnd(29) + "#")
            println("# You survived for $rounds rounds".padEnd(29) + "#")
            exitProcess(0)
        } else if (player == PLAYER2) { //if player is PLAYER2, player 1 wins
            println()
            println("# ${player1Name.blue()} Escapes!".padEnd(29) + "#")
            println("# You survived for $rounds rounds".padEnd(29) + "#")
            exitProcess(0)
        }
    }
}

/**
 * Move Player:
 * Performs necessary checks and moves player one tile if valid.
 */
fun movePlayer(
    house: MutableList<MutableList<String>>,
    fire: MutableList<MutableList<Int>>,
    player: String,
    location: MutableList<Int>,
    yChange: Int,
    xChange: Int,
    playerFireExtinguishers: MutableList<Int>
): Boolean {
    if (house[location[0] + yChange][location[1] + xChange] != WALL) {//if the movement is blocked by a wall

        if (fire[location[0] + yChange][location[1] + xChange] == 0) {//if the movement is blocked by fire

            if (house[location[0] + yChange][location[1] + xChange] != PLAYER1 && house[location[0] + yChange][location[1] + xChange] != PLAYER2) {//if the movement is blocked by another player
                house[location[0]][location[1]] = EMPTY
                if (yChange != 0) { //if movement is on the Y axis
                    location[0] += yChange //add Y change to intended location coordinates
                } else {
                    location[1] += xChange //add X change to intended location coordinates
                }
                if (house[location[0]][location[1]] == FIRE_EXTINGUISHER) { //if intended location contains a fire extinguisher give a fire extinguisher to the correct player
                    if (player == PLAYER1) playerFireExtinguishers[0] += 1
                    if (player == PLAYER2) playerFireExtinguishers[1] += 1
                }
                house[location[0]][location[1]] = player //place player in the new location
                return true
            } else {
                println()
                println("# That Movement Is Blocked by another player #") //gives a reason for the prevention (other player)
                Thread.sleep(1000)
                return false
            }
        } else {
            println()
            println("# That Movement Is Blocked by Fire #") //gives a reason for the prevention (fire)
            Thread.sleep(1000)
            return false
        }
    } else {
        println()
        println("# That Movement Is Blocked by a Wall #") //gives a reason for the prevention (wall)
        Thread.sleep(1000)
        return false
    }
}

/**
 * House Setup:
 * Fills the house and fire lists with empty tiles, builds the walls and places both players.
 */
fun setupHouse(house: MutableList<MutableList<String>>, fire: MutableList<MutableList<Int>>) {

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
 * Show House:
 * Prints the house array, colouring using tile types and the fire array.
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
 * Fire Seed:
 * Places the start of the fire.
 */
fun placeFireSeed(house: MutableList<MutableList<String>>, fire: MutableList<MutableList<Int>>) {
    val fireSeedLocation = mutableListOf( //creates a list of (X, Y)
        Random.nextInt(HOUSE_WIDTH - 5, HOUSE_WIDTH - 1), //random X value
        Random.nextInt(HOUSE_HEIGHT - 5, HOUSE_HEIGHT - 1) //random Y value
    )

    house[fireSeedLocation[1]][fireSeedLocation[0]] = EMPTY //removes walls if there are any
    fire[fireSeedLocation[1]][fireSeedLocation[0]] = INTENSE //sets fire intensity of tile to spreadable
}

/**
 * Fire Extinguisher:
 * Places the fire extinguisher where valid.
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
            placeFireExtinguisher(house, fire) //places a new fire extinguisher
        }
    }
}

fun checkAdjacent(row: Int, tile: Int, fire: MutableList<MutableList<Int>>): Boolean {
    if (row > 0 && fire[row - 1][tile] == INTENSE) //if not outside border, checks tile above for intense fire
        return true
    if (row < HOUSE_HEIGHT - 1 && fire[row + 1][tile] == INTENSE) //if not outside border, checks tile below for intense fire
        return true
    if (tile > 0 && fire[row][tile - 1] == INTENSE) //if not outside border, checks tile to the left for intense fire
        return true
    if (tile < HOUSE_WIDTH - 1 && fire[row][tile + 1] == INTENSE)//if not outside border, checks tile to the right for intense fire
        return true
    return false //otherwise, returns false
}

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
        println("# Fire Extinguished!".padEnd(30) + "#")
        println("# You escaped after $rounds rounds".padEnd(30) + "#")
        exitProcess(0)
    }
}

fun getPlayerName(player: String): String { //asks each player for their name
    while (true) {
        println()
        if (player == PLAYER1) {
            println("# " + "Player 1".blue() + "".padEnd(11) + "#")
            println("# What is your name? #")
            print("> ")
        } else if (player == PLAYER2) {
            println("# " + "Player 2".green() + "".padEnd(11) + "#")
            println("# What is your name? #")
            print("> ")
        }
        val input = readlnOrNull() //gets input
        if (input != null && input != "") { //checks if input is not empty
            println()
            if (player == PLAYER1) println("# Welcome " + input.blue() + "! #")
            if (player == PLAYER2) println("# Welcome " + input.green() + "! #")
            return input
        } else {
            println()
            println("# Invalid input #")
            Thread.sleep(1000)
            continue //resets if the input is invalid (else)
        }
    }
}
import kotlin.system.exitProcess

/**
 * Player.kt
 * This file is where all the player functions are.
 *
 * This file contains the following functions:
 *
 * getPlayerName
 * playerTurn
 * movePlayer
 * checkPlayer
 *
 *
 * FUNCTIONS-----------------------------------------------------------------------------------------------------------|
 *
 * Get Player Name:
 * Asks each player for their name.
 */
fun getPlayerName(player: String): String { //asks each player for their name

    while (true) {
        println()

        if (player == PLAYER1) {
            println("# " + "Player 1".blue() + ".")
            println("# What is your name?")
            print("> ")
        } else if (player == PLAYER2) {
            println("# " + "Player 2".green() + ".")
            println("# What is your name?")
            print("> ")
        }

        val input = readlnOrNull() //gets input

        if (input != null && input != "") { //checks if input is not empty
            println()
            if (player == PLAYER1) println("# Welcome " + input.blue() + "!")
            if (player == PLAYER2) println("# Welcome " + input.green() + "!")
            return input
        } else {
            println()
            println("# Invalid input.")
            Thread.sleep(1000)
            continue //resets if the input is invalid (else)
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
            println("# It is your turn ${playerName.blue()}")
            println("# You have ${playerFireExtinguishers[0]} Fire Extinguishers.") //number of fire extinguishers p1 has
        }
        if (player == PLAYER2) { //Prints turn text based on player
            println("# It is your turn ${playerName.green()}")
            println("# You have ${playerFireExtinguishers[1]} Fire Extinguishers.") //number of fire extinguishers p2 has
        }
        println("# [W][A][S][D] [F] [skip].")
        println("# You have $remainingMoves moves remaining.")
        print("> ")
        when (readlnOrNull()?.uppercase()) { //get player input
            "W" -> {
                if (location[0] > 0) { //checks if the player is not on the top edge of the grid and if so, stops it
                    if (movePlayer(house, fire, player, location, -1, 0, playerFireExtinguishers)) break@turnLoop
                    else continue@turnLoop //resets turn
                } else {
                    println()
                    println("# That Movement Is Blocked by a Wall.") //gives a reason for the prevention (edge)
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
                    println("# That Movement Is Blocked by a Wall.") //gives a reason for the prevention (edge)
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
                    println("# That Movement Is Blocked by a Wall.") //gives a reason for the prevention (edge)
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
                    println("# That Movement Is Blocked by a Wall.") //gives a reason for the prevention (edge)
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
                        println("# You do not have any Fire Extinguishers.") //gives a reason for the prevention (no item)
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
                        println("# You do not have any Fire Extinguishers.") //gives a reason for the prevention (no item)
                        Thread.sleep(1000)
                        continue@turnLoop //resets turn
                    }
                }
            }

            "SKIP" -> break@turnLoop //immediately ends turn without moving the player

            else -> { //if input none of the above (invalid)
                println()
                println("# Invalid input.")
                Thread.sleep(1000)
                println()
                continue@turnLoop //resets turn if input is not valid
            }
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
                println("# That Movement Is Blocked by another player.") //gives a reason for the prevention (other player)
                Thread.sleep(1000)
                return false
            }
        } else {
            println()
            println("# That Movement Is Blocked by Fire.") //gives a reason for the prevention (fire)
            Thread.sleep(1000)
            return false
        }
    } else {
        println()
        println("# That Movement Is Blocked by a Wall.") //gives a reason for the prevention (wall)
        Thread.sleep(1000)
        return false
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
            println("# ${player2Name.green()} Escapes!")
            println("# You survived for $rounds rounds.")
            exitProcess(0)
        } else if (player == PLAYER2) { //if player is PLAYER2, player 1 wins
            println()
            println("# ${player1Name.blue()} Escapes!")
            println("# You survived for $rounds rounds.")
            exitProcess(0)
        }
    }
}
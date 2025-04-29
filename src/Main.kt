import kotlin.system.exitProcess

/**
 * ===================================================================================*
 * Programming Project for NCEA Level 2, Standard 91896
 * -----------------------------------------------------------------------------------*
 * Project Name:   NAME
 * Project Author: Lachlan Hunt
 * GitHub Repo:    https://github.com/waimea-ldhunt/level-2-programming-assessment
 * -----------------------------------------------------------------------------------*
 * Other .kt Files:
 *
 * Player --> Player control and management
 *
 * House --> House/Fire list manipulation
 *
 * ===================================================================================*
 *
 * Main.kt
 *
 * This file contains the main function and the global variables.
 */

const val HOUSE_WIDTH = 14 //Width of the playable area
const val HOUSE_HEIGHT = 10 //Height of the playable area
const val WALL_SEED_CHANCE = 9 //Chance that a tile will become a wall (1 in X)
const val WALL_CONTINUE_CHANCE = 3 //Chance that a wall will extend (1 in X)
const val BORDER_WALL = "#" //Border Character
const val EMPTY = "+ " //Empty tile
const val WALL = "# " //Wall Tile
const val FIRE_EXTINGUISHER = "& " //Fire Extinguisher Tile
const val EXTINGUISHER_STRENGTH = 2
const val PLAYER1 = "1 " //Player Tile
const val PLAYER2 = "2 " //Player Tile
const val INTENSE = 3 //Max Fire Level (Spreads fire)
const val MEDIUM = 2 //Fire level 2 (does not spread)
const val WEAK = 1 //Fire level 1 (does not spread)
const val SAFE = 0 //No fire on the tile
const val MOVES = 2 //Number of movements each player gets per round

/**
 * Main function:
 * Contains game loop and val setup.
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

    playLoop@ while (true) {
        println()
        println("# Welcome to GAME")
        println("1: Play")
        println("2: How to Play")
        print("> ")

        when (readlnOrNull()) { //Asks player if they would like to learn how to play or start
            "1" -> break
            "2" -> {
                while (true) {
                    println()
                    println("# Each player will take turns moving twice using [W][A][S][D][skip].")
                    println("# If you pick up a Fire Extinguisher (&) you may use it with [F].")
                    println("# Your goal is to be the last player standing.")
                    println("# Each difficulty changes game rules:")
                    println("# Easy:   Fire is slow.")
                    println("# Normal: Fire is fast.")
                    println("# Hard:   Fire is fast + only one fire extinguisher.")
                    println()
                    println("1: Play")
                    println("2: Quit")
                    print("> ")
                    when (readlnOrNull()) { //asks for play or quit
                        "1" -> break@playLoop //ends loop (continues game)
                        "2" -> exitProcess(0) //ends process
                        else -> {
                            println()
                            println("# Invalid Input.")
                            Thread.sleep(1000)
                            continue//resets if the input is invalid (else)
                        }
                    }
                }
            }

            else -> {
                println()
                println("# Invalid Input.")
                Thread.sleep(1000)
                continue //resets if the input is invalid (else)
            }
        }
    }

    gameLoop@ while (true) {

        println()
        println("# Choose a Difficulty:")
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
                println("# Invalid input.")
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

            checkEmpty(fire, rounds) //checks if there is no INTENSE fire remaining and then ends game if so.

            repeat(fireSpeed) {
                stepFire(house, fire) //steps the fire
                showHouse(house, fire, player1Tile, player2Tile) //shows the house
                Thread.sleep(1000)

                if (fire[player1Location[0]][player1Location[1]] > 0 && fire[player2Location[0]][player2Location[1]] > 0) { //checks if both players are in the fire and ends accordingly
                    println()
                    println("# No Survivors.")
                    println("# You survived for $rounds rounds.")
                    exitProcess(0)
                } else {
                    checkPlayer(
                        fire,
                        PLAYER1,
                        player1Name,
                        player2Name,
                        player1Location,
                        rounds
                    ) //checks if Player 1 is in the fire and ends if so (checkPlayer).
                    checkPlayer(
                        fire,
                        PLAYER2,
                        player1Name,
                        player2Name,
                        player2Location,
                        rounds
                    ) //checks if Player 2 is in the fire and ends if so (checkPlayer).
                }
            }

            rounds += 1
        }
    }
}
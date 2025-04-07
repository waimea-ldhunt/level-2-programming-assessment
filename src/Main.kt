import kotlin.random.Random
import kotlin.system.exitProcess

/**
 * ===================================================================================*
 * Programming Project for NCEA Level 2, Standard 91896
 * -----------------------------------------------------------------------------------*
 * Project Name:   PROJECT NAME HERE
 * Project Author: Lachlan Hunt
 * GitHub Repo:    https://github.com/waimea-ldhunt/level-2-programming-assessment
 * -----------------------------------------------------------------------------------*
 * Notes:
 *
 *
 * ===================================================================================*
 */

const val HOUSE_WIDTH = 14
const val HOUSE_HEIGHT = 10
const val WALL_SEED_CHANCE = 9
const val WALL_CONTINUE_CHANCE = 3
const val EMPTY = "+ "
const val WALL = "# "
const val BORDER_WALL = "#"
const val FIRE_EXTINGUISHER = "& "
const val PLAYER1 = "1 "
const val PLAYER2 = "2 "
const val INTENSE = 3
const val MEDIUM = 2
const val WEAK = 1
const val SAFE = 0

fun main() {
    val house = mutableListOf<MutableList<String>>() //The 2-dimensional grid that is shown in the showGrid() function
    val fire = mutableListOf<MutableList<Int>>() //The state of the tile, 0 = Not on fire, >0 = On fire, Numbers show the state of the fire.
    val difficulty: Int
    val player1Location = mutableListOf(1,1)
    val player2Location = mutableListOf(1,2)
    val playerFireExtinguishers = mutableListOf(0,0)
    var rounds = 0
    setupHouse(house, fire) //runs the function that starts the house setup
    placeFireSeed(house, fire)
    while (true) {
        println("# Welcome to GAME #")
        println("1: Play")
        println("2: How to Play")
        print("> ")
        when (readlnOrNull()) {
            "1" -> break
            "2" -> {
                println("Rules go here")
            }
            else -> {
                println()
                println("Invalid Input")
                Thread.sleep(1000)
                println()
                continue
            }
        }
    }
    gameLoop@ while (true) {

        println("# Choose a Difficulty: #")
        println("1: Easy")
        println("2: Normal")
        println("3: Hard")
        print("> ")
        difficulty = when (readlnOrNull()) {
            "1" -> 1
            "2" -> 2
            "3" -> 3
            else -> {
                println()
                println("Invalid input")
                Thread.sleep(1000)
                println()
                continue
            }
        }
        var fireSpeed = 1
        var moves = 1
        var fireExtinguisherCount = 2
        when (difficulty) {
            1 -> {
                moves = 2
            }
            3 -> {
                fireSpeed = 2
                fireExtinguisherCount = 1
            }
        }
        stepFire(house, fire)
        Thread.sleep(2000)
        showHouse(house, fire)
        stepFire(house, fire)
        Thread.sleep(2000)
        repeat(fireExtinguisherCount) {placeFireExtinguisher(house, fire)}
        showHouse(house, fire)
        roundLoop@ while (true) {
            repeat(moves) {
                playerTurn(house, fire, PLAYER1, player1Location, playerFireExtinguishers)
                showHouse(house, fire)
            }
            repeat(moves) {
                playerTurn(house, fire, PLAYER2, player2Location, playerFireExtinguishers)
                showHouse(house, fire)
            }
            repeat(fireSpeed) {
                stepFire(house, fire)
                showHouse(house, fire)
                Thread.sleep(1000)
                checkPlayer(fire, PLAYER1, player1Location, rounds)
                checkPlayer(fire, PLAYER2, player2Location, rounds)
            }
            rounds += 1
        }
    }
}
fun playerTurn(house: MutableList<MutableList<String>>, fire: MutableList<MutableList<Int>>, player: String, location: MutableList<Int>, playerFireExtinguishers: MutableList<Int>) {
    turnLoop@ while (true) {
        println()
        println("# It is your move, player $player#")
        print("> ")
        when (readlnOrNull()?.uppercase()) {
            "W" -> {
                if (location[0] > 0) {
                    if (movePlayer(house, fire, player, location, -1, 0, playerFireExtinguishers)) break@turnLoop
                    else continue@turnLoop
                } else {
                    println()
                    println("# That Movement Is Blocked by a Wall #")
                    Thread.sleep(1000)
                    continue@turnLoop
                }
            }
            "A" -> {
                if (location[1] > 0) {
                    if (movePlayer(house, fire, player, location, 0, -1, playerFireExtinguishers)) break@turnLoop
                    else continue@turnLoop
                } else {
                    println()
                    println("# That Movement Is Blocked by a Wall #")
                    Thread.sleep(1000)
                    continue@turnLoop
                }
            }
            "S" -> {
                if (location[0] < HOUSE_HEIGHT-1) {
                    if (movePlayer(house, fire, player, location, 1, 0, playerFireExtinguishers)) break@turnLoop
                    else continue@turnLoop
                } else {
                    println()
                    println("# That Movement Is Blocked by a Wall #")
                    Thread.sleep(1000)
                    continue@turnLoop
                }
            }
            "D" -> {
                if (location[1] < HOUSE_WIDTH) {
                    if (movePlayer(house, fire, player, location, 0, 1, playerFireExtinguishers)) break@turnLoop
                    else continue@turnLoop
                } else {
                    println()
                    println("# That Movement Is Blocked by a Wall #")
                    Thread.sleep(1000)
                    continue@turnLoop
                }
            }
            "F" -> {
                if (player == PLAYER1) {
                    if (playerFireExtinguishers[0] > 0) {
                        for (row in location[0]-2..location[0]+2) {
                            for (tile in location[1]-2..location[1]+2) {
                                if (row in 1..<HOUSE_HEIGHT) {
                                    if (tile in 1..<HOUSE_WIDTH) fire[row][tile] = SAFE
                                }
                            }
                        }
                        playerFireExtinguishers[0] -= 1
                        placeFireExtinguisher(house, fire)
                        showHouse(house, fire)
                        continue@turnLoop
                    } else {
                        println()
                        println("# You do not have a Fire Extinguisher #")
                        Thread.sleep(1000)
                        continue@turnLoop
                    }
                } else if (player == PLAYER2) {
                    if (playerFireExtinguishers[1] > 0) {
                        for (row in location[0]-2..location[0]+2) {
                            for (tile in location[1]-2..location[1]+2) {
                                if (row in 1..<HOUSE_HEIGHT) {
                                    if (tile in 1..<HOUSE_WIDTH) fire[row][tile] = SAFE
                                }
                            }
                        }
                        playerFireExtinguishers[1] -= 1
                        placeFireExtinguisher(house, fire)
                        showHouse(house, fire)
                        continue@turnLoop
                    } else {
                        println()
                        println("# You do not have a Fire Extinguisher #")
                        Thread.sleep(1000)
                        continue@turnLoop
                    }
                }
            }
        }
    }
}
fun checkPlayer(fire: MutableList<MutableList<Int>>, player: String, location: MutableList<Int>, rounds: Int) {
    if (fire[location[0]][location[1]] > 0) {
        if (player == PLAYER1) {
            println()
            println("# PLAYER 2 WINS! #")
            println("# You survived for $rounds rounds #")
            exitProcess(0)
        } else if (player == PLAYER2) {
            println()
            println("# PLAYER 1 WINS! #")
            println("# You survived for $rounds rounds #")
            exitProcess(0)
        }
    }
}
fun movePlayer(house: MutableList<MutableList<String>>, fire: MutableList<MutableList<Int>>, player: String, location: MutableList<Int>, yChange: Int, xChange: Int, playerFireExtinguishers: MutableList<Int>): Boolean {
    if (house[location[0]+yChange][location[1]+xChange] != WALL) {
        if (fire[location[0]+yChange][location[1]+xChange] == 0) {
            if (house[location[0]+yChange][location[1]+xChange] != PLAYER1 && house[location[0]+yChange][location[1]+xChange] != PLAYER2) {
                house[location[0]][location[1]] = EMPTY
                if (yChange != 0) {
                    location[0] += yChange
                } else {
                    location[1] += xChange
                }
                if (house[location[0]][location[1]] == FIRE_EXTINGUISHER) {
                    if (player == PLAYER1) playerFireExtinguishers[0] += 1
                    if (player == PLAYER2) playerFireExtinguishers[1] += 1
                }
                house[location[0]][location[1]] = player
                return true
            } else {
                println()
                println("# That Movement Is Blocked by another player #")
                Thread.sleep(1000)
                println()
                return false
            }
        } else {
            println()
            println("# That Movement Is Blocked by Fire #")
            Thread.sleep(1000)
            println()
            return false
        }
    } else {
        println()
        println("# That Movement Is Blocked by a Wall #")
        Thread.sleep(1000)
        println()
        return false
    }
}
fun setupHouse(house: MutableList<MutableList<String>>, fire: MutableList<MutableList<Int>>) {
    repeat(HOUSE_HEIGHT){
        val buildHouseRow = mutableListOf<String>()
        val buildfireRow = mutableListOf<Int>()
        repeat(HOUSE_WIDTH){
            buildHouseRow.add(EMPTY)
            buildfireRow.add(SAFE)
        }
        house.add(buildHouseRow)
        fire.add(buildfireRow)
    }
    var recentWall = false
    for (row in 1..< house.size-1) {
        for (tile in 1..< house[row].size-1) {
            if (recentWall) {
                if (Random.nextInt(1,WALL_CONTINUE_CHANCE) == 1) {
                    house[row][tile] = WALL
                    recentWall = true
                } else recentWall = false
            } else if (Random.nextInt(1,WALL_SEED_CHANCE) == 1) {
                house[row][tile] = WALL
                recentWall = true
            }
        }
    }
    house[1][1] = PLAYER1
    house[1][2] = PLAYER2
}
fun showHouse (house: MutableList<MutableList<String>>, fire: MutableList<MutableList<Int>>) {
    println()
    println("$BORDER_WALL ".cyan().repeat(HOUSE_WIDTH + 2))
    for (row in 0..< house.size) {
        print("$BORDER_WALL ".cyan())
        for (tile in 0..< house[row].size) {
            if (fire[row][tile] > SAFE) {
                when (fire[row][tile]) {
                    INTENSE -> print(house[row][tile].col(225,225,0))
                    MEDIUM -> print(house[row][tile].col(255,150,25))
                    WEAK -> print(house[row][tile].red())
                }
            } else {
                when (house[row][tile]) {
                    WALL -> print(house[row][tile].cyan())
                    FIRE_EXTINGUISHER  -> print(house[row][tile].magenta())
                    PLAYER1 -> print(house[row][tile].blue())
                    PLAYER2 -> print(house[row][tile].green())
                    EMPTY -> print(house[row][tile].grey())
                }
            }
        }
        println(BORDER_WALL.cyan())
    }
    print("$BORDER_WALL ".cyan().repeat(HOUSE_WIDTH + 2))
    println()
}
fun placeFireSeed (house: MutableList<MutableList<String>>, fire: MutableList<MutableList<Int>>) {
    val fireSeedLocation = mutableListOf(Random.nextInt(HOUSE_HEIGHT - 5, HOUSE_HEIGHT - 1),Random.nextInt(HOUSE_WIDTH - 5, HOUSE_WIDTH - 1))
    house[fireSeedLocation[0]][fireSeedLocation[1]] = EMPTY
    fire[fireSeedLocation[0]][fireSeedLocation[1]] = INTENSE
}
fun placeFireExtinguisher (house: MutableList<MutableList<String>>, fire: MutableList<MutableList<Int>>){
    while (true) {
        val fireExtinguisherLocation = mutableListOf(Random.nextInt(0, HOUSE_HEIGHT),Random.nextInt(0, HOUSE_WIDTH))
        if (fire[fireExtinguisherLocation[0]][fireExtinguisherLocation[1]] == SAFE) {
            if (house[fireExtinguisherLocation[0]][fireExtinguisherLocation[1]] != WALL) {
                if (house[fireExtinguisherLocation[0]][fireExtinguisherLocation[1]] != PLAYER1) {
                    if (house[fireExtinguisherLocation[0]][fireExtinguisherLocation[1]] != PLAYER2) {
                        house[fireExtinguisherLocation[0]][fireExtinguisherLocation[1]] = FIRE_EXTINGUISHER
                        break
                    }
                }
            }
        }
    }
}
fun stepFire (house: MutableList<MutableList<String>>, fire: MutableList<MutableList<Int>>) {
    val burnTiles = mutableListOf<MutableList<Int>>()
    for (row in 0..< fire.size){
        for (tile in 0..< fire[row].size) {
            if (fire[row][tile] == SAFE && house[row][tile] != WALL && checkAdjacent(row, tile, fire)){
                if (house[row][tile] == FIRE_EXTINGUISHER) {
                    house[row][tile] = EMPTY
                    placeFireExtinguisher(house, fire)
                }
                burnTiles.add(mutableListOf(row, tile))
            }
        }
    }
    for (row in 0..< fire.size){
        for (tile in 0..< fire[row].size) {
            if (fire[row][tile] > SAFE) {
                fire[row][tile] -= 1
            }
        }
    }
    for (targetTile in burnTiles) {
        fire[targetTile[0]][targetTile[1]] = INTENSE
    }
}
fun checkAdjacent(row: Int, tile: Int, fire: MutableList<MutableList<Int>>): Boolean {
    if (row > 0 && fire[row - 1][tile] == INTENSE)
        return true
    if (row < HOUSE_HEIGHT-1 && fire[row + 1][tile] == INTENSE)
        return true
    if (tile > 0 && fire[row][tile- 1] == INTENSE)
        return true
    if (tile < HOUSE_WIDTH-1 && fire[row][tile + 1] == INTENSE)
        return true
    return false
}
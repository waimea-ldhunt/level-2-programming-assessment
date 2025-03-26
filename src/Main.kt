import kotlin.random.Random

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
const val WALL_SEED_CHANCE = 12
const val WALL_CONTINUE_CHANCE = 3
const val EMPTY = "+ "
const val WALL = "# "
const val PLAYER1 = "1 "
const val PLAYER2 = "2 "
fun main() {
    val house = mutableListOf<MutableList<String>>() //The 2-dimensional grid that is shown in the showGrid() function
    val fire = mutableListOf<MutableList<Int>>() //The state of the tile, 0 = Not on fire, >0 = On fire, Numbers show the state of the fire.
    setupHouse(house, fire) //runs the function that starts the house setup
    placeFireSeed(fire)
    showHouse(house, fire)
    repeat(10) {
        stepFire(house, fire)
        showHouse(house, fire)
        Thread.sleep(1000)
    }
}
fun setupHouse(house: MutableList<MutableList<String>>, fire: MutableList<MutableList<Int>>) {
    repeat(HOUSE_HEIGHT){
        val buildHouseRow = mutableListOf<String>()
        val buildfireRow = mutableListOf<Int>()
        repeat(HOUSE_WIDTH){
            buildHouseRow.add("+ ")
            buildfireRow.add(0)
        }
        house.add(buildHouseRow)
        fire.add(buildfireRow)
    }
    var recentWall = false
    for (row in 1..< house.size-1) {
        for (tile in 1..< house[row].size-1) {
            if (recentWall) {
                if (Random.nextInt(1,WALL_CONTINUE_CHANCE) == 1) {
                    house[row][tile] = "# "
                    recentWall = true
                } else recentWall = false
            } else if (Random.nextInt(1,WALL_SEED_CHANCE) == 1) {
                house[row][tile] = "# "
                recentWall = true
            }
        }
    }
    house[1][1] = PLAYER1
    house[1][2] = PLAYER2
}
fun showHouse (house: MutableList<MutableList<String>>, fire: MutableList<MutableList<Int>>) {
    println()
    println("# ".cyan().repeat(HOUSE_WIDTH + 2))
    for (row in 0..< house.size) {
        print("# ".cyan())
        for (tile in 0..< house[row].size) {
            if (fire[row][tile] > 0) {
                when (fire[row][tile]) {
                    3 -> print(house[row][tile].col(225,225,0))
                    2 -> print(house[row][tile].col(255,150,25))
                    1 -> print(house[row][tile].red())
                }
            } else {
                when (house[row][tile]) {
                    WALL -> print(house[row][tile].cyan())
                    PLAYER1 -> print(house[row][tile].blue())
                    PLAYER2 -> print(house[row][tile].green())
                    EMPTY -> print(house[row][tile].grey())
                }
            }
        }
        println("#".cyan())
    }
    print("# ".cyan().repeat(HOUSE_WIDTH + 2))
    println()
}
fun placeFireSeed (fire: MutableList<MutableList<Int>>) {
    val fireSeedLocation = mutableListOf(Random.nextInt(HOUSE_HEIGHT - 5, HOUSE_HEIGHT - 1),Random.nextInt(HOUSE_WIDTH - 5, HOUSE_WIDTH - 1))
    fire[fireSeedLocation[0]][fireSeedLocation[1]] = 4
}
fun stepFire (house: MutableList<MutableList<String>>, fire: MutableList<MutableList<Int>>) {
    /**  Step Fire Plan
     *
     *   update fire state of tile, if a tile reaches fire = 1
     *   if the tile is a wall, it burns for one round longer otherwise the fire dies out.
     *   otherwise decrease the state/lifespan by one.
     *   cycle through every tile,
     *   check if the adjacent tiles are on fire,
     *   add the tile to a list,
     *   once all tiles have been checked, use the list to update each tile to fire[][] = 4
     *   check if a player is touching the fire.
     *   if so, end the game else begin turns.
     */
    for ( row in 0..< fire.size) for (tile in 0..< fire[row].size) {
        if (fire[row][tile] > 0) {
            fire[row][tile] -= 1
        }
    }
    val burnTiles = mutableListOf<MutableList<Int>>()
    for (row in 0..< fire.size) {
        for (tile in 0..< fire[row].size) {
            if (fire[row][tile] == 0) if (house[row][tile] != WALL) if (checkAdjacent(row, tile, fire)) {
                burnTiles.add(mutableListOf(row,tile))
            }
        }
    }
    for (tile in burnTiles) {
        fire[tile[0]][tile[1]] = 3
    }
}
fun checkAdjacent(row: Int, tile: Int, fire: MutableList<MutableList<Int>>): Boolean {
    if (row > 0) if (fire[row-1][tile] > 0) return true
    if (row < HOUSE_HEIGHT-1) if (fire[row+1][tile] > 0) return true
    if (tile > 0) if (fire[row][tile-1] > 0) return true
    if (tile < HOUSE_WIDTH-1) if (fire[row][tile+1] > 0) return true
    return false
}
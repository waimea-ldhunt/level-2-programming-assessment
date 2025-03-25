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
    stepFire(fire)
    showHouse(house, fire)
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
    for (row in 0..< house.size) {
        for (tile in 0..< house[row].size) {
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
            if (fire[row][tile] >= 1) {
                when (fire[row][tile]) {
                    4 -> print(house[row][tile].col(225,225,0))
                    3 -> print(house[row][tile].col(255,150,25))
                    2 -> print(house[row][tile].red())
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
fun stepFire (fire: MutableList<MutableList<Int>>) {
    //to do
}
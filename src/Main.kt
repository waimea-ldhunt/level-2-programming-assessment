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
const val EMPTY = ". "
const val WALL = "# "
fun main() {
    val house = mutableListOf<MutableList<String>>()
    val burning = mutableListOf<MutableList<Boolean>>()
    setupHouse(house, burning)
    showHouse(house, burning)
    house[1][2] = "# "
    house[1][3] = "# "
    burning[1][3] = true
    burning[6][6] = true
    showHouse(house, burning)
    println(house[1][2])
}
fun setupHouse(house: MutableList<MutableList<String>>, burning: MutableList<MutableList<Boolean>>) {
    repeat(HOUSE_HEIGHT){
        val buildRow = mutableListOf<String>()
        val buildBurningRow = mutableListOf<Boolean>()
        repeat(HOUSE_WIDTH){
            buildRow.add(". ")
            buildBurningRow.add(false)
        }
        house.add(buildRow)
        burning.add(buildBurningRow)
    }
}
fun showHouse (house: MutableList<MutableList<String>>, burning: MutableList<MutableList<Boolean>>) {
    println()
    for (row in house) {
        for (item in 0..< row.size) {
            if (burning[house.indexOf(row)][item]) {
                print(row[item].red())
            } else {
                when (row[item]) {
                    WALL -> print(row[item].blue())
                    EMPTY -> print(row[item])
                }
            }
        }
        println()
    }
}
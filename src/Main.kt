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



fun main() {
    var house = mutableListOf<MutableList<String>>()
    setupHouse(house)
    println(house)
    (house[1])[2] = "# "; println()
    println(house.toString().replace("], ", "\n").replace("]", "").replace("[", ""))
    println((house[1])[2])
}
fun setupHouse(house: MutableList<MutableList<String>>) {
    repeat(10){
        house.add(mutableListOf(". ",". ",". ",". ",". ",". ",". ",". ",". ",". "))
    }
}



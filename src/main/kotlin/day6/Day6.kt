package day6

fun getDistanceTravelled(heldTime: Long, totalTime: Long): Long {
    val remainingTime = totalTime - heldTime
    if (remainingTime < 0) throw Error("Held time is longer than total time!")
    if (remainingTime == 0L) return 0
    return heldTime * remainingTime
}


fun doesBeatRecord(heldTime: Long, record: Record): Boolean {
    val distance = getDistanceTravelled(heldTime, record.time)
    return distance > record.distance
}

fun findRecordBeatingHoldTimes(record: Record): List<Long> {
    return (0..(record.time)).filter {
        doesBeatRecord(it, record)
    }
}

data class Record(val time: Long, val distance: Long)

/*7  15   30
  9  40  200*/
//Record(7, 9),
//        Record(15, 40),
//        Record(30, 200),


//Time:        54     70     82     75
//Distance:   239   1142   1295   1253
fun main() {
    val given = listOf(
        Record(54, 239),
        Record(70, 1142),
        Record(82, 1295),
        Record(75, 1253),
    )
    val partOneResult = given
        .map { findRecordBeatingHoldTimes(it).size }
        .reduce { a, b -> a * b }

    println("Part One Result: $partOneResult")

    val p2 = Record(54708275, 239114212951253)

    val p2Result = findRecordBeatingHoldTimes(p2).size

    println("Part Two Result: $p2Result")

}

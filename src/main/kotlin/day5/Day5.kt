package day5

fun parseFromString() {

}


class MNot(
    private val valueStart: Long,
    private val indexStart: Long,
    private val rangeSize: Long,
) {

    fun findFor(v: Long): Long {
        val end = indexStart + rangeSize
        if (v < indexStart || v >= end) return v
        return valueStart + (v - indexStart)
    }

    override fun toString(): String {
        return "$valueStart $indexStart $rangeSize"
    }
}

data class MapNotation(
    val notations: List<MNot>
) {

    fun findFor(v: Long): Long {
        return notations.firstNotNullOfOrNull {
            val res = it.findFor(v)
            if (res == v) null else res
        } ?: v
    }

    override fun toString(): String {
        return notations.joinToString("\n")
    }

    companion object {
        fun parseNotation(input: String, label: String): MapNotation {
            val regex = Regex("""$label map:\n((?:\d+ \d+ \d+\n?)+)""")

            val result = regex.find(input)?.groupValues?.get(1)?.split("\n")?.filter { it.isNotBlank() }?.map {
                val (valueStart, indexStart, rangeSize) = it.split(" ").map { it.toLong() }
                MNot(valueStart, indexStart, rangeSize)
            } ?: throw Error("Could not parse $label map from input")

            return MapNotation(result)
        }
    }
}


data class Input(
    val seeds: List<Long>,
    val seedToSoil: MapNotation,
    val soilToFertilizer: MapNotation,
    val fertilizerToWater: MapNotation,
    val waterToLight: MapNotation,
    val lightToTemperature: MapNotation,
    val temperatureToHumidity: MapNotation,
    val humidityToLocation: MapNotation,
) {

    fun findLocationForSeed(seed: Long): Long {
        return findEach(seed).last()
    }

    fun findEach(seed: Long): List<Long> {
        val soil = seedToSoil.findFor(seed)
        val fertilizer = soilToFertilizer.findFor(soil)
        val water = fertilizerToWater.findFor(fertilizer)
        val light = waterToLight.findFor(water)
        val temperature = lightToTemperature.findFor(light)
        val humidity = temperatureToHumidity.findFor(temperature)
        val location = humidityToLocation.findFor(humidity)

        return listOf(soil, fertilizer, water, light, temperature, humidity, location)
    }

    override fun toString(): String {
        return """seeds: ${seeds.joinToString(" ")}

seed-to-soil map:
$seedToSoil
            
soil-to-fertilizer map:
$soilToFertilizer

fertilizer-to-water map:
$fertilizerToWater

water-to-light map:
$waterToLight

light-to-temperature map:
$lightToTemperature

temperature-to-humidity map:
$temperatureToHumidity
            
humidity-to-location map:
$humidityToLocation""".trimIndent()
    }


    companion object {
        fun parse(input: String): Input {
            val seeds = input.split("\n")[0].split(" ").mapNotNull { it.toLongOrNull() }
            val seedToSoil = MapNotation.parseNotation(input, "seed-to-soil")
            val soilToFertilizer = MapNotation.parseNotation(input, "soil-to-fertilizer")
            val fertilizerToWater = MapNotation.parseNotation(input, "fertilizer-to-water")
            val waterToLight = MapNotation.parseNotation(input, "water-to-light")
            val lightToTemperature = MapNotation.parseNotation(input, "light-to-temperature")
            val temperatureToHumidity = MapNotation.parseNotation(input, "temperature-to-humidity")
            val humidityToLocation = MapNotation.parseNotation(input, "humidity-to-location")
            return Input(
                seeds,
                seedToSoil,
                soilToFertilizer,
                fertilizerToWater,
                waterToLight,
                lightToTemperature,
                temperatureToHumidity,
                humidityToLocation,
            )
        }
    }

}


fun areEqual(a: String, b: String): Boolean {
    return a.replace(Regex("""\s"""), "") == b.replace(Regex("""\s"""), "")
}

fun main() {
    val text = ClassLoader.getSystemResource("day5/example-input.txt").readText()
    val seeds = listOf<Long>(79, 14, 55, 13)
    val input = Input.parse(text)

    println(areEqual(text,input.toString()))
    println("=============================================================")
    println(text.replace(Regex("""\s"""), ""))
    println("=============================================================")
    println(input.toString().replace(Regex("""\s"""), ""))
    println("=============================================================")
//    println(seeds.map { input.findLocationForSeed(it) })
//    println(seeds.minBy { input.findLocationForSeed(it) })
}
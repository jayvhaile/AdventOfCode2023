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
            val regex = Regex("""$label map:\n(\d+) (\d+) (\d+)\n(\d+) (\d+) (\d+)""")
            val result = regex.matchAt(input, input.indexOf(label))
                ?.groupValues
                ?.mapNotNull { it.toLongOrNull() }
                ?.chunked(3)
                ?.map { MNot(it[0], it[1], it[2]) }
                ?: throw Error("Could not parse $label")
            return MapNotation(result)
        }
    }
}


data class Input(
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
        return """seed-to-soil map:
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
            val seedToSoil = MapNotation.parseNotation(input, "seed-to-soil")
            val soilToFertilizer = MapNotation.parseNotation(input, "soil-to-fertilizer")
            val fertilizerToWater = MapNotation.parseNotation(input, "fertilizer-to-water")
            val waterToLight = MapNotation.parseNotation(input, "water-to-light")
            val lightToTemperature = MapNotation.parseNotation(input, "light-to-temperature")
            val temperatureToHumidity = MapNotation.parseNotation(input, "temperature-to-humidity")
            val humidityToLocation = MapNotation.parseNotation(input, "humidity-to-location")
            return Input(
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

val given = """seed-to-soil map:
50 98 2
52 50 48

soil-to-fertilizer map:
0 15 37
37 52 2
39 0 15

fertilizer-to-water map:
49 53 8
0 11 42
42 0 7
57 7 4

water-to-light map:
88 18 7
18 25 70

light-to-temperature map:
45 77 23
81 45 19
68 64 13

temperature-to-humidity map:
0 69 1
1 0 69

humidity-to-location map:
60 56 37
56 93 4""".trimIndent()


fun main() {
    val seeds = listOf<Long>(79, 14, 55, 13)
    val input = Input.parse(given)
    println(given)
    println("=============================================================")
    println(input.toString())
//    println(seeds.map { input.findLocationForSeed(it) })
//    println(seeds.minBy { input.findLocationForSeed(it) })
}
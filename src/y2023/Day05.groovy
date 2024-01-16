package y2023

import common.DayRunner
import common.FileAccess

class Day05 extends DayRunner {
    Day05() {
        super(null, false);
    }

    static void main(String[] args) {
        new Day05().run();
    }

    def parseInput(FileAccess fileAccess) {
        List<Long> seeds = fileAccess.lines.first().split(/:/)[1].split(/ /)

        List<Range<Long>> seedSoilRanges1 = fileAccess.text.split(/:/)[2].trim().split(/\n/).collect {
            if (it.matches(/\d+.*/)) {
                Long start = Long.parseLong(it.split(/ /)[1])
                return (start..(start - 1 + Long.parseLong(it.split(/ /)[2])))
            }
            return null
        } - null
        List<Range<Long>> seedSoilRanges2 = fileAccess.text.split(/:/)[2].trim().split(/\n/).collect {
            if (it.matches(/\d+.*/)) {
                Long start = Long.parseLong(it.split(/ /)[0])
                return (start..(start - 1 + Long.parseLong(it.split(/ /)[2])))
            }
            return null
        } - null

        List<Range<Long>> soilFertRanges1 = fileAccess.text.split(/:/)[3].trim().split(/\n/).collect {
            if (it.matches(/\d+.*/)) {
                Long start = Long.parseLong(it.split(/ /)[1])
                return (start..(start - 1 + Long.parseLong(it.split(/ /)[2])))
            }
            return null
        } - null
        List<Range<Long>> soilFertRanges2 = fileAccess.text.split(/:/)[3].trim().split(/\n/).collect {
            if (it.matches(/\d+.*/)) {
                Long start = Long.parseLong(it.split(/ /)[0])
                return (start..(start - 1 + Long.parseLong(it.split(/ /)[2])))
            }
            return null
        } - null

        List<Range<Long>> fertWaterRanges1 = fileAccess.text.split(/:/)[4].trim().split(/\n/).collect {
            if (it.matches(/\d+.*/)) {
                Long start = Long.parseLong(it.split(/ /)[1])
                return (start..(start - 1 + Long.parseLong(it.split(/ /)[2])))
            }
            return null
        } - null
        List<Range<Long>> fertWaterRanges2 = fileAccess.text.split(/:/)[4].trim().split(/\n/).collect {
            if (it.matches(/\d+.*/)) {
                Long start = Long.parseLong(it.split(/ /)[0])
                return (start..(start - 1 + Long.parseLong(it.split(/ /)[2])))
            }
            return null
        } - null

        List<Range<Long>> waterLightRanges1 = fileAccess.text.split(/:/)[5].trim().split(/\n/).collect {
            if (it.matches(/\d+.*/)) {
                Long start = Long.parseLong(it.split(/ /)[1])
                return (start..(start - 1 + Long.parseLong(it.split(/ /)[2])))
            }
            return null
        } - null
        List<Range<Long>> waterLightRanges2 = fileAccess.text.split(/:/)[5].trim().split(/\n/).collect {
            if (it.matches(/\d+.*/)) {
                Long start = Long.parseLong(it.split(/ /)[0])
                return (start..(start - 1 + Long.parseLong(it.split(/ /)[2])))
            }
            return null
        } - null

        List<Range<Long>> lightTempRanges1 = fileAccess.text.split(/:/)[6].trim().split(/\n/).collect {
            if (it.matches(/\d+.*/)) {
                Long start = Long.parseLong(it.split(/ /)[1])
                return (start..(start - 1 + Long.parseLong(it.split(/ /)[2])))
            }
            return null
        } - null
        List<Range<Long>> lightTempRanges2 = fileAccess.text.split(/:/)[6].trim().split(/\n/).collect {
            if (it.matches(/\d+.*/)) {
                Long start = Long.parseLong(it.split(/ /)[0])
                return (start..(start - 1 + Long.parseLong(it.split(/ /)[2])))
            }
            return null
        } - null

        List<Range<Long>> tempHumidRanges1 = fileAccess.text.split(/:/)[7].trim().split(/\n/).collect {
            if (it.matches(/\d+.*/)) {
                Long start = Long.parseLong(it.split(/ /)[1])
                return (start..(start - 1 + Long.parseLong(it.split(/ /)[2])))
            }
            return null
        } - null
        List<Range<Long>> tempHumidRanges2 = fileAccess.text.split(/:/)[7].trim().split(/\n/).collect {
            if (it.matches(/\d+.*/)) {
                Long start = Long.parseLong(it.split(/ /)[0])
                return (start..(start - 1 + Long.parseLong(it.split(/ /)[2])))
            }
            return null
        } - null

        List<Range<Long>> humidLocRanges1 = fileAccess.text.split(/:/)[8].trim().split(/\n/).collect {
            if (it.matches(/\d+.*/)) {
                Long start = Long.parseLong(it.split(/ /)[1])
                return (start..(start - 1 + Long.parseLong(it.split(/ /)[2])))
            }
            return null
        } - null
        List<Range<Long>> humidLocRanges2 = fileAccess.text.split(/:/)[8].trim().split(/\n/).collect {
            if (it.matches(/\d+.*/)) {
                Long start = Long.parseLong(it.split(/ /)[0])
                return (start..(start - 1 + Long.parseLong(it.split(/ /)[2])))
            }
            return null
        } - null
    }

    @Override
    Object partOne(FileAccess fileAccess) {
        List<Long> seeds = (fileAccess.lines.first().split(/:/)[1].split(/ /) - "").collect { Long.parseLong(it) }

        List<Range<Long>> seedSoilRanges1 = fileAccess.text.split(/:/)[2].trim().split(/\n/).collect {
            if (it.matches(/\d+.*/)) {
                Long start = Long.parseLong(it.split(/ /)[1])
                return (start..(start - 1 + Long.parseLong(it.split(/ /)[2])))
            }
            return null
        } - null
        List<Range<Long>> seedSoilRanges2 = fileAccess.text.split(/:/)[2].trim().split(/\n/).collect {
            if (it.matches(/\d+.*/)) {
                Long start = Long.parseLong(it.split(/ /)[0])
                return (start..(start - 1 + Long.parseLong(it.split(/ /)[2])))
            }
            return null
        } - null

        List<Range<Long>> soilFertRanges1 = fileAccess.text.split(/:/)[3].trim().split(/\n/).collect {
            if (it.matches(/\d+.*/)) {
                Long start = Long.parseLong(it.split(/ /)[1])
                return (start..(start - 1 + Long.parseLong(it.split(/ /)[2])))
            }
            return null
        } - null
        List<Range<Long>> soilFertRanges2 = fileAccess.text.split(/:/)[3].trim().split(/\n/).collect {
            if (it.matches(/\d+.*/)) {
                Long start = Long.parseLong(it.split(/ /)[0])
                return (start..(start - 1 + Long.parseLong(it.split(/ /)[2])))
            }
            return null
        } - null

        List<Range<Long>> fertWaterRanges1 = fileAccess.text.split(/:/)[4].trim().split(/\n/).collect {
            if (it.matches(/\d+.*/)) {
                Long start = Long.parseLong(it.split(/ /)[1])
                return (start..(start - 1 + Long.parseLong(it.split(/ /)[2])))
            }
            return null
        } - null
        List<Range<Long>> fertWaterRanges2 = fileAccess.text.split(/:/)[4].trim().split(/\n/).collect {
            if (it.matches(/\d+.*/)) {
                Long start = Long.parseLong(it.split(/ /)[0])
                return (start..(start - 1 + Long.parseLong(it.split(/ /)[2])))
            }
            return null
        } - null

        List<Range<Long>> waterLightRanges1 = fileAccess.text.split(/:/)[5].trim().split(/\n/).collect {
            if (it.matches(/\d+.*/)) {
                Long start = Long.parseLong(it.split(/ /)[1])
                return (start..(start - 1 + Long.parseLong(it.split(/ /)[2])))
            }
            return null
        } - null
        List<Range<Long>> waterLightRanges2 = fileAccess.text.split(/:/)[5].trim().split(/\n/).collect {
            if (it.matches(/\d+.*/)) {
                Long start = Long.parseLong(it.split(/ /)[0])
                return (start..(start - 1 + Long.parseLong(it.split(/ /)[2])))
            }
            return null
        } - null

        List<Range<Long>> lightTempRanges1 = fileAccess.text.split(/:/)[6].trim().split(/\n/).collect {
            if (it.matches(/\d+.*/)) {
                Long start = Long.parseLong(it.split(/ /)[1])
                return (start..(start - 1 + Long.parseLong(it.split(/ /)[2])))
            }
            return null
        } - null
        List<Range<Long>> lightTempRanges2 = fileAccess.text.split(/:/)[6].trim().split(/\n/).collect {
            if (it.matches(/\d+.*/)) {
                Long start = Long.parseLong(it.split(/ /)[0])
                return (start..(start - 1 + Long.parseLong(it.split(/ /)[2])))
            }
            return null
        } - null

        List<Range<Long>> tempHumidRanges1 = fileAccess.text.split(/:/)[7].trim().split(/\n/).collect {
            if (it.matches(/\d+.*/)) {
                Long start = Long.parseLong(it.split(/ /)[1])
                return (start..(start - 1 + Long.parseLong(it.split(/ /)[2])))
            }
            return null
        } - null
        List<Range<Long>> tempHumidRanges2 = fileAccess.text.split(/:/)[7].trim().split(/\n/).collect {
            if (it.matches(/\d+.*/)) {
                Long start = Long.parseLong(it.split(/ /)[0])
                return (start..(start - 1 + Long.parseLong(it.split(/ /)[2])))
            }
            return null
        } - null

        List<Range<Long>> humidLocRanges1 = fileAccess.text.split(/:/)[8].trim().split(/\n/).collect {
            if (it.matches(/\d+.*/)) {
                Long start = Long.parseLong(it.split(/ /)[1])
                return (start..(start - 1 + Long.parseLong(it.split(/ /)[2])))
            }
            return null
        } - null
        List<Range<Long>> humidLocRanges2 = fileAccess.text.split(/:/)[8].trim().split(/\n/).collect {
            if (it.matches(/\d+.*/)) {
                Long start = Long.parseLong(it.split(/ /)[0])
                return (start..(start - 1 + Long.parseLong(it.split(/ /)[2])))
            }
            return null
        } - null

        return seeds.collect { seed ->

            Long soil

            Long mapIndex = seedSoilRanges1.findIndexOf {
                seed >= it.first() && seed < it.first() + it.size()
            }
            if (mapIndex != -1) {
                Long index = seed - seedSoilRanges1[mapIndex].first()
                soil = seedSoilRanges2[mapIndex].first() + index
            } else {
                soil = seed
            }

            Long fert

            mapIndex = soilFertRanges1.findIndexOf { soil >= it.first() && soil < it.first() + it.size() }
            if (mapIndex != -1) {
                Long index = soil - soilFertRanges1[mapIndex].first()
                fert = soilFertRanges2[mapIndex].first() + index
            } else {
                fert = soil
            }

            Long water

            mapIndex = fertWaterRanges1.findIndexOf { fert >= it.first() && fert < it.first() + it.size() }
            if (mapIndex != -1) {
                Long index = fert - fertWaterRanges1[mapIndex].first()
                water = fertWaterRanges2[mapIndex].first() + index
            } else {
                water = fert
            }

            Long light

            mapIndex = waterLightRanges1.findIndexOf { water >= it.first() && water < it.first() + it.size() }
            if (mapIndex != -1) {
                Long index = water - waterLightRanges1[mapIndex].first()
                light = waterLightRanges2[mapIndex].first() + index
            } else {
                light = water
            }

            Long temp

            mapIndex = lightTempRanges1.findIndexOf { light >= it.first() && light < it.first() + it.size() }
            if (mapIndex != -1) {
                Long index = light - lightTempRanges1[mapIndex].first()
                temp = lightTempRanges2[mapIndex].first() + index
            } else {
                temp = light
            }

            Long humid

            mapIndex = tempHumidRanges1.findIndexOf { temp >= it.first() && temp < it.first() + it.size() }
            if (mapIndex != -1) {
                Long index = temp - tempHumidRanges1[mapIndex].first()
                humid = tempHumidRanges2[mapIndex].first() + index
            } else {
                humid = temp
            }

            Long loc

            mapIndex = humidLocRanges1.findIndexOf { humid >= it.first() && humid < it.first() + it.size() }
            if (mapIndex != -1) {
                Long index = humid - humidLocRanges1[mapIndex].first()
                loc = humidLocRanges2[mapIndex].first() + index
            } else {
                loc = humid
            }

            return loc
        }.min()
    }

    @Override
    Object partTwo(FileAccess fileAccess) {
        List<Long> seeds = (fileAccess.lines.first().split(/:/)[1].split(/ /) - "").collect { Long.parseLong(it) }

        List<Range<Long>> seedSoilRanges1 = fileAccess.text.split(/:/)[2].trim().split(/\n/).collect {
            if (it.matches(/\d+.*/)) {
                Long start = Long.parseLong(it.split(/ /)[1])
                return (start..(start - 1 + Long.parseLong(it.split(/ /)[2])))
            }
            return null
        } - null
        List<Range<Long>> seedSoilRanges2 = fileAccess.text.split(/:/)[2].trim().split(/\n/).collect {
            if (it.matches(/\d+.*/)) {
                Long start = Long.parseLong(it.split(/ /)[0])
                return (start..(start - 1 + Long.parseLong(it.split(/ /)[2])))
            }
            return null
        } - null

        List<Range<Long>> soilFertRanges1 = fileAccess.text.split(/:/)[3].trim().split(/\n/).collect {
            if (it.matches(/\d+.*/)) {
                Long start = Long.parseLong(it.split(/ /)[1])
                return (start..(start - 1 + Long.parseLong(it.split(/ /)[2])))
            }
            return null
        } - null
        List<Range<Long>> soilFertRanges2 = fileAccess.text.split(/:/)[3].trim().split(/\n/).collect {
            if (it.matches(/\d+.*/)) {
                Long start = Long.parseLong(it.split(/ /)[0])
                return (start..(start - 1 + Long.parseLong(it.split(/ /)[2])))
            }
            return null
        } - null

        List<Range<Long>> fertWaterRanges1 = fileAccess.text.split(/:/)[4].trim().split(/\n/).collect {
            if (it.matches(/\d+.*/)) {
                Long start = Long.parseLong(it.split(/ /)[1])
                return (start..(start - 1 + Long.parseLong(it.split(/ /)[2])))
            }
            return null
        } - null
        List<Range<Long>> fertWaterRanges2 = fileAccess.text.split(/:/)[4].trim().split(/\n/).collect {
            if (it.matches(/\d+.*/)) {
                Long start = Long.parseLong(it.split(/ /)[0])
                return (start..(start - 1 + Long.parseLong(it.split(/ /)[2])))
            }
            return null
        } - null

        List<Range<Long>> waterLightRanges1 = fileAccess.text.split(/:/)[5].trim().split(/\n/).collect {
            if (it.matches(/\d+.*/)) {
                Long start = Long.parseLong(it.split(/ /)[1])
                return (start..(start - 1 + Long.parseLong(it.split(/ /)[2])))
            }
            return null
        } - null
        List<Range<Long>> waterLightRanges2 = fileAccess.text.split(/:/)[5].trim().split(/\n/).collect {
            if (it.matches(/\d+.*/)) {
                Long start = Long.parseLong(it.split(/ /)[0])
                return (start..(start - 1 + Long.parseLong(it.split(/ /)[2])))
            }
            return null
        } - null

        List<Range<Long>> lightTempRanges1 = fileAccess.text.split(/:/)[6].trim().split(/\n/).collect {
            if (it.matches(/\d+.*/)) {
                Long start = Long.parseLong(it.split(/ /)[1])
                return (start..(start - 1 + Long.parseLong(it.split(/ /)[2])))
            }
            return null
        } - null
        List<Range<Long>> lightTempRanges2 = fileAccess.text.split(/:/)[6].trim().split(/\n/).collect {
            if (it.matches(/\d+.*/)) {
                Long start = Long.parseLong(it.split(/ /)[0])
                return (start..(start - 1 + Long.parseLong(it.split(/ /)[2])))
            }
            return null
        } - null

        List<Range<Long>> tempHumidRanges1 = fileAccess.text.split(/:/)[7].trim().split(/\n/).collect {
            if (it.matches(/\d+.*/)) {
                Long start = Long.parseLong(it.split(/ /)[1])
                return (start..(start - 1 + Long.parseLong(it.split(/ /)[2])))
            }
            return null
        } - null
        List<Range<Long>> tempHumidRanges2 = fileAccess.text.split(/:/)[7].trim().split(/\n/).collect {
            if (it.matches(/\d+.*/)) {
                Long start = Long.parseLong(it.split(/ /)[0])
                return (start..(start - 1 + Long.parseLong(it.split(/ /)[2])))
            }
            return null
        } - null

        List<Range<Long>> humidLocRanges1 = fileAccess.text.split(/:/)[8].trim().split(/\n/).collect {
            if (it.matches(/\d+.*/)) {
                Long start = Long.parseLong(it.split(/ /)[1])
                return (start..(start - 1 + Long.parseLong(it.split(/ /)[2])))
            }
            return null
        } - null
        List<Range<Long>> humidLocRanges2 = fileAccess.text.split(/:/)[8].trim().split(/\n/).collect {
            if (it.matches(/\d+.*/)) {
                Long start = Long.parseLong(it.split(/ /)[0])
                return (start..(start - 1 + Long.parseLong(it.split(/ /)[2])))
            }
            return null
        } - null


        List<Range<Long>> allSeeds = []
        seeds.eachWithIndex {seedLong, index ->
            if(index%2 == 0) {
                allSeeds << (seedLong..(seedLong+seeds[index+1]))
            }
        }

        Long locationIterator = 0
        Map skip = [:]
        Long foundSeed

        while (!foundSeed) {
            Long loc = locationIterator

            Long humid
            Long mapIndex = humidLocRanges2.findIndexOf { it.first() == 0 }
            if (mapIndex != -1) {
                Long index = loc - humidLocRanges2[mapIndex].first()
                humid = humidLocRanges1[mapIndex].first() + index
            } else {
                humid = loc
            }

            Long temp
            mapIndex = tempHumidRanges2.findIndexOf { it.getFrom() <= humid && it.getTo() >= humid }
            if (mapIndex != -1) {
                Long index = humid - tempHumidRanges2[mapIndex].first()
                temp = tempHumidRanges1[mapIndex].first() + index
            } else {
                temp = humid
            }

            Long light
            mapIndex = lightTempRanges2.findIndexOf { it.getFrom() <= temp && it.getTo() >= temp }
            if (mapIndex != -1) {
                Long index = temp - lightTempRanges2[mapIndex].first()
                light = lightTempRanges1[mapIndex].first() + index
            } else {
                light = temp
            }

            Long water
            mapIndex = waterLightRanges2.findIndexOf { it.getFrom() <= light && it.getTo() >= light }
            if (mapIndex != -1) {
                water = light - waterLightRanges2[mapIndex].first() + waterLightRanges1[mapIndex].first()
            } else {
                water = light
            }

            Long fert
            mapIndex = fertWaterRanges2.findIndexOf { it.getFrom() <= water && it.getTo() >= water }
            if (mapIndex != -1) {
                fert = water - fertWaterRanges2[mapIndex].first() + fertWaterRanges1[mapIndex].first()
            } else {
                fert = water
            }

            Long soil
            mapIndex = soilFertRanges2.findIndexOf { it.getFrom() <= fert && it.getTo() >= fert }
            if (mapIndex != -1) {
                soil = fert - soilFertRanges2[mapIndex].first() + soilFertRanges1[mapIndex].first()
            } else {
                soil = fert
            }

            Long seed
            mapIndex = seedSoilRanges2.findIndexOf { it.getFrom() <= soil && it.getTo() >= soil }
            if (mapIndex != -1) {
                seed = soil - seedSoilRanges2[mapIndex].first() + seedSoilRanges1[mapIndex].first()
            } else {
                seed = soil
            }

            if(allSeeds.find {
                seed > it.getFrom() && seed < it.getTo()
            }) {
                foundSeed = loc
            }

            locationIterator += 1
        }
        return foundSeed

//        return seeds.collect { seed ->
//
//            Long soil
//
//            Long mapIndex = seedSoilRanges1.findIndexOf {
//                seed >= it.first() && seed < it.first() + it.size()
//            }
//            if (mapIndex != -1) {
//                Long index = seed - seedSoilRanges1[mapIndex].first()
//                soil = seedSoilRanges2[mapIndex].first() + index
//            } else {
//                soil = seed
//            }
//
//            Long fert
//
//            mapIndex = soilFertRanges1.findIndexOf { soil >= it.first() && soil < it.first() + it.size() }
//            if (mapIndex != -1) {
//                Long index = soil - soilFertRanges1[mapIndex].first()
//                fert = soilFertRanges2[mapIndex].first() + index
//            } else {
//                fert = soil
//            }
//
//            Long water
//
//            mapIndex = fertWaterRanges1.findIndexOf { fert >= it.first() && fert < it.first() + it.size() }
//            if (mapIndex != -1) {
//                Long index = fert - fertWaterRanges1[mapIndex].first()
//                water = fertWaterRanges2[mapIndex].first() + index
//            } else {
//                water = fert
//            }
//
//            Long light
//
//            mapIndex = waterLightRanges1.findIndexOf { water >= it.first() && water < it.first() + it.size() }
//            if (mapIndex != -1) {
//                Long index = water - waterLightRanges1[mapIndex].first()
//                light = waterLightRanges2[mapIndex].first() + index
//            } else {
//                light = water
//            }
//
//            Long temp
//
//            mapIndex = lightTempRanges1.findIndexOf { light >= it.first() && light < it.first() + it.size() }
//            if (mapIndex != -1) {
//                Long index = light - lightTempRanges1[mapIndex].first()
//                temp = lightTempRanges2[mapIndex].first() + index
//            } else {
//                temp = light
//            }
//
//            Long humid
//
//            mapIndex = tempHumidRanges1.findIndexOf { temp >= it.first() && temp < it.first() + it.size() }
//            if (mapIndex != -1) {
//                Long index = temp - tempHumidRanges1[mapIndex].first()
//                humid = tempHumidRanges2[mapIndex].first() + index
//            } else {
//                humid = temp
//            }
//
//            Long loc
//
//            mapIndex = humidLocRanges1.findIndexOf { humid >= it.first() && humid < it.first() + it.size() }
//            if (mapIndex != -1) {
//                Long index = humid - humidLocRanges1[mapIndex].first()
//                loc = humidLocRanges2[mapIndex].first() + index
//            } else {
//                loc = humid
//            }
//
//            return loc
//        }.min()
    }
}
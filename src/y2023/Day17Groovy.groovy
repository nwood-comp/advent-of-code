//package y2023
//
//import common.DayRunner
//import common.FileAccess
//import common.IntCoord2D
//import common.IntGrid2D
//
//import java.util.stream.Collectors
//
//class Day17 extends DayRunner {
//    Day17() {
//        super(null, false);
//    }
//
//    public static void main(String[] args) {
//        new Day17().run();
//    }
//
//    IntGrid2D<Integer> parseInput(FileAccess fileAccess) {
//        Integer[][] input = fileAccess.lines.stream().map { it.toList().stream().map { new Integer(it as Character) }.collect(Collectors.toList()) }.collect(Collectors.toList())
//
//        return IntGrid2D<Integer>.parse(fileAccess.collect { it.collect { new Integer(it as Character) } } as Integer[][])
//    }
//
//    Map<Tuple3<IntCoord2D, Integer, IntCoord2D>, Integer> cache = new HashMap<>();
//
//    @Override
//    public Object partOne(FileAccess fileAccess) {
//        IntGrid2D<Integer> parsedInput = parseInput(fileAccess);
//        int[] extrema = parsedInput.extrema();
//        IntCoord2D end = IntCoord2D.of(extrema[2], extrema[3]);
//        IntCoord2D start = IntCoord2D.of(0, 0);
//        Integer startLocation = parsedInput.get(start);
//        startLocation.distance = 0;
//        int min = 999999;
//        List<Search> searches = new ArrayList<>();
//        searches.add(new Search(start));
//
//        while (!searches.isEmpty()) {
//            Search search = searches.remove(0);
//            // search is at end, save min and early exit
//            if (search.position.equals(end)) {
//                min = Math.min(min, search.totalCooling);
//                System.out.println(min);
//                continue;
//            } else {
//                // all neighbors to current position that are in the grid
//                List<IntCoord2D> neighbors = search.position.getNeighbors(false).stream().filter(parsedInput::containsKey).toList();
//                for (IntCoord2D neighbor : neighbors) {
//                    Search newSearch = search.moveSearch(search.position.translationTo(neighbor), parsedInput.get(neighbor).coolingValue);
//                    //TODO less than 4
//                    // right right right
//                    // right, 3, position
//
//                    //up right down
//                    // down, 1 position
//
//                    //right right down
//                    //direction, consecutive, position
//                    Tuple3<IntCoord2D, Integer, IntCoord2D> cacheKey = newSearch.createCacheKey();
//                    Integer cacheResult = cache.get(cacheKey);
//                    boolean isBacktracking = newSearch.lastVelocities.size() > 1 && newSearch.lastVelocities.get(newSearch.lastVelocities.size() - 1).translate(newSearch.lastVelocities.get(newSearch.lastVelocities.size() - 2)).equals(IntCoord2D.of(0, 0));
//                    if (isBacktracking) {
//                        continue;
//                    } else if (newSearch.totalCooling > min) {
//                        continue;
//                    } else if (cacheResult != null && cacheResult < newSearch.totalCooling) {
//                        continue;
//                    }
//                    if (newSearch.isValid()) {
////                        if (newSearch.trackingAllVelocities == [IntCoord2D.of(1, 0), IntCoord2D.of(2, 0), IntCoord2D.of(2, 1), IntCoord2D.of(3, 1), IntCoord2D.of(4, 1), IntCoord2D.of(5, 1), IntCoord2D.of(5, 0), IntCoord2D.of(6, 0), IntCoord2D.of(7, 0), IntCoord2D.of(8, 0), IntCoord2D.of(8, 1), IntCoord2D.of(8, 2), IntCoord2D.of(9, 2), IntCoord2D.of(10, 2), IntCoord2D.of(10, 3), IntCoord2D.of(10, 4), IntCoord2D.of(11, 4), IntCoord2D.of(11, 5), IntCoord2D.of(11, 6), IntCoord2D.of(11, 7), IntCoord2D.of(12, 7), IntCoord2D.of(12, 8), IntCoord2D.of(12, 9), IntCoord2D.of(12, 10), IntCoord2D.of(11, 10), IntCoord2D.of(11, 11), IntCoord2D.of(11, 12)][0..2]) {
////                            true
////                        }
//                        searches.add(newSearch);
//                        cache.put(cacheKey, newSearch.getTotalCooling());
//                    }
//                }
//            }
//        }
//        return min;
//    }
//
////        while (allPaths.keySet()) {
////            Map<LinkedHashMap<IntCoord2D, Integer>, Set<IntCoord2D>> newPaths = [:]
////            allPaths.each { pathToUnvisited ->
////                LinkedHashMap<IntCoord2D, Integer> path = pathToUnvisited.key
////                Set<IntCoord2D> unvisited = pathToUnvisited.value
////
////                List<IntCoord2D> pathKeys = path.keySet().toList()
////                IntCoord2D current = pathKeys.last()
////                List<IntCoord2D> lastTranslations = []
////                if (pathKeys.size() >= 4) {
////                    pathKeys.subList(pathKeys.size() - 3, pathKeys.size()).reverse().eachWithIndex { IntCoord2D entry, int i ->
////                        IntCoord2D previous = pathKeys[pathKeys.size() - 1 - 1 - i]
////                        lastTranslations << entry.translationFrom(previous)
////                    }
////                }
////
////                List<IntCoord2D> mapNeighborsWithoutBacktracks = current.getNeighbors(false).findAll {
////                    parsedInput.containsKey(it) && unvisited.contains(it)
////                }
////
////                mapNeighborsWithoutBacktracks.each {
////                    List<IntCoord2D> theseTranslations = new ArrayList<IntCoord2D>(lastTranslations)
////                    theseTranslations << it.translationFrom(current)
////
////                    if (theseTranslations.size() == 4 && theseTranslations.unique().size() == 1) {
////                        return
////                    }
////                    int tentativeDistance = path.values().last() + parsedInput.get(it).coolingValue
////
////                    if (tentativeDistance >= min) {
////                        return
////                    }
////
////                    if (it == end) {
////                        min = Math.min(min, tentativeDistance)
////                    } else {
////                        LinkedHashMap<IntCoord2D, Integer> newPath
////                        if (path.keySet().size() < 4) {
////                            newPath = new LinkedHashMap<IntCoord2D, Integer>(path)
////                        } else {
////                            newPath = [:] as LinkedHashMap
////                            path.entrySet().toList().subList(path.keySet().size() - 4, path.keySet().size()).each {
////                                newPath.put(it.key, it.value)
////                            }
////                        }
////                        newPath.put(it, tentativeDistance)
////                        Map<IntCoord2D, Integer> matchingPath = newPaths.keySet().find { it.keySet() == newPath }
//////                        if (pathKeys == [IntCoord2D.of(2, 0), IntCoord2D.of(2, 1), IntCoord2D.of(3, 1), IntCoord2D.of(4, 1), IntCoord2D.of(5, 1), IntCoord2D.of(5, 0), IntCoord2D.of(6, 0), IntCoord2D.of(7, 0), IntCoord2D.of(8, 0), IntCoord2D.of(8,1), IntCoord2D.of(8,2), IntCoord2D.of(9,2), IntCoord2D.of(10,2), IntCoord2D.of(10,3), IntCoord2D.of(10,4), IntCoord2D.of(11,4), IntCoord2D.of(11,5), IntCoord2D.of(11,6), IntCoord2D.of(11,7), IntCoord2D.of(12,7), IntCoord2D.of(12,8), IntCoord2D.of(12,9), IntCoord2D.of(12,10), IntCoord2D.of(11,10), IntCoord2D.of(11,11), IntCoord2D.of(11,12)][-20..-16]) {
//////                            true
//////                        }
////                        if (!matchingPath) {
//////                            if(newPath.keySet() == [IntCoord2D.of(7, 0), IntCoord2D.of(8, 0), IntCoord2D.of(8,1), IntCoord2D.of(8,2), IntCoord2D.of(9,2)] as Set) {
//////                                true
//////                            }
////                            Set<IntCoord2D> newUnvisited = new HashSet<IntCoord2D>(unvisited)
////                            newUnvisited.remove(it)
////                            newPaths.put(newPath, newUnvisited)
////                        } else if (matchingPath && matchingPath.values().last() > newPath.values().last()) {
//////                            if(newPath.keySet() == [IntCoord2D.of(7, 0), IntCoord2D.of(8, 0), IntCoord2D.of(8,1), IntCoord2D.of(8,2), IntCoord2D.of(9,2)] as Set) {
//////                                true
//////                            }
////                            Set<IntCoord2D> newUnvisited = new HashSet<IntCoord2D>(unvisited)
////                            newUnvisited.remove(it)
////                            newPaths.put(newPath, newUnvisited)
////                        }
////                    }
////                }
////            }
////
////            allPaths = newPaths
////        }
//
//    @Override
//    public Object partTwo(FileAccess fileAccess) {
//        Object parsedInput = parseInput(fileAccess);
//        return null;
//    }
//}
//
//class Search {
//    List<IntCoord2D> lastVelocities = new ArrayList<>();
//    List<IntCoord2D> trackingAllVelocities = new ArrayList<>();
//    IntCoord2D position;
//    int totalCooling = 0;
//
//    Search(IntCoord2D position) {
//        this.position = position;
//    }
//
//    public int getTotalCooling() {
//        return totalCooling;
//    }
//
//    Search(IntCoord2D position, List<IntCoord2D> lastVelocities, int cooling, List<IntCoord2D> trackingAllVelocities) {
//        this.position = position;
//        this.lastVelocities = lastVelocities;
//        this.totalCooling = cooling;
//        this.trackingAllVelocities = trackingAllVelocities;
//    }
//
//    Search moveSearch(IntCoord2D velocity, int cooling) {
//        IntCoord2D newPosition = this.position.translate(velocity);
//
//        List<IntCoord2D> newLastVelocities = new ArrayList<>(lastVelocities);
//        if (newLastVelocities.size() == 4) {
//            newLastVelocities.remove(0);
//        } else if (newLastVelocities.size() > 4) {
//            throw new RuntimeException("should not be greater than 4");
//        }
//        newLastVelocities.add(velocity);
//        trackingAllVelocities.add(velocity);
//
//        int newCooling = totalCooling + cooling;
//
//        return new Search(newPosition, newLastVelocities, newCooling, trackingAllVelocities);
//    }
//
//    boolean isValid() {
//        int size = lastVelocities.size();
//        return !(size > 3 && new HashSet<>(lastVelocities.subList(size - 4, size)).size() == 1);
//    }
//
//    public Tuple3<IntCoord2D, Integer, IntCoord2D> createCacheKey() {
//        IntCoord2D direction = lastVelocities.get(lastVelocities.size() - 1);
//        Integer consecutive = 0;
//        for (int i = lastVelocities.size() - 1; i >= 0; i--) {
//            if (lastVelocities.get(i) == direction) {
//                consecutive++;
//            } else {
//                break;
//            }
//        }
//        return new Tuple3<>(direction, consecutive, position);
//    }
//}
//
//class Integer {
//    int coolingValue;
//    int distance = 99999;
//
//    Integer(Character coolingValue) {
//        this.coolingValue = Integer.parseInt(coolingValue.toString());
//    }
//}
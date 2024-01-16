package y2023;

import common.*;

import java.util.*;

class Day17 extends DayRunner {
    Day17() {
        super(null, false);
    }

    public static void main(String[] args) {
        new Day17().run();
    }

    IntGrid2D<Integer> parseInput(FileAccess fileAccess) {
        return Parser.parseInput(fileAccess);
    }

    Map<StateKey, Search> cache = new HashMap<>();

    @Override
    public Object partOne(FileAccess fileAccess) {
        IntGrid2D<Integer> grid = parseInput(fileAccess);
        int[] extrema = grid.extrema();
        IntCoord2D end = IntCoord2D.of(extrema[2], extrema[3]);
        IntCoord2D start = IntCoord2D.of(0, 0);
        grid.put(start, 0);

        int min = Integer.MAX_VALUE;

        Queue<Search> searches = new PriorityQueue<>();
        searches.add(new Search(new StateKey(start, IntCoord2D.of(1, 0), 0), 0));
        searches.add(new Search(new StateKey(start, IntCoord2D.of(0, 1), 0), 0));

        while (!searches.isEmpty()) {
            Search search = searches.remove();
            if (search.stateKey.position.equals(end)) {
                min = Math.min(min, search.totalCooling);
                System.out.println(min);
                continue;
            } else {
                List<Search> neighborSearches = search.stateKey.nextSearches().stream().map(neighbor -> new Search(neighbor, search.totalCooling + grid.get(neighbor.position))).toList();
                for (Search neighborSearch : neighborSearches) {
                    Search cacheResult = cache.get(neighborSearch.stateKey);
                    if (cacheResult != null && cacheResult.totalCooling <= neighborSearch.totalCooling) {
                        continue;
                    } else if (neighborSearch.totalCooling >= min) {
                        continue;
                    }

                    searches.add(neighborSearch);
                    cache.put(neighborSearch.stateKey, neighborSearch);
                }
            }
        }
        return min;
    }

    @Override
    public Object partTwo(FileAccess fileAccess) {
        cache.clear();
        IntGrid2D<Integer> grid = parseInput(fileAccess);
        int[] extrema = grid.extrema();
        IntCoord2D end = IntCoord2D.of(extrema[2], extrema[3]);
        IntCoord2D start = IntCoord2D.of(0, 0);
        grid.put(start, 0);

        int min = Integer.MAX_VALUE;

        Queue<Search> searches = new PriorityQueue<>();
        searches.add(new Search(new StateKey(start, IntCoord2D.of(1, 0), 0), 0));
        searches.add(new Search(new StateKey(start, IntCoord2D.of(0, 1), 0), 0));

        while (!searches.isEmpty()) {
            Search search = searches.remove();
            if (search.stateKey.position.equals(end)) {
                min = Math.min(min, search.totalCooling);
                System.out.println(min);
                continue;
            } else {
                List<Search> neighborSearches = search.stateKey.nextSearches2().stream().map(neighbor -> new Search(neighbor, search.totalCooling + grid.get(neighbor.position))).toList();
                for (Search neighborSearch : neighborSearches) {
                    Search cacheResult = cache.get(neighborSearch.stateKey);
                    if (cacheResult != null && cacheResult.totalCooling <= neighborSearch.totalCooling) {
                        continue;
                    } else if (neighborSearch.totalCooling >= min) {
                        continue;
                    }

                    searches.add(neighborSearch);
                    cache.put(neighborSearch.stateKey, neighborSearch);
                }
            }
        }
        return min;
    }
}

class Search implements Comparable<Search> {
    StateKey stateKey;
    int totalCooling;

    public Search(StateKey stateKey, int totalCooling) {
        this.stateKey = stateKey;
        this.totalCooling = totalCooling;
    }

    @Override
    public int compareTo(Search o) {
        return java.lang.Integer.compare(this.totalCooling, o.totalCooling);
    }
}

class StateKey {
    IntCoord2D position;
    IntCoord2D direction;
    int consecutive;
    int hashCode;

    public StateKey(IntCoord2D position, IntCoord2D direction, int consecutive) {
        this.position = position;
        this.direction = direction;
        this.consecutive = consecutive;
        this.hashCode = Objects.hash(this.position, this.direction, this.consecutive);
    }

    List<StateKey> nextSearches() {
        List<StateKey> next = new ArrayList<>();
        for (IntCoord2D neighbor : this.position.getNeighbors(false)) {
            if (isInBounds(neighbor)) {
                final IntCoord2D dir = this.position.translationTo(neighbor);
                if (this.direction.equals(dir)) {
                    if (this.consecutive < 3) {
                        next.add(new StateKey(neighbor, this.direction, this.consecutive + 1));
                    }
                } else if (!this.direction.translate(dir).equals(IntCoord2D.ZERO)) {
                    next.add(new StateKey(neighbor, dir, 1));
                }
            }
        }
        return next;
    }

    public List<StateKey> nextSearches2() {
        List<StateKey> next = new ArrayList<>();
        for (IntCoord2D neighbor : this.position.getNeighbors(false)) {
            if (isInBounds(neighbor)) {
                final IntCoord2D dir = this.position.translationTo(neighbor);
                if (!this.direction.translate(dir).equals(IntCoord2D.ZERO)) {
                    if (consecutive < 4) {
                        if (this.direction.equals(dir)) {
                            next.add(new StateKey(neighbor, this.direction, this.consecutive + 1));
                        }
                    } else if (consecutive > 9) {
                        if (!this.direction.equals(dir)) {
                            next.add(new StateKey(neighbor, dir, 1));
                        }
                    } else {
                        if (this.direction.equals(dir)) {
                            next.add(new StateKey(neighbor, dir, consecutive + 1));
                        } else {
                            next.add(new StateKey(neighbor, dir, 1));
                        }
                    }
                }
            }
        }
        return next;
    }

    private boolean isInBounds(IntCoord2D neighbor) {
        return neighbor.x() > -1 && neighbor.x() < 141 && neighbor.y() > -1 && neighbor.y() < 141;
//        return neighbor.x() > -1 && neighbor.x() < 12 && neighbor.y() > -1 && neighbor.y() < 5;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StateKey stateKey = (StateKey) o;
        return consecutive == stateKey.consecutive && Objects.equals(position, stateKey.position) && Objects.equals(direction, stateKey.direction);
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }
}
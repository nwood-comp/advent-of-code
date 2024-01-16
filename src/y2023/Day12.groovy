package y2023

import common.DayRunner
import common.FileAccess

class Day12 extends DayRunner {
    Day12() {
        super(null, false);
    }

    public static void main(String[] args) {
        new Day12().run();
    }

//    List<Tuple2<String, List<Integer>>> parseInput(FileAccess fileAccess) {
//        List<Tuple2<String, List<Integer>>> springsCounts = fileAccess.collect {
//            String springs = it.split(/ /)[0]
//            List<Integer> counts = it.split(/ /)[1].split(/,/).collect { Integer.parseInt(it) }
//            return new Tuple2<String, List<Integer>>(springs, counts)
//        }
//        return springsCounts
//    }

    static final Map<String, Long> memo = new HashMap<>();

    @Override
    public Object partOne(FileAccess fileAccess) {
//        List<Tuple2<String, List<Integer>>> parsedInput = parseInput(fileAccess)
//
//        Long sum = 0
//        parsedInput.eachWithIndex { springsCounts, index ->
//            String springs = springsCounts.v1
//            List<Integer> counts = springsCounts.v2
//
//            Long result = arrangements(0, springs, counts)
//            sum += result
//            println("line $index: $sum")
//        }
        List<Tuple2<String, List<Integer>>> springsCountList = new ArrayList<>();
        for (String it : fileAccess) {
            String springs = it.split(" ")[0];
            List<Integer> counts = new ArrayList<>();
            for (String otherIt : it.split(" ")[1].split(",")) {
                counts.add(Integer.valueOf(otherIt));
            }
            springsCountList.add(new Tuple2<>(springs, counts));
        }


        Long sum = 0L;
        for (Tuple2<String, List<Integer>> springsCounts : springsCountList) {
            String springs = springsCounts.getV1();
            List<Integer> counts = springsCounts.getV2();

            Long result = arrangements(0, springs, counts);
            sum += result;
        }

        return sum;
    }

    @Override
    public Object partTwo(FileAccess fileAccess) {

        List<Tuple2<String, List<Integer>>> springsCountList = new ArrayList<>();
        for (String it : fileAccess) {
            String springs = it.split(" ")[0];
            List<Integer> counts = new ArrayList<>();
            for (String otherIt : it.split(" ")[1].split(",")) {
                counts.add(Integer.valueOf(otherIt));
            }
            springsCountList.add(new Tuple2<>(springs, counts));
        }


        Long sum = 0L;
        for (Tuple2<String, List<Integer>> springsCounts : springsCountList) {
            String springs = springsCounts.getV1() + '?' + springsCounts.getV1() + '?' + springsCounts.getV1() + '?' + springsCounts.getV1() + '?' + springsCounts.getV1();
            List<Integer> counts = new ArrayList<>(springsCounts.getV2());
            counts.addAll(springsCounts.getV2());
            counts.addAll(springsCounts.getV2());
            counts.addAll(springsCounts.getV2());
            counts.addAll(springsCounts.getV2());

            Long result = arrangements(0, springs, counts);
            sum += result;
        }

        return sum;
    }

    Long arrangements(int consecutive, String springs, List<Integer> counts) {
        if (springs.isEmpty()) {
            if (consecutive == 0 && counts.isEmpty()) {
                return 1L;
            } else if (Objects.equals(counts, List.of(consecutive))) {
                return 1L;
            }
            return 0L;
        }

        Long memoResult = getMemo(consecutive, springs, counts);
        if (memoResult != null) {
            return memoResult;
        }

        String next = springs.substring(0, 1);

        Long result = null;

        if (next.equals("?")) {
            String hashReplaced = "#" + springs.substring(1);
            String dotReplaced = "." + springs.substring(1);
            result = arrangements(consecutive, hashReplaced, counts) + arrangements(consecutive, dotReplaced, counts);
        } else if (next.equals("#")) {
            if (counts.size() > 0 && consecutive >= counts.get(0)) {
                result = 0L;
            } else {
                result = arrangements(consecutive + 1, springs.substring(1), counts);
            }
        } else if (next.equals(".")) {
            if (consecutive != 0 && (counts.isEmpty() || consecutive != counts.get(0))) {
                result = 0L;
            } else if (consecutive == 0) {
                result = arrangements(0, springs.substring(1), counts);
            } else {
                result = arrangements(0, springs.substring(1), counts.subList(1, counts.size()));
            }
        }
        saveMemo(consecutive, springs, counts, result);
        return result;
    }

    Long getMemo(int consecutive, String springs, List<Integer> counts) {
        return memo.get(consecutive + springs + counts);
    }

    void saveMemo(int consecutive, String springs, List<Integer> counts, Long i) {
        memo.put(consecutive + springs + counts, i);
    }
}

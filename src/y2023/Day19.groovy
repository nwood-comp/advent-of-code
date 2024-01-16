package y2023

import common.DayRunner
import common.FileAccess

class Day19 extends DayRunner {
    Day19() {
        super(null, false);
    }

    static void main(String[] args) {
        new Day19().run();
    }

    Tuple2<Map<String, List<Condition>>, List<Part>> parseInput(FileAccess fileAccess) {
        Map<String, List<Condition>> workflows = fileAccess.text.split(/\n\n/)[0].split(/\n/).collectEntries {
            String id = it.split(/\{/)[0]
            List<Condition> conditions = it.split(/\{/)[1].split(/}/)[0].split(/[,:]/).collect {
                if (it.startsWith('A')) {
                    return new Condition(true, false, null, null)
                } else if (it.startsWith('R')) {
                    return new Condition(false, true, null, null)
                } else if (!(it =~ /[<>=]/)) {
                    return new Condition(false, false, it, null)
                } else {
                    return new Condition(false, false, null, { Part part -> (Boolean) Eval.x(part, "x.$it") } as Closure<Part>)
                }
            }
            return [(id): conditions]
        }

        List<Part> parts = fileAccess.text.split(/\n\n/)[1].split(/\n/).collect {
            new Part(it)
        }

        return new Tuple2<>(workflows, parts)
    }

    Map<String, List<Condition2>> parseInput2(FileAccess fileAccess) {
        Map<String, List<Condition2>> workflows = fileAccess.text.split(/\n\n/)[0].split(/\n/).collectEntries {
            String id = it.split(/\{/)[0]
            List<Condition2> conditions = []
            it.split(/\{/)[1].split(/}/)[0].split(/[,:]/).each {
                boolean isPositiveCase = conditions.size() > 0 && conditions[conditions.size() - 1].ranges != null
                boolean isNegativeCase = conditions.size() > 0 && conditions[conditions.size() - 1]?.isPositiveCase
                boolean isAccept = it.startsWith('A')
                String next = null
                Ranges ranges = null
                if (!(it =~ /[<>=]/)) {
                    next = it
                } else {
                    Integer rangeNum = Integer.parseInt(it.split(/[<>]/)[1])
                    MyIntRange range = null
                    if (it.contains('>')) {
                        range = new MyIntRange(rangeNum + 1, 4000)
                    } else if (it.contains('<')) {
                        range = new MyIntRange(1, rangeNum - 1)
                    }
                    if (it.startsWith('x')) {
                        ranges = new Ranges(range, null, null, null)
                    } else if (it.startsWith('m')) {
                        ranges = new Ranges(null, range, null, null)
                    } else if (it.startsWith('a')) {
                        ranges = new Ranges(null, null, range, null)
                    } else if (it.startsWith('s')) {
                        ranges = new Ranges(null, null, null, range)
                    }
                }
                conditions << new Condition2(isAccept, isPositiveCase, isNegativeCase, next, ranges)
            }
            return [(id): conditions]
        }
    }

    @Override
    Object partOne(FileAccess fileAccess) {
        Tuple2<Map<String, List<Condition>>, List<Part>> parsedInput = parseInput(fileAccess)
        Map<String, List<Condition>> workflows = parsedInput.v1
        return parsedInput.v2.sum { part ->
            List<Condition> workflow = workflows['in']
            Boolean conditionResult = null
            int index = 0
            Condition evaluateCondition = workflow[index]
            while (conditionResult == null) {
                String exitEarly = evaluateCondition.exitEarly()
                if (exitEarly == 'R') {
                    conditionResult = false
                } else if (exitEarly == 'A') {
                    conditionResult = true
                } else if (exitEarly) {
                    workflow = workflows[exitEarly]
                    evaluateCondition = workflow[0]
                    index = 0
                } else {
                    index += evaluateCondition.evaluate(part) ? 1 : 2
                    evaluateCondition = workflow[index]
                }
            }
            if (conditionResult) {
                return part.x + part.m + part.a + part.s
            } else {
                return 0
            }
        }
    }

    class Condition {
        boolean accept
        boolean reject
        String nextCondition
        Closure<Part> evaluation

        Condition(boolean accept, boolean reject, String nextCondition, Closure<Part> evaluation) {
            this.accept = accept
            this.reject = reject
            this.nextCondition = nextCondition
            this.evaluation = evaluation
        }

        String exitEarly() {
            if (accept) {
                return 'A'
            } else if (reject) {
                return 'R'
            } else if (nextCondition) {
                return nextCondition
            }
            return null
        }

        Boolean evaluate(Part part) {
            return evaluation(part)
        }
    }

    class Condition2 {
        boolean accept
        boolean isPositiveCase
        boolean isNegativeCase
        String nextWorkflow
        Ranges ranges

        Condition2(boolean accept, boolean isPositiveCase, boolean isNegativeCase, String nextWorkflow, Ranges ranges) {
            this.accept = accept
            this.isPositiveCase = isPositiveCase
            this.isNegativeCase = isNegativeCase
            this.nextWorkflow = nextWorkflow
            this.ranges = ranges
        }
    }

    class Part {
        int x
        int m
        int a
        int s

        Part(String input) {
            List<String> split = input.split(/[=,}]/)
            this.x = Integer.parseInt(split[1])
            this.m = Integer.parseInt(split[3])
            this.a = Integer.parseInt(split[5])
            this.s = Integer.parseInt(split[7])
        }
    }

    class MyIntRange extends IntRange {
        boolean emptyTracker

        MyIntRange(int from, int to) {
            super(from, to)
            this.emptyTracker = false
        }

        MyIntRange(boolean emptyTracker) {
            super(0, 0)
            this.emptyTracker = emptyTracker
        }

        MyIntRange rangeIntersect(IntRange with) {
            if (emptyTracker) {
                return new MyIntRange(true)
            }
            if (this.from > with.to || this.to < with.from) {
                return new MyIntRange(true)
            } else {
                return new MyIntRange(Math.max(this.from, with.from), Math.min(this.to, with.to))
            }
        }

        Integer rangeSize() {
            if (this.emptyTracker || this.from == 0 && this.to == 0) {
                return 0
            } else {
                return this.size()
            }
        }
    }

    class Ranges {
        MyIntRange x
        MyIntRange m
        MyIntRange a
        MyIntRange s

        Ranges() {
            this.x = new MyIntRange(1, 4000)
            this.m = new MyIntRange(1, 4000)
            this.a = new MyIntRange(1, 4000)
            this.s = new MyIntRange(1, 4000)
        }

        Ranges(MyIntRange x, MyIntRange m, MyIntRange a, MyIntRange s) {
            this.x = x ?: new MyIntRange(1, 4000)
            this.m = m ?: new MyIntRange(1, 4000)
            this.a = a ?: new MyIntRange(1, 4000)
            this.s = s ?: new MyIntRange(1, 4000)
        }

        Ranges intersect(Ranges with) {
            Ranges range = new Ranges()
            range.x = this.x.rangeIntersect(with.x)
            range.m = this.m.rangeIntersect(with.m)
            range.a = this.a.rangeIntersect(with.a)
            range.s = this.s.rangeIntersect(with.s)

            return range
        }

        Ranges opposite() {
            Ranges range = new Ranges()
            range.x = oppositeOfRange(this.x)
            range.m = oppositeOfRange(this.m)
            range.a = oppositeOfRange(this.a)
            range.s = oppositeOfRange(this.s)

            return range
        }

        private MyIntRange oppositeOfRange(MyIntRange range) {
            if (range.getFrom() == 1 && range.getTo() != 4000) {
                return new MyIntRange(range.getTo() + 1, 4000)
            } else if (range.getFrom() != 1 && range.getTo() == 4000) {
                return new MyIntRange(1, range.getFrom() - 1)
            }
            return new MyIntRange(range.getFrom(), range.getTo())
        }

        BigDecimal rangesPermutationsCount() {
            return (BigDecimal) this.x.rangeSize() * (BigDecimal) this.m.rangeSize() * (BigDecimal) this.a.rangeSize() * (BigDecimal) this.s.rangeSize()
        }
    }

    Ranges findRangesForPath(Map<String, List<Condition2>> workflows, Map.Entry<String, List<Condition2>> workflow, Condition2 condition, Ranges ranges, Boolean flipNextCase = false) {
        int conditionIndex = workflow.value.indexOf(condition)

        Ranges shouldFlipAndMerge = null
        if (condition.ranges) {
            shouldFlipAndMerge = flipNextCase ? ranges.intersect(condition.ranges.opposite()) : ranges.intersect(condition.ranges)
        }
        if (workflow.key == 'in' && conditionIndex == 0) return shouldFlipAndMerge;

        if (condition.isPositiveCase) {
            if (condition.ranges) {
                return findRangesForPath(workflows, workflow, workflow.value[conditionIndex - 1], shouldFlipAndMerge)
            }
            return findRangesForPath(workflows, workflow, workflow.value[conditionIndex - 1], ranges)
        } else if (condition.isNegativeCase) {
            if (condition.ranges) {
                return findRangesForPath(workflows, workflow, workflow.value[conditionIndex - 2], shouldFlipAndMerge, true)
            }
            return findRangesForPath(workflows, workflow, workflow.value[conditionIndex - 2], ranges, true)
        } else if (conditionIndex == 0) {
            Map<String, List<Condition2>> leadingWorkflowsMap = workflows.findAll { thisWorkflow ->
                thisWorkflow.value.any {
                    it.nextWorkflow == workflow.key
                }
            } as Map<String, List<Condition2>>
            List<Map.Entry<String, List<Condition2>>> leadingWorkflows = leadingWorkflowsMap.collect { it }
            if (leadingWorkflows.size() != 1) {
                'multiple or no conditions led to this workflow'
            }
            Map.Entry<String, List<Condition2>> leadingWorkflow = leadingWorkflows[0]
            Condition2 leadingCondition = leadingWorkflow.value.find { it.nextWorkflow == workflow.key }
            return findRangesForPath(workflows, leadingWorkflow, leadingCondition, shouldFlipAndMerge)
        } else {
            throw new RuntimeException('unsure')
        }
    }

    @Override
    Object partTwo(FileAccess fileAccess) {
        Map<String, List<Condition2>> workflows = parseInput2(fileAccess)
        Map<String, List<Condition2>> approveWorkflows = workflows.findAll { it.value.any { it.accept } }

        List<Ranges> approveRanges = []
        approveWorkflows.each { approveWorkflow ->
            List<Condition2> approveConditions = approveWorkflow.value.findAll { it.accept }
            approveRanges += approveConditions.collect { approveCondition ->
                findRangesForPath(workflows, approveWorkflow, approveCondition, new Ranges())
            }
        }

        BigDecimal sum = 0
        approveRanges.each {
            sum += it.rangesPermutationsCount()
        }

        return sum

        //41719064124000
        //20679570000000
        //17256096000
        //167409079868000
    }
}
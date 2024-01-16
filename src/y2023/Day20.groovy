package y2023

import common.DayRunner
import common.FileAccess

class Day20 extends DayRunner {
    Day20() {
        super(null, false);
    }

    static void main(String[] args) {
        new Day20().run();
    }

    def parseInput(FileAccess fileAccess) {
        Map<Module, List<Module>> moduleGraph = fileAccess.lines.collectEntries {
            String info = it.split(/ /)[0]
            if (info.startsWith('%')) {
                return [new FlipFlop(info.replace('%', '')): []]
            } else if (info.startsWith('&')) {
                return [new Conjunction(info.replace('&', '')): []]
            } else if (info == 'broadcaster') {
                return [new Broadcaster('broadcaster'): []]
            }
        }

        List<String> replaced = fileAccess.lines.collect {
            it.replace('%', '').replace('&', '')
        }

        replaced.each { line ->
            Module origin = moduleGraph.keySet().find { it.name == line.split(/ /)[0] }
            List<Module> destinations = line.split(/ -> /)[1].split(/,./).collect { name -> moduleGraph.keySet().find { it.name == name } }

            moduleGraph.put(origin, destinations)
        }

        moduleGraph.each {
            if (it.key instanceof Conjunction) {
                Conjunction junction = (Conjunction) it.key
                List<Module> inputs = moduleGraph.findAll { it.value.contains(junction) }*.key
                junction.initializeMemory(inputs)
            }
        }

        moduleGraph << [new Button('button'): [moduleGraph.keySet().find { it instanceof Broadcaster }]]

        return moduleGraph
    }

    @Override
    Object partOne(FileAccess fileAccess) {
        Map<Module, List<Module>> moduleGraph = parseInput(fileAccess)
        Button button = (Button) moduleGraph.keySet().find { it instanceof Button }
        long lowPulseCount = 0
        long highPulseCount = 0
        Conjunction kl = moduleGraph.keySet().find { it.name == 'kl' }
        Map<Module, List<Integer>> klHighPulse = moduleGraph.findAll { it.value.contains(kl) }.collectEntries { [(it.key): []] }
        int requiredSize = 3
        (1..100000000000).each { count ->
            if (klHighPulse.every { it.value.size() > requiredSize }) {
                println(klHighPulse.values())
                requiredSize++
            }
            List<Tuple2<Pulse, List<Module>>> queue = []
            queue << new Tuple2(button.propagate(), moduleGraph[button])
            lowPulseCount++
            while (queue.size()) {
                Tuple2<Pulse, List<Module>> action = queue.pop()
                action.v2.each {
                    it.receive(action.v1)
                    Pulse propagation = it.propagate()
                    List<Module> destinations = moduleGraph[it]
                    if (kl in destinations && propagation.high) {
                        List<Integer> inputCounts = klHighPulse.computeIfAbsent(propagation.origin, a -> [])
                        inputCounts << count
                        klHighPulse.put(propagation.origin, inputCounts)
                    }
                    if (destinations == [null] && propagation.low) {
                        println(count)
                    }
                    if (propagation) {
                        queue << new Tuple2(propagation, destinations - null)
                        if (propagation.low) lowPulseCount += destinations.size()
                        if (propagation.high) highPulseCount += destinations.size()
                    }
                }
            }
        }
        return lowPulseCount * highPulseCount
        //370474
    }

    @Override
    Object partTwo(FileAccess fileAccess) {
        def parsedInput = parseInput(fileAccess)
        return null
    }
}

abstract class Module {
    String name

    Module(String name) {
        this.name = name
    }

    abstract Pulse propagate()

    abstract void receive(Pulse pulse)
}

class FlipFlop extends Module {
    boolean isOn
    boolean shouldPropagate

    FlipFlop(String name) {
        super(name)
    }

    @Override
    void receive(Pulse pulse) {
        if (pulse.low) {
            shouldPropagate = true
        }
    }

    @Override
    Pulse propagate() {
        Pulse pulse = null
        if (shouldPropagate) {
            if (isOn) {
                pulse = new Pulse(this, true, false)
            } else {
                pulse = new Pulse(this, false, true)
            }
            isOn = !isOn
            shouldPropagate = !shouldPropagate
        }
        return pulse
    }
}

class Conjunction extends Module {
    Map<Module, Pulse> memory

    Conjunction(String name) {
        super(name)
    }

    void initializeMemory(List<Module> connections) {
        memory = connections.collectEntries { [(it): new Pulse(it, true, false)] }
    }

    @Override
    Pulse propagate() {
        Pulse pulse
        if (memory.values().every { it.high }) {
            pulse = new Pulse(this, true, false)
        } else {
            pulse = new Pulse(this, false, true)
        }
        return pulse
    }

    @Override
    void receive(Pulse pulse) {
        memory.put(pulse.origin, pulse)

        if (this.name == 'kl' && pulse.high) {
            println("kl memory after high=${memory.findAll { it.value.high }.keySet()}")
        }
    }
}

class Broadcaster extends Module {
    Pulse received

    Broadcaster(String name) {
        super(name)
    }

    @Override
    Pulse propagate() {
        return new Pulse(this, received.low, received.high)
    }

    @Override
    void receive(Pulse pulse) {
        received = pulse
    }
}

class Button extends Module {
    Button(String name) {
        super(name)
    }

    @Override
    Pulse propagate() {
        return new Pulse(this, true, false)
    }

    @Override
    void receive(Pulse pulse) {
        throw new RuntimeException('should not receive')
    }
}

class Pulse {
    Module origin
    boolean low
    boolean high

    Pulse(Module origin, boolean low, boolean high) {
        this.origin = origin
        this.low = low
        this.high = high
    }
}
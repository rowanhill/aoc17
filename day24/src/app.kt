import java.io.File

data class Component(val index: Int, val a: Int, val b: Int)
data class Port(val pins: Int, val component: Component)

fun parseFile(file: String): List<Component> {
    return File(file).readLines().mapIndexed { index, line ->
        val (a, b) =line.split("/").map { it.toInt() }
        Component(index, a, b)
    }
}

fun mapPortGraph(components: List<Component>): Map<Int, List<Port>> {
    return components.fold(
            HashMap<Int, MutableList<Port>>(),
            {acc, component ->
                acc.getOrPut(component.a, { ArrayList() }).add(Port(component.b, component))
                acc.getOrPut(component.b, { ArrayList() }).add(Port(component.a, component))

                acc
            })
}

fun findMaximumBridge(
        portGraph: Map<Int, List<Port>>,
        usedComponents: MutableList<Component>,
        endPortPins: Int,
        bridgeMaximiser: Comparator<List<Component>>
): List<Component> {
    val unusedNextComponents = portGraph[endPortPins]!!
            .filterNot { p -> usedComponents.find { c -> c == p.component } != null }

    // No more neighbours, so return our finished bridge
    if (unusedNextComponents.isEmpty()) {
        return ArrayList(usedComponents)
    }

    return unusedNextComponents.map { port ->
        usedComponents.add(port.component)
        val bridge = findMaximumBridge(portGraph, usedComponents, port.pins, bridgeMaximiser)
        usedComponents.removeAt(usedComponents.lastIndex)

        bridge
    }.maxWith(bridgeMaximiser)!!
}

fun findStrongestBridge(
        portGraph: Map<Int, List<Port>>,
        usedComponents: MutableList<Component>,
        endPortPins: Int
): List<Component> {
    return findMaximumBridge(
            portGraph,
            usedComponents,
            endPortPins,
            compareBy({ bridge -> bridge.map{ it.a + it.b }.sum() })
    )
}

fun findLongestBridge(
        portGraph: Map<Int, List<Port>>,
        usedComponents: MutableList<Component>,
        endPortPins: Int
): List<Component> {
    return findMaximumBridge(
            portGraph,
            usedComponents,
            endPortPins,
            compareBy(
                    { bridge -> bridge.size }, // find longest
                    { bridge -> bridge.map{ it.a + it.b }.sum() } // tie break on strength
            )
    )
}

fun main(args: Array<String>) {
    val components = parseFile("src/input.txt")
    val portGraph = mapPortGraph(components)

    val strongestBridge = findStrongestBridge(portGraph, ArrayList(), 0)
    println(strongestBridge.map { it.a + it.b }.sum())

    val longestBridge = findLongestBridge(portGraph, ArrayList(), 0)
    println(longestBridge.map { it.a + it.b }.sum())
}
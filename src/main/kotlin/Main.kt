import java.util.*

data class Node(val symbol: Char, val probability: Double, var left: Node? = null, var right: Node? = null) : Comparable<Node> {
    override fun compareTo(other: Node): Int = probability.compareTo(other.probability)
}

fun huffman(probabilities: Map<Char, Double>): Map<Char, String> {
    val priorityQueue = PriorityQueue<Node>()
    for ((symbol, probability) in probabilities) {
        val node = Node(symbol, probability)
        priorityQueue.offer(node)
    }

    while (priorityQueue.size > 1) {
        val node1 = priorityQueue.poll()
        val node2 = priorityQueue.poll()
        val parent = Node('\u0000', node1.probability + node2.probability, node1, node2)
        priorityQueue.offer(parent)
    }

    val root = priorityQueue.poll()
    val huffmanCodes = mutableMapOf<Char, String>()
    buildHuffmanTable(root, "", huffmanCodes)
    return huffmanCodes
}

fun buildHuffmanTable(node: Node?, code: String, huffmanCodes: MutableMap<Char, String>) {
    if (node == null) return
    if (node.left == null && node.right == null) {
        huffmanCodes[node.symbol] = code
    } else {
        buildHuffmanTable(node.left, "${code}0", huffmanCodes)
        buildHuffmanTable(node.right, "${code}1", huffmanCodes)
    }
}

fun shannonFano(probabilities: Map<Char, Double>): Map<Char, String> {
    val sortedProbabilities = probabilities.toList().sortedByDescending { it.second }
    val shannonFanoCodes = mutableMapOf<Char, String>()
    shannonFanoHelper(sortedProbabilities, 0, sortedProbabilities.size - 1, "", shannonFanoCodes)
    return shannonFanoCodes
}

fun shannonFanoHelper(sortedProbabilities: List<Pair<Char, Double>>, start: Int, end: Int, code: String, shannonFanoCodes: MutableMap<Char, String>) {
    if (start > end) return
    if (start == end) {
        shannonFanoCodes[sortedProbabilities[start].first] = code
        return
    }

    var i = start
    var j = end
    var sum1 = sortedProbabilities[start].second
    var sum2 = sortedProbabilities[end].second
    while (i < j) {

        if (sum1 < sum2) {
            sum1 += sortedProbabilities[++i].second
        } else {
            sum2 += sortedProbabilities[--j].second
        }
    }

    shannonFanoHelper(sortedProbabilities, start, i, "${code}0", shannonFanoCodes)
    shannonFanoHelper(sortedProbabilities, i + 1, end, "${code}1", shannonFanoCodes)
}

fun main() {
    println("Enter a word:")
    val word = readlnOrNull() ?: ""

    val probabilities = word.groupingBy { it }.eachCount().mapValues { it.value.toDouble() / word.length }

    println("Choose an algorithm:")
    println("1. Huffman")
    println("2. Shannon-Fano")

    val choice = readlnOrNull()?.toInt()

    when (choice) {
        1 -> println("Huffman Codes: ${huffman(probabilities)}")
        2 -> println("Shannon-Fano Codes: ${shannonFano(probabilities)}")
        else -> println("Invalid choice")
    }
}
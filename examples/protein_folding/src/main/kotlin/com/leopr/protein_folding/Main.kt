package com.leopr.protein_folding


import com.leopr.framework.AlgorithmType
import com.leopr.framework.SearchEngine
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default
import kotlin.math.abs

fun main(args: Array<String>) {
    val parser = ArgParser("protein_folding")
    
    val proteinArg by parser.option(ArgType.String, shortName = "p", fullName = "protein", description = "String of 'H' and 'P'")
        .default("PHHPHPPHP")
        
    val algosArg by parser.option(ArgType.String, shortName = "a", fullName = "algorithms", description = "Comma-separated list of algorithms to run (Available: ${AlgorithmType.entries.joinToString()})")
        .default(AlgorithmType.entries.joinToString(","))
        
    val defaultRun by parser.option(ArgType.Boolean, shortName = "d", fullName = "default", description = "Run with default protein and all algorithms").default(false)
        
    val actualArgs = if (args.isEmpty()) arrayOf("--help") else args
    parser.parse(actualArgs)

    val protein = if (defaultRun) "PHHPHPPHP" else proteinArg.uppercase()
    val algorithmsToRun = mutableListOf<AlgorithmType>()
    
    val algoStrings = if (defaultRun) AlgorithmType.entries.joinToString(",").split(",") else algosArg.split(",")
    for (a in algoStrings) {
        try {
            algorithmsToRun.add(AlgorithmType.valueOf(a.trim().uppercase()))
        } catch (e: IllegalArgumentException) {
            println("Error: Unknown algorithm '${a.trim()}'. Available: ${AlgorithmType.entries.joinToString()}")
            return
        }
    }

    println("Protein: $protein")
    println("-".repeat(50))

    for (algo in algorithmsToRun) {
        println("=== $algo === ")
        val problem = ProteinFoldingProblem(protein)
        val engine = SearchEngine(problem, algo)

        val startTime = System.currentTimeMillis()
        val result = engine.solve()
        val endTime = System.currentTimeMillis()

        if (result != null) {
            val finalState = result.state
            val positions = finalState.positions
            var contacts = 0

            // compute actual contacts
            for (i in 0 until positions.size) {
                if (protein[i] == 'H') {
                    for (j in i + 2 until positions.size) {
                        if (protein[j] == 'H') {
                            val p1 = positions[i]
                            val p2 = positions[j]
                            if (abs(p1.x - p2.x) + abs(p1.y - p2.y) == 1) {
                                contacts++
                            }
                        }
                    }
                }
            }
            val energy = -contacts
            println("Found solution in ${endTime - startTime} ms")
            println("Path cost according to engine: ${result.pathCost}")
            println("Depth: ${result.depth}")
            println("Total Contacts: $contacts")
            println("Final Energy: $energy")
            println("Positions: $positions")
            
            // simple grid visualization
            val minX = positions.minOf { it.x }
            val maxX = positions.maxOf { it.x }
            val minY = positions.minOf { it.y }
            val maxY = positions.maxOf { it.y }
            
            for (y in maxY downTo minY) {
                for (x in minX..maxX) {
                    val idx = positions.indexOf(Pos(x, y))
                    if (idx != -1) {
                        print(protein[idx] + " ")
                    } else {
                        print(". ")
                    }
                }
                println()
            }
            
        } else {
            println("Failed to find a solution")
        }
        println("-".repeat(50))
    }
}

package com.leopr.protein_folding


import com.leopr.framework.AlgorithmType
import com.leopr.framework.SearchRunner
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default

fun main(args: Array<String>) {
    val parser = ArgParser("protein_folding")
    
    val proteinArg by parser.option(ArgType.String, shortName = "p", fullName = "protein", description = "String of 'H' and 'P'")
        .default("PHHPHPPHP")
        
    val algosArg by parser.option(ArgType.String, shortName = "a", fullName = "algorithms", description = "Comma-separated list of algorithms to run (Available: ${AlgorithmType.entries.joinToString()})")
        .default(AlgorithmType.entries.joinToString(","))
        
    val defaultRun by parser.option(ArgType.Boolean, shortName = "d", fullName = "default", description = "Run with default protein and all algorithms").default(false)
        
    val exhaustive by parser.option(ArgType.Boolean, shortName = "e", fullName = "exhaustive", description = "Exhaustive search (findBestSolution)").default(false)
        
    val actualArgs = if (args.isEmpty()) arrayOf("--help") else args
    parser.parse(actualArgs)

    val protein = if (defaultRun) "PHHPHPPHP" else proteinArg.uppercase()
    val algorithmsToRun = mutableListOf<AlgorithmType>()
    
    val algoStrings = if (defaultRun) AlgorithmType.entries.joinToString(",").split(",") else algosArg.split(",")
    for (a in algoStrings) {
        try {
            algorithmsToRun.add(AlgorithmType.valueOf(a.trim().uppercase()))
        } catch (_: IllegalArgumentException) {
            println("Error: Unknown algorithm '${a.trim()}'. Available: ${AlgorithmType.entries.joinToString()}")
            return
        }
    }

    println("Protein: $protein")
    println("-".repeat(50))

    val runner = SearchRunner(
        problem = ProteinFoldingProblem(protein),
        algorithms = algorithmsToRun,
        isExhaustive = exhaustive,
        solutionPrinter = { state -> printProteinSolution(protein, state) }
    )
    
    runner.executeAll()
}

private fun printProteinSolution(protein: String, state: ProteinState) {
    val positions = state.positions
    val contacts = state.contacts
    val energy = -contacts
    
    // print stats
    println("Total Contacts: $contacts")
    println("Final Energy: $energy")
    println("Positions: $positions")
    
    // print grid
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
}

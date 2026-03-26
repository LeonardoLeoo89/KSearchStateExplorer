# 2D HP Protein Folding

## Problem

The problem consists of folding a sequence of H and P amino-acids on a 2D integer grid, non-crossing.
The goal is to maximize the number of topological contacts between non-sequential 'H' amino acids.

## Implementation Details:

### State Representation

The state (`ProteinState`) is a list of `Pos(x, y)` coordinates, representing the folded steps, and the number of contacts between 'H' amino acids.

### Actions

Actions are modeled as "where to place the next amino acid": Up, Down, Left, Right.

### Step Cost

Standard algorithms like A\* require non-negative path costs ($c(s, a) \ge 0$).
So the cost of a step is $3 - \text{contacts}$, where contacts is the number of contacts formed by the new amino acid. 3 is the max number of contacts an 'H' amino acid can form.

### Heuristic

The heuristic is based on the idea that topological contacts between cells can exclusively naturally occur between coordinates of opposite parity (like black and white squares on a checkerboard).

By counting how many 'H' amino acids are left to be placed on Even (white) and Odd (black) positions, the heuristic finds the absolute limit of future connections. Since every new contact needs exactly one Even and one Odd 'H', the maximum possible number of contacts is limited by whichever parity has the fewest 'H's remaining. The heuristic formula is:

$$ h(n) = \text{CapacityUnplaced} - \min(\text{UnplacedH}, \text{MaxEven}, \text{MaxOdd}) $$

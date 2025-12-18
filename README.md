# Traveling Salesman Problem (TSP) Solver

This repository contains a Java-based solver for the Traveling Salesman Problem (TSP), along with a C++ verifier to check the validity and cost of the generated tours.

## Overview

The project implements heuristics to find approximate solutions to TSP instances. It adaptively selects algorithms based on the problem size:

-   **Christofides' Algorithm**: Used for small to medium-sized inputs. It guarantees a solution within 1.5 times the optimal but is computationally more expensive ($O(n^3)$).
-   **Greedy Algorithm**: Used for large inputs (e.g., > 18,000 cities) where Christofides' is too slow or memory-intensive.

### Optimizations
Post-processing optimizations are applied to improve the initial tours:
-   **2-Opt**: Iteratively untangles crossing edges.
-   **3-Opt**: (Applied to smaller instances) Relocates segments to further reduce tour cost.
-   **Simulated Annealing**: Used in conjunction with 2-Opt to escape local minima.

## Directory Structure

-   `src/`: Contains the Java source code for the solver.
    -   `Main.java`: Entry point. Runs the solver on specified input files.
    -   `HamiltonianCycle.java`: Implements Greedy and conversion from Eulerian paths.
    -   `MinimumSpanningTree.java`, `BipartiteGraph.java`, `Multigraph.java`: Components for Christofides' algorithm.
    -   `TwoOpt.java`, `ThreeOpt.java`: Local search optimizations.
-   `input/`: Directory for input files (TSP instances).
-   `output/`: Directory where generated tour files are saved.
-   `bestOutput/`: Stores the best-known solutions found so far.
-   `verifier.cpp`: C++ source code for the solutuon verifier.
-   `verifyAll.ps1`: PowerShell script to verify all solutions in `bestOutput/`.

## Prerequisites

-   **Java JDK**: To compile and run the solver.
-   **C++ Compiler (g++)**: To compile the verifier.
-   **PowerShell**: To run the batch verification script (Windows).

## Usage

### 1. Compile the Solver used
Navigate to the root directory and compile the Java sources:
```bash
javac -d bin src/*.java
```
*(Note: The project structure assumes running from `src` or compiling to a generic location. You might need to adjust paths if running directly.)*

Alternatively, you can run directly from `src`:
```bash
cd src
javac *.java
```

### 2. Run the Solver
To run the solver logic (configured in `Main.java`):
```bash
java Main
```
*Modify `src/Main.java` to change the target input files or algorithm parameters.*

### 3. Compile the Verifier
Compile the C++ verifier:
```bash
g++ -o verifier verifier.cpp
```
This produces a `verifier.exe` (on Windows).

### 4. Verify a Solution
Usage: `./verifier <input_file> <solution_file>`

Example:
```bash
./verifier input/tsp_8_1 output/tsp_8_1
```
Output matches the claimed length against the actual calculated geometric distance.

### 5. Verify All Solutions
Use the PowerShell script to verify all solutions in the `bestOutput` folder:
```powershell
./verifyAll.ps1
```

## Input/Output Format

**Input File**:
-   First line: Number of vertices ($N$).
-   Next $N$ lines: vertex data (though many TSP formats just have ID X Y, this parser expects coordinate data).

**Output File**:
-   First line: Total cost of the tour.
-   Second line: Space-separated list of vertex IDs representing the tour order.

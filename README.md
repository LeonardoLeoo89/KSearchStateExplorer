# StateExplorer

AI Search State Exploration framework in Kotlin featuring the following search algorithms:

- BFS
- DFS
- Min-Cost
- IDS
- Greedy
- A\*

## Modules

- `framework`: Contains the core reusable search engine and base classes.
- `examples`: Contains examples of how to use the framework.

### Available examples:

- `protein_folding`: 2D HP Protein Folding problem.

#### How to run examples without building

Replace `{example}` with the name of the example module.

```bash
./gradlew -q :examples:{example}:run --args="--help"
```

#### How to build and run an example

Run this command once in the root directory (this will build all examples):

```bash
./gradlew installDist
```

Then run the example:

```bash
./examples/{example}/build/install/{example}/bin/{example}
```

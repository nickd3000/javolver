# Javolver

**Javolver** is a simple yet powerful **genetic algorithm** system in Java.

![Evolved Tree](https://i.imgur.com/YQOhyQV.png "Evolved tree")

The library is designed to be extremely simple to set up and run. To get started, you typically define a `ScoreFunction` to evaluate your individuals.

An individual's genetic information is stored in a `Chromosome` (an array of doubles between 0.0 and 1.0). You can map these values to any data type your problem requires.

*Documentation*
Java docs for the library are
available [here](http://htmlpreview.github.com/?https://github.com/nickd3000/javolver/blob/master/docs/index.html)

### Simple Example

The following example shows how to set up a solver to evolve a string to match a target word.

```java
    public static void testWord() {
        String targetWord = "HELLOWORLD";
        int populationSize = 100;

        Solver solver = Javolver.builder()
                .dnaSize(targetWord.length())
                .populationTargetSize(populationSize)
                .keepBestIndividualAlive(true)
                .addMutationOperator(new MutationOperatorSimple(1, 0.05))
                .setSelectionOperator(new SelectionOperatorTournament(0.25))
                .setBreedingOperator(new BreedingOperatorUniform())
                .scoreFunction(individual -> {
                    double score = 0;
                    for (int i = 0; i < targetWord.length(); i++) {
                        if (individual.getDna().getChar(i) == targetWord.charAt(i)) {
                            score += 1.0;
                        }
                    }
                    return score;
                })
                .build();

        // Perform a few iterations of evolution.
        for (int j = 0; j < 100; j++) {
            // Call the solver to perform one evolution step.
            solver.doOneCycle();

            Individual best = solver.getBestScoringIndividual();
            System.out.println("Iteration " + j + " Best Score: " + best.getScore());
            
            if (best.getScore() >= targetWord.length()) break;
        }
    }
```

### Included Examples

There are several examples included in the project:
* [Word Finder](src/main/java/com/physmo/reference/WordFinder.java) - Evolving a string to match a target.
* [Sphere Packer](src/main/java/com/physmo/reference/SpherePacker.java) - Packing circles into a set area.
* [Symbolic Regression](src/main/java/com/physmo/reference/symbolicregression/TestSymbolicRegression.java) - Evolving mathematical expressions (trees).

![Sphere Packing](https://i.imgur.com/sidizaf.png "Sphere Packing")
![Basic test](https://i.imgur.com/TT5nKZB.png "Basic test")
![Image Solver](http://i.imgur.com/455ZMAx.png "Image solving")


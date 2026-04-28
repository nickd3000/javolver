package com.physmo.javolver

import spock.lang.Specification

class IndividualSpec extends Specification {

    void "constructor initializes DNA with correct size"() {
        given:
        int dnaSize = 10

        when:
        Individual individual = new Individual(dnaSize)

        then:
        individual.dna != null
        individual.dna.size == dnaSize
    }

    void "copy constructor copies core properties but resets state"() {
        given:
            ScoreFunction scoreFunction = Mock(ScoreFunction)
            Individual original = new Individual(5)
            original.setScoreFunction(scoreFunction)
            original.setScore(100.0)
            original.setProcessed(true)

        when:
            Individual clone = new Individual(original)

        then:
            clone.dna.size == original.dna.size
            clone.scoreFunction == original.scoreFunction
            !clone.processed
            clone.@score == 0.0d
            clone.diversity == 0.0d
    }

    void "getScore calculates score using scoreFunction only once"() {
        given:
        ScoreFunction scoreFunction = Mock(ScoreFunction)
        Individual individual = new Individual(5)
        individual.setScoreFunction(scoreFunction)

        when: "first call to getScore"
        double score1 = individual.getScore()

        then: "scoreFunction is called once"
        1 * scoreFunction.score(individual) >> 42.0d
        score1 == 42.0d

        when: "second call to getScore"
        double score2 = individual.getScore()

        then: "scoreFunction is not called again"
        0 * scoreFunction.score(_)
        score2 == 42.0d
    }

    void "getScoreSquared returns square of score and triggers calculation"() {
        given:
        ScoreFunction scoreFunction = Mock(ScoreFunction)
        Individual individual = new Individual(5)
        individual.setScoreFunction(scoreFunction)

        when:
        double scoreSquared = individual.getScoreSquared()

        then:
        1 * scoreFunction.score(individual) >> 3.0
        scoreSquared == 9.0d
    }

    void "cloneFully creates a deep copy of DNA"() {
        given:
        Individual original = new Individual(3)
        original.dna.set(0, 0.1)
        original.dna.set(1, 0.2)
        original.dna.set(2, 0.3)

        when:
        Individual clone = original.cloneFully()

        then: "DNA values are copied"
        clone.dna.getDouble(0) == 0.1d
        clone.dna.getDouble(1) == 0.2d
        clone.dna.getDouble(2) == 0.3d

        when: "original DNA is modified"
        original.dna.set(0, 0.9)

        then: "clone DNA remains unchanged"
        clone.dna.getDouble(0) == 0.1d
    }

    void "getDifference calculates average squared difference"() {
        given:
        Individual ind1 = new Individual(2)
        ind1.dna.set(0, 0.5)
        ind1.dna.set(1, 0.5)

        Individual ind2 = new Individual(2)
        ind2.dna.set(0, 0.8) // diff = 0.3, sq = 0.09
        ind2.dna.set(1, 0.1) // diff = 0.4, sq = 0.16

        when:
        // sum of squares = 0.09 + 0.16 = 0.25
        // sqrt(0.25) = 0.5
        // result = 0.5 / 2 = 0.25
        double diff = ind1.getDifference(ind2)

        then:
        diff == 0.25
    }

    void "getHash returns consistent hash based on DNA"() {
        given:
        Individual ind1 = new Individual(2)
        ind1.dna.set(0, 0.1)
        ind1.dna.set(1, 0.2)

        Individual ind2 = new Individual(2)
        ind2.dna.set(0, 0.1)
        ind2.dna.set(1, 0.2)

        expect:
        ind1.getHash() == ind2.getHash()

        when:
        ind2.dna.set(1, 0.3)

        then:
        ind1.getHash() != ind2.getHash()
    }
}

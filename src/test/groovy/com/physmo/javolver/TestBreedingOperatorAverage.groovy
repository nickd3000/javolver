package com.physmo.javolver

import com.physmo.javolver.breedingoperator.BreedingOperatorAverage
import spock.lang.Specification

class TestBreedingOperatorAverage extends Specification {

    def "Test TestBreedingOperatorAverage breeding"() {
        given: "Two individuals"
            Individual i1 = new Individual(2)
            Individual i2 = new Individual(2)

        and: "The individuals are initialized to known values"
            i1.dna.init(10)
            setAllDnaValuesTo(i1, 0)
            i2.dna.init(10)
            setAllDnaValuesTo(i2, 1)

        and: "BreedingOperatorAverage is created"
            BreedingOperatorAverage breedingStrategyAverage = new BreedingOperatorAverage()

        when: "The two individuals are bred"
            List<Individual> children = breedingStrategyAverage.breed(i1, i2)

        then: "One individual is created"
            children.size() == 1

        and: "The dna value is the average of the two individuals"
            children.get(0).dna.getDouble(0) - 0.5 < 0.01
    }

    def setAllDnaValuesTo(Individual individual, double val) {
        for (int i = 0; i < individual.dna.getSize(); i++) {
            individual.dna.set(i, val);
        }
    }
}

package com.physmo.javolver

import org.spockframework.compiler.model.ThenBlock
import spock.lang.Specification

class AttenuatorTest extends Specification {

    def "can set score range and add parameters"() {
        given: "an Attenuator instance"
          def att = new Attenuator()

        when: "score range and parameter are registered"
          att.setScoreRange(0, 100)
          att.addParam("mutationRate", 0.5, 0.01)

        then: "parameters map contains the parameter with correct min/max"
          att.getRegisteredParameters().containsKey("mutationRate")
          att.getRegisteredParameters()["mutationRate"] == [0.5, 0.01] as Double[]
    }

    def "returns min value at min score and max at max score"() {
        given: "an attenuator with a parameter"
          def att = new Attenuator()
          att.setScoreRange(0, 100)
          att.addParam("x", 10, 20)

        expect: "returns min value when score is at min"
          att.setScore(0)
          att.getValue("x") == 10

        and: "returns max value when score is at max"
          att.setScore(100)
          att.getValue("x") == 20
    }

    def "returns interpolated value at mid score"() {
        given: "an attenuator with a parameter"
          def att = new Attenuator()
          att.setScoreRange(0, 100)
          att.addParam("y", 0, 10)

        when: "setting score to midpoint"
          att.setScore(50)

        then: "returns interpolated value"
          att.getValue("y") == 5
    }

    def "score clamps at bounds"() {
        given: "an attenuator with a parameter"
          def att = new Attenuator()
          att.setScoreRange(0, 100)
          att.addParam("z", 100, 200)

        when: "score set below min"
          att.setScore(-50)
          def v1 = att.getValue("z")

        and: "score set above max"
          att.setScore(150)
          def v2 = att.getValue("z")

        then: "value is clamped to min at lower bound"
          v1 == 100

        and: "value is clamped to max at upper bound"
          v2 == 200
    }

    def "throws on unknown parameter"() {
        given: "an attenuator with no registered params"
          def att = new Attenuator()
          att.setScoreRange(0, 1)

        when: "getting value for unknown parameter"
          att.getValue("notAdded")

        then: "throws IllegalArgumentException"
          thrown(IllegalArgumentException)
    }

    def "throws for invalid score range"() {
        when: "creating attenuator with invalid range"
          new Attenuator().setScoreRange(10, 5)

        then: "throws IllegalArgumentException"
          thrown(IllegalArgumentException)
    }

    def "supports method chaining for initialization"() {
        given: "Attenuator initialized with method chaining"
          def att = new Attenuator()
                  .setScoreRange(0, 10)
                  .addParam("alpha", 5, 15)
                  .setScore(5)

    }
}
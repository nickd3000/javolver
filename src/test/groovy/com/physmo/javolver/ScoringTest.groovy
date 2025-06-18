package com.physmo.javolver

import spock.lang.Specification
import spock.lang.Unroll

class ScoringTest extends Specification {

    @Unroll
    def "Test scoreValue for various values"(double actual, double expected, double range, double expectedScore) {
        expect:
            Scoring.scoreValue(actual, expected, range) == expectedScore

        where:
            actual | expected | range | expectedScore
            10     | 10       | 100   | 1
            10     | 10       | 10    | 1
            5      | 10       | 100   | 0.9025
            5      | 10       | 50    | 0.81

    }

    def "Test getScoreForCharacter for various values"(char actual, char target, double expectedScore) {
        expect:
            closeTo(Scoring.getScoreForCharacter(actual, target), expectedScore)

        where:
            actual | target | expectedScore
            'a'    | 'a'    | 1.0
            'b'    | 'a'    | 0.871
            'a'    | 'b'    | 0.871
            'c'    | 'a'    | 0.751
            'd'    | 'a'    | 0.640
    }

    boolean closeTo(double a, double b) {
        return Math.abs(a - b) < 0.001
    }
}

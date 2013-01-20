package com.traderapist.scoringsystem

interface IFantasyScoringSystem {
    /**
     * Given a collection of Stat objects, this calculates the fantasy points that these stats
     * would yield for a particular scoring system.
     *
     * @param stats		The stats to evaluate.
     * @return			The number of points scored for the scoring system.
     */
    public double calculateScore(List stats);
}
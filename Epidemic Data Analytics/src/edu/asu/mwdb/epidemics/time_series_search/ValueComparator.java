package edu.asu.mwdb.epidemics.time_series_search;

import java.util.Comparator;

import edu.asu.mwdb.epidemics.domain.SimilarityDomain;
/**
 * 
 * Comparator for priority queue of similarities
 *
 */
class ValueComparator implements Comparator<SimilarityDomain> {

    SimilarityDomain base;
    public ValueComparator(SimilarityDomain base) {
        this.base = base;
    }
    /**
     * compare overrides the compare function of Comparator class
     */
    public int compare(SimilarityDomain file1, SimilarityDomain file2) {
        if (file1.getSimilarity() <= file2.getSimilarity()) {
            return -1;
        } else {
            return 1;
        }
    }
}
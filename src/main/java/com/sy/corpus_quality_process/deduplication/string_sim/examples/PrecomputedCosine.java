package com.sy.corpus_quality_process.deduplication.string_sim.examples;

import com.sy.corpus_quality_process.deduplication.string_sim.Cosine;

/**
 * Example of computing cosine similarity with pre-computed profiles.
 *
 */
public class PrecomputedCosine {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        var s1 = "My first string";
        var s2 = "My other string...";

        // Let's work with sequences of 2 characters...
        var cosine = new Cosine(2);

        // Pre-compute the profile of strings
        var profile1 = cosine.getProfile(s1);
        var profile2 = cosine.getProfile(s2);

        // Prints 0.516185
        System.out.println(cosine.similarity(profile1, profile2));

    }

}

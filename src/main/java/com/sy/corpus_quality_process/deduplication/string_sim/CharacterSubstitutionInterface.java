
package com.sy.corpus_quality_process.deduplication.string_sim;

/**
 * Used to indicate the cost of character substitution.
 *
 * Cost should always be in [0.0 .. 1.0]
 * For example, in an OCR application, cost('o', 'a') could be 0.4
 * In a checkspelling application, cost('u', 'i') could be 0.4 because these are
 * next to each other on the keyboard...
 *
 */
public interface CharacterSubstitutionInterface {
    /**
     * Indicate the cost of substitution c1 and c2.
     * @param c1 The first character of the substitution.
     * @param c2 The second character of the substitution.
     * @return The cost in the range [0, 1].
     */
    double cost(char c1, char c2);
}

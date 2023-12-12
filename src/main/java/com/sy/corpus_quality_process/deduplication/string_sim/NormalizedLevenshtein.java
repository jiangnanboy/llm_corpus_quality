
package com.sy.corpus_quality_process.deduplication.string_sim;

import com.sy.corpus_quality_process.deduplication.string_sim.interfaces.NormalizedStringSimilarity;
import com.sy.corpus_quality_process.deduplication.string_sim.interfaces.NormalizedStringDistance;
import net.jcip.annotations.Immutable;

/**
 * This distance is computed as levenshtein distance divided by the length of
 * the longest string. The resulting value is always in the interval [0.0 1.0]
 * but it is not a metric anymore! The similarity is computed as 1 - normalized
 * distance.
 *
 */
@Immutable
public class NormalizedLevenshtein implements
        NormalizedStringDistance, NormalizedStringSimilarity {

    private final Levenshtein l = new Levenshtein();

    /**
     * Compute distance as Levenshtein(s1, s2) / max(|s1|, |s2|).
     * @param s1 The first string to compare.
     * @param s2 The second string to compare.
     * @return The computed distance in the range [0, 1]
     * @throws NullPointerException if s1 or s2 is null.
     */
    @Override
    public final double distance(final String s1, final String s2) {

        if (s1 == null) {
            throw new NullPointerException("s1 must not be null");
        }

        if (s2 == null) {
            throw new NullPointerException("s2 must not be null");
        }

        if (s1.equals(s2)) {
            return 0;
        }

        int m_len = Math.max(s1.length(), s2.length());

        if (m_len == 0) {
            return 0;
        }

        return l.distance(s1, s2) / m_len;
    }

    /**
     * Return 1 - distance.
     * @param s1 The first string to compare.
     * @param s2 The second string to compare.
     * @return 1.0 - the computed distance
     * @throws NullPointerException if s1 or s2 is null.
     */
    @Override
    public final double similarity(final String s1, final String s2) {
        return 1.0 - distance(s1, s2);
    }

}


package com.sy.corpus_quality_process.deduplication.string_sim;

import com.sy.corpus_quality_process.deduplication.string_sim.interfaces.MetricStringDistance;
import com.sy.corpus_quality_process.deduplication.string_sim.interfaces.NormalizedStringDistance;
import net.jcip.annotations.Immutable;

/**
 * Distance metric based on Longest Common Subsequence, from the notes "An
 * LCS-based string metric" by Daniel Bakkelund.
 *
 */
@Immutable
public class MetricLCS
        implements MetricStringDistance, NormalizedStringDistance {

    private final LongestCommonSubsequence lcs = new LongestCommonSubsequence();

    /**
     * Distance metric based on Longest Common Subsequence, computed as
     * 1 - |LCS(s1, s2)| / max(|s1|, |s2|).
     *
     * @param s1 The first string to compare.
     * @param s2 The second string to compare.
     * @return The computed distance metric value.
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

        var m_len = Math.max(s1.length(), s2.length());
        if (m_len == 0) {
            return 0;
        }
        return 1.0
            - (1.0 * lcs.length(s1, s2))
            / m_len;
    }
}


package com.sy.corpus_quality_process.deduplication.string_sim;

import com.sy.corpus_quality_process.deduplication.string_sim.interfaces.StringDistance;
import net.jcip.annotations.Immutable;

/**
 * Implementation of the the Optimal String Alignment (sometimes called the
 * restricted edit distance) variant of the Damerau-Levenshtein distance.
 *
 * The difference between the two algorithms consists in that the Optimal String
 * Alignment algorithm computes the number of edit operations needed to make the
 * strings equal under the condition that no substring is edited more than once,
 * whereas Damerau-Levenshtein presents no such restriction.
 *
 */
@Immutable
public final class OptimalStringAlignment implements StringDistance {

    /**
     * Compute the distance between strings: the minimum number of operations
     * needed to transform one string into the other (insertion, deletion,
     * substitution of a single character, or a transposition of two adjacent
     * characters) while no substring is edited more than once.
     *
     * @param s1 The first string to compare.
     * @param s2 The second string to compare.
     * @return the OSA distance
     * @throws NullPointerException if s1 or s2 is null.
     */
    @Override
    public double distance(final String s1, final String s2) {
        if (s1 == null) {
            throw new NullPointerException("s1 must not be null");
        }

        if (s2 == null) {
            throw new NullPointerException("s2 must not be null");
        }

        if (s1.equals(s2)) {
            return 0;
        }

        int n = s1.length(), m = s2.length();

        if (n == 0) {
            return m;
        }

        if (m == 0) {
            return n;
        }

        // Create the distance matrix H[0 .. s1.length+1][0 .. s2.length+1]
        var d = new int[n + 2][m + 2];

        //initialize top row and leftmost column
        for (var i = 0; i <= n; i++) {
            d[i][0] = i;
        }
        for (var j = 0; j <= m; j++) {
            d[0][j] = j;
        }

        //fill the distance matrix
        int cost;

        for (var i = 1; i <= n; i++) {
            for (var j = 1; j <= m; j++) {

                //if s1[i - 1] = s2[j - 1] then cost = 0, else cost = 1
                cost = 1;
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    cost = 0;
                }

                d[i][j] = min(
                        d[i - 1][j - 1] + cost, // substitution
                        d[i][j - 1] + 1, // insertion
                        d[i - 1][j] + 1 // deletion
                );

                //transposition check
                if (i > 1 && j > 1
                        && s1.charAt(i - 1) == s2.charAt(j - 2)
                        && s1.charAt(i - 2) == s2.charAt(j - 1)) {
                    d[i][j] = Math.min(d[i][j], d[i - 2][j - 2] + cost);
                }
            }
        }

        return d[n][m];
    }

    private static int min(
            final int a, final int b, final int c) {
        return Math.min(a, Math.min(b, c));
    }
}

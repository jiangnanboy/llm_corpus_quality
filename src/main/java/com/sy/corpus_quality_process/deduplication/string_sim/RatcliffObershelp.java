
package com.sy.corpus_quality_process.deduplication.string_sim;

import com.sy.corpus_quality_process.deduplication.string_sim.interfaces.NormalizedStringSimilarity;
import com.sy.corpus_quality_process.deduplication.string_sim.interfaces.NormalizedStringDistance;
import java.util.List;
import java.util.ArrayList;

import net.jcip.annotations.Immutable;

/**
 * Ratcliff/Obershelp pattern recognition
 * The Ratcliff/Obershelp algorithm computes the similarity of two strings a
 * the doubled number of matching characters divided by the total number of
 * characters in the two strings. Matching characters are those in the longest
 * common subsequence plus, recursively, matching characters in the unmatched
 * region on either side of the longest common subsequence.
 * The Ratcliff/Obershelp distance is computed as 1 - Ratcliff/Obershelp
 * similarity.
 *
 * Ported to java from .net by denmase
 */
@Immutable
public class RatcliffObershelp implements
        NormalizedStringSimilarity, NormalizedStringDistance {

    /**
     * Compute the Ratcliff-Obershelp similarity between strings.
     *
     * @param s1 The first string to compare.
     * @param s2 The second string to compare.
     * @return The RatcliffObershelp similarity in the range [0, 1]
     * @throws NullPointerException if s1 or s2 is null.
     */
    @Override
    public final double similarity(final String s1, final String s2) {
        if (s1 == null) {
            throw new NullPointerException("s1 must not be null");
        }

        if (s2 == null) {
            throw new NullPointerException("s2 must not be null");
        }

        if (s1.equals(s2)) {
            return 1.0d;
        }

        var matches = getMatchList(s1, s2);
        var sum_of_matches = 0;

        for (var match : matches) {
            sum_of_matches += match.length();
        }

        return 2.0d * sum_of_matches / (s1.length() + s2.length());
    }

    /**
     * Return 1 - similarity.
     *
     * @param s1 The first string to compare.
     * @param s2 The second string to compare.
     * @return 1 - similarity
     * @throws NullPointerException if s1 or s2 is null.
     */
    @Override
    public final double distance(final String s1, final String s2) {
        return 1.0d - similarity(s1, s2);
    }

    private static List<String> getMatchList(final String s1, final String s2) {
        var list = new ArrayList<String>();
        var match = frontMaxMatch(s1, s2);

        if (match.length() > 0) {
            var frontsource = s1.substring(0, s1.indexOf(match));
            var fronttarget = s2.substring(0, s2.indexOf(match));
            var frontqueue = getMatchList(frontsource, fronttarget);

            var endsource = s1.substring(s1.indexOf(match) + match.length());
            var endtarget = s2.substring(s2.indexOf(match) + match.length());
            var endqueue = getMatchList(endsource, endtarget);

            list.add(match);
            list.addAll(frontqueue);
            list.addAll(endqueue);
        }

        return list;
    }

    private static String frontMaxMatch(final String s1, final String s2) {
        var longest = 0;
        var longestsubstring = "";

        for (var i = 0; i < s1.length(); ++i) {
            for (var j = i + 1; j <= s1.length(); ++j) {
                var substring = s1.substring(i, j);
                if (s2.contains(substring) && substring.length() > longest) {
                    longest = substring.length();
                    longestsubstring = substring;
                }
            }
        }

        return longestsubstring;
    }
}

package com.sy.corpus_quality_process.deduplication.string_sim.examples;

import com.sy.corpus_quality_process.deduplication.string_sim.Cosine;
import com.sy.corpus_quality_process.deduplication.string_sim.Damerau;
import com.sy.corpus_quality_process.deduplication.string_sim.OptimalStringAlignment;
import com.sy.corpus_quality_process.deduplication.string_sim.Jaccard;
import com.sy.corpus_quality_process.deduplication.string_sim.JaroWinkler;
import com.sy.corpus_quality_process.deduplication.string_sim.Levenshtein;
import com.sy.corpus_quality_process.deduplication.string_sim.LongestCommonSubsequence;
import com.sy.corpus_quality_process.deduplication.string_sim.NGram;
import com.sy.corpus_quality_process.deduplication.string_sim.NormalizedLevenshtein;
import com.sy.corpus_quality_process.deduplication.string_sim.QGram;
import com.sy.corpus_quality_process.deduplication.string_sim.SorensenDice;
import com.sy.corpus_quality_process.deduplication.string_sim.WeightedLevenshtein;

/**
 *
 */
public class Examples {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Levenshtein
        // ===========
        System.out.println("\nLevenshtein");
        var levenshtein = new Levenshtein();
        System.out.println(levenshtein.distance("中华人民共和国", "中华民国"));
        System.out.println(levenshtein.distance("中华人民共和国", "中华人民"));
        System.out.println(levenshtein.distance("中华人民共和国", "中国"));

        // Jaccard index
        // =============
        System.out.println("\nJaccard");
        var j2 = new Jaccard(2);
        // AB BC CD DE DF
        // 1  1  1  1  0
        // 1  1  1  0  1
        // => 3 / 5 = 0.6
        System.out.println(j2.similarity("我们都是好学生", "我们都是好同志"));

        // Jaro-Winkler
        // ============
        System.out.println("\nJaro-Winkler");
        var jw = new JaroWinkler();

        // substitution of s and t : 0.9740740656852722
        System.out.println(jw.similarity("中华人民共和国", "中华人民共和国"));

        // substitution of s and n : 0.8962963223457336
        System.out.println(jw.similarity("中华人民共和国", "中华人民"));

        // Cosine
        // ======
        System.out.println("\nCosine");
        var cos = new Cosine(3);

        // ABC BCE
        // 1  0
        // 1  1
        // angle = 45°
        // => similarity = .71
        System.out.println(cos.similarity("ABC", "ABCE"));

        cos = new Cosine(2);
        // AB BA
        // 2  1
        // 1  1
        // similarity = .95
        System.out.println(cos.similarity("ABAB", "BAB"));

        // Damerau
        // =======
        System.out.println("\nDamerau");
        var damerau = new Damerau();

        // 1 substitution
        System.out.println(damerau.distance("ABCDEF", "ABDCEF"));

        // 2 substitutions
        System.out.println(damerau.distance("ABCDEF", "BACDFE"));

        // 1 deletion
        System.out.println(damerau.distance("ABCDEF", "ABCDE"));
        System.out.println(damerau.distance("ABCDEF", "BCDEF"));

        System.out.println(damerau.distance("ABCDEF", "ABCGDEF"));

        // All different
        System.out.println(damerau.distance("ABCDEF", "POIU"));


        // Optimal String Alignment
        // =======
        System.out.println("\nOptimal String Alignment");
        var osa = new OptimalStringAlignment();

        //Will produce 3.0
        System.out.println(osa.distance("CA", "ABC"));


        // Longest Common Subsequence
        // ==========================
        System.out.println("\nLongest Common Subsequence");
        var lcs = new LongestCommonSubsequence();

        // Will produce 4.0
        System.out.println(lcs.distance("AGCAT", "GAC"));

        // Will produce 1.0
        System.out.println(lcs.distance("AGCAT", "AGCT"));

        // NGram
        // =====
        // produces 0.416666
        System.out.println("\nNGram");
        var twogram = new NGram(2);
        System.out.println(twogram.distance("ABCD", "ABTUIO"));

        // produces 0.97222
        var s1 = "我在北方的冬天里";
        var s2 = "我在北方的冬天里喝奶茶";
        var ngram = new NGram(4);
        System.out.println(ngram.distance(s1, s2));

        // Normalized Levenshtein
        // ======================
        System.out.println("\nNormalized Levenshtein");
        var l = new NormalizedLevenshtein();

        System.out.println(l.distance("My string", "My $tring"));
        System.out.println(l.distance("My string", "M string2"));
        System.out.println(l.distance("My string", "abcd"));

        // QGram
        // =====
        System.out.println("\nQGram");
        var dig = new QGram(2);

        // AB BC CD CE
        // 1  1  1  0
        // 1  1  0  1
        // Total: 2
        System.out.println(dig.distance("ABCD", "ABCE"));

        System.out.println(dig.distance("", "QSDFGHJKLM"));

        System.out.println(dig.distance(
                "平静的湖面，随着微风，泛起一圈圈波纹",
                "平静的湖面，随着微风，泛起涟漪"));

        // Sorensen-Dice
        // =============
        System.out.println("\nSorensen-Dice");
        var sd = new SorensenDice(2);

        // AB BC CD DE DF FG
        // 1  1  1  1  0  0
        // 1  1  1  0  1  1
        // => 2 x 3 / (4 + 5) = 6/9 = 0.6666
        System.out.println(sd.similarity("ABCDE", "ABCDFG"));

        // Weighted Levenshtein
        // ====================
        System.out.println("\nWeighted Levenshtein");
        var wl = new WeightedLevenshtein(
                (c1, c2) -> {
                    // The cost for substituting 't' and 'r' is considered
                    // smaller as these 2 are located next to each other
                    // on a keyboard
                    if (c1 == 't' && c2 == 'r') {
                        return 0.5;
                    }
                    // For most cases, the cost of substituting 2 characters
                    // is 1.0
                    return 1.0;
                });

        System.out.println(wl.distance("String1", "Srring2"));

        // K-Shingling
        System.out.println("\nK-Shingling");
        s1 = "my string,  \n  my song";
        s2 = "another string, from a song";
        var cosine = new Cosine(4);
        System.out.println(cosine.getProfile(s1));
        System.out.println(cosine.getProfile(s2));

        cosine = new Cosine(2);
        System.out.println(cosine.getProfile("ABCAB"));

    }

}

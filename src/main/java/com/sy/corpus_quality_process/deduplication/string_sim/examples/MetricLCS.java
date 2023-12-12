
package com.sy.corpus_quality_process.deduplication.string_sim.examples;

/**
 *
 */
public class MetricLCS {

        public static void main(String[] args) {
        
        com.sy.corpus_quality_process.deduplication.string_sim.MetricLCS lcs =
                new com.sy.corpus_quality_process.deduplication.string_sim.MetricLCS();
        
        var s1 = "ABCDEFG";
        var s2 = "ABCDEFHJKL";
        // LCS: ABCDEF => length = 6
        // longest = s2 => length = 10
        // => 1 - 6/10 = 0.4
        System.out.println(lcs.distance(s1, s2));
        
        // LCS: ABDF => length = 4
        // longest = ABDEF => length = 5
        // => 1 - 4 / 5 = 0.2
        System.out.println(lcs.distance("ABDEF", "ABDIF"));
    } 
}

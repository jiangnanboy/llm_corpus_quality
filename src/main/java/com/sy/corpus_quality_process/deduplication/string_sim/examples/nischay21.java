
package com.sy.corpus_quality_process.deduplication.string_sim.examples;

import com.sy.corpus_quality_process.deduplication.string_sim.Cosine;
import com.sy.corpus_quality_process.deduplication.string_sim.Damerau;
import com.sy.corpus_quality_process.deduplication.string_sim.Jaccard;
import com.sy.corpus_quality_process.deduplication.string_sim.JaroWinkler;
import com.sy.corpus_quality_process.deduplication.string_sim.Levenshtein;
import com.sy.corpus_quality_process.deduplication.string_sim.NGram;
import com.sy.corpus_quality_process.deduplication.string_sim.SorensenDice;
import com.sy.corpus_quality_process.deduplication.string_sim.interfaces.StringDistance;
import java.util.LinkedList;

/**
 *
 */
public class nischay21 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        var s1 = "MINI GRINDER KIT";
        var s2 = "Weiler 13001 Mini Grinder Accessory Kit, For Use With Small Right Angle Grinders";
        var s3 = "Milwaukee Video Borescope, Rotating Inspection Scope, Series: M-SPECTOR 360, 2.7 in 640 x 480 pixels High-Resolution LCD, Plastic, Black/Red";

        var algos = new LinkedList<StringDistance>();
        algos.add(new JaroWinkler());
        algos.add(new Levenshtein());
        algos.add(new NGram());
        algos.add(new Damerau());
        algos.add(new Jaccard());
        algos.add(new SorensenDice());
        algos.add(new Cosine());


        System.out.println("S1 vs S2");
        for (var algo : algos) {
            System.out.print(algo.getClass().getSimpleName() + " : ");
            System.out.println(algo.distance(s1, s2));
        }
        System.out.println();

        System.out.println("S1 vs S3");
        for (var algo : algos) {
            System.out.print(algo.getClass().getSimpleName() + " : ");
            System.out.println(algo.distance(s1, s3));
        }
        System.out.println();

        System.out.println("With .toLower()");
        System.out.println("S1 vs S2");
        for (var algo : algos) {
            System.out.print(algo.getClass().getSimpleName() + " : ");
            System.out.println(algo.distance(s1.toLowerCase(), s2.toLowerCase()));
        }
        System.out.println();

        System.out.println("S1 vs S3");
        for (var algo : algos) {
            System.out.print(algo.getClass().getSimpleName() + " : ");
            System.out.println(algo.distance(s1.toLowerCase(), s3.toLowerCase()));
        }
        System.out.println();

    }

}

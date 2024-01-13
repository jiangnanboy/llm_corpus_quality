package com.sy.corpus_quality_process.quality_evaluation;
import com.sy.corpus_quality_process.quality_evaluation.ngram.NGramModel;
import edu.berkeley.nlp.lm.ArrayEncodedProbBackoffLm;

/**
 * @author sy
 * @date 2024/1/13 19:19
 */
public class Init {
    public static ArrayEncodedProbBackoffLm<String> model = null;

    /**
     * @param modelPath
     * @throws
     */
    public static void initModel(String modelPath) {
        System.out.println("init ngram model...");
        model = NGramModel.getLm(false, modelPath);
    }
}

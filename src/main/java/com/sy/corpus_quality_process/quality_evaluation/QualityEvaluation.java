package com.sy.corpus_quality_process.quality_evaluation;

import java.util.List;

/**
 * @author sy
 * @date 2024/1/13 19:20
 */
public class QualityEvaluation {

    public QualityEvaluation(String modelPath) {
        Init.initModel(modelPath);
    }

    /**
     * perplexity score
     * @param wordsList
     * @return
     */
    public double pplScore(List<String> wordsList) {
        return Init.model.scoreSentence(wordsList);
    }
}

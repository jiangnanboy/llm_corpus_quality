package com.sy.corpus_quality_main;

import com.sy.corpus_quality_process.quality_evaluation.QualityEvaluation;
import com.sy.corpus_quality_process.rule_quality.RuleQuality;
import com.sy.corpus_quality_process.deduplication.hash_sim.DeDuplication;
import com.sy.corpus_quality_process.sensitivity_advertising.advertising.AdDetection;
import com.sy.corpus_quality_process.sensitivity_advertising.sensitivity.SenDetection;
import com.sy.util.CollectionUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author sy
 * @date 2023/12/7 22:33
 */
public class CorpusQuality {
    RuleQuality ruleQuality = null;
    SenDetection senDetection = null;
    AdDetection adDetection = null;
    DeDuplication deDuplication = null;
    QualityEvaluation qualityEvaluation = null;
    double ppl = 0.0;
    public CorpusQuality(RuleQuality cleanRule, double ppl) {
        this(cleanRule, null, ppl);
    }

    public CorpusQuality(RuleQuality cleanRule, DeDuplication deDuplication, double ppl) {
        this(cleanRule, null, deDuplication, ppl);
    }

    public CorpusQuality(RuleQuality cleanRule, SenDetection senDetection, DeDuplication deDuplication, double ppl) {
        this(cleanRule, senDetection, null, deDuplication, ppl);
    }

    public CorpusQuality(RuleQuality ruleQuality, SenDetection senDetection, AdDetection adDetection, DeDuplication deDuplication, double ppl) {
        this(ruleQuality, senDetection, adDetection, deDuplication, null, ppl);
    }

    public CorpusQuality(RuleQuality ruleQuality, SenDetection senDetection, AdDetection adDetection, DeDuplication deDuplication, QualityEvaluation qualityEvaluation, double ppl) {
        this.ruleQuality = ruleQuality;
        this.senDetection = senDetection;
        this.adDetection = adDetection;
        this.deDuplication = deDuplication;
        this.qualityEvaluation = qualityEvaluation;
        this.ppl = ppl;
    }

    public List<String> quality(List<String> listStr) {
        List<String> paraList = CollectionUtil.newArrayList();
        for(var para : listStr) {
            para = para.trim();
            // stage 1, rule Quality
            if( this.ruleQuality.isBlank(para) ||
                    this.ruleQuality.isLessThanSize(para) ||
                    this.ruleQuality.isLessThanPercentChineseEnglishNumber(para) ||
                    this.ruleQuality.isLessThanPercent(para) ||
                    this.ruleQuality.isMuchOrLittlePunctuation(para) ||
                    this.ruleQuality.isLastTokenPunctuation(para) ||
                    this.ruleQuality.isIDPhoneEMailUrlIp(para) ||
                    this.ruleQuality.isAllDigital(para) ||
                    this.ruleQuality.isLessTextDensity(para)) {
                continue;
            }
            para = this.ruleQuality.consecutiveSpecialSymbol(para);
            para = this.ruleQuality.specialSymbolNoMeaning(para);

            // stage 2, sensitivity and advertising
            if(Optional.ofNullable(this.senDetection).isPresent()) {
                if(this.senDetection.findWords(para).size() > 0) {
                    continue;
                }
            }

            if(Optional.ofNullable(this.adDetection).isPresent()) {
                if(this.adDetection.isAd(para)) {
                    continue;
                }
            }

            // stage 3, deduplication
            if(Optional.ofNullable(this.deDuplication).isPresent()) {
                if(this.deDuplication.isDuplicate(para)) {
                    continue;
                }
            }

            // stage4, quality evaluation
            if(Optional.ofNullable(this.qualityEvaluation).isPresent()) {
                List<String> list = Arrays.asList(para.split(""));
                if(this.qualityEvaluation.pplScore(list) > this.ppl) {
                    continue;
                }
            }

            paraList.add(para);
        }
        return paraList;
    }

    /**
     * quality
     * @param text
     */
    public String quality(String text) {
        text = text.trim();
        // stage 1, rule Quality
        if( this.ruleQuality.isBlank(text) ||
            this.ruleQuality.isLessThanSize(text) ||
            this.ruleQuality.isLessThanPercentChineseEnglishNumber(text) ||
            this.ruleQuality.isLessThanPercent(text) ||
            this.ruleQuality.isMuchOrLittlePunctuation(text) ||
            this.ruleQuality.isLastTokenPunctuation(text) ||
            this.ruleQuality.isIDPhoneEMailUrlIp(text) ||
            this.ruleQuality.isAllDigital(text) ||
            this.ruleQuality.isLessTextDensity(text)) {
            return "";
        }
        text = this.ruleQuality.consecutiveSpecialSymbol(text);
        text = this.ruleQuality.specialSymbolNoMeaning(text);

        // stage 2, sensitivity and advertising
        if(Optional.ofNullable(this.senDetection).isPresent()) {
            if(this.senDetection.findWords(text).size() > 0) {
                return "";
            }
        }

        if(Optional.ofNullable(this.adDetection).isPresent()) {
            if(this.adDetection.isAd(text)) {
                return "";
            }
        }

        // stage 3, deduplication
        if(Optional.ofNullable(this.deDuplication).isPresent()) {
            if(this.deDuplication.isDuplicate(text)) {
                return "";
            }
        }

        // stage4, quality evaluation
        if(Optional.ofNullable(this.qualityEvaluation).isPresent()) {
            List<String> list = Arrays.asList(text.split(""));
            if(this.qualityEvaluation.pplScore(list) > this.ppl) {
                return "";
            }
        }

        return text;
    }

}



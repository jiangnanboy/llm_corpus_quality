package com.sy.corpus_quality_main;

import com.sy.corpus_quality_process.deduplication.hash_sim.DeDuplication;
import com.sy.corpus_quality_process.rule_quality.RuleQuality;
import com.sy.corpus_quality_process.sensitivity_advertising.advertising.AdDetection;
import com.sy.corpus_quality_process.sensitivity_advertising.sensitivity.SenDetection;
import com.sy.corpus_quality_process.sensitivity_advertising.sensitivity.SimpleSenDetectionProcessor;
import com.sy.util.PropertiesReader;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author sy
 * @date 2023/12/7 22:33
 */
public class CorpusQualityMain {
    public static void main(String...args) {
        // hash data of corpus to deduplication (read and save)
        var hashFile = PropertiesReader.get("dedeplication_hash_path");

        //1
        var ruleQuality = new RuleQuality();
        //2
        var simpleSenDetectionProcessor = SimpleSenDetectionProcessor.newInstance();
        var senDetection = simpleSenDetectionProcessor.getKWSeeker("sensitive_words_path");

        var ad_detect_model_path = PropertiesReader.get("ad_detect_model_path");
        var ad_dict_path = PropertiesReader.get("ad_dict_path");
        var stop_words_path = PropertiesReader.get("stop_words_path");
        var adDetection = new AdDetection(ad_detect_model_path, ad_dict_path, stop_words_path);

        //3
        var deDuplication = new DeDuplication(4, 3);
        // load hash
        if(Files.exists(Paths.get(hashFile))) {
            deDuplication.loadHash(hashFile);
        }

        var corpusQuality = new CorpusQuality(ruleQuality, senDetection, adDetection, deDuplication);
        var corpus = "对未按土地、环保和投资管理等法律法规履行相关手续或手续不符合规定的违规项目，地方政府要按照要求进行全面清理。一，凡是未开工的违规项目，一律不得开工建设；二，凡是不符合产业政策、准入标准、环保要求的违规项目一律停建。";
        var result = corpusQuality.quality(corpus);
        System.out.println(result);

        // save hash
        deDuplication.saveHash(hashFile);
    }
}

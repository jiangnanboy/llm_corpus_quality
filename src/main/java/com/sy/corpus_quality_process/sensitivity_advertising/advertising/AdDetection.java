package com.sy.corpus_quality_process.sensitivity_advertising.advertising;

import ai.onnxruntime.*;
import com.sy.util.CollectionUtil;
import com.sy.util.PropertiesReader;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.sy.corpus_quality_process.sensitivity_advertising.advertising.Init.*;

/**
 * @author sy
 * @date 2023/12/7 22:33
 */
public class AdDetection {

    /**
     * @param args
     * @throws OrtException
     */
    public static void main(String...args) throws OrtException {
        String ad_detect_model_path = PropertiesReader.get("ad_detect_model_path");
        String ad_dict_path = PropertiesReader.get("ad_dict_path");
        String stop_words_path = PropertiesReader.get("stop_words_path");
        AdDetection adDetection = new AdDetection(ad_detect_model_path, ad_dict_path, stop_words_path);
        System.out.println(adDetection.isAd("对未按土地、环保和投资管理等法律法规履行相关手续或手续不符合规定的违规项目，地方政府要按照要求进行全面清理。一，凡是未开工的违规项目，一律不得开工建设；二，凡是不符合产业政策、准入标准、环保要求的违规项目一律停建。"));
    }

    public AdDetection(String ad_detect_model_path, String ad_dict_path, String stop_words_path) {
        try {
            initModel(ad_detect_model_path);
        } catch (OrtException e) {
            e.printStackTrace();
        }
        initDict(ad_dict_path);
        initStopWords(stop_words_path);
    }
    /**
     * @param sent
     * @return
     */
    public boolean isAd(String sent) {
        boolean flag = false;
        try {
            Map<String, OnnxTensor> inputMap = parse(sent);
            double prob = infer(inputMap);
            if(prob > 0.5) {
                flag = true;
            }
        } catch (OrtException e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * parse sentence
     * @param sent
     * @return
     * @throws OrtException
     */
    public Map<String, OnnxTensor> parse(String sent) throws OrtException {
        return parse(sent, 500);
    }

    /**
     * parse sentence
     * @param sent
     * @param maxLength
     * @return
     * @throws OrtException
     */
    public Map<String, OnnxTensor> parse(String sent, int maxLength) throws OrtException {
        var tokenList = CollectionUtil.newArrayList();
        for(var i=0;i<sent.length();i++) {
            tokenList.add(sent.substring(i, i+1));
        }
//        tokenList = tokenList.stream().filter(token -> !Init.stopWords.contains(token)).collect(Collectors.toList());
        if(tokenList.size() > maxLength) {
            tokenList = tokenList.subList(0, maxLength - 1);
        } else if(tokenList.size() < maxLength) {
            var range = maxLength - tokenList.size();
            for(var i=0; i<range; i++) {
                tokenList.add("<pad>");
            }
        }
        var tokenIds = tokenList.stream().map(token -> Init.dict.getOrDefault(token, 0L)).collect(Collectors.toList());
        var inputIds = new long[tokenIds.size()];
        for(var i=0; i<tokenIds.size(); i++) {
            inputIds[i] = tokenIds.get(i);
        }
        var shape = new long[]{1, inputIds.length};
        var ObjInputIds = OrtUtil.reshape(inputIds, shape);
        var inputOnnx = OnnxTensor.createTensor(Init.env, ObjInputIds);
        Map<String, OnnxTensor> inputMap = CollectionUtil.newHashMap();
        inputMap.put("input_1", inputOnnx);
        return inputMap;
    }

    /**
     * infer
     * @param inputs
     * @return
     */
    public double infer(Map<String, OnnxTensor> inputs) {
        var prob = 0.;
        try (OrtSession.Result result = Init.session.run(inputs)) {
            var onnxValue = result.get(0);
            var labels = (float[][])onnxValue.getValue();
            var resultLabels = labels[0];
            var softmaxResults = softmax(resultLabels);
            prob = getProb(softmaxResults);
        } catch (OrtException e) {
            e.printStackTrace();
        }
        return prob;
    }

    /**
     * get max prob
     * @param probabilities
     * @return
     */
    public double getMaxProb(double[] probabilities) {
        double maxVal = Float.NEGATIVE_INFINITY;
        for (var i = 0; i < probabilities.length; i++) {
            if (probabilities[i] > maxVal) {
                maxVal = probabilities[i];
            }
        }
        return maxVal;
    }

    /**
     * get prob
     * @param probabilities
     * @return
     */
    public double getProb(double[] probabilities) {
        var prob = probabilities[1];
        return prob;
    }

    /**
     * softmax
     * @param input
     * @return
     */
    private double[] softmax(float[] input) {
        List<Float> inputList = CollectionUtil.newArrayList();
        for(var i=0; i<input.length; i++) {
            inputList.add(input[i]);
        }
        var inputSum = inputList.stream().mapToDouble(Math::exp).sum();
        var output = new double[input.length];
        for (var i=0; i<input.length; i++) {
            output[i] = (Math.exp(input[i]) / inputSum);
        }
        return output;
    }

}



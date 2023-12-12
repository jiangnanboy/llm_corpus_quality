package com.sy.corpus_quality_process.sensitivity_advertising.advertising;

import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;
import com.sy.util.CollectionUtil;
import com.sy.util.PropertiesReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @author sy
 * @date 2023/12/7 22:33
 */
public class Init {
    public static OrtSession session;
    public static OrtEnvironment env;
    public static Map<String, Long> dict;
    public static Set<String> stopWords;

    /**
     * @param modelPath
     * @throws OrtException
     */
    public static void initModel(String modelPath) throws OrtException {
        System.out.println("init model...");
        env = OrtEnvironment.getEnvironment();
        session = env.createSession(modelPath, new OrtSession.SessionOptions());
    }

    public static void closeModel() {
        System.out.println("close model...");
        if(Optional.ofNullable(session).isPresent()) {
            try {
                session.close();
            } catch (OrtException e) {
                e.printStackTrace();
            }
        }
        if(Optional.ofNullable(env).isPresent()) {
            env.close();
        }
    }

    /**
     * @param dictPath
     */
    public static void initDict(String dictPath) {
        System.out.println("init dict...");
        try(var br = Files.newBufferedReader(Paths.get(dictPath), StandardCharsets.UTF_8)) {
            dict = CollectionUtil.newHashMap();
            String line;
            while ((line = br.readLine()) != null) {
                String[] strs = line.split(" ");
                var word = strs[0].trim();
                var index = Long.valueOf(strs[1].trim());
                dict.put(word, index);
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param stopWordsPath
     */
    public static void initStopWords(String stopWordsPath) {
        System.out.println("init stop words...");
        try(BufferedReader br = Files.newBufferedReader(Paths.get(stopWordsPath), StandardCharsets.UTF_8)) {
            stopWords = CollectionUtil.newHashset();
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                stopWords.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}



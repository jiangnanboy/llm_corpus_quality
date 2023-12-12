package com.sy.corpus_quality_process.sensitivity_advertising.sensitivity;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Set;

import com.sy.corpus_quality_process.sensitivity_advertising.sensitivity.conf.Config;
import com.sy.util.CollectionUtil;

/**
 * 简单的敏感词处理器，从配置文件读取敏感词初始化，<br>
 * 使用者只需要在classpath放置sensitive-word.properties敏感词文件即可
 */
public class SimpleSenDetectionProcessor extends SenDetectionManage {

    private static volatile SimpleSenDetectionProcessor instance;

    /**
     * 获取实例
     * 
     * @return
     */
    public static SimpleSenDetectionProcessor newInstance() {
        if (null == instance) {
            synchronized (SimpleSenDetectionProcessor.class) {
                if (null == instance) {
                    instance = new SimpleSenDetectionProcessor();
                }
            }
        }
        return instance;
    }

    /**
     * 私有构造器
     */
    private SimpleSenDetectionProcessor() {
        initialize();
    }

    /**
     * 初始化敏感词
     */
    private void initialize() {
        System.out.println("init sensitive words...");
        var map = Config.newInstance().getAll();
        var entrySet = map.entrySet();

        var seekers = new HashMap<String, SenDetection>();
        Set<KeyWord> kws = null;

        for (var entry : entrySet) {
            var sensitivePath = entry.getValue();
            try(var br = Files.newBufferedReader(Paths.get(sensitivePath), StandardCharsets.UTF_8)) {
                String line = null;
                kws = CollectionUtil.newHashset();
                while(null != (line = br.readLine())) {
                    line = line.trim();
                    kws.add(new KeyWord(line));
                }
                seekers.put(entry.getKey(), new SenDetection(kws));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.seekers.putAll(seekers);
        System.out.println("sensitive words : " + kws.size());
    }
}

package com.sy.corpus_quality_process.sensitivity_advertising.sensitivity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 敏感词管理器
 */
public class SenDetectionManage {

    /**
     * 敏感词模块. key为模块名，value为对应的敏感词搜索器
     */
    protected Map<String, SenDetection> seekers = new ConcurrentHashMap<>();

    /**
     * 初始化
     */
    public SenDetectionManage() {
    }

    /**
     * 
     * @param seekers
     */
    public SenDetectionManage(Map<String, SenDetection> seekers) {
        this.seekers.putAll(seekers);
    }

    /**
     * 获取一个敏感词搜索器
     * 
     * @param wordType
     * @return
     */
    public SenDetection getKWSeeker(String wordType) {
        return seekers.get(wordType);
    }

    /**
     * 加入一个敏感词搜索器
     * 
     * @param wordType
     * @param kwSeeker
     * @return
     */
    public void putKWSeeker(String wordType, SenDetection kwSeeker) {
        seekers.put(wordType, kwSeeker);
    }

    /**
     * 移除一个敏感词搜索器
     * 
     * @param wordType
     * @return
     */
    public void removeKWSeeker(String wordType) {
        seekers.remove(wordType);
    }

    /**
     * 清除空搜索器
     */
    public void clear() {
        seekers.clear();
    }

}

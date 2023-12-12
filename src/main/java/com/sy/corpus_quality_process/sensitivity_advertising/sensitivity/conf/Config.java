package com.sy.corpus_quality_process.sensitivity_advertising.sensitivity.conf;

import com.sy.util.CollectionUtil;
import com.sy.util.PropertiesReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

/**
 * 配置工具类-单例<br>
 */
public final class Config {

    // 缓存配置数据
    private Map<String, String> cacheConfig = CollectionUtil.newHashMap();

    /**
     * 当前部署环境根元素
     */
    private String root;

    /**
     * 实例-volatile
     */
    private static volatile Config conf;

    private Config() {
        var sensitiveWordPath = PropertiesReader.get("sensitive_words_path");
        if(Files.exists(Paths.get(sensitiveWordPath))) {
            cacheConfig.put("sensitive_words_path", sensitiveWordPath);
        }
    }

    /**
     * 获取唯一实例
     * 
     * @return
     */
    public static Config newInstance() {
        if (null == conf) {
            synchronized (Config.class) {
                if (null == conf) {
                    conf = new Config();
                }
            }
        }
        return conf;
    }

    /**
     * 获取全部配置数据
     * 
     * @return
     */
    public Map<String, String> getAll() {
        return cacheConfig;
    }

    /**
     * 基于root根元素的配置获取.<br>
     * key格式：<br>
     * 如配置为:develop.img_upload_server_path 则key为：img_upload_server_path<br>
     * 如配置为:img_upload_server_path 则key为：img_upload_server_path<br>
     * 默认以root.key获取.获取不到则直接根据key获取<br>
     * 如果未配置root元素，则直接以key获取<br>
     * 
     * @param key
     * @return
     */
    public String getString(String key) {
        String value = null;
        // root为前缀获取
        if (null != root) {
            value = cacheConfig.get(root + "." + key);
        }
        // 无前缀.直接key获取
        if (null == value || "".equals(value)) {
            value = cacheConfig.get(key);
        }
        if (null == value) {
            throw new RuntimeException("config key is not found !");
        }
        return value;
    }

    /**
     * 获取int
     * 
     * @param key
     * @return
     */
    public int getInt(String key) {
        var propertie = getString(key);
        if (null == propertie) {
            return 0;
        }
        return Integer.valueOf(propertie);
    }

    /**
     * 获取Long
     * 
     * @param key
     * @return
     */
    public long getLong(String key) {
        var propertie = getString(key);
        if (null == propertie) {
            return 0;
        }
        return Long.valueOf(propertie);
    }

    /**
     * 获取Boolean
     * 
     * @param key
     * @return
     */
    public boolean getBoolean(String key) {
        var propertie = getString(key);
        if (null == propertie) {
            return false;
        }
        return Boolean.valueOf(propertie);
    }

    /**
     * 当前是运行在生产环境
     * 
     * @return
     */
    public boolean isRuningProduc() {
        var b = null == root || "".equals(root) ? false : root.endsWith("produc");
        return b;
    }
}

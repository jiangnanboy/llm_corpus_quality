package com.sy.corpus_quality_process.sensitivity_advertising.sensitivity;

import java.util.HashMap;
import java.util.HashSet;

/**
 * 敏感词测试
 */
public class Test {

    public static void main(String[] args) {

//        test1();
        test2();
//        test3();
    }

    /**
     * 代码调用初始化
     */
    public static void test1() {
        // 初始化敏感词
        var kws1 = new HashSet<KeyWord>();
        kws1.add(new KeyWord("黄色"));
        kws1.add(new KeyWord("AV"));

        var kws2 = new HashSet<KeyWord>();
        kws2.add(new KeyWord("中国"));
        kws2.add(new KeyWord("美国"));
        kws2.add(new KeyWord("黄色"));

        // 根据敏感词,初始化敏感词搜索器
        var kwSeeker1 = new SenDetection(kws1);
        var kwSeeker2 = new SenDetection(kws2);

        // 搜索器组,构建敏感词管理器,可同时管理多个搜索器，map的key为自定义搜索器标识
        var seekers = new HashMap<String, SenDetection>();
        var wordType1 = "sensitive-word-1";
        seekers.put(wordType1, kwSeeker1);
        var wordType2 = "sensitive-word-2";
        seekers.put(wordType2, kwSeeker2);

        var kwSeekerManage = new SenDetectionManage(seekers);

        // 开始测试
        var text1 = "这是一部黄色电影，也是一部AV电影";
        System.out.println("原文：" + text1);
        System.out.println("敏感词：" + kws1);
        System.out.println("识别结果:");

        var words1 = kwSeekerManage.getKWSeeker(wordType1).findWords(text1);
        System.out.println("返回敏感词及下标：" + words1);
        var s1 = kwSeekerManage.getKWSeeker(wordType1).htmlHighlightWords(text1);
        System.out.println("html高亮：" + s1);
        var r1 = kwSeekerManage.getKWSeeker(wordType1).replaceWords(text1);
        System.out.println("字符替换：" + r1);

        System.out.println("\n============================\n");

        // 开始测试
        var text2 = "在中国这是一部黄色电影，在日本这不是一部黄色电影，在美国这不是一部黄色电影";
        System.out.println("原文：" + text2);
        System.out.println("敏感词：" + kws2);
        System.out.println("识别结果:");

        var words2 = kwSeekerManage.getKWSeeker(wordType2).findWords(text2);
        System.out.println("返回敏感词及下标：" + words2);
        var s2 = kwSeekerManage.getKWSeeker(wordType2).htmlHighlightWords(text2);
        System.out.println("html高亮：" + s2);
        var r2 = kwSeekerManage.getKWSeeker(wordType2).replaceWords(text2);
        System.out.println("字符替换：" + r2);
    }

    /**
     * 配置文件读取初始化
     */
    public static void test2() {

        // 搜索器组,构建敏感词管理器,可同时管理多个搜索器，
        var kwSeekerManage = SimpleSenDetectionProcessor.newInstance();

        // 开始测试
        var text1 = "一，凡是未开工的违规项目，一律不得开工建设；二，凡是不符合产业政策、准入标准、环保要求的违规项目一律停建。";
        System.out.println("原文：" + text1);

        var kwSeeker = kwSeekerManage.getKWSeeker("sensitive_words_path");
//        System.out.println("contains: " + kwSeeker.isContainsSensitivity(text1));

        var words1 = kwSeeker.findWords(text1);
        System.out.println("返回敏感词及下标：" + words1);
        var s1 = kwSeeker.htmlHighlightWords(text1);
        System.out.println("html高亮：" + s1);
        var r1 = kwSeeker.replaceWords(text1);
        System.out.println("字符替换：" + r1);

        System.out.println("\n============================\n");
    }

    /**
     * bug fix, 关键字互相包含，也能正确搜索出来。 gomesun
     */
    public static void test3() {
        var kws = new HashSet<KeyWord>();
        kws.add(new KeyWord("中"));
        kws.add(new KeyWord("中国"));
        kws.add(new KeyWord("中国人"));
        kws.add(new KeyWord("国人"));
        SenDetection kwSeeker = new SenDetection(kws);
        var findWords = kwSeeker.findWords("我是中国人来的，真的是中国人来的");
        System.out.println(findWords);
    }

}

package com.sy.corpus_quality_process.sensitivity_advertising.sensitivity.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.sy.corpus_quality_process.sensitivity_advertising.sensitivity.KeyWord;
import com.sy.corpus_quality_process.sensitivity_advertising.sensitivity.SensitiveWordResult;
import com.sy.corpus_quality_process.sensitivity_advertising.sensitivity.util.AnalysisUtil;

/**
 * 对文本中的关键词进行提取。主要根据关键词对文本中的关键词进行提取！
 */
public class WordFinder implements Processor {

    /**
     * 将文本中的关键词提取出来。
     */
    @Override
    public List<SensitiveWordResult> process(Map<String, Map> wordsTree, String text,
                                             AbstractFragment fragment, int minLen) {
        // 词的前面一个字
        String pre = null;
        // 词匹配的开始位置
        var startPosition = 0;
        // 返回结果
        var rs = new ArrayList<SensitiveWordResult>();

        while (true) {
            try {
                if (wordsTree == null || wordsTree.isEmpty() || StringUtils.isEmpty(text)) {
                    return rs;
                }
                if (text.length() < minLen) {
                    return rs;
                }
                var chr = text.substring(0, 1);
                text = text.substring(1);
                var nextWord = wordsTree.get(chr);
                // 没有对应的下一个字，表示这不是关键词的开头，进行下一个循环
                if (nextWord == null) {
                    pre = chr;
                    continue;
                }

                var keywords = new ArrayList<KeyWord>();
                var kw = AnalysisUtil.getSensitiveWord(chr, pre, nextWord, text, keywords);
                if (keywords == null || keywords.size() == 0) {
                    // 没有匹配到完整关键字，下一个循环
                    pre = chr;
                    continue;
                }
                for (var tmp : keywords) {
                    // 同一个word多次出现记录在一起
                    var result = new SensitiveWordResult(startPosition, tmp.getWord());
                    var index = rs.indexOf(result);
                    if (index > -1) {
                        rs.get(index).addPosition(startPosition, tmp.getWord());
                    } else {
                        rs.add(result);
                    }
                }

                // 从text中去除当前已经匹配的内容，进行下一个循环匹配
                // 这行注释了，避免"中国人"，导致"国人"。搜索不出来，逐个字符遍历
                // text = text.substring(kw.getWordLength() - 1);
                pre = kw.getWord().substring(kw.getWordLength() - 1, kw.getWordLength());
                continue;
            } finally {
                if (pre != null) {
                    startPosition = startPosition + pre.length();
                }
            }

        }
    }

    public boolean isContainsSensitivity(Map<String, Map> wordsTree, String text, int minLen) {
        var isContainBoolean = false;
        // 词的前面一个字
        String pre = null;
        // 词匹配的开始位置
        var startPosition = 0;
        var loop = true;
        while (loop) {
            try {
                var chr = text.substring(0, 1);
                text = text.substring(1);
                var nextWord = wordsTree.get(chr);
                // 没有对应的下一个字，表示这不是关键词的开头，进行下一个循环
                if (nextWord == null) {
                    pre = chr;
                    continue;
                }

                var keywords = new ArrayList<KeyWord>();
                var kw = AnalysisUtil.getSensitiveWord(chr, pre, nextWord, text, keywords);
                if (keywords == null || keywords.size() == 0) {
                    // 没有匹配到完整关键字，下一个循环
                    pre = chr;
                    continue;
                }
                isContainBoolean = true;
                loop = false;
                // 从text中去除当前已经匹配的内容，进行下一个循环匹配
                // 这行注释了，避免"中国人"，导致"国人"。搜索不出来，逐个字符遍历
                // text = text.substring(kw.getWordLength() - 1);
                pre = kw.getWord().substring(kw.getWordLength() - 1, kw.getWordLength());
                continue;
            } finally {
                if (pre != null) {
                    startPosition = startPosition + pre.length();
                }
            }

        }
        return isContainBoolean;
    }
}

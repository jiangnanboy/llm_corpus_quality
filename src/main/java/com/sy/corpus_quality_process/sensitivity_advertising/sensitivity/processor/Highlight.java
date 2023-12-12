package com.sy.corpus_quality_process.sensitivity_advertising.sensitivity.processor;

import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.sy.corpus_quality_process.sensitivity_advertising.sensitivity.KeyWord;
import com.sy.corpus_quality_process.sensitivity_advertising.sensitivity.util.AnalysisUtil;

/**
 * 对文本进行高亮处理。
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class Highlight implements Processor {
    /**
     * 将文本中的关键词提取出来。
     * 
     * @param wordsTree 关键词库树
     * @param text 待处理的文本
     * @return 返回提取的关键词或null
     */
    @Override
    public String process(Map<String, Map> wordsTree, String text, AbstractFragment fragment,
                          int minLen) {
        var result = new StringBuffer("");
        String pre = null;// 词的前面一个字
        while (true) {
            if (wordsTree == null || wordsTree.isEmpty() || StringUtils.isEmpty(text)) {
                return result.append(text).toString();
            }
            if (text.length() < minLen) {
                return result.append(text).toString();
            }
            var chr = text.substring(0, 1);
            text = text.substring(1);
            var nextWord = wordsTree.get(chr);
            // 没有对应的下一个字，表示这不是关键词的开头，进行下一个循环
            if (nextWord == null) {
                result.append(chr);
                pre = chr;
                continue;
            }
            var keywords = new ArrayList<KeyWord>();
            var kw = AnalysisUtil.getSensitiveWord(chr, pre, nextWord, text, keywords);
            // 没有匹配到完整关键字，下一个循环
            if (kw == null) {
                result.append(chr);
                pre = chr;
                continue;
            }

            // 处理片段
            result.append(fragment.format(kw));
            // 从text中去除当前已经匹配的内容，进行下一个循环匹配
            text = text.substring(kw.getWordLength() - 1);
            pre = kw.getWord().substring(kw.getWordLength() - 1, kw.getWordLength());
            continue;
        }
    }

}

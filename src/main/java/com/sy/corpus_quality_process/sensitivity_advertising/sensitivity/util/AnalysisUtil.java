package com.sy.corpus_quality_process.sensitivity_advertising.sensitivity.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.sy.corpus_quality_process.sensitivity_advertising.sensitivity.KeyWord;

/**
 * 关键词分析工具类。
 */
public class AnalysisUtil {

    /**
     * 构建关键词
     * 
     * @param word 词语
     * @param endTag 属性信息
     * @return
     */
    public static KeyWord getKeyWord(String word, Map<String, Object> endTag) {
        var tmp = new KeyWord(word);
        return tmp;
    }

    /**
     * 将指定的词构造到一棵树中。
     * 
     * @param tree 构造出来的树
     * @param word 指定的词
     * @param KeyWord 对应的词
     * @return
     */
    public static Map<String, Map> makeTreeByWord(Map<String, Map> tree, String word,
            KeyWord KeyWord) {
        if (StringUtils.isEmpty(word)) {
            tree.putAll(EndTagUtil.buind(KeyWord));
            return tree;
        }
        var next = word.substring(0, 1);
        var nextTree = tree.get(next);
        if (nextTree == null) {
            nextTree = new HashMap<String, Map>();
        }
        // 递归构造树结构
        tree.put(next, makeTreeByWord(nextTree, word.substring(1), KeyWord));
        return tree;
    }

    /**
     * 根据精确、模糊等匹配方式返回相应的实际关键词。
     * 
     * @param kw 敏感词
     * @param pre 前缀
     * @param sufix 后缀
     * @return
     */
    private static KeyWord checkPattern(KeyWord kw, String pre, String sufix) {
        if (StringUtils.isNotBlank(kw.getPre()) && StringUtils.isNotBlank(kw.getSufix())) {
            if (null == pre || null == sufix) {
                return null;
            }
            if (!pre.matches(kw.getPre()) || !sufix.matches(kw.getSufix())) {
                return null;
            }
        } else if (StringUtils.isNotBlank(kw.getPre()) && StringUtils.isBlank(kw.getSufix())) {
            if (null == pre) {
                return null;
            }
            if (!pre.matches(kw.getPre())) {
                return null;
            }
        } else if (StringUtils.isBlank(kw.getPre()) && StringUtils.isNotBlank(kw.getSufix())) {
            if (null == sufix) {
                return null;
            }
            if (!sufix.matches(kw.getSufix())) {
                return null;
            }
        }
        return kw;
    }

    /**
     * 查询文本开头的词是否在词库树中，如果在，则返回对应的词，如果不在，则返回null。return 返回找到的最长关键词
     * 
     * @param append 追加的词
     * @param pre 词的前一个字，如果为空，则表示前面没有内容
     * @param nextWordsTree 下一层树
     * @param text 剩余的文本内容
     * @param keywords 返回的keywords，可能多个
     * @return 返回找到的最长关键词
     */
    public static KeyWord getSensitiveWord(String append, String pre,
                    Map<String, Map> nextWordsTree, String text, List<KeyWord> keywords) {
        if (nextWordsTree == null || nextWordsTree.isEmpty()) {
            return null;
        }

        var endTag = nextWordsTree.get(EndTagUtil.TREE_END_TAG);
        // 原始文本已到末尾
        if (StringUtils.isEmpty(text)) {
            // 如果有结束符，则表示匹配成功，没有，则返回null
            if (endTag != null) {
                keywords.add(checkPattern(getKeyWord(append, endTag), pre, null));
                return checkPattern(getKeyWord(append, endTag), pre, null);
            } else {
                return null;
            }
        }

        var next = text.substring(0, 1);
        var suffix = text.substring(0, 1);
        var nextTree = nextWordsTree.get(next);

        // 没有遇到endTag，继续匹配
        if (endTag == null) {
            if (nextTree != null && nextTree.size() > 0) {
                // 没有结束标志，则表示关键词没有结束，继续往下走。
                return getSensitiveWord(append + next, pre, nextTree, text.substring(1), keywords);
            }

            // 如果没有下一个匹配的字，表示匹配结束！
            return null;
        } else { // endTag ， 添加关键字
            var tmp = getKeyWord(append, endTag);
            keywords.add(checkPattern(tmp, pre, suffix));
        }

        // 有下一个匹配的词则继续匹配，一直取到最大的匹配关键字
        KeyWord tmp = null;
        if (nextTree != null && nextTree.size() > 0) {
            // 如果大于0，则表示还有更长的词，继续往下找
            tmp = getSensitiveWord(append + next, pre, nextTree, text.substring(1), keywords);
            if (tmp == null) {
                // 没有更长的词，则就返回这个词。在返回之前，先判断它是模糊的，还是精确的
                tmp = getKeyWord(append, endTag);
            }
            return checkPattern(tmp, pre, suffix);
        }

        // 没有往下的词了，返回该关键词。
        return checkPattern(getKeyWord(append, endTag), pre, suffix);

    }

}

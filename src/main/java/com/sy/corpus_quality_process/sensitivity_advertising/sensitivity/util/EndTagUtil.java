package com.sy.corpus_quality_process.sensitivity_advertising.sensitivity.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.sy.corpus_quality_process.sensitivity_advertising.sensitivity.KeyWord;

/**
 * 关键词尾节点
 *
 */
public class EndTagUtil implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 8278503553932163596L;

    /**
     * 尾节点key
     */
    public static final String TREE_END_TAG = "end";

    public static Map<String, Map> buind(KeyWord KeyWord) {
        var endTag = new HashMap<String, Map>();
        endTag.put(TREE_END_TAG, new HashMap<String, String>());
        return endTag;
    }

}

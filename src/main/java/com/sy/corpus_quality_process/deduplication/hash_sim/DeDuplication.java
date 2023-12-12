package com.sy.corpus_quality_process.deduplication.hash_sim;

import com.sy.util.CollectionUtil;
import com.sy.word_seg.WordSegment;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DeDuplication {

    private List<Map<String, List<Long>>> hashStorage;
    private int bitNum = 64;
    private int fracCount = 4;
    private int fracBitNum = bitNum / fracCount;
    private int hammingThresh = 3;

    public DeDuplication() {
        hashStorage = CollectionUtil.newArrayList();
        for (var i = 0; i < fracCount; i++) {
            hashStorage.add(CollectionUtil.newHashMap());
        }
    }

    public DeDuplication(int fracCount, int hammingThresh) {
        this.fracCount = fracCount;
        fracBitNum = bitNum / fracCount;
        this.hammingThresh = hammingThresh;
        hashStorage = CollectionUtil.newArrayList();
        for (var i = 0; i < fracCount; i++) {
            hashStorage.add(CollectionUtil.newHashMap());
        }
    }

    /**
     * @param content
     * @return Long
     */
    public Long calSimhash(String content) {
        var filterContent = content.trim().replaceAll("\\p{Punct}|\\p{Space}", "");
        var entryList = WordSegment.textSegment(filterContent);
        // 按照词语的hash值，计算simHashWeight(低位对齐)
        var weight = new Integer[bitNum];
        Arrays.fill(weight, 0);
        for (var entry : entryList) {
            var wordHash = HashCode.hash64(entry.getWord().getBytes());
            for (var i = 0; i < bitNum; i++) {
                if (((wordHash >> i) & 1) == 1) {
                    weight[i] += 1;
                } else {
                    weight[i] -= 1;
                }
            }
        }

        // 计算得到Simhash值
        var sb = new StringBuilder();
        for (var i = 0; i < bitNum; i++) {
            if (weight[i] > 0) {
                sb.append(1);
            } else {
                sb.append(0);
            }
        }
        return new BigInteger(sb.toString(), 2).longValue();
    }

    /**
     * @param content
     * @return
     */
    public boolean isDuplicate(String content) {
        var flag = false;
        var simhash = calSimhash(content);
        var lFrac = splitSimhash(simhash);
        for (var i = 0; i < fracCount; i++) {
            var frac = lFrac.get(i);
            var fracMap = hashStorage.get(i);
            if (fracMap.containsKey(frac)) {
                for (var simhash2 : fracMap.get(frac)) {
                    if (hamming(simhash, simhash2) < hammingThresh) {
                        flag = true;
                        break;
                    }
                }
                if(flag) {
                    break;
                }
            }
        }
        store(simhash);
        return flag;
    }

    /**
     * 按照(frac, <simhash>)索引进行存储
     * @param simhash
     */
    public void store(Long simhash) {
        var lFrac = splitSimhash(simhash);
        for (var i = 0; i < fracCount; i++) {
            var frac = lFrac.get(i);
            var fracMap = hashStorage.get(i);
            if (fracMap.containsKey(frac)) {
                fracMap.get(frac).add(simhash);
            } else {
                List<Long> ls = CollectionUtil.newArrayList();
                ls.add(simhash);
                fracMap.put(frac, ls);
            }
        }
    }

    /**
     * @param s1
     * @param s2
     * @return
     */
    private int hamming(Long s1, Long s2) {
        var dis = 0;
        for (var i = 0; i < bitNum; i++) {
            if ((s1 >> i & 1) != (s2 >> i & 1)) {
                dis++;
            }
        }
        return dis;
    }

    /**
     * @param s1
     * @param s2
     * @return
     */
    private int hamming(String s1, String s2) {
        if (s1.length() != s2.length()) {
            return 0;
        }
        var dis = 0;
        for (var i = 0; i < s1.length(); i++) {
            if (s1.charAt(i) != s2.charAt(i)) {
                dis++;
            }
        }
        return dis;
    }

    /**
     * 将simhash分成n段
     * @param simhash
     * @return
     */
    private List<String> splitSimhash(Long simhash) {
        List<String> ls = CollectionUtil.newArrayList();
        var sb = new StringBuilder();
        for (var i = 0; i < bitNum; i++) {
            sb.append(simhash >> i & 1);
            if ((i + 1) % fracBitNum == 0) {
                ls.add(sb.toString());
                sb.setLength(0);
            }
        }
        return ls;
    }

    /**
     * @param file
     */
    public void saveHash(String file) {
        try(var out = new ObjectOutputStream(new FileOutputStream(new File(file)))) {
            out.writeObject(hashStorage);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param file
     */
    public void loadHash(String file) {
        if(Files.exists(Paths.get(file))) {
            try(var in = new ObjectInputStream(new FileInputStream(new File(file)))) {
                hashStorage = (List<Map<String, List<Long>>>) in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}


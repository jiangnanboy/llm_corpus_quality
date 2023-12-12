package com.sy.word_seg;


/**
 * @author sy
 * @date 2023/12/7 22:33
 */
public class Entry {
    private String word;
    private String pos;

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    private int offset;

    public Entry(String word, String pos) {
        this.word = word;
        this.pos = pos;
    }

    public Entry(String word, String pos, int offset) {
        this.word = word;
        this.pos = pos;
        this.offset = offset;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getPos() {
        return pos;
    }

    public void setFreq(String pos) {
        this.pos = pos;
    }

}

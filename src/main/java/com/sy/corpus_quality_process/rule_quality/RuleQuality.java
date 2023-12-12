package com.sy.corpus_quality_process.rule_quality;

import com.sy.word_seg.WordSegment;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author sy
 * @date 2023/12/7 22:33
 */
public class RuleQuality {

    /**
     * @param text
     */
    public boolean isEnglish(String text) {
        var flag = true;
        for(var i = 0; i < text.length(); i++) {
            var c = text.charAt(i);
            if(isChinese(c)) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    /**
     * @param c
     * @return
     */
    public boolean isChinese(char c) {
        return c >= '\u4e00' && c <= '\u9fa5';
    }

    /**
     * @param text
     */
    public boolean isBlank(String text) {
        return StringUtils.isBlank(text);
    }

    public boolean isAllDigital(String text) {
        var regexNum = "[0-9]+";
        return isMatch(regexNum, text);
    }

    /**
     * @param text
     * @param size
     */
    public boolean isLessThanSize(String text, int size) {
        return text.length() <= size;
    }

    /**
     * @param text
     * @return
     */
    public boolean isLessThanSize(String text) {
        return isLessThanSize(text, 10);
    }

    /**
     * @param text
     * @param percent
     */
    public boolean isLessThanPercent(String text, double percent) {
        var flag = true;
        var regex = "[\\u4e00-\\u9fa5]";
        var result = findAllMatch(regex, text);
        if(1.0 * result.length() / text.length() >= percent) {
            flag = false;
        }
        return flag;
    }

    /**
     * @param text
     * @return
     */
    public boolean isLessThanPercent(String text) {
        return isLessThanPercent(text, 0.5);
    }

    /**
     * @param text
     * @param percent
     * @return
     */
    public boolean isLessThanPercentChineseEnglishNumber(String text, double percent) {
        var flag = true;
        var regexChinese = "[\\u4e00-\\u9fa5]";
        var regexEnglish = "[a-zA-Z]";
        var regexNum = "[0-9]+";
        var resultChinese = findAllMatch(regexChinese, text);
        var resultEnglish = findAllMatch(regexEnglish, text);
        var resultNum = findAllMatch(regexNum, text);
        if(1.0 * (resultChinese.length() + resultEnglish.length() + resultNum.length()) / text.length() >= percent) {
            flag = false;
        }
        return flag;
    }

    /**
     * @param text
     * @return
     */
    public boolean isLessThanPercentChineseEnglishNumber(String text) {
        return isLessThanPercentChineseEnglishNumber(text, 0.4);
    }

    /**
     * @param text
     */
    public boolean isMuchOrLittlePunctuation(String text, double littlePercent, double muchPercent) {
        var flag = true;
        var punctuation = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~。？！，、；：：”“（）——……《 》▪—";
        var punctuationList = Arrays.stream(punctuation.split("")).collect(Collectors.toList());
        var punctuationCount = Arrays.stream(text.split(""))
                .filter(word -> punctuationList.contains(word))
                .count();
        if((1.0 * punctuationCount / text.length() >= littlePercent) || (1.0 * punctuationCount / text.length() <= muchPercent)) {
            flag = false;
        }
        return flag;
    }

    /**
     * @param text
     * @return
     */
    public boolean isMuchOrLittlePunctuation(String text) {
        return isMuchOrLittlePunctuation(text, 0.005, 0.5);
    }

    /**
     * @param text
     */
    public boolean isNotLastTokenPunctuation(String text, String[] punctuation) {
        var lastToken = text.substring(text.length() - 1).trim();
        var flag = true;
        if(StringUtils.equalsAny(lastToken, punctuation)) {
            flag = false;
        }
        return flag;
    }

    /**
     * @param text
     * @return
     */
    public boolean isLastTokenPunctuation(String text) {
        return isNotLastTokenPunctuation(text, new String[]{"。", ".", "!", "！", "?", "？", "……", "…"});
    }

    /**
     * @param text
     * @return
     */
    public boolean isIDPhoneEMailUrlIp(String text) {
        var flag = false;
        var urlRegex = "(http(s)?://)?([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";
        var emailRegex = "\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?";
        var idRegex = "\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?";
        var mobileRegex = "(\\+\\d+)?1[3458]\\d{9}$";
        var telephoneRegex = "(\\+\\d+)?(\\d{3,4}\\-?)?\\d{7,8}$";
        var ipRegex = "\\d+.\\d+.\\d+.\\d+";
        if(isMatch(urlRegex, text) || isMatch(emailRegex, text) || isMatch(idRegex, text) ||
           isMatch(mobileRegex, text) || isMatch(telephoneRegex, text) || isMatch(ipRegex, text)) {
            flag = true;
        }
        return flag;
    }

    /**
     * @param text
     * @param percent
     * @return
     */
    public boolean isLessTextDensity(String text, double percent) {
        var flag = false;
        var punctuation = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~。？！，、；：：”“（）——……《 》▪—";
        var punctuationList = Arrays.stream(punctuation.split("")).collect(Collectors.toList());
        var allEntryList = WordSegment.textSegment(text);
        allEntryList = allEntryList.stream().filter(entry -> !punctuationList.contains(entry.getWord())).collect(Collectors.toList());
        var allEntrySize = allEntryList.size();
        var entrySize = allEntryList.stream().filter(entry -> entry.getWord().length() >= 2).count();
        if(1.0 * entrySize / allEntrySize < percent) {
            flag = true;
        }
        return flag;
    }

    public boolean isLessTextDensity(String text) {
        return isLessTextDensity(text, 0.6);
    }

    /**
     * @param text
     * @return
     */
    public String consecutiveSpecialSymbol(String text) {
        var consecutiveRegex = "[-]{4,}|[.]{4,}|[=]{4,}";
        return matchReplacement(consecutiveRegex, text);
    }

    /**
     * @param text
     * @return
     */
    public String specialSymbolNoMeaning(String text) {
        var specialRegex = "[\\¤\\⌒\\々\\〓\\▌\\◇\\▲\\△\\▪\\★\\◆\\▼\\●\\▽\\◁\\☆\\○]";
        return matchReplacement(specialRegex, text);
    }

    /**
     * @param regex
     * @param str
     * @return
     */
    public String findAllMatch(String regex, String str) {
        var sb = new StringBuilder();
        var pattern = Pattern.compile(regex);
        var matcher = pattern.matcher(str);
        while (matcher.find()) {
            sb.append(matcher.group());
        }
        return sb.toString();
    }

    /**
     * @param regex
     * @param str
     * @return
     */
    public boolean isMatch(String regex, String str) {
        var pattern = Pattern.compile(regex);
        var matcher = pattern.matcher(str);
        return matcher.matches();
    }

    /**
     * @param regex
     * @param str
     * @return
     */
    public String matchReplacement(String regex, String str) {
        var pattern = Pattern.compile(regex);
        var matcher = pattern.matcher(str);
        return matcher.replaceAll("");
    }

}



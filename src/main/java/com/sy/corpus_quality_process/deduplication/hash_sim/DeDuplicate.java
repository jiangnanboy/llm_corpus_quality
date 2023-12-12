package com.sy.corpus_quality_process.deduplication.hash_sim;

/**
 * @author sy
 * @date 2023/12/7 22:33
 */
public class DeDuplicate {
    public static void main(String...args) {
        var textTest = new String[]{"2022年是中国东盟全面战略伙伴关系开局之年，也是中国与东盟建立对话关系31周年。中国与东盟之间有着坚实的合作基础和巨大的发展潜力。\n" +
                "\t\n","专访中国-东盟商务理事会执行理事长许宁宁：中国东盟自贸区3.0版建设有助贸易投资提质升级，增强产业合作和对接十分重要\n", "“一带一路”旗舰项目巡礼之二十一丨中马友谊大桥创世界桥梁建造之最 马尔代夫期待多元化战略对接\n",
        "菲总统访华签署14项合作文件，中菲关系开创新“黄金时代”\n", "中国与东盟之间有着坚实的合作基础和巨大的发展潜力"};
        var hashStoragePath = "storage.txt";
        var deDuplicate = new DeDuplicate();
        var deDuplication = new DeDuplication(4, 3);
        deDuplication.loadHash(hashStoragePath);
        for(var txt : textTest) {
            deDuplicate.dataDeDuplication(deDuplication, txt);
        }
        deDuplication.saveHash(hashStoragePath);
    }

    /**
     * @param text
     */
    public void dataDeDuplication(DeDuplication deDuplication, String text) {
        System.out.println(deDuplication.isDuplicate(text));
    }

}



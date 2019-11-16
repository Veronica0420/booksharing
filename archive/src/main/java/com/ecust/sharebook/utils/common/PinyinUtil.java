package com.ecust.sharebook.utils.common;

import lombok.extern.slf4j.Slf4j;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * @author zhangyu
 * @description 汉语拼音工具类
 * @date 2019/5/23 17:42
 */
@Slf4j
public class PinyinUtil {

    /**
     * 将文字转为汉语拼音
     * @param chineseLanguage 要转成拼音的中文
     */
    public static String convertToPinyin(String chineseLanguage){
        char[] cl_chars = chineseLanguage.trim().toCharArray();
        String pinyin = "";
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        // 输出拼音全部小写
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        // 不带声调
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        defaultFormat.setVCharType(HanyuPinyinVCharType.WITH_V) ;
        try {
            for (int i=0; i<cl_chars.length; i++){
                // 如果字符是中文,则将中文转为汉语拼音
                if (String.valueOf(cl_chars[i]).matches("[\u4e00-\u9fa5]+")){
                    pinyin += PinyinHelper.toHanyuPinyinStringArray(cl_chars[i], defaultFormat)[0];
                } else {
                    // 如果字符不是中文,则不转换
                    pinyin += cl_chars[i];
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            System.out.println("字符:{"+chineseLanguage+"},转拼音异常，原因为{"+e+"}");
        }
        return pinyin;
    }


    /**
     * 取第一个汉字的第一个字符
     * @Title: getFirstLetter
     * @Description: TODO
     * @return String
     * @throws
     */
    public static String getFirstLetter(String chineseLanguage){
        char[] cl_chars = chineseLanguage.trim().toCharArray();
        String pinyin = "";
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        // 输出拼音全部大写
        defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        // 不带声调
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        try {
            String str = String.valueOf(cl_chars[0]);
            // 如果字符是中文,则将中文转为汉语拼音,并取第一个字母
            if (str.matches("[\u4e00-\u9fa5]+")) {
                pinyin = PinyinHelper.toHanyuPinyinStringArray(cl_chars[0], defaultFormat)[0].substring(0, 1);
            } else if (str.matches("[0-9]+")) {
                // 如果字符是数字,取数字
                //pinyin += cl_chars[0];
                //本次需求数字变成#
                return "#";
            } else if (str.matches("[a-zA-Z]+")) {
                // 如果字符是字母,取字母
                pinyin += cl_chars[0];
            } else {
                // 否则返回'#'
                return "#";
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            System.out.println("字符:{"+chineseLanguage+"},转拼音异常，原因为{"+e+"}");
        }
        if(chineseLanguage.startsWith("重庆") || chineseLanguage.startsWith("长沙")
                || chineseLanguage.startsWith("长春") ||chineseLanguage.startsWith("长治")){
            return "C";
        }
        if(chineseLanguage.startsWith("厦门")){
            return "X";
        }
        return pinyin;
    }

    public static void main(String[] args) {
        System.out.println(convertToPinyin("张飞"));
        System.out.println(getFirstLetter("长沙市"));
    }
}


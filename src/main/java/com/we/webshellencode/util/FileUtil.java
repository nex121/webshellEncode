package com.we.webshellencode.util;

import java.io.BufferedReader;
import java.io.FileReader;

public class FileUtil {
    public static String ReadFileContent(String filePath) {
        StringBuilder result = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String s = null;
            while ((s = br.readLine()) != null) {
                result.append(s);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    public static String txt2Unicode(String fileContent) {
        StringBuilder unicode = new StringBuilder();
        boolean ignoreEncode = false;

        for (int i = 0; i < fileContent.length(); i++) {
            char c = fileContent.charAt(i);
            if (c == '@' && fileContent.charAt(i + 1) == 'p') {
                ignoreEncode = true;
            }

            if (ignoreEncode) {
                if (c == '>' && i > 0 && fileContent.charAt(i - 1) == '%') {
                    unicode.append(c);
                    ignoreEncode = false;
                    continue;
                } else {
                    unicode.append(c);
                    continue;
                }
            }

            if (c == '<' && i < fileContent.length() - 1 && fileContent.charAt(i + 1) == '%') {
                unicode.append(c);
                unicode.append(fileContent.charAt(i + 1));
                i++; // 跳过一个字符
            } else if (c == '%' && i > 0 && fileContent.charAt(i + 1) == '>') {
                unicode.append(c);
                unicode.append(fileContent.charAt(i + 1));
                i++;
            } else if (c == '!' || c == '@' || c == '#') {
                unicode.append(c);
            } else {
                unicode.append("\\u");
                unicode.append(Integer.toHexString(c | 0x10000).substring(1));
            }
        }
        return unicode.toString();
    }

}

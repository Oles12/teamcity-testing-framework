package com.example.teamcity.api.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringNameConversion {

    /** Converts 'name' value to 'id' value using special format string
     Example:
     * Input: test_rJject_000 → Output: TestRJject
     * Input: TestnYTYkHxmbv → Output: TestNYTYkHxmbvv
     */
    public static String convertNameToId(String name) {
        // Remove underscores and capitalize the character immediately following each underscore
        String id = name.replaceAll("_(.)", "$1");

        // Capitalize the first letter of the string
        Pattern pattern = Pattern.compile("^[a-z]");
        Matcher matcher = pattern.matcher(id);
        if (matcher.find()) {
            id = matcher.replaceFirst(matcher.group(0).toUpperCase());
        }

        // Manually loop through characters and capitalize letters after lowercase sequences
        StringBuilder result = new StringBuilder();
        char[] chars = id.toCharArray();
        boolean capitalizeNext = false;

        for (int i = 0; i < chars.length; i++) {
            char currentChar = chars[i];

            if (i == 0) {
                // Capitalize first character
                result.append(Character.toUpperCase(currentChar));
            } else if (capitalizeNext) {
                result.append(Character.toUpperCase(currentChar));
                capitalizeNext = false;
            } else {
                // Append as-is, if not uppercase already
                if (Character.isLowerCase(currentChar)) {
                    capitalizeNext = (i < chars.length - 1 && Character.isUpperCase(chars[i + 1]));
                }
                result.append(currentChar);
            }
        }

        // Remove any leading zeros after the last underscore
        id = result.toString().replaceAll("0+", "");

        return id;
    }
}

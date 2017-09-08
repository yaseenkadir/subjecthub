package com.example.subjecthub.utils;

import me.xdrop.fuzzywuzzy.FuzzySearch;
import me.xdrop.fuzzywuzzy.StringProcessor;

public class FuzzyUtils {

    public static final int DEFAULT_MATCH_THRESHOLD = 70;

    /**
     * Custom string processor to perform case insensitive fuzzy matching.
     */
    public static StringProcessor caseInsensitiveProcessor = s -> s.toLowerCase();

    /**
     * Shortcut method to check similarity between two strings. Allows specifying processor.
     *
     * @param processor preprocesses strings before comparing similarity
     * @return
     */
    public static int similarity(String s1, String s2, StringProcessor processor) {
        return FuzzySearch.partialRatio(s1, s2, processor);
    }

    /**
     * Shortcut method to check similarity between two strings. Uses case insensitive matching.
     *
     * @return similarity as a ratio between 0 and 100.
     */
    public static int similarity(String s1, String s2) {
        return similarity(s1, s2, caseInsensitiveProcessor);
    }

    /**
     * Checks if a string is similar to another string.
     * @param s1 string to compare similarity
     * @param s2 string to compare similarity
     * @param ratio ratio of what is considered similar (0 - 100)
     * @param processor preprocesses strings before comparing similarity
     * @return
     */
    public static boolean isSimilar(String s1, String s2, int ratio, StringProcessor processor) {
        return similarity(s1, s2, processor) >= ratio;
    }

    /**
     * Checks if a string is similar to another string. Case insensitive by default.
     * @param s1
     * @param s2
     * @param ratio ratio of what is considered similar (0 - 100)
     * @return
     */
    public static boolean isSimilar(String s1, String s2, int ratio) {
        return similarity(s1, s2, caseInsensitiveProcessor) >= ratio;
    }

    /**
     * Checks if a sting is similar to another string. Case insensitive by default. Uses
     * {@value #DEFAULT_MATCH_THRESHOLD} as threshold of what is considered similar.
     */
    public static boolean isSimilar(String s1, String s2) {
        return similarity(s1, s2) >= DEFAULT_MATCH_THRESHOLD;
    }
}

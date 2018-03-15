/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaClass;

import static java.awt.SystemColor.text;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 *
 * @author lin
 */
public class WordCount {
    public static class Word implements Comparable<Word> {
        String word;
        int count;

        @Override
        public int hashCode() { return word.hashCode(); }

        @Override
        public boolean equals(Object obj) { return word.equals(((Word)obj).word); }

        @Override
        public int compareTo(Word b) { return b.count - count; }
    }
    
    public static void main(String[] args) throws IOException{
    
    }
    
    
    public SortedSet<Word> checkWordCount(String message) throws IOException {
        //message is for the 
        message = "";
        long time = System.currentTimeMillis();

        Map<String, Word> countMap = new HashMap<String, Word>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(message.getBytes(StandardCharsets.UTF_8))));
        String line ="";
        while ((line = reader.readLine()) != null) {
            String[] words = line.split("[^A-ZÃƒâ€¦Ãƒâ€žÃƒâ€“a-zÃƒÂ¥ÃƒÂ¤ÃƒÂ¶]+");
            for (String word : words) {
                if ("".equals(word)) {
                    continue;
                }

                Word wordObj = countMap.get(word);
                if (wordObj == null) {
                    wordObj = new Word();
                    wordObj.word = word;
                    wordObj.count = 0;
                    countMap.put(word, wordObj);
                }

                wordObj.count++;
            }
        }

        reader.close();

        SortedSet<Word> sorted = new TreeSet<Word>(countMap.values());
        int i = 0;
        int maxWordsToDisplay = 10;

        String[] wordsToIgnore = {"the", "and", "a"};

        for (Word word : sorted) {
            if (i >= maxWordsToDisplay) { //10 is the number of words you want to show frequency for
                break;
            }

            if (Arrays.asList(wordsToIgnore).contains(word.word)) {
                i++;
                maxWordsToDisplay++;
            } else {
                System.out.println(word.count + "\t" + word.word);
                i++;
            }

        }

        time = System.currentTimeMillis() - time;

        System.out.println("Finished in " + time + " ms");
        return sorted;
    }

}


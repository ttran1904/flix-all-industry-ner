package com.flix.ner;

import java.io.*;

public class templateTrainingData {

    public static void template(String fileIn, String fileOut) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileIn));
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileOut));

        String line;
        while ((line = reader.readLine()) != null) {
            line = line.replace("/O", "").replace(",", "");
            String[] arr = line.split(" ");

            for (int i = 0; i < arr.length; i++) {
                String word = arr[i].trim().replace(" ", "");
                if (word.contains("/B-")) {
                    word = new StringBuilder("<START:"
                            + word.substring(word.indexOf("/B-") + 3).toLowerCase()
                            + ">" + word.substring(0, word.indexOf("/B-")) ).
                            toString();
                } else { continue;}

                // Check the consecutive words if the current one has a /B- tag
                for (int j = 1; i+j < arr.length; j++) {
                    String nextWord = arr[i+j].trim();
                    String wordAfter;
                    if (i+j+1 < arr.length) { wordAfter = arr[i+j+1].trim(); } // Fix this to check last word!
                    else { wordAfter = ""; }


                    if (nextWord.contains("/B-") || wordNotTagBoth(nextWord)) {
                        word = new StringBuilder(word + "<END>").toString();
                    } else if (nextWord.contains("/I-")
                            && (wordAfter.contains("/B-") || wordNotTagBoth(wordAfter))) {
                        nextWord = deleteOldTag(nextWord);
                        nextWord = new StringBuilder(nextWord + "<END>").toString();
                    } else if (nextWord.contains("/I-") && !wordNotTag(wordAfter, "/I-")) {
                        nextWord = deleteOldTag(nextWord);
                        arr[i+j] = nextWord;
                        continue;
                    }
                    arr[i+j] = nextWord;
                    break;
                }
                arr[i] = word;
                //System.out.println("Does not contain any tags?: " + wordNotTagBoth(word));
            }

            StringBuilder builder = new StringBuilder();
            for (String s : arr) {
                builder.append(s);
                builder.append(" ");
            }
            writer.write(builder.toString());
            writer.newLine();
        }
        reader.close();
        writer.close();
    }

    public static boolean wordNotTag(String word, String tag) {
        if (!word.contains(tag)) { return true; }
        return false;
    }

    public static boolean wordNotTagBoth (String word) {
        if ( !(word.contains("/B-") || word.contains("/I-")) ) { return true; }
        return false;
    }

    public static String deleteOldTag(String word) {
        if (word.contains("/B-")) {
            return word.substring(0, word.indexOf("/B-"));
        } else if (word.contains("/I-")) {
            return word.substring(0, word.indexOf("/I-"));
        }
        return word;
    }


    public static void main(String [] args) throws IOException {
        String file = "data/ner_new_0.txt";
        template(file, file.replace("0.txt", "1_edit.txt"));
    }
}

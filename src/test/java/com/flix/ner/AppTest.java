package com.flix.ner;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import java.util.*;
import java.io.*;
import vn.pipeline.*;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() { assertTrue( true ); }

    public static void main(String[] args) throws IOException {


        writeTSV("data/ner_0.txt", "models/ner/vi-ner.tsv");
//        // "wseg", "pos", "ner", and "parse" refer to as word segmentation, POS tagging, NER and dependency parsing, respectively.
//        String[] annotators = {"wseg", "ner"};
//        VnCoreNLP pipeline = new VnCoreNLP(annotators);
//
//        String str = "Màn hình ss A 8 là bao nhieu vay";
//
//        Annotation annotation = new Annotation(str);
//        pipeline.annotate(annotation);
//
//        System.out.println(annotation.toString());
//        // 1    Ông                 Nc  O       4   sub
//        // 2    Nguyễn_Khắc_Chúc    Np  B-PER   1   nmod
//        // 3    đang                R   O       4   adv
//        // 4    làm_việc            V   O       0   root
//        // ...
//
//        //Write to file
//        PrintStream outputPrinter = new PrintStream("output.txt");
//        pipeline.printToFile(annotation, outputPrinter);
//
//        // You can also get a single sentence to analyze individually
//        Sentence firstSentence = annotation.getSentences().get(0);
//        System.out.println(firstSentence.toString());
    }

    public static void writeTSV (String input, String output) {
        try {
            // Tab delimited file will be written to data with the name tab-file.csv
            FileWriter fos = new FileWriter(output);
            PrintWriter dos = new PrintWriter(fos);

            BufferedReader reader = new BufferedReader(new FileReader(input));
            String line;
            while ( (line = reader.readLine()) != null ) {
                String [] str = line.split(" ");

                for (int i = 1; i < str.length; i++) {
                    String s = str[i];
                    String form = s.substring(0, s.indexOf("/"));
                    String nament = s.substring(s.indexOf("/") + 1);

                    dos.print(i+"\t");
                    dos.print(form+"\t"); //form
                    dos.print(form.toLowerCase()+"\t"); //lemma
                    dos.print(nament+"\t");
                    dos.println();
                }
            }
            reader.close();
            dos.close();
            fos.close();
        } catch (IOException e) {
            System.out.println("Error Printing or Reading Files");
        }
    }
}

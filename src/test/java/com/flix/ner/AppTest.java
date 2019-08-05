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

        // "wseg", "pos", "ner", and "parse" refer to as word segmentation, POS tagging, NER and dependency parsing, respectively.
        String[] annotators = {"wseg", "ner"};
        VnCoreNLP pipeline = new VnCoreNLP(annotators);

        String str = "Màn hình ss A 8 là bao nhieu vay";

        Annotation annotation = new Annotation(str);
        pipeline.annotate(annotation);

        System.out.println(annotation.toString());
        // 1    Ông                 Nc  O       4   sub
        // 2    Nguyễn_Khắc_Chúc    Np  B-PER   1   nmod
        // 3    đang                R   O       4   adv
        // 4    làm_việc            V   O       0   root
        // ...

        //Write to file
        PrintStream outputPrinter = new PrintStream("output.txt");
        pipeline.printToFile(annotation, outputPrinter);

        // You can also get a single sentence to analyze individually
        Sentence firstSentence = annotation.getSentences().get(0);
        System.out.println(firstSentence.toString());
    }
}

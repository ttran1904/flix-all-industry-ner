package com.flix.ner;

import java.io.*;
import java.util.*;
import java.nio.charset.StandardCharsets;

import opennlp.tools.namefind.*;
import opennlp.tools.util.*;


/**
 * Building Named Entity Recognition for flix all industries purposes. Including cars, mobile,
 * celebrity, food & drinks, etc.
 *
 */
public class App 
{
    public static void main(String[] args) {
        // Read train data
        InputStreamFactory in = null;
        try {
            in = new MarkableFileInputStreamFactory(new File("data/ner_new_0_edit.txt"));
        } catch (FileNotFoundException e2) { e2.printStackTrace(); }

        ObjectStream sampleStream = null;
        try {
            sampleStream = new NameSampleDataStream(new PlainTextByLineStream(in, StandardCharsets.UTF_8));
        } catch (IOException e1) { e1.printStackTrace(); }

        // Setting train parameters
        System.out.println(">> Training parameters");
        TrainingParameters params = new TrainingParameters();
        params.put(TrainingParameters.ITERATIONS_PARAM, 70);
        params.put(TrainingParameters.CUTOFF_PARAM, 1);

        // Train model
        System.out.println(">> Training model");
        TokenNameFinderModel nameFinderModel = null;
        try {
            nameFinderModel = NameFinderME.train("en", null, sampleStream,
                    params, TokenNameFinderFactory.create(null, null, Collections.<String, Object>emptyMap(), new BioCodec()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // saving the model to "ner-custom-model.bin" file
        System.out.println(">> Saving model to bin");
        try {
            File output = new File("bin/ner-custom-model.bin");
            FileOutputStream outputStream = new FileOutputStream(output);
            nameFinderModel.serialize(outputStream);

        } catch (FileNotFoundException e) { e.printStackTrace(); } catch (IOException e) { e.printStackTrace(); }



        // testing the model and printing the types it found in the input sentence
        System.out.println(">> Starting namefinder");
        TokenNameFinder nameFinder = new NameFinderME(nameFinderModel);

        String[] testSentence = {"Thông_thường",  "phí" ,"thay", "chính_hãng", "cho", "iPhone", "hết",  "bảo_hành" };

        System.out.println("Finding types in the test sentence..");
        Span[] names = nameFinder.find(testSentence);
        for(Span name:names){
            String personName="";
            for(int i=name.getStart();i<name.getEnd();i++){
                personName+=testSentence[i]+" ";
            }
            System.out.println(name.getType()+" : "+personName+"\t [probability="+name.getProb()+"]");
        }
    }
}

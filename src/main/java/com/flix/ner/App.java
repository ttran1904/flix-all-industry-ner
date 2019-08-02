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
            in = new MarkableFileInputStreamFactory(new File("data/AnnotatedSentences.txt"));
        } catch (FileNotFoundException e2) { e2.printStackTrace(); }

        ObjectStream sampleStream = null;
        try {
            sampleStream = new NameSampleDataStream(
                    new PlainTextByLineStream(in, StandardCharsets.UTF_8));
        } catch (IOException e1) { e1.printStackTrace(); }

        // Setting train parameters
        TrainingParameters params = new TrainingParameters();
        params.put(TrainingParameters.ITERATIONS_PARAM, 70);
        params.put(TrainingParameters.CUTOFF_PARAM, 1);

        // Train model
        TokenNameFinderModel nameFinderModel = null;
        try {
            nameFinderModel = NameFinderME.train("en", null, sampleStream,
                    params, TokenNameFinderFactory.create(null, null, Collections.<String, Object>emptyMap(), new BioCodec()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // saving the model to "ner-custom-model.bin" file
        try {
            File output = new File("ner-custom-model.bin");
            FileOutputStream outputStream = new FileOutputStream(output);
            nameFinderModel.serialize(outputStream);

        } catch (FileNotFoundException e) { e.printStackTrace(); } catch (IOException e) { e.printStackTrace(); }

        // testing the model and printing the types it found in the input sentence
        TokenNameFinder nameFinder = new NameFinderME(nameFinderModel);

        String[] testSentence ={"Alisa","Fernandes","is","a","tourist","from","Spain"};

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

//    public static void main (String[] args) throws IOException{
//        //Loading the NER - Person model
//        //assert new File("./resources/Menu_de_itens.txt").canRead();
//        InputStream inputStream = new FileInputStream("./data/AnnotatedSentences.txt");
//        TokenNameFinderModel model = new TokenNameFinderModel(inputStream);
//
//        //Instantiating the NameFinder class
//        NameFinderME nameFinder = new NameFinderME(model);
//
//        //Getting the sentence in the form of String array
//        String [] sentence = new String[]{
//                "Mike",
//                "and",
//                "Smith",
//                "are",
//                "good",
//                "friends"
//        };
//
//        //Finding the names in the sentence
//        Span nameSpans[] = nameFinder.find(sentence);
//
//        //Printing the spans of the names in the sentence
//        for(Span s: nameSpans)
//            System.out.println(s.toString());
//    }

}

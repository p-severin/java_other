import sun.awt.SunHints;
import sun.reflect.generics.tree.Tree;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


/**
 * Created by patry on 20.10.2017.
 */
public class App {



    public static void main(String[] args) throws IOException {

        Integer thresh = 5;
        Integer k = 10;

        BufferedReader reader = new BufferedReader(new FileReader("filename.txt"));
        try{
            String line;
            StringBuilder sb = new StringBuilder();



            while((line = reader.readLine()) != null){
                sb.append(line);
                sb.append(" ");

            }
            String wholeText = sb.toString();
            String[] words = wholeText.split(" ");
            List<String> wordsList = Arrays.asList(words);

            long startTime = System.nanoTime();

            Collections.sort(wordsList);

//            Set<String> uniqueWords = new HashSet<String>();
//            uniqueWords.addAll(wordsList);
//            System.out.println(uniqueWords);



//            for(String word : wordsList){
//                if(wordDictionary.containsKey(word)) {
//                    wordDictionary.replace(word, wordDictionary.get(word) + 1);
//                } else{
//                    wordDictionary.put(word, 1);
//                }
//            }
//
//
//
//            sorted_map.putAll(wordDictionary);
//
//            Iterator<Map.Entry<String, Integer>> iterator = sorted_map.entrySet().iterator();
//            int count = 1;
//
//            while(iterator.hasNext() != false){
//                Map.Entry<String, Integer> next = iterator.next();
//                if (next.getValue() >= thresh){
//                    System.out.println("No. " + count + ": " + next);
//                }
//                count++;
//                if (count > k){
//                    break;
//                }
//            }
//
//            long endTime = System.nanoTime();
//            System.out.println(endTime - startTime);
//
        } finally {
            reader.close();
        }


    }
}




import java.io.*;
import java.util.*;

public class SpellCheck { 
    BufferedReader dictionary;
    public static void main(String[] args){

        long start = System.nanoTime();
        double[] results = matchWords(buildDictionary(args[0]), args[1]);
        long stop = System.nanoTime();
        
        
        System.out.println("Time for search: "+ ((double)(stop-start)/1000000000.0)+" seconds");
        System.out.println("");
        
        System.out.println("Words Found:                     "+ (int)results[0]);
        System.out.println("Words Not Found:                 "+ (long)results[2]);
        System.out.println("");
        System.out.println("Comparisons - Found :            "+ (int)results[1]);
        System.out.println("Comparisons - Not Found:         "+ (long)results[3]);
        System.out.println("");
        System.out.println("Average Comparisons (Found):     "+ (float)(results[2]/results[0]));
        System.out.println("Average Comparisons (Not Found): "+ (float)(results[3]/results[1]));
    }
    
    private static double[] matchWords(MyLinkedList[] list, String file){
        String thisError = "";
        double wordsFound = 0, wordsNotFound = 0, compsFound = 0, compsNotFound = 0;
        double[] values = new double[5];

        try{
            File fFile = new File(file);
            try (Scanner input = new Scanner(fFile)) {
                while(input.hasNext()) {
                    String word = input.next().replaceAll("[^a-zA-Z']", "").toLowerCase().trim();      
                    if(word.length()>0 && ((int)word.charAt(0) == 39 || (int)word.charAt(word.length()-1) == 39))
                        word = word.replaceAll("'", "");
                    
                    if(word.length() > 0){
                        //System.out.println(word+"  First Char:"+word.charAt(0)+" atVal "+(int)word.charAt(0)+"  Last Char:"+word.charAt(word.length()-1)+" atVal "+(int)word.charAt(word.length()-1));
                        int pos = list[(int)word.charAt(0) -97].indexOf(word);
                        if(pos != -1){ 
                            wordsFound++; 
                            compsFound += (double)(pos+1);
                        }
                        else { 
                            wordsNotFound++; 
                            compsNotFound += list[(int)word.toLowerCase().charAt(0) -97].size();
                        }
                    }
                }           
            }
        }    
        catch(IOException e){
            System.out.println(thisError);
            return null;
        }
        values[0] = wordsFound;
        values[1] = wordsNotFound;
        values[2] = compsFound;
        values[3] = compsNotFound;
        return values;
    }
        private static MyLinkedList[] buildDictionary(String file) {
        MyLinkedList[] ListArray = new MyLinkedList[26];
        MyLinkedList<String> list = new MyLinkedList<>();
        String thisError = null;
        String line;
        
        for(int i=0;i<26;i++){  // Create an array of 26 MyLinkedList objects
            ListArray[i] = new MyLinkedList();
            ListArray[i].add(list);
        }        
        try{
            thisError = ("Error getting file:"+file);  // update error message before trying 
            BufferedReader dictionary = new BufferedReader(new FileReader(file));
            
            // Populate dictionary
            // ********************
            thisError = ("Error populating dictionary!"); // update error message before trying
            while ((line = dictionary.readLine()) != null) {
                int index = ((int)line.toLowerCase().charAt(0) -97);
                ListArray[index].add(0, line.toLowerCase());
            }
        }    
        catch(IOException e){
            System.out.println(thisError);
            return null;
        }
        return ListArray;
    }
}
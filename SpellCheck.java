import java.io.*;
import java.util.regex.Pattern;

/*
 *
 *
*/

public class SpellCheck { 
    public static void main(String[] args){
        
        long start = System.nanoTime();
        double[] results = matchWords(getWords(args[1]),buildDictionary(args[0]));
        long stop = System.nanoTime();
        
        System.out.println("Time for search: "+ ((double)(stop-start)/1000000000.0)+" seconds");
        System.out.println("");
        
        
        System.out.println("results[0] (words found):     "+ (int)results[0]);
        System.out.println("results[2] (words not found): "+ (long)results[2]);
        System.out.println("results[1] (comparisons - words found) :    "+ (int)results[1]);
        System.out.println("results[3] (comparisons - words not found:  "+ (long)results[3]);
        
        System.out.println("Average Comparisons (Found):     "+ (results[2]/results[0]));
        System.out.println("Average Comparisons (Not Found): "+ (results[3]/results[1]));
    }

    /* 
     * @param file - the file with all the dictionary words 
     * to build the dictionary from
     * @return array of MyLinkedLists.  
    */
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
    private static BufferedReader getWords(String file){
        String thisError = null;
        BufferedReader source;
        
        try{
            thisError = ("Error getting file:"+file);  // update error message before trying 
            source = new BufferedReader(new FileReader(file));
        }
        catch(IOException e){
            System.out.println(thisError);
            return null;
        }
        return source;
    }
    private static double[] matchWords(BufferedReader lines,MyLinkedList[] list){
        String thisError = null, line;
        double wordsFound = 0, wordsNotFound = 0, compsFound = 0, compsNotFound = 0;
        double[] values = new double[5];
        try{
            while ((line = lines.readLine()) != null) {
                System.out.println("Line prior: "+line);
                line = line.replaceAll("[^a-zA-Z]", " ");
                System.out.println("Line post:  "+line);
                System.out.println(" ");
                
                if (!(line.trim().isEmpty())){
                    String[] words = line.split("\\s+");
                    if(line.length()>0){
                        for (int i = 0; i < words.length; i++) {      
                            //System.out.println(words[i]);
                            String word = words[i].toLowerCase().trim();                                
                            if(word.length() > 0){
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
}
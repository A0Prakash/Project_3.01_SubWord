import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;


/**
 * public class SubWordFinder
 * @author 26prakash
 * @version 2.9.2023
 * EXTRA: OUTPUTS MOST FREQUENTLY OCCURING PREFIX SUBWORD
 * on line 68
 */
public class SubWordFinder implements WordFinder {
    private ArrayList<ArrayList<String>> databaseball = new ArrayList<>();
    private String alpha = "abcdefghijklmnopqrstuvwxyz";

    /**
     * simple constructor for SubWordFinder
     * creates buckets for arraylist
     * calls populate dictionary to populate the buckets
     */
    public SubWordFinder() {
        databaseball = new ArrayList<>();
        for(int i = 0; i < 26; i++) {
            databaseball.add(new ArrayList<String>());
        }
        populateDictionary();
    }

    private void parseWord(String word) {
        String front = "", back = "";
        for(int i = 2; i < word.length()-2; i++) {
            front = word.substring(0, i);
            back = word.substring(i);
        }
    }

    /**
     * Populates the dictionary from the text file contents
     * The dictionary object should contain 26 buckets, each
     * bucket filled with an ArrayList<String>
     * The String objects in the buckets are sorted A-Z because
     * of the nature of the text file words.txt
     */
    public void populateDictionary() {
        try{
            Scanner in = new Scanner(new File("new_scrabble.txt"));
            while(in.hasNext()) {
                String word = in.nextLine();
                databaseball.get(alpha.indexOf(word.substring(0,1))).add(word);
            }
            in.close();
            for(int i = 0; i < databaseball.size(); i++) {
                Collections.sort(databaseball.get(i));
            }

        }
        catch(Exception e) {
            System.out.println(e);
        }
    }

    /**
     * getMostFrequentWord method of the Arra
     * @param subwords an arraylist containing subwords
     * @return a string containing the most used prefix
     */
    public String getMostFrequentWord(ArrayList<SubWord> subwords) {
        ArrayList<String> prefixes= new ArrayList<>();
        ArrayList<Integer> values = new ArrayList<>();
        for(int i = 0; i < subwords.size(); i++) {
            //prefixes.contains(subwords.get(i).getPrefix())
            if(binarySearch(prefixes, subwords.get(i).getPrefix()) >= 0) {
                if(prefixes.indexOf(subwords.get(i).getPrefix()) >= 1)
                    values.set(prefixes.indexOf(subwords.get(i).getPrefix()), values.get(prefixes.indexOf(subwords.get(i).getPrefix()))+1);

            } else {
                prefixes.add(subwords.get(i).getPrefix());
                values.add(1);
            }
        }
        return prefixes.get(values.indexOf(Collections.max(values)));
    }


    /**
     * Retrieve all SubWord objects from the dictionary.
     * A SubWord is defined as a word that can be split into two
     * words that are also found in the dictionary.  The words
     * MUST be split evenly, e.g. no unused characters.
     * For example, "baseball" is a SubWord because it contains
     * "base" and "ball" (no unused characters)
     * To do this, you must look through every word in the dictionary
     * to see if it is a SubWord object
     * @return An ArrayList containing the SubWord objects
     * pulled from the file words.txt
     */
    public ArrayList<SubWord> getSubWords() {
        ArrayList<SubWord> ret = new ArrayList<>();
        for(int c = 0; c < databaseball.size(); c++) {
            for(int a = 0; a < databaseball.get(c).size(); a++) {
                for(int i = 2; i < databaseball.get(c).get(a).length()-1; i++) {
                    String word1 = databaseball.get(c).get(a).substring(0,i);
                    String word2 = databaseball.get(c).get(a).substring(i, databaseball.get(c).get(a).length());
                    if(inDictionary(word1)&&inDictionary(word2)) {
                        ret.add(new SubWord(databaseball.get(c).get(a), word1, word2));
                    }
                }
            }
        }
        return ret;
    }

    // never used in other methods
    private boolean isSubWord(String word) {
        if(!inDictionary(word)) {
            return false;
        }
        for(int i = 2; i < word.length()-1; i++) {
            String word1 = word.substring(0,i);
            //System.out.print(word1 + " ");
            String word2 = word.substring(i,word.length());
            //System.out.println(word2);

            if(inDictionary(word1)&&inDictionary(word2)) {
                return true;
            }
        }
        return false;
    }



    //hehehe dont have to javadoc
    //but i just did javadoc by saying that i don't have to
    //thinky face emoji
    private int binarySearch(ArrayList<String> arr, String word){
        word = word.toLowerCase();
        int left = 0, right = arr.size() - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (arr.get(mid).equals(word)) {
                return mid;
            } else if(arr.get(mid).compareTo(word) < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1;
    }


    /**
     * Look through the entire dictionary object to see if
     * word exists in dictionary
     * @param word The item to be searched for in dictionary
     * @return true if word is in dictionary, false otherwise
     * NOTE: EFFICIENCY O(log N) vs O(N) IS A BIG DEAL HERE!!!
     * You MAY NOT use Collections.binarySearch() here; you must use
     * YOUR OWN DEFINITION of a binary search in order to receive
     * the credit as specified on the grading rubric.
     */
    public boolean inDictionary(String word) {
        return binarySearch(databaseball.get(alpha.indexOf(word.substring(0, 1))), word) >= 0;
    }

    public static void main(String[] args) {
        SubWordFinder goober = new SubWordFinder();
        ArrayList<SubWord> subwords = goober.getSubWords();
        System.out.println("* List of SubWord objects in dictionary *");
        for(SubWord temp: subwords) {
            System.out.println(temp);
        }
        System.out.println(subwords.size() + " total SubWords");
        System.out.println("Most frequently used prefix: " + goober.getMostFrequentWord(subwords));
    }
}



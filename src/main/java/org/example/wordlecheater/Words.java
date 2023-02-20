package org.example.wordlecheater;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

// A class representing all the words
public class Words {
    // The list in all its glory
    public List<String> allWords;

    // A constructor to recreate this class and shuffle it
    public Words(Words words) {
        allWords = new ArrayList<>(words.allWords);

        Collections.shuffle(allWords, new Random());
    }

    // The constructor to get all the words
    public Words() {
        // Initialise the list
        allWords = new ArrayList<>();

        try {
            // Download a txt file from online with a huge list of words we can work with
            URL url = new URL("https://raw.githubusercontent.com/dwyl/english-words/master/words_alpha.txt");
            BufferedReader read = new BufferedReader(new InputStreamReader(url.openStream()));

            // Then take each line of that file and put it into our list
            String i;
            while ((i = read.readLine()) != null) {
                // My lil brother who's not a coder at all had this idea.
                // Why store all the words when we will be only using the 5 letter ones?
                // What a legend. Thanks :)
                if (i.length() == 5) allWords.add(i);
            }
            // Close the file reader
            read.close();

            // Shuffle dem apples
            Collections.shuffle(allWords, new Random());

        // If there was an error make it nice to read, so it hurts my feelings slightly less :')
        } catch (IOException e) {
            System.out.println("====================================");
            System.out.println("= There was an error getting words =");
            System.out.println("====================================");
            System.out.println(e.getMessage());
            System.out.println("====================================");
        }
    }
}

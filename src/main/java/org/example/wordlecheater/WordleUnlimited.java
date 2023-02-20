package org.example.wordlecheater;

// Wordle Unlimited is the exact same as the New York Times one, and therefore we only need to change the URL.
// Since we are subclassing we get to keep all the previously written functionality :)
public class WordleUnlimited extends NyTimes {
    WordleUnlimited() {
        super();
        url = "https://wordleunlimited.org/";
    }
}

package org.example.wordlecheater;

import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// This is an implementation of the wordle class specifically written to be compatible with the New York Times wordle website
public class NyTimes extends Wordle {
    NyTimes() {
        url = "https://www.nytimes.com/games/wordle/index.html";
    }

    // By overriding the loadBoard method we can extend its functionality and make it perform more specific tasks relating the implementations needs.
    @Override
    protected void loadBoard(String ...args) {
        // Reset fields to be clean and ready for usage in this fresh game
        results = new Letter[6][5];
        currentRow = 0;

        // Call the parent classes implementation
        super.loadBoard(args);

        // Grab the browser elements for usage later on

        WebElement themeManager = driver.findElement(By.tagName("game-app"))
                .getShadowRoot()
                .findElement(By.cssSelector("game-theme-manager"));

        themeManager
                .findElement(By.cssSelector("game-modal"))
                .getShadowRoot()
                .findElement(By.cssSelector("game-icon"))
                .click();

        board = themeManager.findElement(By.cssSelector("#board"));

        rows = board.findElements(By.cssSelector("game-row"));
    }

    // Now that we are overriding the sendWord method we provide its intended functionality
    @Override
    public boolean sendWord(@NotNull String word) throws Exception {
        // By calling the parent implementation we are able to share the common code without writing it twice between different implementations of the Wordle class
        if (super.sendWord(word)) {
            // Grab the main body element
            WebElement body = driver.findElement(By.tagName("body"));

            // Send the commands to the browser to type in the letters and submit
            body.sendKeys(word);
            body.sendKeys(Keys.ENTER);

            // Put this thread to sleep for 2000 ms in order to allow the animations to complete before continuing onward
            Thread.sleep(2000);

            // If the row was found and the evaluation is not null
            if (rows.get(currentRow).getShadowRoot().findElement(By.cssSelector("game-tile")).getAttribute("evaluation") != null) {
                // Grab a list of the tiles
                List<WebElement> tiles = rows.get(currentRow).getShadowRoot().findElements(By.cssSelector("game-tile"));

                // Loop over the tiles
                for (int i = 0; i < tiles.size(); i++) {
                    WebElement tile = tiles.get(i);

                    // Create a new letter class in our representation of the game to be used later on
                    (results[currentRow])[i] = new Letter(tile.getAttribute("letter").charAt(0), tile.getAttribute("evaluation"), i);
                }

                // Increment the current row
                currentRow++;

                // Return that sending the word was successful
                return true;
            }
        }

        // If something failed, and we didn't hit the return above then return that sending the word failed
        return false;
    }

    // Clearing the board by using backspace (such as when an invalid word is detected)
    @Override
    public void clearBoard() {
        WebElement body = driver.findElement(By.tagName("body"));

        // Hit backspace 5 times
        for (int i = 0; i < 5; i++) {
            body.sendKeys(Keys.BACK_SPACE);
        }
    }

    // Check if we won
    @Override
    public boolean checkWin() {
        // For each row in our results
        for (Letter[] row : results) {
            // Store how many letters were correct
            int correctLetters = 0;

            for (Letter letter : row) {
                if (letter == null) break;

                // If letter was correct increment the correctLetters counter
                if (letter.evaluation == Evaluation.Correct) correctLetters++;
            }

            // If all letters were correct we won. Yay! :)
            if (correctLetters == row.length) return true;
        }

        // Otherwise we didn't win
        return false;
    }
}

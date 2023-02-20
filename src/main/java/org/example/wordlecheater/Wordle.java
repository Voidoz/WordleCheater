package org.example.wordlecheater;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Arrays;
import java.util.List;

// A class to be used for each wordle site.
// This class contains all the common information that each implementation will need such as the url to point to, and abstract representations of the game mechanics.
public abstract class Wordle {
    // Used to interact with the browser such as by clicking buttons
    protected WebDriver driver;

    // Used to interact with scripting side of browser
    protected JavascriptExecutor js;

    // The element containing the rows
    protected WebElement board;

    // The rows containing the letter squares
    protected List<WebElement> rows;

    // Each letter with its results
    protected Letter[][] results;

    // The row we are currently working on
    protected int currentRow = 0;

    // The url of the game itself
    protected String url;

    // Here we set up the browser and prepare to start playing the game
    protected void loadBoard(String ...args) {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();

        options.addArguments(Arrays.asList(args));

        driver = new ChromeDriver(options);

        js = (JavascriptExecutor) driver;

        driver.get(url);
    };

    // This is a super class implementation and doesn't do what the function is intended to do.
    // All this does is enforce the common rule that a word must be 5 letters.
    // You can see how subclasses utilise this by looking at how they override it.
    boolean sendWord(@NotNull String word) throws Exception {
        if (word.length() != 5) {
            System.out.println("Error: Tried to input a word that isn't 5 letters");

            return false;
        }

        return true;
    }

    // Abstract methods have no implementation but rather expect subclasses to provide one instead

    public abstract void clearBoard();

    public abstract boolean checkWin();
}

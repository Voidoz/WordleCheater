package org.example.wordlecheater;

// The worker class manages each instance of the bot.
// Here we are able to manage multi-threading, so we can properly use the CPUs capabilities to run multiple bots at once.
// It also handles the clever logic to make sure each guess is the best it can be.
public class Worker {
    // The backend instance
    private final Backend backend;

    // The bot instance
    private final Wordle wordle;

    // The name given to the worker
    private final String name;

    // The thread we are running on
    private Thread workerThread;

    protected WorkerStatus status;

    // Will we keep going after completion?
    public boolean repeat;

    Worker(Backend backend, Wordle wordle, String name, boolean repeat) {
        this.status = WorkerStatus.Waiting;
        this.backend = backend;
        this.wordle = wordle;
        this.name = name;
        this.repeat = repeat;
    }

    public void startWorker() {
        // If the worker is already running, stop it
        if (workerThread != null && !workerThread.isInterrupted()) stopWorker();

        // Start the worker in a new thread
        workerThread = new Thread(() -> {
            status = WorkerStatus.Running;

            // Tell backend that we've changed
            backend.updateWorkers();

            // And here we go!
            do {
                // Load the board
                wordle.loadBoard();

                // Shuffle the words
                Words newWords = new Words(backend.getWords());

                // Run me 5 times (the first word has already been entered)
                for (int i = 0; i < 6;) {
                    // Run through every 5-letter word
                    for (int j = 0; j < newWords.allWords.size(); j++) {
                        // Store word
                        String word = newWords.allWords.get(j);

                        // Skip the word if we're on the first guess and there are repeat letters
                        if (i == 0 && !Helper.isIsogram(word)) continue;

                        // Word is valid until we detect it isn't
                        boolean validWord = true;

                        // Check every row of the results we have currently collected
                        // Also label this outer loop, so we can break it from a couple layers down
                        row_loop:
                        for (Letter[] row : wordle.results) {
                            // Go through each letter of the row
                            for (Letter letter : row) {
                                // If we have a letter to look at
                                if (letter != null) {
                                    // Grab the letter evaluation
                                    switch (letter.evaluation) {
                                        // If word contains an absent letter, discard it.
                                        case Absent:
                                            if (word.contains(String.valueOf(letter.letter))) {
                                                validWord = false;
                                                newWords.allWords.remove(word);
                                                break row_loop;
                                            }
                                            break;

                                        // If we know the letter exists but is in the wrong spot then only keep it if the letter we are testing against is not in that spot.
                                        case Present:
                                            if (!word.contains(String.valueOf(letter.letter)) || word.charAt(letter.position) == letter.letter) {
                                                validWord = false;
                                                newWords.allWords.remove(word);
                                                break row_loop;
                                            }
                                            break;

                                        // If the letter is correct only keep it if it is in that same spot
                                        // There is an issue with this, and it's that if the letter is in the word twice then this will prevent a correct guess.
                                        // I decided to do it this way because otherwise it comes up with every equally possible incorrect answer.
                                        case Correct:
                                            if (!word.contains(String.valueOf(letter.letter)) || word.charAt(letter.position) != letter.letter) {
                                                validWord = false;
                                                newWords.allWords.remove(word);
                                                break row_loop;
                                            }
                                            break;
                                    }
                                }
                            }
                        }

                        // If the word is indeed valid to guess with
                        if (validWord) {
                            try {
                                // Send it out and see how we went!
                                if (wordle.sendWord(word)) {
                                    if (wordle.checkWin()) {
                                        i = 6;
                                    }
                                    else i++;
                                } else throw new Exception("Word not accepted");

                            // And if we fail remove the word from the list and try again
                            } catch (Exception e) {
                                newWords.allWords.remove(word);
                                wordle.clearBoard();
                            }

                            break;
                        }
                    }

                    // If we ran out of words tap out
                    if (newWords.allWords.size() == 0) break;
                }

                // If we repeat close the window to start up again
                if (repeat) wordle.driver.close();

            // And if are to repeat we keep going
            } while (repeat);
        });

        // Start the thread
        workerThread.start();
    }

    // Clean up the worker
    public void stopWorker() {
        // Close the web driver
        wordle.driver.close();
        // Stop the thread
        workerThread.interrupt();
        // Set the status accordingly
        status = WorkerStatus.Stopped;
        // Tell the backend we've changed and should be updated
        backend.updateWorkers();
    }

    // A getter for the name, so we don't accidentally change it.
    // It should be set at the creation of the worker and never any time later on.
    public String getName() {
        return name;
    }

    // A getter for the status to protect it from being changed by outside sources.
    // The status should only ever be changed from within this class.
    // This method also makes the status human-readable as an enum is technically just an alias for a number.
    public String getStatus() {
        switch (status) {
            case Waiting:
                return "Waiting";
            case Running:
                return "Running";
            case Complete:
                return "Complete";
            case Stopped:
                return "Stopped";
            case Error:
                return "Error";
        }

        return "Unknown";
    }
}

package org.example.wordlecheater;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;

import java.util.ArrayList;
import java.util.List;

// The Backend class is the heart of the entire application
public class Backend {
    // All the words that the application will use when guessing
    private Words words;

    // A worker is a running bot
    private final List<Worker> workers = new ArrayList<>();

    // This will be confusing if you don't understand how rxjava works.
    // In essence is provides a way for the app to keep live data of the list of each worker as well as their respective states.
    private final PublishSubject<List<Worker>> workerSubject = PublishSubject.create();

    // A getter function to see the live data
    public Observable<List<Worker>> getWorker() {
        return workerSubject.hide();
    }

    // Create a worker
    public void addWorker(Worker newWorker) {
        // We add the worker to the list
        workers.add(newWorker);

        // And tell the worker subject that the data has changed and that it's time to update any relevant usages of the worker list
        updateWorkers();
    }

    // Tell any places that are looking at the workers list that the data has updated
    public void updateWorkers() {
        workerSubject.onNext(workers);
    }

    // A getter for the words.
    // To improve startup performance we check if words already exists and if not create an instance.
    // This means we only initialise the words class from the first usage of it.
    public Words getWords() {
        if (words == null) words = new Words();
        return words;
    }
}

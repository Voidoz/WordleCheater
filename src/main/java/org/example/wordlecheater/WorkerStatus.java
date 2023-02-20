package org.example.wordlecheater;

// This is how we know what the worker is actually doing at any given moment
public enum WorkerStatus {
    Waiting,
    Running,
    Complete,
    Stopped,
    Error
}

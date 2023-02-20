package org.example.wordlecheater;

// A class representation of a single letter and it's state
public class Letter {
    char letter;

    Evaluation evaluation;

    int position;

    Letter(char letter, String evaluation, int position) {
        this.letter = letter;
        this.position = position;

        switch (evaluation) {
            case "present":
                this.evaluation = Evaluation.Present;
                break;
            case "correct":
                this.evaluation = Evaluation.Correct;
                break;
            default:
                this.evaluation = Evaluation.Absent;
                break;
        }
    }
}

package org.example.wordlecheater;

import javafx.util.Callback;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.function.Function;

// A collection of static functions that are useful to have around
public class Helper {
    public static boolean isIsogram(String string) {
        if(string == null)
            return false;

        // Converting all letters of a word to lowercase
        string = string.toLowerCase();

        // Convert String to char[]
        char[] arr = string.toCharArray();

        //Iterating the char array
        for (char ch : arr) {
            /* if positions returned by the indexOf() and lastIndexOf() methods are not same
               then the word is NOT Isogram*/
            if(string.indexOf(ch) != string.lastIndexOf(ch)) {
                return false;
            }
        }
        return true;
    }

    public static Callback<Class<?>, Object> getControllerFactory(Backend backend) {
        return (param) -> {
            try {
                return param.getDeclaredConstructor(backend.getClass()).newInstance(backend);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
            return null;
        };
    }
}

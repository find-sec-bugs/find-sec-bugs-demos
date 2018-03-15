package com.h3xstream.sandbox;

import java.util.Random;

public class SpecialCase1 {


    class CanYouSeeMeInner {
        public String generateToken(String[] args){
            return ""+new Random().nextLong(); //FINDME: Insecure random generator
        }
    }
}

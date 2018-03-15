package com.h3xstream.sandbox;

import java.util.Random;

public class SpecialCase3 {


}

class CanYouSeeMeOuter2 {

    class FollowTheWhiteRabbit {
        public String generateToken(String[] args){
            return ""+new Random().nextLong(); //FINDME: Insecure random generator
        }
    }
}
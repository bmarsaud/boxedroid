package fr.bmarsaud.boxedroid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Boxedroid {
    private Logger logger = LoggerFactory.getLogger(Boxedroid.class);

    public Boxedroid() {
        logger.info("Hello World boxedroid!");
    }

    public static void main(String[] args) {
        new Boxedroid();
    }
}

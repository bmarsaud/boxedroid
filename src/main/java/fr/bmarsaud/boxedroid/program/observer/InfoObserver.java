package fr.bmarsaud.boxedroid.program.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Observable;
import java.util.Observer;

public class InfoObserver implements Observer {
    private Logger logger = LoggerFactory.getLogger(InfoObserver.class);

    @Override
    public void update(Observable observable, Object o) {
        String line = (String) o;
        logger.info(line);
    }
}

package main.dream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogCreator {
    private static Logger LOG;

    public static Logger getLogger() {
        if (LOG == null) {
           LOG = LogManager.getLogger();
        }
        return LOG;
    }
}

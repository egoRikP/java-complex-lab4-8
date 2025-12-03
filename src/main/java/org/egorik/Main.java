package org.egorik;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.egorik.menu.MainMenu;

public class Main {

    static private final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {

        logger.info("Start Chef-Salad program");
        AppContext appContext = null;

        try {
            System.out.println("Chef-Salad program");
            appContext = new AppContext();
            new MainMenu(appContext).menuCycle();
        } catch (Exception exception) {
            logger.error("Fatal error: {}", exception.getMessage(), exception);
            System.out.println("Fatal error!");
        } finally {
            if (appContext == null) {
                appContext.inputManager.getMyScanner().close();
                logger.debug("Close console scanner - System.In!");
            }
        }

        logger.info("End Chef-Salad program!");
    }
}
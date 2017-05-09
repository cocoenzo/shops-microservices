package com.gft.db.shops.app;

/**
 * Some constants used along the application.<br/>
 * 
 * Messages should be created in the "shopstext.properties" to be read as labels
 * for multi-language, however it is enough for this purpose.
 * 
 * @author Ignacio Elorriaga
 * 
 */
public interface Constants {
    /** Radius in Kilometers. */
    double RADIUS = 6372.8;

    String DEFAULT_COUNTRY = "GB";

    String PAGE_SHOP = "shop";
    String PAGE_INDEX = "index";
    String WEB_SHOP_DATA = "shop";
    String WEB_ERROR_MSG = "error";
    String WEB_MSG = "message";

    String ACTION_READ_MSG = "Shop read successfully";
    String ACTION_REMOVE_MSG = "Shop removed successfully";
    String ACTION_UPDATED_MSG = "Shop updated successfully";
    String ACTION_CREATED_MSG = "Shop created successfully";
    String ERROR_MSG = "There was an error in the process";
    String ACTION_NOT_FOUND = "Shop not found";

    String ACTION_INVALID_PARAMS = "Invalid input parameters. Street and Name are mandatories.";
}

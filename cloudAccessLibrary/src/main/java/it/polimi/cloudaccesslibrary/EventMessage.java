package it.polimi.cloudaccesslibrary;



/**
 * Created by saeed on 11/29/2017.
 */

public class EventMessage {
    String message;


    public EventMessage(String messageContent) {
        this.message = messageContent;

    }


    public String getMessage() {
        return message;
    }
}

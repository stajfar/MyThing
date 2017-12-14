package it.polimi.cloudaccesslibrary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by saeed on 11/29/2017.
 */

public class EventProducer {
    List<Component> listComponent=new ArrayList<>();
    public void registerConsumer(Component component){
        listComponent.add(component);
    }

    public void sendEventtoConsumenrs(EventMessage eventMessage){
        for(Component cmp : listComponent){
            cmp.receive(eventMessage);
        }
    }

}

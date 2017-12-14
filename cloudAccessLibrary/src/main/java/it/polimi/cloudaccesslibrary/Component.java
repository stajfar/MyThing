package it.polimi.cloudaccesslibrary;

import java.util.List;

/**
 * Created by saeed on 11/29/2017.
 */

public interface Component {
    void receive(EventMessage message);
    void send(EventMessage message);
    void addNextComponents(List<Component> listComponents);

}

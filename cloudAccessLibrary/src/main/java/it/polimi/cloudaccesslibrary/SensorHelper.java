package it.polimi.cloudaccesslibrary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by saeed on 12/5/2017.
 */

public class SensorHelper implements Component {
    List<Component> listNextComponents=new ArrayList<>();
    private float latstSensorValue;

    public void setLatestSensorValue(float latstSensorValue) {
        this.latstSensorValue = latstSensorValue;
    }

    public SensorHelper() {

    }



    @Override
    public void receive(EventMessage message) {
        send(new EventMessage(String.valueOf(latstSensorValue)));
    }

    @Override
    public void send(EventMessage message) {
        //this sends the message to next component/s
        for (Component cmp: listNextComponents) {
            cmp.receive(message);
        }
    }

    @Override
    public void addNextComponents(List<Component> listComponents) {
        this.listNextComponents = listComponents;
    }
}

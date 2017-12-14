package it.polimi.cloudaccesslibrary;

import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by saeed on 12/1/2017.
 */

public class TimerHelper implements Component{
    long runnableIntervalMillies;
    //EventProducer timerEventProducer=new EventProducer();
    List<Component> listNextComponents=new ArrayList<>();


    public TimerHelper(long triggerIntervalMilliSec) {
       this.runnableIntervalMillies=triggerIntervalMilliSec;
    }


    // Start the initial runnable task by posting through the handler
    public void startTimer(){
        handler.post(runnableCode);
    }


    Handler handler = new Handler();
    // Define the code block to be executed
    private Runnable runnableCode = new Runnable() {
        @Override
        public void run() {
            // Do something here on the main thread
            Log.i("Handlers", "Called on main thread");
            //produce Timer Event send to who has subscribed to this event
            //timerEventProducer.sendEventtoConsumenrs(new EventMessage(String.valueOf(System.currentTimeMillis())));
            send(new EventMessage(String.valueOf(System.currentTimeMillis())));
            // Repeat this the same runnable code block again another xxx seconds
            // 'this' is referencing the Runnable object
            handler.postDelayed(this, runnableIntervalMillies);

        }
    };




    //provides this event producer to who wants to register itself for timer events
   /* public EventProducer getEventProducer(){
        return  this.timerEventProducer;
    }
    */

    @Override
    public void receive(EventMessage message) {
        //this should not be implemented as this component is a trigger-typed and will not have any
        //component behind it, so there will be not any message to receive.
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

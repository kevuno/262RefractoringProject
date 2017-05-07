package bowling;

import java.util.ArrayList;

/**
 * A generic Event interface for all Observers to use.
 * The only method it will share for all Events is getting all the data of the Event
 */
public interface Event {

    /**
     * Collects the Event fields and returns them inside an arrayList
     */
    ArrayList<Object> getEventData();
}

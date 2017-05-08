package bowling;

/* bowling.ControlDeskEvent.java
 *
 *  Version:
 *  		$Id$
 * 
 *  Revisions:
 * 		$Log$
 * 
 */

/**
 * Class that represents control desk event
 */

import java.util.ArrayList;
import java.util.Vector;

public class ControlDeskEvent implements Event {

  /** A representation of the wait queue, containing party names */
  private Vector partyQueue;

  /**
   * Constructor for the bowling.ControlDeskEvent
   *
   * @param partyQueue  a Vector of Strings containing the names of the parties in the wait queue
   *
   */

  public ControlDeskEvent(Vector partyQueue) {
    this.partyQueue = partyQueue;
  }

  /**
   * Accessor for partyQueue
   *
   * @return a Vector of Strings representing the names of the parties in the wait queue
   *
   */

  public Vector getPartyQueue() {
    return partyQueue;
  }

  @Override
  public ArrayList<Object> getEventData() {
    ArrayList<Object> data = new ArrayList<>();
    data.add(partyQueue);
    return data;
  }
}

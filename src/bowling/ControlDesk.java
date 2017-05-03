package bowling;/* bowling.ControlDesk.java
 *
 *  Version:
 *  		$Id$
 * 
 *  Revisions:
 * 		$Log: bowling.ControlDesk.java,v $
 * 		Revision 1.13  2003/02/02 23:26:32  ???
 * 		bowling.ControlDesk now runs its own thread and polls for free lanes to assign queue members to
 * 		
 * 		Revision 1.12  2003/02/02 20:46:13  ???
 * 		Added " 's bowling.model.Party" to party names.
 * 		
 * 		Revision 1.11  2003/02/02 20:43:25  ???
 * 		misc cleanup
 * 		
 * 		Revision 1.10  2003/02/02 17:49:10  ???
 * 		Fixed problem in getPartyQueue that was returning the first element as every element.
 * 		
 * 		Revision 1.9  2003/02/02 17:39:48  ???
 * 		Added accessor for lanes.
 * 		
 * 		Revision 1.8  2003/02/02 16:53:59  ???
 * 		Updated comments to match javadoc format.
 * 		
 * 		Revision 1.7  2003/02/02 16:29:52  ???
 * 		Added bowling.ControlDeskEvent and bowling.ControlDeskObserver. Updated bowling.Queue to allow access to Vector so that contents could be viewed without destroying. Implemented observer model for most of bowling.ControlDesk.
 * 		
 * 		Revision 1.6  2003/02/02 06:09:39  ???
 * 		Updated many classes to support the bowling.view.ControlDeskView.
 * 		
 * 		Revision 1.5  2003/01/26 23:16:10  ???
 * 		Improved thread handeling in lane/controldesk
 * 		
 * 
 */

/**
 * Class that represents control desk
 */

import bowling.model.Bowler;
import bowling.model.Party;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

public class ControlDesk extends Thread {

  /** The collection of Lanes */
  private HashSet<Lane> lanes;

  /** The party wait queue */
  private Queue partyQueue;

  /** The number of lanes represented */
  private int numLanes;

  /** The collection of subscribers */
  private Vector<ControlDeskObserver> subscribers;

  /**
   * Constructor for the bowling.ControlDesk class
   *
   * @param numLanes  the number of lanes to be represented
   *
   */

  public ControlDesk(int numLanes) {
    this.numLanes = numLanes;
    lanes = new HashSet<>(numLanes);
    partyQueue = new Queue();

    subscribers = new Vector<>();

    for (int i = 0; i < numLanes; i++) {
      lanes.add(new Lane());
    }

    this.start();
  }

  /**
   * Main loop for bowling.ControlDesk's thread
   *
   */
  public void run() {
    while (true) {

      assignLane();

      try {
        sleep(250);
      } catch (Exception e) {
      }
    }
  }


  /**
   * Retrieves a matching bowling.model.Bowler from the bowler database.
   *
   * @param nickName  The NickName of the bowling.model.Bowler
   *
   * @return a bowling.model.Bowler object.
   *
   */

  private Bowler registerPatron(String nickName) {
    Bowler patron = null;

    try {
      // only one patron / nick.... no dupes, no checks

      patron = BowlerFile.getBowlerInfo(nickName);

    } catch (FileNotFoundException e) {
      System.err.println("Error..." + e);
    } catch (IOException e) {
      System.err.println("Error..." + e);
    }

    return patron;
  }

  /**
   * Iterate through the available lanes and assign the paties in the wait queue if lanes are available.
   *
   */

  public void assignLane() {
    Iterator it = lanes.iterator();

    while (it.hasNext() && partyQueue.hasMoreElements()) {
      Lane curLane = (Lane) it.next();

      if (!curLane.isPartyAssigned()) {
        System.out.println("ok... assigning this party");
        curLane.assignParty(((Party) partyQueue.next()));
      }
    }
    publish(new ControlDeskEvent(getPartyQueue()));
  }

  /**
   */

  public void viewScores(Lane ln) {
    // TODO: attach a LaneScoreView object to that lane
  }

  /**
   * Creates a party from a Vector of nickNames and adds them to the wait queue.
   *
   * @param partyNicks  A Vector of NickNames
   *
   */

  public void addPartyQueue(Vector partyNicks) {
    Vector<Bowler> partyBowlers = new Vector<>();
    for (Object partyNick : partyNicks) {
      Bowler newBowler = registerPatron(((String) partyNick));
      partyBowlers.add(newBowler);
    }
    Party newParty = new Party(partyBowlers);
    partyQueue.add(newParty);
    publish(new ControlDeskEvent(getPartyQueue()));
  }

  /**
   * Returns a Vector of party names to be displayed in the GUI representation of the wait queue.
   *
   * @return a Vector of Strings
   *
   */

  public Vector getPartyQueue() {
    Vector<String> displayPartyQueue = new Vector<>();
    for (int i = 0; i < (partyQueue.asVector()).size(); i++) {
      String nextParty =
          ((Bowler) (((Party) partyQueue.asVector().get(i)).getMembers())
              .get(0))
              .getNickName() + "'s bowling.model.Party";
      displayPartyQueue.addElement(nextParty);
    }
    return displayPartyQueue;
  }

  /**
   * Accessor for the number of lanes represented by the bowling.ControlDesk
   *
   * @return an int containing the number of lanes represented
   *
   */

  public int getNumLanes() {
    return numLanes;
  }

  /**
   * Allows objects to subscribe as observers
   *
   * @param adding  the bowling.ControlDeskObserver that will be subscribed
   *
   */

  public void subscribe(ControlDeskObserver adding) {
    subscribers.add(adding);
  }

  /**
   * Broadcast an event to subscribing objects.
   *
   * @param event  the bowling.ControlDeskEvent to broadcast
   *
   */

  public void publish(ControlDeskEvent event) {
    Iterator eventIterator = subscribers.iterator();
    while (eventIterator.hasNext()) {
      (
          (ControlDeskObserver) eventIterator
              .next())
          .receiveControlDeskEvent(
              event);
    }
  }

  /**
   * Accessor method for lanes
   *
   * @return a HashSet of Lanes
   *
   */

  public HashSet getLanes() {
    return lanes;
  }
}

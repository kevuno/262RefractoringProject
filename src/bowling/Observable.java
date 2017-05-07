package bowling;

/**
 * Created by kevin
 */
interface Observable {

  /**
   * subscribe
   * <p>
   * Method that will add a subscriber
   *
   * @param observer Observer that is to be added
   */
  void subscribe(Observer observer);

  /**
   * publish
   * <p>
   * Method that publishes an event to subscribers
   *
   * @param e Event that is to be published
   */
  void publish(Event e);

}

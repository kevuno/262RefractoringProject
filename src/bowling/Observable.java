package bowling;

/**
 * Created by Kevin and Nik
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

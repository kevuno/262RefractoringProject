package bowling;/* bowling.BowlerFile.java
 *
 *  Version:
 *  		$Id$
 * 
 *  Revisions:
 * 		$Log: bowling.BowlerFile.java,v $
 * 		Revision 1.5  2003/02/02 17:36:45  ???
 * 		Updated comments to match javadoc format.
 * 		
 * 		Revision 1.4  2003/02/02 16:29:52  ???
 * 		Added bowling.ControlDeskEvent and bowling.ControlDeskObserver. Updated bowling.Queue to allow access to Vector so that contents could be viewed without destroying. Implemented observer model for most of bowling.ControlDesk.
 * 		
 * 
 */

/**
 * Class for interfacing with bowling.model.Bowler database
 */

import bowling.model.Bowler;

import java.io.*;
import java.util.Vector;

public class BowlerFile {

  /** The location of the bowler database */
  private static String BOWLER_DAT = "BOWLERS.DAT";
  private static File BOWLER_DAT_FILE = new File(BOWLER_DAT);

  /**
   * Retrieves bowler information from the database and returns a bowling.model.Bowler objects with populated fields.
   *
   * @param nickName  the nickName of the bowler to retrieve
   *
   * @return a bowling.model.Bowler object
   *
   */

  public static Bowler getBowlerInfo(String nickName) throws IOException {
    BufferedReader in = new BufferedReader(new FileReader(BOWLER_DAT_FILE));
    String data;
    while ((data = in.readLine()) != null) {
      // File format is nick\tfname\te-mail
      String[] bowler = data.split("\t");
      if (nickName.equals(bowler[0])) {
        System.out.println(
            "Nick: "
                + bowler[0]
                + " Full: "
                + bowler[1]
                + " email: "
                + bowler[2]);
        return (new Bowler(bowler[0], bowler[1], bowler[2]));
      }
    }
    System.out.println("Nick not found...");
    return null;
  }

  /**
   * Stores a bowling.model.Bowler in the database
   *
   * @param nickName  the NickName of the bowling.model.Bowler
   * @param fullName  the FullName of the bowling.model.Bowler
   * @param email  the E-mail Address of the bowling.model.Bowler
   *
   */

  public static void putBowlerInfo(String nickName, String fullName, String email) throws IOException {

    String data = nickName + "\t" + fullName + "\t" + email + "\n";

    RandomAccessFile out = new RandomAccessFile(BOWLER_DAT, "rw");
    out.skipBytes((int) out.length());
    out.writeBytes(data);
    out.close();
  }

  /**
   * Retrieves a list of nicknames in the bowler database
   *
   * @return a Vector of Strings
   *
   */

  public static Vector getBowlers() throws IOException {
    Vector<String> allBowlers = new Vector<>();

    if (BOWLER_DAT_FILE.createNewFile()){
      System.out.println("Creating " + BOWLER_DAT);
    }

    BufferedReader in = new BufferedReader(new FileReader(BOWLER_DAT_FILE));
    String data;
    while ((data = in.readLine()) != null) {
      // File format is nick\tfname\te-mail
      String[] bowler = data.split("\t");
      //"Nick: bowler[0] Full: bowler[1] email: bowler[2]
      allBowlers.add(bowler[0]);
    }
    return allBowlers;
  }
}

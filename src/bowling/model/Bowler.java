package bowling.model;

/*
 * bowling.model.Bowler.java
 *
 * Version:
 *     $Id$
 *
 * Revisions:
 *     $Log: bowling.model.Bowler.java,v $
 *     Revision 1.3  2003/01/15 02:57:40  ???
 *     Added accessors and and equals() method
 *
 *     Revision 1.2  2003/01/12 22:23:32  ???
 *     *** empty log message ***
 *
 *     Revision 1.1  2003/01/12 19:09:12  ???
 *     Adding bowling.model.Party, bowling.Lane, bowling.model.Bowler, and bowling.model.Alley.
 *
 */

/**
 * Class that holds all bowler info
 */

public class Bowler {

  private String fullName;
  private String nickName;
  private String email;

  public Bowler(String nick, String full, String mail) {
    nickName = nick;
    fullName = full;
    email = mail;
  }

  public String getNickName() {
    return nickName;
  }

  public String getFullName() {
    return fullName;
  }

  public String getEmail() {
    return email;
  }

  @Override
  public boolean equals(Object other) {
    if (other == null || !Bowler.class.isAssignableFrom(other.getClass())) {
      return false;
    }
    final Bowler otherBowler = (Bowler) other;
    return nickName.equals(otherBowler.getNickName())
        && fullName.equals(otherBowler.getFullName())
        && email.equals(otherBowler.getEmail());
  }
}

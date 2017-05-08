package bowling;

/**
 * SMTP implementation based on code by R�al Gagnon mailto:real@rgagnon.com
 */

import bowling.model.Bowler;
import bowling.model.Score;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Vector;

public class ScoreReport {

  private StringBuilder content;

  //A score report is created upon the completion of a game
  public ScoreReport(Bowler bowler, int[] scores, int games) {
    String nick = bowler.getNickName();
    String full = bowler.getFullName();
    Vector<Score> v = null;
    try {
      v = ScoreHistoryFile.getScores(nick);
    } catch (Exception e) {
      System.err.println("Error: " + e);
    }

    //build content
    content = new StringBuilder();
    content.append("--Lucky Strike Bowling Alley Score Report--\n");
    content.append("\n");
    content.append("Report for ").append(full).append(", aka \"").append(nick).append("\":\n");
    content.append("\n");
    content.append("Final scores for this session: ");
    content.append(scores[0]);
    for (int i = 1; i < games; i++) {
      content.append(", ").append(scores[i]);
    }
    content.append(".\n");
    content.append("\n");
    content.append("\n");
    content.append("Previous scores by date: \n");
    for (Score score : v) {
      content.append("  ").append(score.getDate()).append(" - ").append(score.getScore()).append("\n");
      content.append("\n");
    }
    content.append("\n\n");
    content.append("Thank you for your continuing patronage.");
  }

  //sendEmail allows bowlers to receive score reports directly to their email
  //This service is automatic
  public void sendEmail(String recipient) {
    try {
      Socket s = new Socket("osfmail.rit.edu", 25);
      BufferedReader in =
          new BufferedReader(
              new InputStreamReader(s.getInputStream(), "8859_1"));
      BufferedWriter out =
          new BufferedWriter(
              new OutputStreamWriter(s.getOutputStream(), "8859_1"));

      // here you are supposed to send your username
      sendln(in, out, "HELLO world");
      sendln(in, out, "MAIL FROM: <abc1234@rit.edu>");
      sendln(in, out, "RCPT TO: <" + recipient + ">");
      sendln(in, out, "DATA");
      sendln(out, "Subject: Bowling Score Report ");
      sendln(out, "From: <Lucky Strikes Bowling Club>");

      sendln(out, "Content-Type: text/plain; charset=\"us-ascii\"\r\n");
      sendln(out, content + "\n\n");
      sendln(out, "\r\n");

      sendln(in, out, ".");
      sendln(in, out, "QUIT");
      s.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  //This method allows the program to connect to a print and print this score report
  public void sendPrintout() {
    PrinterJob job = PrinterJob.getPrinterJob();

    PrintableText printobj = new PrintableText(content.toString());

    job.setPrintable(printobj);

    if (job.printDialog()) {
      try {
        job.print();
      } catch (PrinterException e) {
        e.printStackTrace();
      }
    }
  }

  //This sends a string to an output stream
  public void sendln(BufferedReader in, BufferedWriter out, String s) {
    try {
      out.write(s + "\r\n");
      out.flush();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  //Same method as before but prints to System.out rather than a buffered writer
  public void sendln(BufferedWriter out, String s) {
    try {
      out.write(s + "\r\n");
      out.flush();
      System.out.println(s);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

import java.io.*;
import java.lang.*;
import javax.microedition.io.*;
import javax.microedition.rms.*;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class NokiaRetro extends MIDlet {
  public static final boolean COLOR = false;
  public static final boolean DEBUG = false;
  private Display display = null;
  private FontCanvas fontCanvas = null;
  private boolean painting = false;
  private static Image badge = null;
  private static Image fontImageLarge = null; // mx:6
  private static Image fontImageSmall = null; // mx:4
  private static String fontFileLarge = "/font42x54.png";
  private static String fontFileTrace = "/font35x44.png";
  private static String fontFileSmall = "/font28x36.png";
  private static String chrIndex = "m MWw ?KNOQTVXY <>JLScrs (),/1;=fjt{} !.:I[]`il '|";
  private static String chrWidth = "9 888 777777777 55555555 444444444444 333333333 22";
  public Calendar calendar;

  public NokiaRetro() {
    display = Display.getDisplay(this);
    fontCanvas = new FontCanvas(this);
  }

  public void startApp() throws MIDletStateChangeException {
    display.setCurrent(fontCanvas);
  }

  public void pauseApp() {}

  protected void destroyApp(boolean unconditional)
      throws MIDletStateChangeException {}

  class FontCanvas extends Canvas {
    private NokiaRetro parent = null;
    private int width = getWidth();
    private int height = getHeight();
    private int panel_height = width / 10 * 7;
    private int panel_offset = height - panel_height - 20;

    public FontCanvas(NokiaRetro parent) {
      this.parent = parent;
      this.setFullScreenMode(true);
      try {
        badge = Image.createImage ("/badge.png");
        fontImageLarge = Image.createImage (fontFileLarge);
        fontImageSmall = Image.createImage (fontFileSmall);
      } catch (Exception ex) {
      }
    }

    public void customFont(Graphics g, Image img, int mx, String phrase, int fx, int fy) {
      for (int i = 0; i < phrase.length(); i++) {
        int cw = 6;
        int ch = 9;
        char character = phrase.charAt(i);
        int ascii = (int) character;
        if (ascii > 32 && ascii < 127) {
          int cx = ((ascii - 32) % 8) * 7;
          int cy = ((ascii - 32) / 8) * 9;
          if (ascii == 34 || ascii == 92) {
            cw = 3; // straight-double-quote & backslash
          } else {
            int chrWidthIndex = chrIndex.indexOf(character);
            System.out.println("character: " + character);
            System.out.println("chrWidthIndex: " + chrWidthIndex);
            if (chrWidthIndex != -1) {
              cw = ((int) chrWidth.charAt(chrWidthIndex)) - 48;
              if (cw > 7) { cx -= cw - 7; }
            }
          }
          System.out.println("cw: " + cw);
          cw *= mx; ch *= mx;
          cx *= mx; cy *= mx;
          g.setClip(fx, fy, cw, ch);
          g.drawImage(img, fx - cx, fy - cy, Graphics.LEFT | Graphics.TOP);
        } else {
          cw = 3 * mx;
        }
        fx += cw;
      }
    }

    public void paint(Graphics g) {
      g.setColor(0, 0, 0);
      g.fillRect(0, 0, width, height + 25);
      g.setColor(130, 200, 170); // Green Screen
      g.fillRect(0, panel_offset, width, panel_height);

      // when System.getProperty("phone.imei")
      g.drawImage (badge, width / 2, height / 3 - 30, Graphics.VCENTER | Graphics.HCENTER);

      g.setColor(0, 0, 0);
      customFont(g, fontImageLarge, 6, "NOKIA", 33, panel_offset + 60);
      customFont(g, fontImageSmall, 4, "Menu", 70, panel_offset + 130);
      painting = false;
    }
  }
}

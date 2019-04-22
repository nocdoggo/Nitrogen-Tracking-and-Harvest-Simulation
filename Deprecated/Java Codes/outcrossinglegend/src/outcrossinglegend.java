import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import javax.imageio.ImageIO;
import java.lang.Double;

public class outcrossinglegend
{
  private String folder;
  private double max = 0.0D;
  private double min = 0.0D;
  private double interval = 0.0D;

  private void findmaxmin()
  {
    try
    {
      FileReader fr = new FileReader(this.folder + "\\conc_dep_data.txt");
      BufferedReader textreader = new BufferedReader(fr);
      String linestr = "";

      this.max = 0.0D;
      this.min = 1000000000.0D;

      textreader.readLine();
      while ((linestr = textreader.readLine()) != null) {
        double curr_dep = Double.parseDouble((linestr.split("\\s+"))[6]);
        if (curr_dep > this.max)
          this.max = curr_dep;
        if (curr_dep < this.min) {
          this.min = curr_dep;
        }
      }
      this.max = Math.log10(this.max);
      this.min = Math.log10(this.min);

      this.interval = ((this.max * 1000000000.0D - this.min * 1000000000.0D) / 6.0D);

      textreader.close();
      fr.close();
    } catch (IOException e) {
      System.out.println(e);
    }
  }

  public static void main(String[] args) throws Exception
  {
    outcrossinglegend w = new outcrossinglegend();
    w.folder = args[6];
    try
    {
      int width = 250; int height = 350;

      BufferedImage bi = new BufferedImage(width, height, 2);
      Graphics2D ig2 = bi.createGraphics();

      Font font = new Font("Comic Sans MS", 1, 20);
      ig2.setFont(font);
      String message = "Outcrossing";
      FontMetrics fontMetrics = ig2.getFontMetrics();

      ig2.setPaint(new Color(120, 120, 120));
      ig2.fill(new RoundRectangle2D.Double(0.0D, 0.0D, width, height, 20.0D, 20.0D));

      ig2.setPaint(Color.cyan);
      ig2.fill(new RoundRectangle2D.Double(20.0D, 40.0D, 50.0D, 50.0D, 15.0D, 15.0D));
      ig2.setPaint(Color.green);
      ig2.fill(new RoundRectangle2D.Double(20.0D, 100.0D, 50.0D, 50.0D, 15.0D, 15.0D));
      ig2.setPaint(Color.blue);
      ig2.fill(new RoundRectangle2D.Double(20.0D, 160.0D, 50.0D, 50.0D, 15.0D, 15.0D));
      ig2.setPaint(Color.yellow);
      ig2.fill(new RoundRectangle2D.Double(20.0D, 220.0D, 50.0D, 50.0D, 15.0D, 15.0D));
      ig2.setPaint(Color.red);
      ig2.fill(new RoundRectangle2D.Double(20.0D, 280.0D, 50.0D, 50.0D, 15.0D, 15.0D));

      ig2.setPaint(Color.white);
      ig2.drawString(message, 10, 25);

      w.findmaxmin();
      try
      {
        FileWriter fw = new FileWriter(w.folder + "\\legenddata.txt");
        BufferedWriter textwriter = new BufferedWriter(fw);

        font = new Font("TimesRoman", 1, 12);
        ig2.setFont(font);
        ig2.setPaint(Color.white);

        message = String.format("%.5G", new Object[] { Double.valueOf(Math.pow(10.0D, w.min)) }) + " to " + String.format("%.5G", new Object[] { Double.valueOf(Math.pow(10.0D, (w.min * 1000000000.0D + w.interval) / 1000000000.0D)) });
        textwriter.write(message + "\r\n");
        ig2.drawString(message, 90, 70);

        message = String.format("%.5G", new Object[] { Double.valueOf(Math.pow(10.0D, (w.min * 1000000000.0D + w.interval) / 1000000000.0D)) }) + " to " + String.format("%.5G", new Object[] { Double.valueOf(Math.pow(10.0D, (w.min * 1000000000.0D + 2.0D * w.interval) / 1000000000.0D)) });
        textwriter.write(message + "\r\n");
        ig2.drawString(message, 90, 130);

        message = String.format("%.5G", new Object[] { Double.valueOf(Math.pow(10.0D, (w.min * 1000000000.0D + 2.0D * w.interval) / 1000000000.0D)) }) + " to " + String.format("%.5G", new Object[] { Double.valueOf(Math.pow(10.0D, (w.min * 1000000000.0D + 3.0D * w.interval) / 1000000000.0D)) });
        textwriter.write(message + "\r\n");
        ig2.drawString(message, 90, 190);

        message = String.format("%.5G", new Object[] { Double.valueOf(Math.pow(10.0D, (w.min * 1000000000.0D + 3.0D * w.interval) / 1000000000.0D)) }) + " to " + String.format("%.5G", new Object[] { Double.valueOf(Math.pow(10.0D, (w.min * 1000000000.0D + 4.0D * w.interval) / 1000000000.0D)) });
        textwriter.write(message + "\r\n");
        ig2.drawString(message, 90, 250);

        message = String.format("%.5G", new Object[] { Double.valueOf(Math.pow(10.0D, (w.min * 1000000000.0D + 4.0D * w.interval) / 1000000000.0D)) }) + " to " + String.format("%.5G", new Object[] { Double.valueOf(Math.pow(10.0D, w.max)) });
        textwriter.write(message + "\r\n");
        ig2.drawString(message, 90, 310);

        textwriter.close();
        fw.close();
      } catch (IOException e) {
        System.out.println(e);
      }

      ImageIO.write(bi, "PNG", new File("outcrossinglegend.png"));

      FileInputStream fis = new FileInputStream("outcrossinglegend.png");
      FileOutputStream fos = new FileOutputStream(w.folder + "\\outcrossinglegend.png");
      byte[] buffer = new byte[1024];
      int noOfBytes = 0;
      while ((noOfBytes = fis.read(buffer)) != -1) {
        fos.write(buffer, 0, noOfBytes);
      }

      fis.close();
      fos.close();
    }
    catch (IOException ie)
    {
      ie.printStackTrace();
    }
  }
}
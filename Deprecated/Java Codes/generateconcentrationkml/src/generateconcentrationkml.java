import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class generateconcentrationkml
{
  private int expcount = 5;
  private int latloncount = 1000000;
  private int[] count9to13;
  private double[][][] arr_explatlon;
  private String folder = "";
  private String lat = "";
  private String lon = "";

  public generateconcentrationkml()
  {
    this.count9to13 = new int[12];
    for (int i = 0; i < 5; i++) {
      this.count9to13[i] = 0;
    }
    this.arr_explatlon = new double[this.expcount][this.latloncount][2];
    for (int i = 0; i < this.expcount; i++)
      for (int j = 0; j < this.latloncount; j++)
        for (int k = 0; k < 2; k++)
          this.arr_explatlon[i][j][k] = 0.0D;
  }

  public void readfile()
  {
    try
    {
      FileReader fr = new FileReader(this.folder + "\\conc_dep_data.txt");
      BufferedReader textreader = new BufferedReader(fr);
      String linestr = "";

      int concexp = 4;

      textreader.readLine();

      FileReader frtemp = new FileReader(this.folder + "\\legenddata.txt");
      BufferedReader textreadertemp = new BufferedReader(frtemp);
      double[] doubles = new double[6];
      for(int i = 0;  i < 5; i++){
    	  String[] ranges = textreadertemp.readLine().split("\\s+");
    	  if(i==0){
    		  doubles[i] = Double.valueOf(ranges[0]).doubleValue();
    	  }
    	  doubles[i+1] = Double.valueOf(ranges[2]).doubleValue();
      }
      textreadertemp.close();
      frtemp.close();
      
      while ((linestr = textreader.readLine()) != null) {
        String[] strdata = linestr.split("\\s+");

        double curr_val = Double.valueOf(strdata[5]).doubleValue();

        for (int y = 0; y < 5; y++) {
            if ((curr_val >= doubles[y]) && (curr_val <=  doubles[y+1])) {
                concexp = 4 - y;
                break;
              }
          concexp = 4;
        }

        this.arr_explatlon[concexp][this.count9to13[concexp]][0] = Double.valueOf(strdata[2]).doubleValue();
        this.arr_explatlon[concexp][this.count9to13[concexp]][1] = Double.valueOf(strdata[3]).doubleValue();
        this.count9to13[concexp] += 1;
      }
      textreader.close();
      fr.close();
    }
    catch (IOException e) {
      System.out.println(e);
    }
  }

  private void showvalues(int exp) {
    int count = 0;
    while (count < this.count9to13[(exp - 5)])
    {
      count++;
    }
  }

  private void writekmlcode() {
        BufferedReader file_gridsize = null;
    try {
            FileReader fr = null;
            FileWriter fw = null;
            BufferedReader textreader = null;
            BufferedWriter textwriter = null;
            file_gridsize = new BufferedReader(new FileReader("gridsize.txt"));
            double gridsize = Double.parseDouble(file_gridsize.readLine());
            gridsize/=2.0;
            double x1 = 0.0D;
            double x2 = 0.0D;
            double y1 = 0.0D;
            double y2 = 0.0D;
            String[] color = { "red", "yellow", "blue", "green", "cyan" };
            try {
              fr = new FileReader("reference.kml");
              textreader = new BufferedReader(fr);
              fw = new FileWriter(this.folder + "\\concentration.kml");
              textwriter = new BufferedWriter(fw);

              String readline = "";
              while ((readline = textreader.readLine()) != null) {
                textwriter.write(readline + "\r\n");
              }

              textwriter.write("\r\n");

              textreader.close();
              fr.close();

              fr = new FileReader(this.folder + "\\latlong.txt");
              textreader = new BufferedReader(fr);
              String[] latlon = textreader.readLine().split(",\\s+");

              for (int placemark = 0; placemark < this.expcount; placemark++) {
                for (int coordinates = 0; coordinates < this.count9to13[placemark]; coordinates++) {
                  x1 = this.arr_explatlon[placemark][coordinates][0] + gridsize;
                  x2 = this.arr_explatlon[placemark][coordinates][0] - gridsize;
                  y1 = this.arr_explatlon[placemark][coordinates][1] - gridsize;
                  y2 = this.arr_explatlon[placemark][coordinates][1] + gridsize;
                  textwriter.write("<Placemark>\r\n");
                  textwriter.write("  <name></name>\r\n");
                  textwriter.write("  <styleUrl>#" + color[placemark] + "</styleUrl>\r\n");
                  textwriter.write("  <Polygon>\r\n");
                  textwriter.write("    <outerBoundaryIs>\r\n");
                  textwriter.write("      <LinearRing>\r\n");
                  textwriter.write("        <coordinates>\r\n");
                  textwriter.write("          " + y1 + "," + x1 + "\r\n");
                  textwriter.write("          " + y2 + "," + x1 + "\r\n");
                  textwriter.write("          " + y2 + "," + x2 + "\r\n");
                  textwriter.write("          " + y1 + "," + x2 + "\r\n");
                  textwriter.write("          " + y1 + "," + x1 + "\r\n" + "\r\n");
                  textwriter.write("        </coordinates>\r\n");
                  textwriter.write("      </LinearRing>\r\n");
                  textwriter.write("    </outerBoundaryIs>\r\n");
                  textwriter.write("  </Polygon>\r\n");
                  textwriter.write("</Placemark>\r\n");
                }

              }

              double stx = Double.valueOf(latlon[0]).doubleValue();
              double sty = Double.valueOf(latlon[1]).doubleValue();

              System.out.println(sty + "," + stx);

              textwriter.write("<Placemark>\r\n");
              textwriter.write("<name>Source Location</name>\r\n");
              textwriter.write("<description><![CDATA[<pre>\r\n");
              textwriter.write("");
              textwriter.write("");
              textwriter.write("</pre>]]>\r\n");
              textwriter.write("</description>\r\n");
              textwriter.write("<Style id=\"sorc\">\r\n");
              textwriter.write("<IconStyle>\r\n");
              textwriter.write("<scale>1</scale>\r\n");
              textwriter.write("<color>ff000000</color>\r\n");
              textwriter.write("<Icon>\r\n");
              textwriter.write("<href>http://maps.google.com/mapfiles/kml/shapes/placemark_circle.png</href>\r\n");
              textwriter.write("</Icon>\r\n");
              textwriter.write("<hotSpot x=\"0.5\" y=\"0.5\" xunits=\"fraction\" yunits=\"fraction\"></hotSpot>\r\n");
              textwriter.write("</IconStyle>\r\n");
              textwriter.write("<LabelStyle>\r\n");
              textwriter.write("<color>ff000000</color>\r\n");
              textwriter.write("</LabelStyle>\r\n");
              textwriter.write("<LineStyle>\r\n");
              textwriter.write("<color>ff000000</color>\r\n");
              textwriter.write("<width>4</width>\r\n");
              textwriter.write("</LineStyle>\r\n");
              textwriter.write("</Style>\r\n");
              textwriter.write("<Point>\r\n");
              textwriter.write("<extrude>1</extrude>\r\n");
              textwriter.write("<altitudeMode>relativeToGround</altitudeMode>\r\n");
              textwriter.write("<coordinates>" + sty + "," + stx + ",1.8</coordinates>\r\n");
              textwriter.write("</Point>\r\n");
              textwriter.write("</Placemark>\r\n\r\n");

              textwriter.write("<ScreenOverlay id=\"legend\">\r\n");
              textwriter.write("<name>Give the file a display name</name>\r\n");
              textwriter.write("<description>Write what you want displayed in the properties textbox</description>\r\n");
              textwriter.write("<scale>0.5</scale>\r\n");

              textwriter.write("<Icon><href>concentrationlegend.png</href></Icon>\r\n");
              textwriter.write("<overlayXY  x=\"0\" y=\".055\" xunits=\"fraction\" yunits=\"fraction\"/>\r\n");
              textwriter.write("<screenXY  x=\"0\" y=\".055\" xunits=\"fraction\" yunits=\"fraction\"/>\r\n");
              textwriter.write("<rotation>0</rotation><size x=\"0\" y=\"0\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n");
              textwriter.write("</ScreenOverlay>\r\n");

              textwriter.write("</Document>\r\n");
              textwriter.write("</kml>\r\n");
            } catch (IOException ex) {
              Logger.getLogger(generateconcentrationkml.class.getName()).log(Level.SEVERE, null, ex);
              try
              {
                textreader.close();
                fr.close();
                textwriter.close();
                fw.close();
              } catch (IOException ex1) {
                Logger.getLogger(generateconcentrationkml.class.getName()).log(Level.SEVERE, null, ex1);
              }
            }
            finally
            {
              try
              {
                textreader.close();
                fr.close();
                textwriter.close();
                fw.close();
              } catch (IOException ex) {
                Logger.getLogger(generateconcentrationkml.class.getName()).log(Level.SEVERE, null, ex);
              }
            }
    }   catch (IOException ex) {
            Logger.getLogger(generateconcentrationkml.class.getName()).log(Level.SEVERE, null, ex);
        } 
    finally
    {
      try
      {
        file_gridsize.close();
      } catch (IOException ex) {
        Logger.getLogger(generateconcentrationkml.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }

  public static void main(String[] args) {
    generateconcentrationkml g = new generateconcentrationkml();
    g.folder = args[6];
    g.lat = args[1];
    g.lon = args[2];
    g.readfile();
    g.showvalues(6);
    System.out.println();
    g.writekmlcode();
  }
}
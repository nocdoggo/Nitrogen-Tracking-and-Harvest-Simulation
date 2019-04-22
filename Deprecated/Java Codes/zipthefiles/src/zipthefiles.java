import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class zipthefiles
{
  public static void main(String[] args)
    throws InterruptedException
  {
    String folder = args[6];
    String lat = args[1];
    String lon = args[2];

    String filePrefix = args[9];
    String outputFileName = filePrefix + "output_data.txt";
    String depositionFileName = filePrefix + "deposition.kmz";
    String concentrationFileName = filePrefix + "concentration.kmz";
    String outcrossingFileName = filePrefix + "outcrossing.kmz";
    try
    {
      File oldfile = new File(folder + "/temp_output_data.txt"); 

      File newfile = new File(folder + "/" + outputFileName);
      oldfile.renameTo(newfile);
      
      oldfile = new File(folder + "/conc_dep_data.txt");
      oldfile.delete();

      oldfile = new File(folder + "/latlong.txt");
      oldfile.delete();

      oldfile = new File(folder + "/temp_data");
      oldfile.delete();

      oldfile = new File(folder + "/legenddata.txt");
      oldfile.delete();

      oldfile = new File(folder + "/summed_data_" + lat + "_" + lon);
      oldfile.delete();

      JarUtil ju = new JarUtil(zipthefiles.class);

      Logger.getLogger(zipthefiles.class.getName()).log(Level.WARNING, null, ju.getJarPath());

      String execstr = "7z.exe a -tzip " + folder + "/" + concentrationFileName + 
        " \"" + ju.getJarPath() + "/" + folder + "/" + "concentration.kml\"" + 
        " \"" + ju.getJarPath() + "/" + folder + "/" + "concentrationlegend.png\"";
      Process p = Runtime.getRuntime().exec(execstr);

      StreamGobbler errorGobbler = new StreamGobbler(p.getErrorStream(), "ERROR");
      errorGobbler.start();

      StreamGobbler outGobbler = new StreamGobbler(p.getInputStream(), "STDOUT");
      outGobbler.start();
      try
      {
        p.waitFor();
      }
      catch (InterruptedException ex)
      {
        Logger.getLogger(zipthefiles.class.getName()).log(Level.WARNING, null, ex);
      }

/************************************************/      
      
      execstr = "7z.exe a -tzip " + folder + "/" + depositionFileName + 
        " \"" + ju.getJarPath() + "/" + folder + "/" + "deposition.kml\"" + 
        " \"" + ju.getJarPath() + "/" + folder + "/" + "depositionlegend.png\"";
      p = Runtime.getRuntime().exec(execstr);

      errorGobbler = new StreamGobbler(p.getErrorStream(), "ERROR");
      errorGobbler.start();

      outGobbler = new StreamGobbler(p.getInputStream(), "STDOUT");
      outGobbler.start();
      try
      {
        p.waitFor();
      }
      catch (InterruptedException ex)
      {
        Logger.getLogger(zipthefiles.class.getName()).log(Level.WARNING, null, ex);
      }

/************************************************/
      
            
      execstr = "7z.exe a -tzip " + folder + "/" + outcrossingFileName + 
        " \"" + ju.getJarPath() + "/" + folder + "/" + "outcrossing.kml\"" + 
        " \"" + ju.getJarPath() + "/" + folder + "/" + "outcrossinglegend.png\"";
      p = Runtime.getRuntime().exec(execstr);

      errorGobbler = new StreamGobbler(p.getErrorStream(), "ERROR");
      errorGobbler.start();

      outGobbler = new StreamGobbler(p.getInputStream(), "STDOUT");
      outGobbler.start();
      try
      {
        p.waitFor();
      }
      catch (InterruptedException ex)
      {
        Logger.getLogger(zipthefiles.class.getName()).log(Level.WARNING, null, ex);
      }

/************************************************/
      
      oldfile = new File(folder + "/concentrationlegend.png");
      oldfile.delete();

      oldfile = new File(folder + "/depositionlegend.png");
      oldfile.delete();
      
      oldfile = new File(folder + "/outcrossinglegend.png");
      oldfile.delete();

      oldfile = new File(folder + "/deposition.kml");
      oldfile.delete();

      oldfile = new File(folder + "/concentration.kml");
      oldfile.delete();
      
      oldfile = new File(folder + "/outcrossing.kml");
      oldfile.delete();
    }
    catch (IOException e)
    {
      Logger.getLogger(zipthefiles.class.getName()).log(Level.WARNING, null, e);
    }
  }
}
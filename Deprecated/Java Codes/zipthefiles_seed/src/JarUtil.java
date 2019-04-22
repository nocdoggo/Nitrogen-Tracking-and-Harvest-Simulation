import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.security.CodeSource;
import java.security.ProtectionDomain;

public class JarUtil
{
  private String jarName;
  private String jarPath;

  public JarUtil(Class<?> clazz)
    throws UnsupportedEncodingException
  {
    String path = clazz.getProtectionDomain().getCodeSource().getLocation().getPath();

    path = URLDecoder.decode(path, "UTF-8");

    File jarFile = new File(path);
    this.jarName = jarFile.getName();

    File parent = jarFile.getParentFile();
    if (parent != null)
    {
      this.jarPath = parent.getAbsolutePath();
    }
    else
    {
      this.jarPath = jarFile.getAbsolutePath();
    }
  }

  public String getJarName()
  {
    return this.jarName;
  }

  public String getJarPath()
  {
    return this.jarPath;
  }
}

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Me
 */
public class generatesetupfile {
    public void createfile(){
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            fw = new FileWriter("EMITIMES");
            bw = new BufferedWriter(fw);
            bw.write("YYYY MM DD HH    DURATION(hhhh) #RECORDS"+"\r\n");
            bw.write("YYYY MM DD HH MM DURATION(hhmm) LAT LON HGT(m) RATE(/h) AREA(m2) HEAT(w)"+"\r\n");
            
            String index[] = new String[3];
            if(st_mth < 10)
                index[0] = "0";
            else
                index[0] = "";
            if(st_day < 10)
                index[1] = "0";
            else
                index[1] = "";
            if(st_hr < 10)
                index[2] = "0";
            else
                index[2] = "";
            
            int[] dayslist;
            if(year%4 == 0)
                dayslist = new int[] {31,29,31,30,31,30,31,31,30,31,30,31};
            else
                dayslist = new int[] {31,28,31,30,31,30,31,31,30,31,30,31};
          
            int duration = 0;
            
            //Find duration of the calculation in hours
            for(int month = st_mth; month <= end_mth; month++){
                int startday = 1;
                if(month == st_mth)
                    startday = st_day;
                int lastday = dayslist[month-1];
                if(month == end_mth)
                    lastday = end_day;
                for(int day = startday; day <= lastday; day++){
                    int starthour = 0;
                    if(day == st_day && month == st_mth)
                        starthour = st_hr;
                    int lasthour = 23;
                    if(day == end_day && month == end_mth)
                        lasthour = end_hr-1;
                    for(int hour = starthour; hour <= lasthour; hour++){
                        duration++;
                    }
                }
            }
            //End finding duration of calculation in hours
            
            bw.write(year+" "+index[0]+st_mth+" "+index[1]+st_day+" "+index[2]+st_hr+" "+duration+" "+duration+"\r\n");
            
            
            //Seek the reading point to the start of calculation
            int linecount = 1;
            FileReader fr = new FileReader("strengthdata");
            BufferedReader br = new BufferedReader(fr);

            String strengthdata = "";
            
            for(int month = 1; month <= st_mth; month++){
                int startday = 1;
                int endday = dayslist[month-1];
                if(month == st_mth)
                    endday = st_day;
                for(int day = startday; day <= endday; day++){
                    int starthour = 0;
                    int endhour = 23;
                    if(month == st_mth && day == st_day)
                        endhour = st_hr-1;
                    for(int hour = starthour; hour <= endhour; hour++){
                        //br.readLine();
                        strengthdata = br.readLine();
                        String[] splitted = strengthdata.split(" ");
                        if(("02/29".equals(splitted[0]) && (year%4 != 0))){
                            hour--;
                            linecount++;
                            continue;
                        }
                        linecount++;
                    }
                }
            }
            System.out.println(linecount);
            //Start writing data lines to setup file
            for(int month = st_mth; month <= end_mth; month++){
                int startday = 1;
                if(month == st_mth)
                    startday = st_day;
                int lastday = dayslist[month-1];
                if(month == end_mth)
                    lastday = end_day;
                for(int day = startday; day <= lastday; day++){
                    int starthour = 0;
                    if(day == st_day && month == st_mth)
                        starthour = st_hr;
                    int lasthour = 23;
                    if(day == end_day && month == end_mth)
                        lasthour = end_hr-1;
                    for(int hour = starthour; hour <= lasthour; hour++){
                        strengthdata = br.readLine();
                        String[] splitted = strengthdata.split(" ");
                        if(("02/29".equals(splitted[0]) && (year%4 != 0))){
                            hour--;
                            continue;
                        }
                        else{
                            if(month < 10)
                                index[0] = "0";
                            else
                                index[0] = "";
                            if(day < 10)
                                index[1] = "0";
                            else
                                index[1] = "";
                            if(hour < 10)
                                index[2] = "0";
                            else
                                index[2] = "";
                            
                            double datatowrite = Double.parseDouble(splitted[2])*density*area;
                            bw.write(year+" "+index[0]+month+" "+index[1]+day+" "+index[2]+hour+" 00 0100 "+lat+" "+lon+" 1.8 "+datatowrite+" 0.0 0.0"+"\r\n");
                        }
                    }
                }
            }
//            strengthdata = br.readLine();
//            String[] splitted = strengthdata.split(" ");
//            System.out.println(splitted[2]);
        } catch (IOException ex) {
            Logger.getLogger(generatesetupfile.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                bw.close();
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(generatesetupfile.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    public static void main(String args[]){
        generatesetupfile g = new generatesetupfile();
        g.year = Integer.parseInt(args[0]);
        g.st_mth = Integer.parseInt(args[1]);
        g.st_day = Integer.parseInt(args[2]);
        g.st_hr = Integer.parseInt(args[3]);

        g.end_mth = Integer.parseInt(args[4]);
        g.end_day = Integer.parseInt(args[5]);
        g.end_hr = Integer.parseInt(args[6]);
        
        g.lat = args[7];
        g.lon = args[8];
        
        g.density = Double.parseDouble(args[9]);
        g.area = Double.parseDouble(args[10]);
        
        g.createfile();
    }
    int year;
    int st_mth;
    int st_day;
    int st_hr;
    int end_mth;
    int end_day;
    int end_hr;
    String lat;
    String lon;
    double density;
    double area;
}

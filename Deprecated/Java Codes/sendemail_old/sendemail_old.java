import java.io.*;
import java.util.Scanner;


public class sendemail {
    
    public void calculate_total_concentration_and_deposition(String summed_data_filename){

        try{
            FileReader tempfile = new FileReader(folder + "\\temp_data");
            BufferedReader tempreader = new BufferedReader(tempfile);
            FileReader sumfile = new FileReader(summed_data_filename);
            BufferedReader sumreader = new BufferedReader(sumfile);
            
            FileWriter finalfile = new FileWriter(folder + "\\conc_dep_data.txt");
            BufferedWriter finalwriter = new BufferedWriter(finalfile);
            
            String templine;
            String sumline;
            
            String[] str_tempdata = new String[6];
            String[] str_sumdata = new String[6];
            
            boolean eof_flag_temp = false;
            boolean eof_flag_sum = false;
            
            boolean read_templine = true;
            boolean read_sumline = true;
            
            double[] val_temp = new double[4];
            double[] val_sum = new double[4];
            
            templine = tempreader.readLine();
            sumline = sumreader.readLine();
            
            finalwriter.write("DAY HR   LAT     LON     DEP       CON\r\n");
            
            str_tempdata[0] = "";
            str_sumdata[0] = "";
                
            do{
                
                if(eof_flag_temp == false && read_templine == true){
                    templine = tempreader.readLine();
                    if(templine == null){
                        eof_flag_temp = true;
                    }
                    else{
                    	str_tempdata = templine.split("\\s+");
                    }
                }
                
                if(eof_flag_sum == false && read_sumline == true){
                    sumline = sumreader.readLine();
                    if(sumline == null){
                        eof_flag_sum = true;
                    }
                    else{
                        str_sumdata=sumline.split("\\s+");
                    }
                }
                
                String str_write = "";
                str_sumdata[1] = "" + end_hr;
                str_tempdata[1] = "" + end_hr;
                
                
                double tempdouble = ((Double.valueOf(str_tempdata[5]))*10000000000.0);
                double sumdouble = ((Double.valueOf(str_sumdata[5]))*10000000000.0);
                
                if(eof_flag_temp == false && eof_flag_sum == true){
                    str_write = str_tempdata[0];
                    tempdouble /= 10000000000.0;
                    str_tempdata[5] = "" + tempdouble;
                    for(int i = 1; i < 6; i++){
                        str_write += " " + str_tempdata[i];
                    }
                    
                    read_sumline = false;
                    read_templine = true;
                    str_tempdata[0] = "";
                }
                if(eof_flag_temp == true && eof_flag_sum == false){
                    str_write = str_sumdata[0];
                    sumdouble /= 10000000000.0;
                    str_sumdata[5] = "" + sumdouble;
                    for(int i = 1; i < 6; i++){
                        str_write += " " + str_sumdata[i];
                    }
                    read_sumline = true;
                    read_templine = false;
                    str_sumdata[0] = "";
                }
                if(eof_flag_temp == true && eof_flag_sum == true){
                    break;
                }
                if(eof_flag_temp == false && eof_flag_sum == false){
                    for(int i = 0; i < 3; i++){
                        val_temp[i] = Double.valueOf(str_tempdata[i+2]);
                        val_sum[i] = Double.valueOf(str_sumdata[i+2]);
                    }
                    if(val_temp[1] > val_sum[1]){
                        str_write = str_sumdata[0];
                        sumdouble /= 10000000000.0;
                        str_sumdata[5] = "" + sumdouble;
                        for(int i = 1; i < 6; i++){
                            str_write += " " + str_sumdata[i];
                        }
                        read_sumline = true;
                        str_sumdata[0] = "";
                        read_templine = false;
                    }
                    else if(val_temp[1] < val_sum[1]){
                        str_write = str_tempdata[0];
                        tempdouble /= 10000000000.0;
                        str_tempdata[5] = "" + tempdouble;
                        for(int i = 1; i < 6; i++){
                            str_write += " " + str_tempdata[i];
                        }
                        read_sumline = false;
                        read_templine = true;
                        str_tempdata[0] = "";
                    }
                    else if(val_temp[1] == val_sum[1]){
                        if(val_temp[0] == val_sum[0]){
                            str_write = str_sumdata[0];
                            str_tempdata[5] = "" + tempdouble;
                            str_sumdata[5] = "" + sumdouble;
                            double deposition = val_sum[2] + val_temp[2];
                            double concentration = (tempdouble + sumdouble)/10000000000.0;
                            
                            for(int i = 1; i < 6; i++){
                                if(i == 4){
                                    str_sumdata[i] = "" + deposition;
                                }
                                if(i == 5){
                                    str_sumdata[i] = "" + concentration;
                                }
                                str_write += " " + str_sumdata[i];
                            }
                            read_sumline = true;
                            str_sumdata[0] = "";
                            read_templine = true;
                            str_tempdata[0] = "";
                        }
                        else if(val_temp[0] > val_sum[0]){
                            str_write = str_sumdata[0];
                            sumdouble /= 10000000000.0;
                            str_sumdata[5] = "" + sumdouble;
                            for(int i = 1; i < 6; i++){
                                str_write += " " + str_sumdata[i];
                            }
                            read_sumline = true;
                            str_sumdata[0] = "";
                            read_templine = false;
                            
                        }
                        else if(val_temp[0] < val_sum[0]){
                            str_write = str_tempdata[0];
                            tempdouble /= 10000000000.0;
                            str_tempdata[5] = "" + tempdouble;
                            for(int i = 1; i < 6; i++){
                                str_write += " " + str_tempdata[i];
                            }
                            read_sumline = false;
                            read_templine = true;
                            str_tempdata[0] = "";
                        }
                    }
                }
                
                finalwriter.write(str_write);
                finalwriter.write("\r\n");
                
            }while(!(eof_flag_temp == true && eof_flag_sum == true));

            tempfile.close();
            tempreader.close();
            
            sumfile.close();
            sumreader.close();            
            
            finalwriter.close();
            finalfile.close();
            
            FileInputStream fis = new FileInputStream(folder+"\\conc_dep_data.txt");      
            FileOutputStream fos = new FileOutputStream(summed_data_filename);
            byte[] buffer = new byte[1024];
            int noOfBytes;
            while ((noOfBytes = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, noOfBytes);
            }
            fos.close();
            fis.close();
        }
        
        catch(IOException e){
            System.err.println(e);
        }
    }
    
    public static void main(String[] args) {
        try {
            sendemail s = new sendemail();
            
            s.latitude = args[0];
            s.longitude = args[1];
            
            s.st_mth = args[2];
            s.st_day = args[3];
            s.st_hr = args[4];
            
            s.end_mth = args[5];
            s.end_day = args[6];
            s.end_hr = args[7];
            
            s.folder = args[8];
            s.year_avg = args[9];
            
            s.datatype = args[10];
            
            //args[11] has been used as folder of data
            s.outcrosscoefficient = Double.parseDouble(args[12]);
            s.density = Double.parseDouble(args[13]);
            s.area = Double.parseDouble(args[14]);
            
            File delfile;
            
            int countdividerforconcentration = 0;
            
            int totaldays = 0;
            
            String summed_data_filename = "";
            int startyear = 2013 - Integer.parseInt(s.year_avg);
            
            int startmonth;
            int startday;
            int starthour = Integer.parseInt(s.st_hr);
            
            int endyear = 2012;
            int endmonth;
            
            int endday;
            int endhour = Integer.parseInt(s.end_hr);
            boolean start_month = true;
            boolean firstday = true;
            
            BufferedWriter file_gridsize = new BufferedWriter(new FileWriter("gridsize.txt"));
            file_gridsize.write("0.01\r\n");
            file_gridsize.close();
            
            FileWriter file_latlon = new FileWriter(s.folder + "\\latlong.txt");
            BufferedWriter bw_latlong = new BufferedWriter(file_latlon);
            String str_latlon_write = ""+s.latitude+", "+s.longitude+"\r\n";
            bw_latlong.write(str_latlon_write);
            bw_latlong.close();
            file_latlon.close();
            
            
            for(int yearloop = startyear; yearloop <= endyear; yearloop++){
                start_month = true;
                
                int days_list[] = new int[] {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
                if(yearloop%4 == 0)
                    days_list[1] = 28;
                
                startmonth = Integer.parseInt(s.st_mth);
                startday = Integer.parseInt(s.st_day);
                starthour = Integer.parseInt(s.st_hr);
                //int firststarthour = starthour;
                endmonth = Integer.parseInt(s.end_mth);
                endday = Integer.parseInt(s.end_day);
                                
                totaldays = 0;
                
                for(int i = startmonth; i <= endmonth; i++){
                    if(startmonth == endmonth){
                        totaldays += endday - startday + 1;
                    }
                    else if(i == startmonth)
                        totaldays += days_list[i-1] - startday + 1;
                    else if(i == endmonth)
                        totaldays += endday;
                    else
                        totaldays += days_list[i-1];
                }
                
                endmonth = startmonth;
                int rem_days = totaldays;
                do{
                    rem_days -= days_list[endmonth];
                    endmonth += 1;
                }while(rem_days > 0);
                rem_days = totaldays;
                
                endmonth = Integer.parseInt(s.end_mth);
                
                for(int monthloop = startmonth; monthloop <= endmonth; monthloop++){
                    if(monthloop != startmonth){
                        start_month = false;
                    }
                    
                    if(start_month){
                        if((totaldays + startday) > days_list[startmonth - 1]){
                            endday = days_list[startmonth - 1];
                            rem_days = rem_days - days_list[startmonth - 1] + startday - 1;
                        }
                        else
                            endday = totaldays + startday - 1; 
                    }
                    else{
                        startday = 1;
                        if(rem_days > days_list[monthloop - 1]){
                            endday = days_list[monthloop - 1];
                            rem_days = rem_days - days_list[monthloop - 1];
                        }
                        else
                            endday = rem_days;
                    }
                    
                    for(int dayloop = startday; dayloop <= endday; dayloop++){
                        if(!(monthloop == endmonth && dayloop == endday))
                            endhour = 24;
                        else
                            endhour = Integer.parseInt(s.end_hr);
                        
                        if(!(monthloop == startmonth && dayloop == startday)){
                            //if (starthour%2 == 0)
                                starthour = 0;
//                            else
//                                starthour = 1;
                        }
                        boolean alternate_flag = false;
                        
                        for(int hourloop = starthour; hourloop < endhour; hourloop+=2){
                            System.out.println(""+yearloop+" "+monthloop+" "+dayloop+" "+hourloop);
                            
                            delfile = new File("CONTROL");
                            delfile.delete();

                            FileReader fr = new FileReader("ref_control");
                            BufferedReader textreader = new BufferedReader(fr);
                            FileWriter fw = new FileWriter("CONTROL");
                            BufferedWriter textwriter = new BufferedWriter(fw);
                            String control_text;
                            int linecount = 0;
                            while((control_text = textreader.readLine()) != null){
                                linecount++;
                                String[] format = new String[3];
                                format[0] = "";
                                format[1] = "";
                                format[2] = "";
                                if(monthloop < 10)  format[0] = "0";
                                if(dayloop < 10)    format[1] = "0";
                                if(hourloop < 10)   format[2] = "0";
                                if(linecount == 1){
                                    control_text = "" + yearloop;
                                    char[] temp = control_text.toCharArray();
                                    control_text = "";
                                    control_text = temp[2] + "" + temp[3];
                                    control_text += " " + format[0] + monthloop + " " + format[1] + dayloop + " " + format[2] + hourloop;
                                }
                                if(linecount == 2){
                                    control_text = "2";
                                }
                                if(linecount == 3){
                                    control_text = s.latitude + " " + s.longitude + " " + "1.8";
                                    textwriter.write(control_text + "\n");
                                }
                                if(linecount == 7){
                                    if(hourloop == 21 || hourloop == 22 || hourloop == 23)
                                        control_text = "2";
                                    else
                                        control_text = "1";
                                }
                                if(linecount == 8){
                                    if("narr".equals(s.datatype))
                                        control_text = args[11] + "/NARR/"+yearloop+"/"+monthloop+"/";
                                    else
                                        control_text = args[11] + "/NAM/"+yearloop+"/"+monthloop+"/";
                                }
                                if(linecount == 9){
                                    if("narr".equals(s.datatype)){
                                        if(hourloop == 21 || hourloop == 22 || hourloop == 23){
                                            control_text = "" + yearloop + "" + format[0] + "" + monthloop + "" + format[1] + dayloop + ".arl";
                                            textwriter.write(control_text + "\n");
                                                                                        
                                            int nextday = dayloop + 1;
                                            int nextmonth = monthloop;
                                            if(dayloop == days_list[monthloop - 1]){
                                                nextday = 1;
                                                nextmonth += 1;
                                            }
                                            format[0] = "";
                                            format[1] = "";
                                            if(nextmonth < 10)    format[0] = "0";
                                            if(nextday < 10)    format[1] = "0";
                                            
                                            control_text = args[11] + "/NARR/"+yearloop+"/"+nextmonth+"/";
                                            textwriter.write(control_text + "\n");
                                            
                                            control_text = "" + yearloop + "" + format[0] + "" + nextmonth + "" + format[1] + nextday + ".arl";
                                        }
                                        else
                                            control_text = "" + yearloop + "" + format[0] + "" + monthloop + "" + format[1] + dayloop + ".arl";
                                    }
                                    else{
                                        if(hourloop == 21 || hourloop == 22 || hourloop == 23){
                                            control_text = "" + yearloop + "" + format[0] + "" + monthloop + "" + format[1] + dayloop + "_nam12";
                                            textwriter.write(control_text + "\n");
                                                                                        
                                            int nextday = dayloop + 1;
                                            int nextmonth = monthloop;
                                            if(dayloop == days_list[monthloop - 1]){
                                                nextday = 1;
                                                nextmonth += 1;
                                            }
                                            format[0] = "";
                                            format[1] = "";
                                            if(nextmonth < 10)    format[0] = "0";
                                            if(nextday < 10)    format[1] = "0";
                                            
                                            control_text = args[11] + "/NAM/"+yearloop+"/"+nextmonth+"/";
                                            textwriter.write(control_text + "\n");
                                            
                                            control_text = "" + yearloop + "" + format[0] + "" + nextmonth + "" + format[1] + nextday + "_nam12";
                                        }
                                        else{
                                            control_text = "" + yearloop + "" + format[0] + "" + monthloop + "" + format[1] + dayloop + "_nam12";
                                        }
                                    }
                                        
                                }
                                textwriter.write(control_text + "\n");
                            }
                            textreader.close();
                            textwriter.close();
                            fr.close();
                            fw.close();
                            
                            Runtime rt = Runtime.getRuntime();
							
                            Process p = rt.exec("cmd /c copy/b seedsetupfile.cfg SETUP.CFG");   //name is misleading but seedsetupfile contains the source strength of pollen
                            p.waitFor();
                            
                            rt = Runtime.getRuntime();
                            System.out.println(""+hourloop);
                            if((endhour - hourloop == 1 && dayloop == endday && monthloop == endmonth) || hourloop == 23){
                                p = rt.exec("java -jar generatepollensetupfile.jar "+yearloop+" "+monthloop+" "+dayloop+" "+hourloop+" "+monthloop+" "+dayloop+" "+(hourloop+1)+" "+s.latitude+" "+s.longitude+" "+s.density+" "+s.area);
                                if(alternate_flag)
                                    countdividerforconcentration++;
                                else
                                    alternate_flag = true;
                            }
                            else{
                                p = rt.exec("java -jar generatepollensetupfile.jar "+yearloop+" "+monthloop+" "+dayloop+" "+hourloop+" "+monthloop+" "+dayloop+" "+(hourloop+2)+" "+s.latitude+" "+s.longitude+" "+s.density+" "+s.area);
                                countdividerforconcentration++;
                            }
                            p.waitFor();
                            
                            //System.out.println("EMITIMES CREATED");
                                                        
                            //rt = Runtime.getRuntime();
                            p= rt.exec("hycs_std.exe");
                            //Thread.sleep(3000);
                            //p.waitFor();
                            InputStream is = p.getInputStream();
                            InputStreamReader isr = new InputStreamReader(is);
                            BufferedReader br = new BufferedReader(isr);
                            String line;

                            while ((line = br.readLine()) != null) {
                                System.out.println(line);
                                //Thread.sleep(1000);
                            }
                            br.close();
                            isr.close();
							// cd-2012.11.14: run an external application "con2asc.exe cdump"
                            //rt = Runtime.getRuntime();
                            p = rt.exec("con2asc.exe cdump");
                            //Thread.sleep(3000);
                            p.waitFor();

                            fr = new FileReader("CON2ASC.OUT");
                            textreader = new BufferedReader(fr);
                            String out_filename = textreader.readLine();
                            textreader.close();
                            fr.close();

                            char[] temp = out_filename.toCharArray();
                            out_filename = "";
                            int charcount = 0;
                            do{    
                                out_filename += temp[charcount];
                            }while(temp[++charcount] != ' ');

                            String data_filename = s.folder + "\\temp_data";

                            FileInputStream fis = new FileInputStream(out_filename);      
                            FileOutputStream fos = new FileOutputStream(data_filename);
                            byte[] buffer = new byte[1024];
                            int noOfBytes;
                            while ((noOfBytes = fis.read(buffer)) != -1) {
                                fos.write(buffer, 0, noOfBytes);
                            }
                            fos.close();
                            fis.close();
                            
                            summed_data_filename = s.folder + "\\summed_data_" + s.latitude + "_" + s.longitude;
                            
                            if(firstday == true){
                                firstday = false;
                                fis = new FileInputStream(out_filename);      
                                fos = new FileOutputStream(summed_data_filename);
                                buffer = new byte[1024];
                                while ((noOfBytes = fis.read(buffer)) != -1) {
                                    fos.write(buffer, 0, noOfBytes);
                                }
                                fos.close();
                                fis.close();
                            }
                            else{
                                s.calculate_total_concentration_and_deposition(summed_data_filename);
                            }
                            
                            delfile = new File(out_filename);
                            delfile.delete();
                           
                        }
                    }
                } 
            }
            delfile = new File("SETUP.CFG");
            delfile.delete();
            
            /////////////Calculate average concentration and save/////////////
            FileReader fr = new FileReader(summed_data_filename);
            BufferedReader textreader = new BufferedReader(fr);
            FileWriter fw = new FileWriter(s.folder + "\\conc_dep_data.txt");
            BufferedWriter textwriter = new BufferedWriter(fw);
            String readstring;
            String writestring;
            String[] writewords = new String[6];
            
            textreader.readLine();
            textwriter.write("DAY HR LAT    LON      DEP     CON      OUT\r\n");
            
            while((readstring = textreader.readLine()) != null){
            	writewords = readstring.split("\\s+");
                writewords[4] = "" + String.format("%6.2e",Double.valueOf(writewords[4]));
                double tempdoub = Double.valueOf(writewords[5]);
                tempdoub /= countdividerforconcentration;//(totaldays*(endhour - starthour)/2*Integer.parseInt(s.year_avg));
                writewords[5] = "" + String.format("%6.2e",tempdoub);
                
                writestring = "";
                for(int i = 0; i < 6; i++){
                    if(i == 5){
                        writestring += writewords[i];
                    }
                    else{
                        writestring += writewords[i] + " ";
                    }
                }
                
                double dd = Double.parseDouble(writewords[4])*s.outcrosscoefficient;
                dd = dd > 1 ? 1 : dd;
                textwriter.write(writestring+" "+String.format("%6.2e",dd));
                textwriter.write("\r\n");
            }
            
            textreader.close();
            textwriter.close();
            fr.close();
            fw.close();
            
        }
        catch (Exception e) {  
            System.err.println(e);
        }
    }
    
    String latitude = "48.0";
    String longitude = "-104.0";
    
    String folder = "sudeep";
    String year_avg = "1";
    
    String datatype = "";
    
    String st_mth = "";
    String st_day = "";
    String st_hr = "";
    
    String end_mth = "";
    String end_day = "";
    String end_hr = "";
    double outcrosscoefficient = 3*10e-8;
    double density;
    double area;
}

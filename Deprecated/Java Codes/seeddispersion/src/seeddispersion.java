import java.io.*;

public class seeddispersion {
    
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
            
            finalwriter.write("DAY HR   LAT     LON     DEP\r\n");
            
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
            seeddispersion s = new seeddispersion();
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
            
            s.density = Double.parseDouble(args[12]);
            s.area = Double.parseDouble(args[13]);
                
            String summed_data_filename = "";
            int startyear = 2013 - Integer.parseInt(s.year_avg);
            int startmonth = Integer.parseInt(s.st_mth);
            int startday = Integer.parseInt(s.st_day);
            int originalstartday = startday;
            int starthour = Integer.parseInt(s.st_hr);
            
            int endyear = 2012;
            int endmonth = Integer.parseInt(s.end_mth);
            int endday = Integer.parseInt(s.end_day);
            int endhour = Integer.parseInt(s.end_hr);
            
            FileWriter file_latlon = new FileWriter(s.folder + "\\latlong.txt");
            BufferedWriter bw_latlong = new BufferedWriter(file_latlon);
            String str_latlon_write = ""+s.latitude+", "+s.longitude+"\r\n";
            bw_latlong.write(str_latlon_write);
            bw_latlong.close();
            file_latlon.close();
            int totalhours;
            boolean firstyear = true;
            File delfile;
            Runtime rt = Runtime.getRuntime();
            Process proc;
            
            proc = rt.exec("cmd /c copy/b setupfile.cfg SETUP.CFG");
            proc.waitFor();
            
            for(int yearloop = startyear; yearloop <= endyear; yearloop++){
                totalhours = 0;
                String[] format = new String[3];
                boolean firsttimeflag = true;
                int days_list[] = new int[] {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
                if(yearloop%4 == 0)
                    days_list[1] = 28;
                               
                for(int monthloop = startmonth; monthloop <= endmonth; monthloop++){
                    if(monthloop != startmonth)
                        startday = 1;
                    else
                        startday = Integer.parseInt(s.st_day);
                    if(monthloop != endmonth)
                        endday = days_list[monthloop-1];
                    else
                        endday = Integer.parseInt(s.end_day);
                    for(int dayloop = startday; dayloop <= endday; dayloop++){
                        totalhours += 24;
                        
                        format[0] = "";
                        format[1] = "";
                        format[2] = "";
                        if(monthloop < 10)  format[0] = "0";
                        if(dayloop < 10)    format[1] = "0";
                        if(starthour < 10)  format[2] = "0";
                        System.out.println(""+yearloop+" "+format[0]+monthloop+" "+format[1]+dayloop);
                        
                        String filetocopy = "";
                        if("narr".equals(s.datatype))
                            filetocopy = args[11] + "\\NARR\\"+yearloop+"\\"+monthloop+"\\" + yearloop + "" + format[0] + "" + monthloop + "" + format[1] + dayloop + ".arl";
                        else
                            filetocopy = args[11] + "\\NAM\\"+yearloop+"\\"+monthloop+"\\" + yearloop + "" + format[0] + "" + monthloop + "" + format[1] + dayloop + "_nam12";
                        
                        if(firsttimeflag){
                            proc = rt.exec("cmd /c copy/b "+filetocopy+" "+s.folder+"\\"+"datafile.arl");
                            firsttimeflag = false;
                        }
                        else
                            proc = rt.exec("cmd /c copy/b "+s.folder+"\\"+"datafile.arl+"+filetocopy+" "+s.folder+"\\"+"datafile.arl");
                        proc.waitFor();
                    }
                } 
                totalhours = totalhours - starthour - (24 - endhour);
                delfile = new File("CONTROL");
                delfile.delete();
                
                rt = Runtime.getRuntime();
                Process p = rt.exec("java -jar generatesetupfile.jar "+yearloop+" "+startmonth+" "+startday+" "+starthour+" "+endmonth+" "+endday+" "+endhour+" "+s.latitude+" "+s.longitude+" "+s.density+" "+s.area);
                p.waitFor();

                FileReader fr = new FileReader("ref_control");
                BufferedReader textreader = new BufferedReader(fr);
                FileWriter fw = new FileWriter("CONTROL");
                BufferedWriter textwriter = new BufferedWriter(fw);
                String control_text;
                int linecount = 0;
                int repeat_source = 1;
                while((control_text = textreader.readLine()) != null){
                    linecount++;
                    if(linecount == 1){
                        System.out.println(originalstartday);
                        control_text = "" + yearloop;
                        char[] temp = control_text.toCharArray();
                        control_text = "";
                        control_text = temp[2] + "" + temp[3];

                        control_text += " " + format[0] + startmonth + " " + format[1] + originalstartday + " " + format[2] + starthour;
                        System.out.println(control_text);
                    }
                    if(linecount == 2){
                        FileReader frduration = new FileReader("EMITIMES");
                        BufferedReader brduration = new BufferedReader(frduration);
                        brduration.readLine();
                        brduration.readLine();
                        String duration[] = brduration.readLine().split(" ");
                        repeat_source = Integer.parseInt(duration[duration.length-2]);
                        control_text = ""+repeat_source;
                        brduration.close();
                        frduration.close();
                    }
                    if(linecount == 3){
                        control_text = s.latitude + " " + s.longitude + " " + "1.8";
                        while(repeat_source > 1){
                            textwriter.write(control_text + "\n");
                            repeat_source--;
                        }
                    }
                    if(linecount == 4){
                        control_text = ""+totalhours;
                    }
                    if(linecount == 8){
                        control_text = s.folder+"/";
                    }
                    if(linecount == 9){
                        control_text = "datafile.arl";
                    }
                    if(linecount == 13){
                        control_text = ""+totalhours;
                    }
                    if(linecount == 17){
                        BufferedWriter file_gridsize = new BufferedWriter(new FileWriter("gridsize.txt"));
                        if(totalhours <= 10){
                            control_text = "0.01 0.01";
                            file_gridsize.write("0.01\r\n");
                        }
                        else{
                            control_text = "0.05 0.05";
                            file_gridsize.write("0.05\r\n");
                        }
                        file_gridsize.close();
                    }
                    if(linecount == 18){
                        if(totalhours <= 40)
                            control_text = "20.0 20.0";
                        else
                            control_text = "180.0 360.0";
                    }
                    if(linecount == 25){
                        control_text = "0 "+totalhours+" 00";
                    }
                    textwriter.write(control_text + "\n");
                }
                textreader.close();
                textwriter.close();
                fr.close();
                fw.close();
                
                System.out.println("Running Hysplit");
                rt = Runtime.getRuntime();
                                            // cd-2012.11.14: run an external application "hycs_std.exe"
                p = rt.exec("hycs_std.exe");
                //Process p = rt.exec("cmd /c a.bat");
                
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
                //p.waitFor();

                System.out.println("Convert cdump to ASCII");
                                            // cd-2012.11.14: run an external application "con2asc.exe cdump"
                p = rt.exec("con2asc.exe cdump");
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

                if(firstyear == true){
                    firstyear = false;
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

                System.out.println("Preparing to delete unnecesary files");
                
                delfile = new File(out_filename);
                delfile.delete();
                
                delfile = new File(s.folder+"\\"+"datafile.arl");
                delfile.delete();
                
                System.out.println("Unnecessary files deleted");
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
            ///////////// Write the input information into the temp output file///////////
            File tempFile = new File(s.folder + "\\temp_output_data.txt");
            FileWriter fw1 = new FileWriter(tempFile);
            BufferedWriter textwriter1 = new BufferedWriter(fw1);
            textwriter1.write("====== The input information was as follows======\r\n");
            textwriter1.write("Start Date(Month/Day:Hour): " + s.st_mth + "/" + s.st_day + ":" + s.st_hr + "\r\n");
            textwriter1.write("End Date(Month/Day:Hour): " + s.end_mth + "/" + s.end_day + ":" + s.end_hr + "\r\n");
            textwriter1.write("How many years' average?: " + s.year_avg + "\r\n");
            textwriter1.write("Latitude: " + s.latitude + "      Longitude: " + s.longitude + "\r\n");
            textwriter1.write("Density(plants/acre): " + s.density + "      Area(acre): " + s.area + "      Data Type: " + s.datatype + "\r\n");
            textwriter1.write("======== The output data are as follows:=========\r\n");
            textwriter1.write("LAT: Latitude\r\n");
            textwriter1.write("LON: Longitude\r\n");
            textwriter1.write("DEP: Deposition(grains/m2)\r\n");
            textwriter1.write("CON: Concentration(grains/m3)\r\n");
            textwriter1.write("DAY HR LAT    LON      DEP     CON\r\n");
            ///////////// Write the output data into the file///////////
            textwriter.write("DAY HR LAT    LON      DEP     CON\r\n");
            
            while((readstring = textreader.readLine()) != null){
            	  writewords = readstring.split("\\s+");
            	
                double tempdoub = Double.valueOf(writewords[4]);
                tempdoub /= Integer.parseInt(s.year_avg);
                writewords[4] = "" + String.format("%6.2e",Double.valueOf(tempdoub));
                        
                tempdoub = Double.valueOf(writewords[5]);
                tempdoub /= Integer.parseInt(s.year_avg);
                writewords[5] = "" + String.format("%6.2e",Double.valueOf(tempdoub));
                
                writestring = "";
                for(int i = 0; i < 6; i++){
                    if(i == 5){
                        writestring += writewords[i];
                    }
                    else{
                        writestring += writewords[i] + " ";
                    }
                }
                
//                double dd = Double.parseDouble(writewords[4])*s.outcrosscoefficient;
//                dd = dd > 1 ? 1 : dd;
//                textwriter.write(writestring+"    "+dd);
                textwriter.write(writestring+"    ");
                textwriter.write("\r\n");
                textwriter1.write(writestring+"    " + "\r\n");
            }
            
            textreader.close();
            textwriter.close();
            textwriter1.close();
            fr.close();
            fw.close();
            fw1.close();
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
    
    String st_yr = "";
    String st_mth = "";
    String st_day = "";
    String st_hr = "";
    String end_yr = "";
    String end_mth = "";
    String end_day = "";
    String end_hr = "";
    double density;
    double area;
}

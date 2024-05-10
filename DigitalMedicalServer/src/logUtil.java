import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class logUtil {

	public void log(String s) {
		BufferedWriter bw=null;
		try {
			Date date=new Date();
			SimpleDateFormat fo=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("C:\\digitalMedicalServerFolder\\digitalMedicalServerLog.txt"), true), "UTF-8"));
			bw.newLine();
			bw.write("["+fo.format(date)+"] "+s);
			bw.close();
			System.out.println(s);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(bw!=null) {
				try{
					bw.close();
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}

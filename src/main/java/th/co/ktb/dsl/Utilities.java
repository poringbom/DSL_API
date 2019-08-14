package th.co.ktb.dsl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.core.io.Resource;

public class Utilities {
	public static Date parseDate(String d) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return sdf.parse(d);
		} catch (ParseException pe) {
			return Calendar.getInstance().getTime();
		}
	}
	
	public static byte[] resourceToByteArray(Resource r) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (InputStream is = r.getInputStream()){
	        byte[] buf = new byte[2048]; int cnt = 0;
	    		while ((cnt=is.read(buf)) > 0) {
	    			baos.write(buf,0,cnt);
	    		}
        }
        return baos.toByteArray();
	}
}

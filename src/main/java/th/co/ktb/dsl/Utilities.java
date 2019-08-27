package th.co.ktb.dsl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.core.io.Resource;

import com.fasterxml.jackson.databind.ObjectMapper;

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
	

    public static final String ROOT_PACKAGE_NAME = DSLApiApplication.class.getPackage().getName();
    public static Throwable filterStackTrace(Throwable t) {
    		DSLApiApplication.class.getPackage().getName();
		List<StackTraceElement> stList= new ArrayList<StackTraceElement>();
		if (t != null) {
			for(StackTraceElement s : t.getStackTrace()) {
				String clzName = s.getClassName();
				if (clzName.startsWith(ROOT_PACKAGE_NAME) && !clzName.contains("CGLIB$$")){
					stList.add(s);
				} 
			}
			t.setStackTrace(stList.toArray(new StackTraceElement[0]));
			if (t.getCause() != null) filterStackTrace(t.getCause());
		}	
		return t;
    }
    
    private static ObjectMapper objMapper;
    synchronized public static ObjectMapper getObjectMapper() {
    		if (objMapper == null) {
    			objMapper = new ObjectMapper();
//    			objMapper.enable(SerializationFeature.INDENT_OUTPUT);
    			return objMapper;
    		} else return objMapper;
    }
}

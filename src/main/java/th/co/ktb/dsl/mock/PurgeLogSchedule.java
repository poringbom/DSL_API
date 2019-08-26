package th.co.ktb.dsl.mock;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableScheduling
@Slf4j
public class PurgeLogSchedule {
	
	@Autowired MockResponseSQL mockResponse;
	
	@Scheduled(cron="0 30 12 * * ?")
	public void purgeData() {
	    log.info("Start Purge LogApiTesting");
	    Integer hr = 24;
	    Date current = Calendar.getInstance().getTime();
	    int count = -1, total = 0;
	    log.info("Delete LogApiTesting older than {} hr.",hr);
	    while (count != 0) {
		    total += count = mockResponse.deleteLogResponse(hr, current);
		    log.info("Delete {} records",count);
	    }
	    log.info("End Purge LogApiTesting (total delete: {})",total);
	    
	    count = -1; total = 0;
	    log.info("Delete UploadFile older than {} hr.",hr);
	    while (count != 0) {
		    total += count = mockResponse.deleteUpload(hr, current);
		    log.info("Delete {} records",count);
	    }
	    log.info("End Purge UploadFile (total delete: {})",total);
	}

}

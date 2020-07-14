package java142.todak.common;
import java.util.List;
import java.util.TimerTask;
import java142.todak.scheduler.controller.SchedulerController;
import java142.todak.scheduler.service.SchedulerService;
import java142.todak.scheduler.vo.CommuteVO;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class CommuteTimerTask extends TimerTask{
	
	Logger logger = Logger.getLogger(SchedulerController.class);
	
	@Autowired
	private SchedulerService schedulerService;
	
	@Autowired
	private PlatformTransactionManager ptm;
	DefaultTransactionDefinition dtd = null;
	TransactionStatus ts = null;

/*
	@Override
	public void run() {

		CommuteVO cvo = new CommuteVO();
		
		String hc_lasthour = "";
		String hc_totalhour = "";
		String hc_extraworking = "";
		
	
		hc_lasthour = "2400";
		hc_totalhour = "2400";
		
		Calendar cd = Calendar.getInstance();
		int today = cd.get(Calendar.DAY_OF_WEEK);
		logger.info("today >>> " + today); 
		
		boolean insertTAndAResult = false;
		try{
			logger.info("전체데이터넣는중");
			cvo.setToday(today);
			cvo.setHc_lasthour(hc_lasthour);
			cvo.setHc_totalhour(hc_totalhour);
			insertTAndAResult = schedulerService.insertTAndA(cvo);
		}catch(Exception e){
			
			e.printStackTrace();
			
		}
		logger.info("자동으로 입력이 성공했을가,,,? result >>> : " +  insertTAndAResult);
		logger.info("SchedulerController.insertTAndA 종료");
	}	

	*/
	
	@Override
	public void run() {
		CommuteVO cvo = new CommuteVO();
		cvo.setHm_empnum("H202002190006");
		List<CommuteVO> list = schedulerService.selectCheabun(cvo);
		VOPrintUtil.commuteVOPrint(list.get(0));
		
	}
	

}

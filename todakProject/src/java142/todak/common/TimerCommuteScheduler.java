package java142.todak.common;

import java.util.Calendar;
import java.util.Timer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


public class TimerCommuteScheduler {

	public TimerCommuteScheduler(){
		
		Timer timer = new Timer(false);
		Calendar date = Calendar.getInstance();
		date.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
		date.set(Calendar.AM_PM, Calendar.PM);
		date.set(Calendar.HOUR, 2);
		date.set(Calendar.MINUTE, 55);
		date.set(Calendar.SECOND, 50);
		date.set(Calendar.MILLISECOND, 0);
		
//		timer.schedule(new CommuteTimerTask(), , 24*60*60*100);
		timer.schedule(new CommuteTimerTask(), 1000);


	}
	
	public static void main(String[] args) {
	// TODO Auto-generated method stub
		new TimerCommuteScheduler();
	}
}


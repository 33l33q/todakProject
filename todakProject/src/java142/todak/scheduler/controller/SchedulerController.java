package java142.todak.scheduler.controller;

import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java142.todak.common.ChaebunUtils;
import java142.todak.common.VOPrintUtil;
import java142.todak.etc.utils.LoginSession;
import java142.todak.human.service.HumanHdateService;
import java142.todak.human.vo.HumanHdateVO;
import java142.todak.human.vo.HumanHolidayVO;
import java142.todak.scheduler.service.SchedulerService;
import java142.todak.scheduler.utils.JsonUtil;
import java142.todak.scheduler.vo.CommuteVO;
import java142.todak.scheduler.vo.SchedulerVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(value="/scheduler")
public class SchedulerController {
	Logger logger = Logger.getLogger(SchedulerController.class);
	
	@Autowired
	private SchedulerService schedulerService;
	
	@Autowired
	private HumanHdateService humanHdateService;
	
	private static final String GUBUN_SCHEDULER = "S";
	// 휴가사용 날짜기록 테이블
	private static final String GUBUN_HUMANHDATE = "T";
	
	/*--------------------------------------근태코드 시작 -------------------------------------*/
	
	/* 탄력근무제로 무조건 평일에 출근을 해야하며
	 * 주 40시간 근무 필수
	 * 출근은 하루 한 번만 가능,퇴근은 여러번 등록 가능함
	 * 최대 초과 근무 시간은 720시간으로 2400시간을 사용하면 초과근무시간이 깎이는 구조
	 * 스케줄러 어노테이션 사용이 불가해 특정 시간에 모든 사용자 정보를 넣는 것이 아니라
	 * 가장 먼저 출근한 사람이 출근 기록시에 모든 사용자 직원의 해당날짜 출퇴근 정보가 기록됨(배치)
	 * */
	
	@Autowired
	private PlatformTransactionManager ptm;
	DefaultTransactionDefinition dtd = null;
	TransactionStatus ts = null;
	
	
	//자동으로 출퇴근 기본정보가 저장
	@RequestMapping(value="/timeTest")
	public String boardTask(@ModelAttribute CommuteVO cvo, Model model){
		logger.info("(log)boardTask 진입 ");
		String message = "timeTest 다녀온다";
		System.out.println("boardTask");
		
		Calendar date = Calendar.getInstance();
		date.set(Calendar.HOUR_OF_DAY, 24);
		date.set(Calendar.MINUTE, 12);
		date.set(Calendar.SECOND, 31);
		date.set(Calendar.MILLISECOND, 0);
		
		Timer timer = new Timer(false);
		timer.schedule(new TimerCommuteTask(cvo), date.getTime(), 1000*60*60*24);

		model.addAttribute("message", message);
		
		logger.info("(log)boardTask 종료, 메인페이지로 돌아가기");
		return "/scheduler/commute";
	}
	
	class TimerCommuteTask extends TimerTask{ 
		
		CommuteVO cvo = null;
		boolean insertTAndAResult = false;
		TimerCommuteTask(CommuteVO cvo){
			this.cvo = cvo;
		}
		
		@Override
		public void run() {
			
			System.out.println("Time Task !");
		     try{
		    	 	insertTAndAResult = schedulerService.insertTAndA(cvo);
		     }catch(Exception e){
		    	 
		     };
		     	System.out.println(">>> : " + insertTAndAResult );
		}
	}	

			
		
	//출근등록
   @RequestMapping(value="/goWork")
   	public String goWork(@ModelAttribute CommuteVO cvo , Model model){
      logger.info("<<<<<<<<<<<<<<<<<<<<goWork 출근기록 함수 시작 >>>>>>>>>>>>>>>>>>>");

      String message = "";
      
      String hc_lasthour = ""; 
      String hc_totalhour = "";
      String hc_extraworking = "";
      
      /*주 5일를 기준으로한 한주의 총근 무 시간*/
      hc_lasthour = "2400"; //남은시간
      hc_totalhour = "2400"; //한 주 총 근무 시간
      
      //날짜 불러오기 -> 월요일일때와 다른 요일일때 들어가는 정보값이 다르기 때문에
      Calendar cd = Calendar.getInstance();
      int today = cd.get(Calendar.DAY_OF_WEEK);
      logger.info("today >>> " + today); 
      
      //트랜젝션처리
      boolean insertTAndAResult = false;
      boolean insertCommuteResult = false;
      boolean insertLastHourResult = false;
      
      dtd = new DefaultTransactionDefinition();
      dtd.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
      ts = ptm.getTransaction(dtd);
      
      try{
         
         List<CommuteVO> cheabunList = null;
         cheabunList = schedulerService.selectCheabun(cvo);//채번
         
         if(cheabunList.size() == 0){
            //default값 배치 함수로 넣어주기
            logger.info("전체데이터넣는중");
            cvo.setToday(today);
            cvo.setHc_lasthour(hc_lasthour);
            cvo.setHc_totalhour(hc_totalhour);
            insertTAndAResult = schedulerService.insertTAndA(cvo);
            cheabunList = schedulerService.selectCheabun(cvo);
         }
      
         String hc_comnum = cheabunList.get(0).getHc_comnum();
         cvo.setHc_comnum(hc_comnum);
   
         if(today != 2){
            logger.info("월요일이 아닌경우, 이전 남은시간 가져오기");
            List<CommuteVO> beforeHourList = null;
            beforeHourList = schedulerService.selectLastHour(cvo);
               if(beforeHourList.get(0) != null ){
                  //전날의 총 근무시간, 남은 근무시간, 추가 근무시간 가져옴
                  hc_lasthour = beforeHourList.get(0).getHc_lasthour();
                  hc_totalhour = beforeHourList.get(0).getHc_totalhour();
                  hc_extraworking = beforeHourList.get(0).getHc_extraworking();
                  logger.info(hc_lasthour +hc_totalhour +hc_extraworking);
                  
                  cvo.setHc_lasthour(hc_lasthour);
                  cvo.setHc_totalhour(hc_totalhour);
                  cvo.setHc_extraworking(hc_extraworking);
               
                  insertLastHourResult = schedulerService.insertLastHour(cvo);
         
               }else{
                  message = "지난 출근기록이 존재하지 않습니다. 관리자에게 문의하세요";
                  model.addAttribute("message", message);
                  return "scheduler/goWork";
               }
               
            }else{
            	logger.info("월요일인 경우 값 넣어주기");
               cvo.setHc_lasthour(hc_lasthour);
               cvo.setHc_totalhour(hc_totalhour);
               cvo.setHc_extraworking("0");
            
            }
         
         insertCommuteResult = schedulerService.insertCommute(cvo);
   
         if(insertCommuteResult){
            message = "출근등록 완료~!.";
            List<CommuteVO> selectCommute = null; 
            selectCommute = schedulerService.selectCommute(cvo);
            model.addAttribute("selectCommute", selectCommute);
         }else{
            message = "출근은 하루 한 번만 등록가능합니다.";
         }                                             
         
         ptm.commit(ts);
         
      }catch(Exception e){
         
         ptm.rollback(ts);
         message = "출근 등록 오류 발생 ";
      }
      
      model.addAttribute("message", message);
      
      logger.info("<<<<<<<<<<<<<<<<<<<<goWork 출근기록 함수 종료 >>>>>>>>>>>>>>>>>>>");
      return "scheduler/goWork";
   }


	//퇴근등록
	@RequestMapping(value="/goHome")
	public String goHome(@ModelAttribute CommuteVO cvo , Model model){
		logger.info("<<<<<<<<<<<<<<<<<<<<goHome 퇴근기록 함수 시작 >>>>>>>>>>>>>>>>>>>");
		String message = "";
		String lastHourMessage = "";
		
		Calendar cd = Calendar.getInstance();
		int today = cd.get(Calendar.DAY_OF_WEEK);
		logger.info("오늘 요일은 : today >>> " + today); 
		
		List<CommuteVO> cheabunList = null;
		cheabunList = schedulerService.selectCheabun(cvo);			
		String hc_comnum = cheabunList.get(0).getHc_comnum();
		if(hc_comnum  != null){
			cvo.setHc_comnum(hc_comnum);
	
			boolean updateCommuteResult = false;
			boolean insertExtraworkResult = false;
			
			dtd = new DefaultTransactionDefinition();
			dtd.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
			ts = ptm.getTransaction(dtd);
			
			try {
				updateCommuteResult = schedulerService.updateCommute(cvo);
				
				String hc_weekhour = null;
				
				if(updateCommuteResult){
					message = "퇴근등록완료~!.";
					List<CommuteVO> selectCommute = null; 
					selectCommute = schedulerService.selectCommute(cvo);
					
					//주간시간 계산하려고 오늘 근무한 시간들 다 받아옴
					int totalHour = (int)Double.parseDouble(selectCommute.get(0).getHc_totalhour());
					int todayWorkHour  = (int)Double.parseDouble(selectCommute.get(0).getHc_dayhour());
					cvo.setHc_dayhour(Integer.toString(todayWorkHour));
					logger.info("오늘 근무한 시간 확인하기~~~ >> todayWorkHour" + todayWorkHour);
		
					//월요일이 아닌경우에만~! --> 월요일은 지난 초과근무시간이 존재하지 않기 때문에
					if(today != 2){
					
						//어제까지의 초과근무시간 구해오기
						int beforeExtraworking = 0;
						List<CommuteVO> beforeHourList = null;
						beforeHourList = schedulerService.selectLastHour(cvo);
						String result = beforeHourList.get(0).getHc_extraworking();
						if(result != null){
							beforeExtraworking = Integer.parseInt(beforeHourList.get(0).getHc_extraworking());
							logger.info("이전까지 초과근무 시간 " + beforeExtraworking);
						}
						
						String beforeHc_lasthour = beforeHourList.get(0).getHc_lasthour();
						logger.info("hc_lasthour >>> : " + beforeHc_lasthour);
						int beforeLastHour = (int)Double.parseDouble(beforeHc_lasthour);
						int todayLastHour = (int)Double.parseDouble(selectCommute.get(0).getHc_lasthour()); //오늘자 기준 남은 근무시간
						logger.info("lastHour >>> : " + beforeLastHour);
						
						
						//근무시간 전부 소진 전에~~~
						if(beforeLastHour > 0 ){
							int weekHour = totalHour - beforeLastHour + todayWorkHour;
							logger.info(weekHour);
							hc_weekhour = Integer.toString(weekHour);
							logger.info("hc_weekhour >>> : " + hc_weekhour);
							cvo.setHc_weekhour(hc_weekhour);
							todayLastHour = totalHour - weekHour;
							cvo.setHc_lasthour(Integer.toString(todayLastHour));
							schedulerService.insertExtrawork(cvo);
							selectCommute = schedulerService.selectCommute(cvo);
						}
			
						   logger.info("초과근무 확인하기 ~~~~ >>> beforeLastHour : " + beforeLastHour + ", todatLastHour : " + todayLastHour + ", beforeExtraworking : " +beforeExtraworking);
						   //근무시간 소진 시 초과근무로 돌리기~~~
							if((beforeLastHour == 0 || todayLastHour == 0 )&& beforeExtraworking == 0){
								logger.info("기본 근무 시간을 달성한 경우 >>>");
								lastHourMessage = "기본 근무 시간을 달성하셨습니다.";
								cvo.setHc_weekhour(Integer.toString(totalHour));
								model.addAttribute("lastHourMessage", lastHourMessage);
							
							}else if(beforeLastHour <= -720 || todayLastHour <= -720 || beforeExtraworking >= 720){
								logger.info("최대 근무 시간을 초과한 경우 >>>");
								cvo.setHc_extraworking("720");
								cvo.setHc_lasthour("0000");
								VOPrintUtil.commuteVOPrint(cvo);
								
								int weekHour = totalHour + 720;
								cvo.setHc_weekhour(Integer.toString(weekHour));
								schedulerService.insertExtrawork(cvo);
								
								insertExtraworkResult = schedulerService.insertExtrawork(cvo);
								if(insertExtraworkResult){
									lastHourMessage = "최대 근무 시간을 초과했습니다.";
									model.addAttribute("lastHourMessage", lastHourMessage);
								}
								
							}else if(beforeLastHour < 0 || todayLastHour < 0){
								logger.info("기본 근무 시간을 초과한 경우 >>>");
								int hc_extraworking = 0;
								
								cvo.setHc_lasthour("0000");
								hc_extraworking = todayWorkHour;
								
								logger.info("todayWorkHour  : " + todayWorkHour);
								
								logger.info("총 초과근무 시간  : " + hc_extraworking);
								hc_extraworking = hc_extraworking + beforeExtraworking;
								cvo.setHc_extraworking(Integer.toString(hc_extraworking));
								
								int weekHour = hc_extraworking + totalHour;
								hc_weekhour = Integer.toString(weekHour);
								cvo.setHc_weekhour(hc_weekhour);
									
								insertExtraworkResult = schedulerService.insertExtrawork(cvo);
								if(insertExtraworkResult){
									
									lastHourMessage = "기본 근무 시간을 " + hc_extraworking / 60  +"시간 "+ hc_extraworking % 60 +"분 초과했습니다";
									model.addAttribute("lastHourMessage", lastHourMessage);
								}
							}
						}else if(today == 2){
			                  //월요일인 경우에~~!
			                  cvo.setHc_weekhour(Integer.toString(todayWorkHour));
			                  cvo.setHc_lasthour(Integer.toString(totalHour - todayWorkHour));
			                  schedulerService.insertExtrawork(cvo);
		
			               }
					
					//수정한 정보 넣어서 다시 출력하기
		            selectCommute = schedulerService.selectCommute(cvo);
					
					model.addAttribute("selectCommute", selectCommute);
					VOPrintUtil.commuteVOPrint(selectCommute.get(0));
				}else{
					message = "출근 기록이 존재하지않습니다";
				}
				
				ptm.commit(ts);
			}catch(Exception e){
				ptm.rollback(ts);
				
				e.printStackTrace();
				message = "퇴근 기록 실패";
			}
		}else{
			message = "출근 기록이 존재하지않습니다";
		}
		model.addAttribute("message", message);
		logger.info("<<<<<<<<<<<<<<<<<<<<goHome 퇴근기록 함수 종료 >>>>>>>>>>>>>>>>>>>");
		return "scheduler/goWork";
	}
	
	
	/*--------------------------------------근태코드 끝-------------------------------------*/
	
	@ResponseBody
	@RequestMapping(value="/selectSchedule",method=RequestMethod.POST )
	public JSONArray selectSchedule(HttpSession session, HttpServletRequest req, SchedulerVO svo ,String util, Model model){
		logger.info("selectSchedule 진입 성공. <<<<<<<<< ");
		LoginSession selectSession = (LoginSession)req.getSession().getAttribute("login");
		String result = selectSession.getUserID(session.getId());
		svo.setHm_empnum(result);
		JSONArray jsonArray = null;
		List<SchedulerVO> list = null;
		list = schedulerService.selectSchedulerWorkSchedule(svo);

		List<HumanHolidayVO> hList = null;
		HumanHdateVO hvo = new HumanHdateVO();
		hvo.setHm_empnum(result);
		hList = humanHdateService.selectHumanHoliday(hvo);
		for (HumanHolidayVO humanHolidayVO : hList) {
		}
		
		try {				
				jsonArray = JsonUtil.jsonSelectSchedule(list,result);
		} catch (Exception e) {
			e.printStackTrace();
			//System.out.println(" [Controller] select Error >>>> : " + e.getMessage());
			
		}
		System.out.println(" jsonArray  >>>> : " + jsonArray);		
		logger.info("selectSchedule 탈출 성공. >>>>>>>>> ");

		return jsonArray;
		
	}// end of selectScheduler()
	
	// 일정표 페이지로 이동
		@RequestMapping(value="moveScheduler")
		public String moveSchedyler(){
			return "scheduler/selectSchedule";
		}
	
	@ResponseBody
	@RequestMapping(value="/insertSchedule",method=RequestMethod.POST)
	public JSONArray insertSchedule(HttpSession session, HttpServletRequest req,HumanHdateVO hvo,@ModelAttribute SchedulerVO svo){
		logger.info("insertSchedule진입 성공. <<<<<<<<< ");
	
		LoginSession insertSession = (LoginSession)req.getSession().getAttribute("login");
		String insertResult = insertSession.getUserID(session.getId());
		svo.setHm_empnum(insertResult);
	
		String bool = svo.getSw_repetitiontype();
		if(!bool.equals("true")){
			bool = "false";
		}
		svo.setSw_repetitiontype(bool);
		JSONArray jsonArray = null;		
		List<SchedulerVO> list = null; 
			
		// 채번하기  ----------------------------
		list = schedulerService.chaebunSchedulerWorkSchedule();
		//System.out.println("[CONTROLLER] list >>>>>>> : " + list);
		svo.setSw_num(ChaebunUtils.cNum(list.get(0).getSw_num(),GUBUN_SCHEDULER));
		// ----------------------------------  
		String test = svo.getSw_num();
		String hmemp = svo.getHm_empnum();
		//System.out.println("svo.getSw_num >>>>>>>>> : " + test);
		int result = schedulerService.insertSchedulerWorkSchedule(svo);
		
		//---------------------HUMAN_HDATE TABLE ------------------------------
		// 휴가 관련 insert
		// 시작 날짜 , 끝날짜 주입
		String start = svo.getSw_startdate();		
		hvo.setHhd_sholidaycode(start);

		String end = svo.getSw_enddate();
		hvo.setHhd_eholidaycode(end);

		// 코드값 주입
		String codeNum = svo.getSw_type();
		hvo.setHhd_holidaycode(codeNum);

		// hm_empnum 가져오기
		String hm_emp = svo.getHm_empnum(); 
		hvo.setHm_empnum(hm_emp);
		
		List<HumanHdateVO> hList = null;
		hList = humanHdateService.chaebunHumanhdate();
		hvo.setHhd_tablenum(ChaebunUtils.cNum(hList.get(0).getHhd_tablenum(),GUBUN_HUMANHDATE));
		
		int hResult = humanHdateService.insertHumanhdate(hvo);

		// 휴가 관련 insert 종료  		
		//---------------------HUMAN_HDATE TABLE ------------------------------
		
		logger.info("insertSchedule탈출 성공. >>>>>>>>> "); 
		return jsonArray;
	}// end of insertScheduler()
	
	@ResponseBody
	@RequestMapping(value="/updateSchedule",method=RequestMethod.POST)
	public JSONArray updateSchedule(HttpSession session, HttpServletRequest req,@ModelAttribute SchedulerVO svo){
		logger.info("updateSchedule진입 성공. <<<<<<<<< ");
		
		LoginSession updateSession = (LoginSession)req.getSession().getAttribute("login");
		String updateResult = updateSession.getUserID(session.getId());
		svo.setHm_empnum(updateResult);
		
		int result = 0;
		result =schedulerService.updateSchedulerWorkSchedule(svo);
		
		if(result ==0){
			//System.out.println("[CONTROLLER] update 실패!!!");		
		}else{
			//System.out.println("[CONTROLLER] update 성공!!!");
		}
		
		logger.info("updateSchedule탈출 성공. >>>>>>>>> ");
		return null;
	}// end of updateScheduler()
	
	@ResponseBody
	@RequestMapping(value="/deleteSchedule",method=RequestMethod.POST)
	public JSONArray deleteSchedule(HttpSession session, HttpServletRequest req,@ModelAttribute SchedulerVO svo){
		logger.info("deleteSchedule진입 성공. <<<<<<<<< ");
		
		LoginSession deleteSession = (LoginSession)req.getSession().getAttribute("login");
		String deleteResult = deleteSession.getUserID(session.getId());
		svo.setHm_empnum(deleteResult);

		int result = 0;
		result = schedulerService.deleteSchedulerWorkSchedule(svo);
		
		if(result ==0){
			//System.out.println("[CONTROLLER] delete 실패 !!!");
			
		}else{
			//System.out.println("[CONTROLLER] delete 성공 !!! ");
		}
		logger.info("deleteSchedule탈출 성공. >>>>>>>>> ");
		return null;
	}// end of deleteScheduler() 
	
	@ResponseBody
	@RequestMapping(value="/selectHolidayCount",method=RequestMethod.POST)
	public JSONObject selectHolidayCount(HttpSession session, HttpServletRequest req){
		
		LoginSession selectSession = (LoginSession)req.getSession().getAttribute("login");
		String result = selectSession.getUserID(session.getId());
		
		List<HumanHolidayVO> hList = null;
		HumanHdateVO hvo = new HumanHdateVO();
		hvo.setHm_empnum(result);
		hList = humanHdateService.selectHumanHoliday(hvo);
		JSONObject jObj = new JSONObject();
		for (HumanHolidayVO humanHolidayVO : hList) {
			humanHolidayVO.setHmEmpnum(hvo.getHm_empnum());
			jObj.put("empnum", humanHolidayVO.getHmEmpnum());
			jObj.put("regResult", humanHolidayVO.getRegResult());
			jObj.put("useResult", humanHolidayVO.getUseResult());
			jObj.put("hoResult", humanHolidayVO.getHoResult());
		}
		return jObj;
	}// end of selectholiday() �쑕媛� 議고쉶 

}// end of SchedulerController class

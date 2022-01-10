package com.pgmate.app.ctl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.pgmate.app.dao.CPDAO;
import com.pgmate.app.dao.TaskDAO;
import com.pgmate.app.util.SessionUtil;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;

@Controller
public class TaskController {
	
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.ctl.TaskController.class );
	
	@RequestMapping(value = "/system/task/view", method = RequestMethod.GET)
    public ModelAndView view(HttpServletRequest request) {
		TaskDAO taskDAO = new TaskDAO();
		List<SharedMap<String,Object>> resList = new TaskDAO().getList(SessionUtil.getUserId(request), SessionUtil.get(request).getGrade()).getRows();
		resList = taskDAO.replaceDate(resList);
		request.setAttribute("DATAMAP", resList);
		return new ModelAndView("/system/task/view");
    }
	
	@RequestMapping(value = {"/system/task/insert"}, method = RequestMethod.POST)
    public @ResponseBody SharedMap<String,Object> insert(HttpServletRequest request, 
    		@RequestParam String task, 
    		@RequestParam(value="grade", defaultValue = "") String grade, 
    		@RequestParam(value="targetId", defaultValue = "") String targetId) {
		CPDAO cpDAO = new CPDAO();
		SharedMap<String,Object> resMap = new SharedMap<String,Object>();
		if(targetId.length() <= 0) {
			grade = "본사";
		}
		
		if(cpDAO.insert("INSERT INTO PG_TASK SET task='"+task+"',targetId='"+targetId+"',targetGrade='"+grade+"',"
				+ "regId='"+SessionUtil.getUserId(request)+"',regDay='"+CommonUtil.getCurrentDate("yyyyMMdd")+"'")) {
			resMap.put("result", "OK");
		} else {
			resMap.put("result", "Fail");
			resMap.put("MSG", "DB 입력 실패");
		}
		
		return resMap;
    }
	
	@RequestMapping(value = {"/system/task/remove"}, method = RequestMethod.POST)
    public @ResponseBody SharedMap<String,Object> remove(HttpServletRequest request, @RequestParam String idx) {
		CPDAO cpDAO = new CPDAO();
		SharedMap<String,Object> resMap = new SharedMap<String,Object>();
		
		if(cpDAO.update("UPDATE PG_TASK SET status= '삭제' WHERE idx='"+idx+"' AND regId='"+SessionUtil.getUserId(request)+"'")) {
			resMap.put("result", "OK");
		} else {
			resMap.put("result", "Fail");
			resMap.put("MSG", "DB 입력 실패");
		}
		
		return resMap;
    }
}

package com.pgmate.app.ctl;

import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import com.pgmate.app.model.ajax.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.pgmate.app.dao.CPDAO;
import com.pgmate.app.dao.NoticeDAO;
import com.pgmate.app.model.ajax.CPRequest;
import com.pgmate.app.model.ajax.CPResponse;
import com.pgmate.app.session.CPSession;
import com.pgmate.app.util.CPRUtil;
import com.pgmate.app.util.CPUtil;
import com.pgmate.app.util.InfoBankSMS;
import com.pgmate.app.util.SessionUtil;
import com.pgmate.app.util.WebCache;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.sms.SmsUtil;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;

@Controller
public class NoticeController {

	private static Logger logger = LoggerFactory.getLogger(com.pgmate.app.ctl.NoticeController.class);

	@RequestMapping(value = { "/system/notice/form" })
	public ModelAndView form(HttpServletRequest request) {
		return new ModelAndView("/system/notice/form");
	}

	@RequestMapping(value = { "/system/notice/add" })
	public ModelAndView add(HttpServletRequest request) {
		return new ModelAndView("/system/notice/add");
	}

	@RequestMapping(value = "/system/notice/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView list(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		NoticeDAO noticeDAO = new NoticeDAO();
		CPSession session = SessionUtil.get(request);
		if (!(session.getGrade().equals("본사") && !session.getRole().equals("일반"))) {
			cpRequest.setData("status", "개시", "eq", "", true);
			cpRequest.setData("pubDay", CommonUtil.getCurrentDate("yyyyMMdd"), "le", "", true);
			cpRequest.setData("closeDay", CommonUtil.getCurrentDate("yyyyMMdd"), "ge", "", true);
//			if(!CommonUtil.isNullOrSpace(session.getDistId())) {
//				cpRequest.setData("parentId", "'" + session.getDistId() + "', ''", "in", "", true);
//			}
//			cpRequest.setData("grade", "'" + session.getGrade() + "', ''", "in", "", true);
		}

		RecordSet rset = noticeDAO.list(cpRequest.data, cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset, noticeDAO).setView(request, "/system/notice/list", "");
	}

	@RequestMapping(value = "/system/notice/view/{idx}", method = RequestMethod.GET)
	public ModelAndView view(HttpServletRequest request, @PathVariable String idx) {
		SharedMap<String, Object> map = new NoticeDAO().getById(idx).getRowFirst();
		map.put("summary", CommonUtil.unescapeHtml(map.getString("summary")));

		CPSession cpSession =  SessionUtil.get(request);
		if(map.getString("status").equals("미개시") && !cpSession.getGrade().equals("본사")) {
			map.clear();
		}

		if(map.getString("status").equals("미개시") && !cpSession.getUserId().equals(map.getString("regId"))) {
			map.clear();
		}

		request.setAttribute("DATAMAP", map);
		return new ModelAndView("/system/notice/view");
	}

	@RequestMapping(value = "/system/notice/modal/{idx}", method = RequestMethod.GET)
	public ModelAndView modal(HttpServletRequest request, @PathVariable String idx) {
		SharedMap<String, Object> map = new NoticeDAO().getById(idx).getRowFirst();
		map.put("summary", CommonUtil.unescapeHtml(map.getString("summary")));
		request.setAttribute("DATAMAP", map);
		return new ModelAndView("/system/notice/modal");
	}

	@RequestMapping(value = "/system/notice/modify/{idx}", method = RequestMethod.GET)
	public ModelAndView modify(HttpServletRequest request, @PathVariable String idx) {
		SharedMap<String, Object> map = new NoticeDAO().getById(idx).getRowFirst();
		map.put("summary", CommonUtil.unescapeHtml(map.getString("summary")));
		request.setAttribute("DATAMAP", map);
		return new ModelAndView("/system/notice/modify");
	}

	@RequestMapping(value = {
			"/system/notice/insert" }, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CPResponse insert(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();
		cpRequest.setData("parentId", SessionUtil.get(request).getDistId());

		if (cpDAO.insert("PG_NOTICE", SessionUtil.getUserId(request), cpRequest.data)) {
			SessionUtil.initSessionData(request);
			return new CPRUtil(cpRequest).resultOK(CPUtil.RESULT_DATA_INSERTED).redirect(cpRequest.redirect)
					.cpResponse();
		} else {
			return new CPRUtil(cpRequest).resultNOK(CPUtil.RESULT_DATA_INFAIL, cpDAO.getError()).cpResponse();
		}
	}

	@RequestMapping(value = {
			"/system/notice/update" }, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CPResponse update(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();

		String idx = cpRequest.getKeyValue("idx");

		SharedMap<String, Object> map = new NoticeDAO().getById(idx).getRowFirst();
		String regId = map.getString("regId");

		String reqId = SessionUtil.getUserId(request);

		if(!reqId.equals(regId)) {
			return new CPRUtil(cpRequest).resultNOK("작성자가 아닙니다.", cpDAO.getError()).cpResponse();
		}

		if (cpDAO.update("PG_NOTICE", SessionUtil.getUserId(request), cpRequest.data)) {
			return new CPRUtil(cpRequest).resultOK("공지사항 정보가 변경되었습니다.").cpResponse();
		} else {
			return new CPRUtil(cpRequest).resultNOK("공지사항 정보 변경에 실패하였습니다.", cpDAO.getError()).cpResponse();
		}
	}

	// ======================================================== 공지사항 전송
	/*
	 * 공지사항 전송 폼
	 */
	@RequestMapping(value = { "/system/send/add" })
	public ModelAndView sendAdd(HttpServletRequest request) {
		return new ModelAndView("/system/send/add");
	}
	
	@RequestMapping(value = { "/system/send/form" })
	public ModelAndView sendForm(HttpServletRequest request) {
		return new ModelAndView("/system/send/form");
	}
	
	/*
	 * 공지사항 전송 내역 조회
	 */
	@RequestMapping(value = "/system/send/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView sendList(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		NoticeDAO noticeDAO = new NoticeDAO();
		
		/*SharedMap<String, Object> res = new SharedMap<String, Object>();
		res = noticeDAO.sendListMcht().getRow(0);
		request.setAttribute("DATAMAP", res);*/
		
		RecordSet rset = noticeDAO.sendList(cpRequest.data, cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset, noticeDAO).setView(request, "/system/send/list", "");
	}
	
	/*
	 * 공지사항 이메일/SMS 전송
	 */
	@RequestMapping(value = {"/system/send/sendForm" }, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CPResponse sendTo(HttpServletRequest request, @RequestBody CPRequest cpRequest) throws Exception {
		String parentId = cpRequest.getValue("parentId");
		String grade = cpRequest.getValue("grade");
		String content = cpRequest.getValue("summary");
		String email = cpRequest.getValue("email");
		String phone = cpRequest.getValue("phone");
		String selectSend = cpRequest.getValue("selectSend");
		String getAll = cpRequest.getValue("all");
		
		String allId = "";
		String getGrade = ""; //전송 대상 소속
		String getEmail = ""; // 전송 이메일
		String getTel1 = ""; // 전송 휴대전화 번호
		String getTel2 = ""; // 전송 휴대전화 번호2
		String setTel1 = "";
		String setTel2 = "";

		NoticeDAO dao = new NoticeDAO();
		SharedMap<String, Object> map = new SharedMap<String, Object>();
		SharedMap<String, Object> recipient = new SharedMap<String, Object>();
		
		if (selectSend == "partSend" || "partSend".equals(selectSend)) {
			recipient = dao.checkId(parentId, grade);
			getGrade = recipient.getString("grade");
			getEmail = recipient.getString("email");
			getTel1 = recipient.getString("tel1");
			getTel2 = recipient.getString("tel2");
		} else if (selectSend == "inputSend" || "inputSend".equals(selectSend)) {
			getGrade = "개별전송";
			getEmail = email;
			getTel1 = phone;
		}

		//전체 대상의 경우 PG_NOTICE_LIST 테이블의 데이터대로 전송한다.
		if (selectSend == "allSend" || "allSend".equals(selectSend)) {

			if (getAll == "distAll" || "distAll".equals(getAll)) {
				allId = "대행사";
			} else if (getAll == "agencyAll" || "agencyAll".equals(getAll)) {
				allId = "에이전시";
			} else if (getAll == "salesAll" || "salesAll".equals(getAll)) {
				allId = "지사";
			} else if (getAll == "mchtAll" || "mchtAll".equals(getAll)) {
				allId = "가맹점";
			}

			List<SharedMap<String, Object>> recipientList = dao.checkIdAll(allId);

			for (SharedMap<String, Object> list : recipientList) {
				getEmail = list.getString("email");
				getTel1 = list.getString("tel1");
				getTel2 = list.getString("tel2");

				if(getTel1 == getTel2 || getTel1.equals(getTel2)) {
					setTel1 = getTel1;
					setTel2 = "";
				}else {
					setTel1 = getTel1;
					setTel2 = getTel2;
				}
				
				map.put("grade", allId);
				map.put("content", content);
				map.put("regId", SessionUtil.getUserId(request));
				
				if("".equals(getEmail) || getEmail == null) {
				} else {
					if(sendEmail(content.replaceAll("\n", "<br>"), getEmail)) {
						map.put("tel1", "");
						map.put("tel2", "");
						map.put("email", getEmail);
						dao.insertNotice(map);
					} else {
						logger.info("공지사항 이메일 전송 실패 : {}", getEmail);
					}
				}
				if(setTel1.equals("") || setTel1 == null) {
				} else {
					if(sendSMS(content, setTel1)) {
						map.put("email", "");
						map.put("tel2", "");
						map.put("tel1", setTel1);
						dao.insertNotice(map);
					} else {
						logger.info("공지사항 SMS tel1 전송 실패 : {}", setTel1);
					}
				}
				if(setTel2.equals("") || setTel2 == null) {
				} else {
					if(sendSMS(content, setTel2)) {
						map.put("email", "");
						map.put("tel1", "");
						map.put("tel2", setTel2);
						dao.insertNotice(map);
					} else {
						logger.info("공지사항 SMS tel2 전송 실패 : {}", setTel2);
					}
				}
			}
		} else {
			getTel1 = getTel1.replaceAll("-", "");
			getTel2 = getTel2.replaceAll("-", "");
			
			if(getTel1 == getTel2 || getTel1.equals(getTel2)) {
				setTel1 = getTel1;
				setTel2 = "";
			}else {
				setTel1 = getTel1;
				setTel2 = getTel2;
			}
			
			map.put("grade", getGrade);
			map.put("content", content);
			map.put("regId", SessionUtil.getUserId(request));
			
			if("".equals(getEmail) || getEmail == null) {
			} else {
				if(sendEmail(content.replaceAll("\n", "<br>"), getEmail)) {
					map.put("tel1", "");
					map.put("tel2", "");
					map.put("email", getEmail);
					dao.insertNotice(map);
				} else {
					logger.info("공지사항 이메일 전송 실패 : {}", getEmail);
				}
			}
			if(setTel1.equals("") || setTel1 == null) {
			} else {
				if(sendSMS(content, setTel1)) {
					map.put("email", "");
					map.put("tel2", "");
					map.put("tel1", setTel1);
					dao.insertNotice(map);
				} else {
					logger.info("공지사항 SMS tel1 전송 실패 : {}", setTel1);
				}
			}
			if(setTel2.equals("") || setTel2 == null) {
			} else {
				if(sendSMS(content, setTel2)) {
					map.put("email", "");
					map.put("tel1", "");
					map.put("tel2", setTel2);
					dao.insertNotice(map);
				} else {
					logger.info("공지사항 SMS tel2 전송 실패 : {}", setTel2);
				}
			}
		}
		return new CPRUtil(cpRequest).resultOK().cpResponse();
	}
	
	/*
	 * 텍스트 SMS 전송
	 */
	public boolean sendSMS(String msg, String setTel) throws Exception {
		WebCache wc = new WebCache();
		String number = String.format("%1$" + 6 + "s", ((int) (Math.random() * 999999) + 1)).replace(' ', '0');
		wc.setSMSKey(setTel, number);

		InfoBankSMS infoBankSMS = new InfoBankSMS();
		String msgBody = "[(주)건흥페이먼츠] " + msg + "";

		try {
			//infoBankSMS.sendSms(InfoBankSMS.LMS_URL, setTel.replaceAll("\\[^0-9]+", ""), msgBody);
			SmsUtil.sendSms(SmsUtil.LMS_URL, setTel.replaceAll("\\[^0-9]+", ""), msgBody);
			logger.debug("NoticeSend SMS SEND");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			logger.error("공지사항 SMS 전송 중 오류발생. 확인요망 [" + e.getMessage() + "]");
			
			return false;
		}
		return true;
	}

	/*
	 * 텍스트 EMAIL 전송
	 */
	public boolean sendEmail(String msg, String setEmail) throws AddressException, MessagingException {
		logger.info("---NoticeSend Email START---");
		
		String host = "smtp.gmail.com";
		String port = "465";

		String fromId = "ghpay@ghpayments.co.kr"; // 발신자
		String fromPw = "bk2763!@#"; // 발신자 비밀번호
		String fromName = "[(주)건흥페이먼츠]"; // 발신자 정보
		String to = setEmail; // 수신자
		String subject = "[(주)건흥페이먼츠]공지사항 안내"; // 이메일 제목

		try {
			Properties props = System.getProperties();

			// SMTP 서버 정보 설정
			props.put("mail.transport.protocol", "smtp");
			props.put("mail.smtp.host", host);
			props.put("mail.smtp.port", port);
			props.put("mail.smtp.auth", "true");

			props.put("mail.smtp.ssl.enable", "true");
			props.put("mail.smtp.ssl.trust", host);

			// 발신자 메일 서버 인증
			Authenticator auth = new Authenticator() {
				public PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(fromId, fromPw);
				}
			};
			// 메일 세션 생성
			Session session = Session.getInstance(props, auth);
			session.setDebug(true);

			// 메일 송/수신 옵션 설정
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(fromId, fromName)); // 발신자
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(to)); // 수신자
			message.setSubject(subject);
			message.setContent(msg, "text/html; charset=UTF-8");
			
			Transport.send(message);
			logger.info("---NoticeSend Email END---");
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			logger.error("공지사항 이메일 전송 중 오류발생. 확인요망 [" + e.getMessage() + "]");
			
			return false;
		}
		return true;
	}
	// ======================================================== 공지사항 전송
}

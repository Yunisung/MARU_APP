package com.pgmate.app.ctl;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.pgmate.app.dao.CPDAO;
import com.pgmate.app.dao.EformDAO;
import com.pgmate.app.model.ajax.CPRequest;
import com.pgmate.app.model.ajax.CPResponse;
import com.pgmate.app.session.CPSession;
import com.pgmate.app.util.CPRUtil;
import com.pgmate.app.util.SessionUtil;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.map.SharedMap;

/*
 * eform/auth	: 관리자 권한
 * eform/member	: 업체 로그인시 해당 업체 정보 조회
 */
@Controller
public class EformController {
	private static Logger logger = LoggerFactory.getLogger(com.pgmate.app.ctl.EformController.class);

	private String apiID = "";
	private String apiKEY = "";

	//발급받은 id, key를 이용해서 액세스 토큰 발급 (10분간 유효, config 파일로 관리)
	@RequestMapping(value = "/eform/eform_token")
	public String eform_token() throws Exception {
		configSetting();

		String apiUrl = "https://api.eform.io/v2/token";
		String setMethod = "GET";
		String id = apiID;

		JSONObject resJson = connectionRes(apiUrl, "", setMethod, id, "");

		String token = (String) resJson.get("access_token");

		return token;
	}

	//계약서 목록 조회
	@RequestMapping(value = "/eform/main")
	public String main(HttpServletRequest request) throws Exception {
		String token = eform_token();
		request.setAttribute("token", token);

		// 로그인한 계정의 권한 설정된 계약서 가져오기
		CPSession session = SessionUtil.get(request);
		request.setAttribute("DATAMAPFORM", new EformDAO().getEformById(session.getUserId()).getRows());

		return "/eform/main";
	}

	//선택한 계약서 정보 호출
	@RequestMapping(value = "/eform/eform_detail", method = RequestMethod.POST)
	public @ResponseBody HashMap<String, Object> eform_detail(String formId, String formName, String token,
			HttpServletRequest request) throws Exception {
		String apiUrl = "https://api.eform.io/v2/form/detail?form_id=" + formId;
		String setMethod = "GET";

		JSONObject resJson = connectionRes(apiUrl, token, setMethod, "", "");

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("token", token);
		map.put("formId", formId);
		map.put("formName", formName);
		map.put("resJson", resJson);

		return map;
	}

	//계약서 전송
	@ResponseBody
	@RequestMapping(value = "/eform/eform_send", method = RequestMethod.POST)
	public String eform_send(String token, String setParams, HttpServletRequest request) throws Exception {
		String apiUrl = "https://api.eform.io/v2/form/send";
		String setMethod = "POST";
	
		CPDAO cpDAO = new CPDAO();
		EformDAO eformDAO = new EformDAO();
		JSONArray jArr = new JSONArray();
		JSONArray jArr2 = new JSONArray();
		JSONParser jParser = new JSONParser();
		SharedMap<String, Object> map = new SharedMap<String, Object>();
	
		Object obj = jParser.parse(setParams);
		JSONObject jobj = (JSONObject) obj;
		
		String form_id = (String) jobj.get("form_id");
		String send_type = (String) jobj.get("send_type");
		//String password = (String) jobj.get("password");
		String expiration_date = (String) jobj.get("expiration_date");
		String auth_phone = (String) jobj.get("auth_phone");
		String title = (String) jobj.get("title");
		String mail_title = (String) jobj.get("mail_title");
		String mail_content = (String) jobj.get("mail_content");
		
		JSONObject resJson = connectionRes(apiUrl, token, setMethod, "", setParams);
		String sendResult = resJson.toJSONString();
	
		jArr = (JSONArray) resJson.get("result");
		jArr2 = (JSONArray) jobj.get("items");
	
		String getItem = "";
	
		for (int i = 0; i < jArr.size(); i++) {
	
			if (jArr2 != null) {
				String[] items = new String[jArr2.size()];
				for (int j = 0; j < jArr2.size(); j++) {
					JSONObject tmp = (JSONObject) jArr2.get(j);
	
					String id = (String) tmp.get("id");
					String value = (String) tmp.get("value");
	
					items[i] = "아이템 : " + id + ", " + "값 : " + value + " / ";
					
					getItem = Arrays.toString(items);
					logger.info("items : " + getItem);
				}
			}
	
			CPSession session = SessionUtil.get(request);
			
			map.put("id", session.getUserId());
			map.put("name", session.getName());
			map.put("grade", session.getGrade());
			
			JSONObject tmp = (JSONObject) jArr.get(i);
			//String checkPassword = (String) tmp.get("password");
			
			String name = (String) tmp.get("receiver_name");
			String email = (String) tmp.get("receiver_email");
			String mobile = (String) tmp.get("receiver_mobile");
			
			map.put("doc_name", (String) tmp.get("doc_name"));
			map.put("send_type", send_type);
			map.put("receiver_meta_id", (String) tmp.get("receiver_meta_id"));
			map.put("receiver_name", cpDAO.getAESEnc(name));
			map.put("receiver_email", cpDAO.getAESEnc(email));
			map.put("receiver_mobile", cpDAO.getAESEnc(mobile));
			
			/*if(checkPassword == "" || checkPassword == null || "".equals(checkPassword)) {
				map.put("receiver_password", "");
			} else {
				map.put("receiver_password", (String) tmp.get("password"));
			}*/
			
			//map.put("password", cpDAO.getAESEnc(password));
			map.put("expiration_date", expiration_date);
			map.put("items", getItem);
			map.put("auth_phone", auth_phone);
			map.put("title", title);
			map.put("mail_title", mail_title);
			map.put("mail_content", mail_content);
			map.put("form_id", form_id);
			map.put("regId", SessionUtil.getUserId(request));
			
			eformDAO.insert(map);
		}
		return sendResult;
	}

	//발송된 계약서 목록 조회
	@RequestMapping(value = "/eform/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView eform_list(HttpServletRequest request, @RequestBody CPRequest cpRequest) throws Exception {
		EformDAO eformDAO = new EformDAO();
		RecordSet rset = eformDAO.list(cpRequest.data, cpRequest.page);

		return new CPRUtil(cpRequest).dataList(rset, eformDAO).setView(request, "/eform/list", "");
	}

	//전자계약서 권한(eformStatus) '사용'인 대상 목록 조회
	@RequestMapping(value = "/eform/auth/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView auth_list(HttpServletRequest request, @RequestBody CPRequest cpRequest) throws Exception {
		EformDAO eformDAO = new EformDAO();

		RecordSet rset = eformDAO.authList(cpRequest.data, cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset, eformDAO).setView(request, "/eform/auth/list", "");
	}

	//멤버관리 > ID > 전자계약 관리 > 발송 권한 수정 : 사용 / 중지
	@RequestMapping(value = "/eform/auth/authModify/{id}", method = RequestMethod.POST)
	public @ResponseBody boolean authModify(HttpServletRequest request, @PathVariable String id, @RequestParam String grade, @RequestParam String parentId) {
		EformDAO eformDAO = new EformDAO();
		
		String eformStatus = request.getParameter("eformStatus");
		
		if("사용".equals(eformStatus)) {
			if("대행사".equals(grade) || "본사".equals(grade)) {
				eformDAO.authModify(eformStatus, id);
				
			} else {
				//해당 업체의 상위 대행사가 '사용' 상태일 경우에만 UPDATE
				String getId = eformDAO.checkDistId(parentId, grade);
				String getResult = eformDAO.checkStatus(getId);
				
				if("사용".equals(getResult)) {
					eformDAO.authModify(eformStatus, id);
				}else if("중지".equals(getResult)) {
					return false;
				}
			}
			
		}else if("중지".equals(eformStatus) && !"대행사".equals(grade)) {
			eformDAO.authModify(eformStatus, id);
			
			//상태 `중지`로 변경시 해당 id로 등록된 계약서 전부 삭제
			eformDAO.listDelete(id);
			
		//대행사 사용 중지 시, 해당 대행사의 하위 업체 일괄 중지 변경 및 계약서 삭제
		}else if("중지".equals(eformStatus) && "대행사".equals(grade)) {
			eformDAO.authModify(eformStatus, id);
			
			eformDAO.listDelete(id);			//해당 id의 계약서 일괄 삭제
			
			List<SharedMap<String, Object>> idList = eformDAO.checkId(parentId);
			
			for (SharedMap<String, Object> getId : idList) {
				String checkAgency =  getId.getString("agencyId");
				String checkSales =  getId.getString("salesId");
				
				if(checkAgency != "" || checkAgency != null) {
					eformDAO.listDeleteBelow(checkAgency);	//해당 id의 하위 업체 계약서 일괄 삭제
					eformDAO.updateBelow(eformStatus, checkAgency); //해당 id의 하위 업체 상태 변경
				}
				if(checkSales != "" || checkSales != null) {
					eformDAO.listDeleteBelow(checkSales);	//해당 id의 하위 업체 계약서 일괄 삭제
					eformDAO.updateBelow(eformStatus, checkSales); //해당 id의 하위 업체 상태 변경
				}
			}
		}
		return true;
	}

	//멤버관리 > ID > 전자계약 관리 > 계약서 추가
	@RequestMapping(value = "/eform/auth/add", method = RequestMethod.POST)
	public @ResponseBody void authAdd(HttpServletRequest request) {
		EformDAO eformDAO = new EformDAO();
		SharedMap<String, Object> map = new SharedMap<String, Object>();

		map.put("id", request.getParameter("id"));
		map.put("name", request.getParameter("name"));
		map.put("grade", request.getParameter("grade"));
		map.put("parentId", request.getParameter("parentId"));
		map.put("form_id", request.getParameter("form_id"));
		map.put("doc_name", request.getParameter("doc_name"));
		map.put("regId", SessionUtil.getUserId(request));

		eformDAO.authInsert(map);
	}
	
	//멤버관리 > ID > 전자계약 관리 > 계약서 삭제
	@RequestMapping(value = "/eform/auth/delete/{idx}", method = RequestMethod.POST)
	public @ResponseBody void authDelete(HttpServletRequest request, @PathVariable String idx) {
		EformDAO eformDAO = new EformDAO();

		eformDAO.authDelete(idx);
	}

	//전자계약서 내역 조회 > 발송 내역 상세 보기 modal
	@RequestMapping(value = "/eform/view/{idx}", method = RequestMethod.GET)
	public ModelAndView view(HttpServletRequest request, @PathVariable String idx) throws Exception {
		request.setAttribute("DATAMAP", new EformDAO().getDetail(idx).getRow(0));

		return new ModelAndView("/eform/auth/modal");
	}

	//전자계약서 내역 조회 > 발송 내역 상세 보기 modal > 전자계약서 처리 결과 수정
	@RequestMapping(value = "/eform/auth/addResult", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CPResponse addResult(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();

		if (cpDAO.update("PG_EFORM", cpRequest.data)) {
			return new CPRUtil(cpRequest).resultOK("정보가 변경되었습니다.").cpResponse();
		} else {
			return new CPRUtil(cpRequest).resultNOK("정보 변경에 실패하였습니다.", cpDAO.getError()).cpResponse();
		}
	}

	//대행사/에이전시/지사 로그인 시 : 해당 아이디 전자계약서 전송 내역 조회
	@RequestMapping(value = "/eform/member/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView member_list(HttpServletRequest request, @RequestBody CPRequest cpRequest) throws Exception {
		EformDAO eformDAO = new EformDAO();
		CPSession session = SessionUtil.get(request);
		String id = session.getUserId();
		
		RecordSet rset = eformDAO.memList(cpRequest.data, cpRequest.page, id);
		return new CPRUtil(cpRequest).dataList(rset, eformDAO).setView(request, "/eform/member/list", "");
	}
	
	//완료된 계약서, 인증서를 압축파일(zip)로 다운로드 API
	@RequestMapping(value = "/eform/download/{receiver_meta_id}", method = RequestMethod.GET)
	public void eform_download(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String receiver_meta_id) throws Exception {
		configSetting();

		String token = eform_token();
		String apiUrl = "https://api.eform.io/v2/doc/download?receiver_meta_id=" + receiver_meta_id;

		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter writer = response.getWriter();

		URL url = new URL(apiUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestProperty("x-api-key", apiKEY);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestMethod("GET");
		conn.setRequestProperty("x-access-token", token);

		try {
			if (conn.getResponseCode() != 200) {
				logger.error("connectionRes Connection error : " + conn.getResponseCode());

				writer.println("<script>alert('파일이 존재하지 않습니다.'); location.href='/eform/form.jsp';</script>");
			} else {
				// A binary file was returned
				String disposition = conn.getHeaderField("Content-Disposition");
				int index = disposition.indexOf("filename=");
				String name = disposition.substring(index + 10, disposition.length() - 1);
				
				byte b[] = new byte[4096];

				response.reset();
				response.setContentType("application/octet-stream");

				String Encoding = new String(name.getBytes("UTF-8"), "8859_1");
				response.setHeader("Content-Disposition", "attatchment; filename = " + Encoding);

				InputStream is = conn.getInputStream();
				ServletOutputStream sos = response.getOutputStream();

				int numRead;
				while((numRead = is.read(b,0,b.length)) != -1){
					sos.write(b,0,numRead);
				}

				sos.flush();
				sos.close();
				is.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}

	//이폼 서비스 API 연동
	private JSONObject connectionRes(String urlAddr, String token, String setMethod, String apiId, String setParams)
			throws Exception {
		JSONObject apiRes = new JSONObject();

		configSetting();
		
		URL url = new URL(urlAddr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestProperty("x-api-key", apiKEY);
		conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
		conn.setDoInput(true);
		conn.setDoOutput(true);
		
		if (setMethod.equals("GET")) {
			conn.setRequestMethod("GET");
		} else if (setMethod.equals("POST")) {
			conn.setRequestMethod("POST");
		}
		
		if (apiId != null && !"".equals(apiId)) {
			conn.setRequestProperty("x-api-id", apiId);
		}
		
		if (token != null) {
			conn.setRequestProperty("x-access-token", token);
		}
		
		if (setParams != null && !"".equals(setParams)) {
			OutputStream os = conn.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
			osw.write(setParams);
			osw.flush();
			osw.close();
		}
		
		try {
			if (conn.getResponseCode() != 200) {
				logger.error("connectionRes Connection error : " + conn.getResponseCode());
			} else {
				String line = "";
				String result = "";
				InputStream in = new BufferedInputStream(conn.getInputStream());
				try (BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"))) {
					line = br.lines().collect(Collectors.joining(System.lineSeparator()));
				}
				result = line;
		
				in.close();
		
				JSONParser jParser = new JSONParser();
				apiRes = (JSONObject) jParser.parse(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}

		return apiRes;
	}

	//전자계약서 계정 정보 config 파일 읽어서 변수에 세팅
	public void configSetting() {
		try {
			// 프로퍼티 파일 위치
			// 운영
			//String propFile = "/home/MARU/MARU_APP/conf/eform.properties";
			// 테스트
			String propFile = "/home/MARU/MARU_APP/conf/eform.properties";
			// 로컬
			//String propFile = "C:/01/MARU_APP/conf/eform.properties";

			// 프로퍼티 객체 생성
			Properties props = new Properties();

			// 프로퍼티 파일 스트림에 담기
			FileInputStream fis = new FileInputStream(propFile);

			// 프로퍼티 파일 로딩
			props.load(new java.io.BufferedInputStream(fis));

			// 항목 읽기
			apiID = props.getProperty("apiID");
			apiKEY = props.getProperty("apiKEY");
			
			logger.info("apiID : [{}], apiKEY : [{}]", apiID, apiKEY);
		} catch (Exception e) {
			logger.info(e.getMessage(), e);
		}
	}
}

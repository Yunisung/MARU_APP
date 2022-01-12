package com.pgmate.app.model.ajax;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.pgmate.lib.util.map.SharedMap;

/**
 * @author Administrator
 *
 */
//view단으로 보내주는 데이터에 대한 정보들 담는 객체
@XmlRootElement(name = "response")
public class CPResponse implements java.io.Serializable{
	//KJM : 기능 수행 결과 정보
	@XmlElement(name = "result")
	public Result result	= null;
	
	//KJM : list, excel, pdf
	@XmlElement(name = "type")
	public String type		= "";
	
	@XmlElement(name = "redirect")
	public String redirect	= "";
	
	@XmlElement(name = "page")
	public Page page		= null;
	// ex) 엑셀일 경우 파일 다운로드 시 나오는 테이블의 컬럼 값이다.
	@XmlElement(name = "thead")
	public String thead		= null;
	
	@XmlElement(name = "data")
	public List<SharedMap<String,Object>> data	= null;
	@XmlElement(name = "sum")
	public SharedMap<String,Object> sum	= null;
	
	@XmlElement(name = "file")
	public Files file		= null;
	
	
	
	
	public CPResponse() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @return the result
	 */
	public Result getResult() {
		return result;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return the redirect
	 */
	public String getRedirect() {
		return redirect;
	}

	/**
	 * @return the page
	 */
	public Page getPage() {
		return page;
	}

	/**
	 * @return the data
	 */
	public List<SharedMap<String, Object>> getData() {
		return data;
	}

	/**
	 * @return the file
	 */
	public Files getFile() {
		return file;
	}
	
	
	public SharedMap<String, Object> getSum() {
		return sum;
	}

}

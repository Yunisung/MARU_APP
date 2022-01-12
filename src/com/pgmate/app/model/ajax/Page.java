package com.pgmate.app.model.ajax;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Administrator
 *
 */
@XmlRootElement(name = "page")
public class Page implements java.io.Serializable{
	// 현재 페이지
	@XmlElement(name = "current")
	public long current	= 1;
	
	// 전체 리스트 개수
	@XmlElement(name = "total")
	public long total	= 0;
	
	// 한 페이지 당 보여줄 리스트 개수
	@XmlElement(name = "size")
	public long size	= 20;
	
	@XmlElement(name = "hash")
	public String hash	= "";
	
	// 전체 페이지 수
	@XmlElement(name = "totalPage")
	public long totalPage	= 0;
	
	public Page() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @return the current
	 */
	public long getCurrent() {
		return current;
	}

	/**
	 * @return the total
	 */
	public long getTotal() {
		return total;
	}

	/**
	 * @return the size
	 */
	public long getSize() {
		return size;
	}

	/**
	 * @return the hash
	 */
	public String getHash() {
		return hash;
	}

	/**
	 * @return the totalPage
	 */
	public long getTotalPage() {
		return totalPage;
	}
}

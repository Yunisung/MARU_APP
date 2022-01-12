package com.pgmate.app.model.ajax;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author Administrator
 * <pre>
 * 210812_PYS
 * Data는 name, val, oper, order, key로 되어있다.
 * 주로 SQL에서 WHERE문에 쓰도록 만들어짐
 * name랑 val이 oper에 따라 조건문이 만들어지고 order로 정렬을 한다.
 * 
 *  현재 form.jsp에서 form태그안 속성명이 일치하는것들을 맵핑
 * ex) form 태크안 name = "boram" 일경우 this.name의 값은 boram으로 맵핑
 * </pre>
 */

//  reuqst 로 값 전달 시 자동 맵핑
@XmlRootElement(name = "data")
public class Data implements java.io.Serializable{
	
	@XmlElement(name = "name")
	public String name	= "";
	//  input 입력 value ( 다르게 작동 가능)
	@XmlElement(name = "val")
	public Object val	= "";
	
	@XmlElement(name = "oper")
	public String oper	= "";
	
	@XmlElement(name = "order")
	public String order	= "";
	
	//KJM : script에서 조건 값 보내줄때 key를 true로 세팅해줌
	@XmlElement(name = "key")
	public boolean key	= false;
	
	public Data() {
		// TODO Auto-generated constructor stub
	}

}

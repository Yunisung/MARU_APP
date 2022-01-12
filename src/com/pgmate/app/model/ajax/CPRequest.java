package com.pgmate.app.model.ajax;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.pgmate.lib.util.lang.CommonUtil;

/**
 * @author Administrator
 *
 */
//  view에서 넘어온 데이터 담는 객체
@XmlRootElement(name = "request")
public class CPRequest implements java.io.Serializable{
	
	@XmlElement(name = "type")
	public String type		= "";
	
	//  excel 파일이름 or PDF title  
	@XmlElement(name = "reason")
	public String reason	= "";
	
	//   이벤트가 일어난 뒤 즉시 이동되는 URL 값 셋팅
	@XmlElement(name = "redirect")
	public String redirect	= "";
	
	//  페이징 정보
	@XmlElement(name = "page")
	public Page page		= null;
	
	//  컬럼들에 대한 값
	@XmlElement(name = "data")
	public List<Data> data	= null;
	
	//  컬럼명 (영어:한글)
	@XmlElement(name = "thead")
	public String thead	= null;
	
	public CPRequest() {
		// TODO Auto-generated constructor stub
	}
	
	public CPRequest(String type){
		this.type = type; 
	}
	
	public void replaceKeyName(String name,String replace){
		if(data == null){return;}
		else{
			for(int i=0;i<data.size();i++){
				Data dataObj = (Data)data.get(i);
//				System.out.println("data에는 어떤 : " + dataObj);
				if(dataObj.name.equals(name) && dataObj.key == true){
					dataObj.name = replace;
					data.remove(i);
					data.add(i, dataObj);
				}
			}
		}
	}
	
	public void replaceName(String name,String replace){
		if(data == null){return;}
		else{
			for(int i=0;i<data.size();i++){
				Data dataObj = (Data)data.get(i);
				if(dataObj.name.equals(name) && dataObj.key == false){
					dataObj.name = replace;
					data.remove(i);
					data.add(i, dataObj);
				}
			}
		}
	}
	
	/*
	 * 식별번호의 경우 암호화 후 비교해야 하기 때문에 암호화 된 값을 대신 넣어준다
	 * @param name : 컬럼명
	 * @param replace : 암호화 된 값
	 */
	public void replaceKeyValue(String name,String replace){
		/*
		 *   data의 경우 조회 조건 안줬을 경우 전 단계에서 설정해준 status의 값만 있다
		 * 조회 조건 줬을 경우 조건에 대한 값도 들어가있음
		 */
		
		if(data == null){return;}
		else{
			//  data.size = 조회 조건 개수
			for(int i=0;i<data.size();i++){
				Data dataObj = (Data)data.get(i);
				//  해당 컬럼명(name)의 조회 조건 있으면 그에 대한 값을 복호화된 값으로 바꾼다
				if(dataObj.name.equals(name) && dataObj.key == true){
					//   암호화 된 값을 적용
					dataObj.val = replace;	//   암호화 된 값 셋팅
					//   val에 있는 암호화 전 값 삭제  
					data.remove(i); 
					//   해당 i번째 값 다시 셋팅 
					data.add(i, dataObj);
				}
			}
		}
	}
	
	/** KBR 
	 * param
	 * name : data.key
	 * replace : 암호화된 값 
	 * **/
	public void replaceValue(String name,String replace){
		if(data == null){return;}
		else{
			for(int i=0;i<data.size();i++){
				
				Data dataObj = (Data)data.get(i);
				
				if(dataObj.name.equals(name) && dataObj.key == false){
					dataObj.val = replace;
					data.remove(i);
					data.add(i, dataObj);
				}
			}
		}
	}
	
	
	public void replaceKey(String name,boolean key){
		if(data == null){return;}
		else{
			for(int i=0;i<data.size();i++){
				Data dataObj = (Data)data.get(i);
				if(dataObj.name.equals(name)){
					dataObj.key = key;
					data.remove(i);
					data.add(i, dataObj);
				}
			}
		}
	}
	
	/*
	 *   해당 컬럼명의 조건 값 주었을 경우 value에 해당 값 넣어준뒤 반환
	 * @param : 컬럼명
	 * 
	 */
	//   data안에 있는 모든 값이 아닌 단일값 리턴 시
	public String getValue(String name){
		
		if(data == null){return "";}
		else{
			String value = "";
			for(int i=0;i<data.size();i++){
				//  조회 input값에 해당 컬럼의 값 있는지 확인 후 value에 담는다
				Data dataObj = (Data)data.get(i);
//				KJM: key == false를 조건으로 줄 경우 조회 Obj.key == false)
				if(dataObj.name.equals(name) && dataObj.key == false){
					value = CommonUtil.toString(dataObj.val);
					break;
				}
			}
			return value;
		}
	}
	
	public String getValue(String name,String replaceValue){
		if(data == null){return "";}
		else{
			String value = "";
			for(int i=0;i<data.size();i++){
				Data dataObj = (Data)data.get(i);
				if(dataObj.name.equals(name) && dataObj.key == false){
					value = CommonUtil.toString(dataObj.val);
					if(value.equals("")){value=replaceValue;}
					break;
				} 
			}
			return value;
		}
	}
	
	public long getLongValue(String name){
		if(data == null){return 0;}
		else{
			long value = 0;
			for(int i=0;i<data.size();i++){
				Data dataObj = (Data)data.get(i);
				if(dataObj.name.equals(name) && dataObj.key == false){
					value = CommonUtil.parseLong(dataObj.val);
					break;
				}
			}
			return value;
		}
	}
	
	public double getDoubleValue(String name){
		if(data == null){return 0;}
		else{
			double value = 0;
			for(int i=0;i<data.size();i++){
				Data dataObj = (Data)data.get(i);
				if(dataObj.name.equals(name) && dataObj.key == false){
					value = CommonUtil.parseDouble(dataObj.val);
					break;
				}
			}
			return value;
		}
	}
	
	/*	KJM 
	 *	@param : 컬럼명(status)
	 *	조회 조건 입력 시 그에 맞는 값 세팅
	 */
	/* 
	 *   data가 null일 때 바로 "" 값 리턴
	 * data는 조회 input 값 들어옴
	 * input에 아무것도 입력 안해도 기본선택값이나 오늘날짜가 기본으로 들어오는거 같음(기능별 다름)
	*/
	public String getKeyValue(String name){
		if(data == null){
			return "";}
		else{
			String value = "";
			for(int i=0;i<data.size();i++){
				Data dataObj = (Data)data.get(i);
				//  조회 조건 입력값에 상태값 입력 시
				//   input에 값이 있는채로 search 됐지는 여부 체크 조건문  
				if(dataObj.name.equals(name) && dataObj.key == true){
					//  입력한 상태값에 맞게 조회하기 위해 value값에 세팅
					value = CommonUtil.toString(dataObj.val);
					break;
				}
			}
			//  value 값 리턴
			//   가맹점 리스트 조회 화면 일 경우 "사용" 리턴 (검색값이 없을 경우)
			return value;
		}
	}
	
	public long getKeyLongValue(String name){
		if(data == null){return 0;}
		else{
			long value = 0;
			for(int i=0;i<data.size();i++){
				Data dataObj = (Data)data.get(i);
				if(dataObj.name.equals(name) && dataObj.key == true){
					value = CommonUtil.parseLong(dataObj.val);
					break;
				}
			}
			return value;
		}
	}
	
	public double getKeyDoubleValue(String name){
		if(data == null){return 0;}
		else{
			double value = 0;
			for(int i=0;i<data.size();i++){
				Data dataObj = (Data)data.get(i);
				if(dataObj.name.equals(name) && dataObj.key == true){
					value = CommonUtil.parseDouble(dataObj.val);
					break;
				}
			}
			return value;
		}
	}
 	
	/*   키가 true인 것
	 * 현재 view단에서 넘어오는 파라메터중 id값을 true로 넘겨주고 있음
	 * -- id가 수정된 값인지 아닌지를 체크할 때 사용 
	 * -- 예를들어 터미널 수정에서 매입이 있을 경우는 id 수정이 불가함 그럴경우 id값은 true로 key값을 설정하여 넘겨주고 있음
	 * */
	
	public Data getKeyData(String name){
		
		if(data == null){return null;}
		else{
			Data dataValue = null;
			for(int i=0;i<data.size();i++){
				Data dataObj = (Data)data.get(i);
				if(dataObj.name.equals(name) && dataObj.key == true){
					dataValue = dataObj;
					break;
				}
			}
			return dataValue;
		}
	}
	
	
	/*   키가 false인 것
	 * 현재 view단에서 넘어오는 파라메터중 id값 제외 모든 항목을 false로 넘겨주고 있음 (대체적으로)
	 * -- id가 수정된 값인지 아닌지를 체크할 때 사용 
	 * -- 예를들어 터미널 수정에서 매입이 없을 경우 id 수정이 가능함 그럴경우 수정된 id값은 false로 key값을 설정하여 넘겨주고 있음
	 * */
	public Data getData(String name){
		
		if(data == null){return null;}
		else{
			Data dataValue = null;
			for(int i=0;i<data.size();i++){
				Data dataObj = (Data)data.get(i);
				if(dataObj.name.equals(name) && dataObj.key == false){
					dataValue = dataObj;
					break;
				}
			}
			return dataValue;
		}
	}
	
	
	public void deleteData(String name){
		if(data == null){return;}
		else{
			for(int i=0;i<data.size();i++){
				Data dataObj = (Data)data.get(i);
				if(dataObj.name.equals(name) && dataObj.key == false){
					data.remove(i);
				}
			}
		}
	}
	
	//KJM : 조회 조건에서 입력한 값을 삭제
	public void deleteKeyData(String name){
		//KJM : data가 없을 경우 return
		if(data == null){return;}
		//KJM : data가 있을 경우 data의 이름이 name과 일치하고 key가 true(조회 조건으로 들어올때 key=true)일 때 해당 값을 삭제
		else{
			for(int i=0;i<data.size();i++){
				Data dataObj = (Data)data.get(i);
				if(dataObj.name.equals(name) && dataObj.key == true){
					data.remove(i);
				}
			}
		}
	}
	 
	public void insertData(Data addData){
		
		if(data == null){
			data = new ArrayList<Data>();
		}
		
		data.add(addData);
		
	}
	
	//   Data 클래스안 멤버변수에 값 할당 후 insertData 메서드로 전달   
	public void setData(String name,String value,String oper,String order,boolean key){
		
		Data dataObj = new Data();
		
		dataObj.name = name;
		dataObj.val = value;
		dataObj.oper = oper;
		dataObj.order = order;
		dataObj.key = key;
//		dataObj.sys("setData");
		
		insertData(dataObj);
	}
	
	
	public void setData(String name,Object value,String oper,String order,boolean key){
		Data dataObj = new Data();
		dataObj.name = name;
		dataObj.val = value;
		dataObj.oper = oper;
		dataObj.order = order;
		dataObj.key = key;
		insertData(dataObj);
	}
	
	// 지정 값 data에 add 
	public void setData(String name,String value){
		Data dataObj = new Data();
		dataObj.name = name;
		dataObj.val = value;
		insertData(dataObj);
	}
	 
	// KBR : object 타입 추가 _ 211126
	public void setData(String name,Object value){
		Data dataObj = new Data();
		dataObj.name = name;
		dataObj.val = value;
		insertData(dataObj);
	}
	
	
	public void setData(String name,long value){
		Data dataObj = new Data();
		dataObj.name = name;
		dataObj.val = value;
		insertData(dataObj);
	}
	
	
	public void setData(String name,Timestamp value){
		Data dataObj = new Data();
		dataObj.name = name;
		dataObj.val = value;
		insertData(dataObj);
	}
	
	

}

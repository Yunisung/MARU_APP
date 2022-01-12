package com.pgmate.app.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.app.model.ajax.Data;
import com.pgmate.app.model.ajax.Page;
import com.pgmate.app.util.CPUtil;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.key.CPKEY;
import com.pgmate.lib.key.GenKey;

/**
 * @author Administrator
 *
 */
//KJM : 가맹점 터미널 정보
public class MchtTmnDAO extends DAO{
	
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.MchtTmnDAO.class );
	private static final String TABLE = "VW_MCHT_TMN";
	private static final String COLUMNS = "*";
	
	public MchtTmnDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(MchtTmnDAO.COLUMNS);
	}
	
	//KJM : 단말기아이디에 대한 터미널 정보
	public RecordSet getById(String tmnId){
		addWhere("tmnId",tmnId.toLowerCase(),eq);
		return search();
	}
	
	public RecordSet getByMchtId(String mchtId){
		addWhere("lower(mchtId)",mchtId.toLowerCase(),eq);
		return search();
	}
	
	public RecordSet search(List<Data> datas){
		CPUtil.setDAO(this, datas);			//DATA to CONDITION 
		return super.search();				//단일 검색
	}
	
	public RecordSet list(List<Data> datas,Page page){
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}

	public boolean insertDefault(String tmnId, String pk, String mchtId, String taxId, String serial) {
		String query = "INSERT INTO PG_MCHT_TMN	(`tmnId`,`mchtId`,`taxId`,`status`, `serial`,`payKey`,`activeDate`,`regId`,`regDay`,`regDate`) "
					 + "SELECT '"+tmnId+"', mchtId, '"+taxId+"', '사용','"+serial+"','"+pk+"',regDay,regId,regDay,regDate "
					 + "FROM PG_MCHT WHERE mchtId = '"+mchtId+"'";
		
		return new CPDAO().update(query); 
	}
	
	/**
	 * 터미널에서 웹결제창이 '사용'이고 터미널상태가 '사용'일때 결제키를 리턴
	 * <pre>
	 * SELECT * FROM VW_MCHT_TMN WHERE LOWER(mchtId) = '아이디' AND STATUS = '사용' AND webpay = '사용'
	 * </pre>
	 * @param mchtId
	 * @return
	 */
	public String getWebPay(String mchtId){
		addWhere("lower(mchtId)",mchtId.toLowerCase(),eq);
		addWhere("status","사용",eq);
		addWhere("webPay","사용",eq);
		RecordSet rset = search();
		if(rset.size() == 0){
			return "";
		}else{
			return rset.getRow(0).getString("payKey");
		}
		
	}

	public String getTmnWebPay(String tmnId){
		addWhere("lower(tmnId)",tmnId.toLowerCase(),eq);
		addWhere("status","사용",eq);
		addWhere("webPay","사용",eq);
		RecordSet rset = search();
		if(rset.size() == 0){
			return "";
		}else{
			return rset.getRow(0).getString("payKey");
		}
		
	}
	
	public RecordSet getHtByMchtId(String mchtId) {
		setTable("HT_MCHT_TMN");
		setColumns("*");
		addWhere("lower(mchtId)", mchtId.toLowerCase(), eq);
		return search();
	}
	// KBR : 새로 새성될 터미널 ID
	public String getNewId() {
		//KBR : 왼쪽으로 0을 넣어서 총 6자리 수를 String으로 만들기
		return "TMN" + String.format("%06d", Integer.parseInt(getFunction("FN_NEXTVAL", "TERMINAL")));
	}
}
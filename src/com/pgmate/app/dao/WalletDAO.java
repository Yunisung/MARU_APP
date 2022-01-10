package com.pgmate.app.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.app.model.ajax.CPRequest;
import com.pgmate.app.util.CPUtil;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;

/**
 * @author Administrator
 *
 */
public class WalletDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.WalletDAO.class );
	private static final String TABLE = "WL_USER";
	private static final String COLUMNS = "*";
	
	public WalletDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(WalletDAO.COLUMNS);
	}
	
	public SharedMap<String, Object> getWalletByTmnId(String tmnId) {
		super.setTable("WL_USER A LEFT JOIN WL_PTN_MNG B on A.ptnId = B.ptnId LEFT JOIN WL_USER_ACCNT C ON A.accntId = C.accntId");
		super.setColumns("A.walletId,A.ptnId,B.apiKey,C.bankCd, C.account, A.accntId");
		super.addWhere("A.tmnId", tmnId);
		super.setOrderBy("A.regDate desc");
		RecordSet rset = super.search();
		super.initRecord();
		if(rset.size() > 0) {
			return rset.getRowFirst();
		}else {
			return null;
		}
		
		
	}
	
	public SharedMap<String, Object> getWalletAccnt(String accntId,String walletId) {
		super.setTable("WL_USER_ACCNT");
		super.setColumns("*");
		super.addWhere("accntId", accntId,eq);
		super.addWhere("walletId", walletId,eq);
		RecordSet rset = super.search();
		super.initRecord();
		if(rset.size() > 0) {
			return rset.getRowFirst();
		}else {
			return null;
		}
		
		
	}

	public List<SharedMap<String, Object>> getWalletBymchtId(String mchtId) {
		super.setTable("PG_MCHT_TMN A left join WL_USER B on A.tmnId = B.tmnId");
		super.setColumns("A.*, B.walletId");
		super.addWhere("A.mchtId", mchtId,eq);
		super.addWhere("B.walletId is not null");
		RecordSet rset = super.search();
		super.initRecord();
		if(rset.size() > 0) {
			return rset.getRows();
		}else {
			return null;
		}
	}

	public void updateWallet(SharedMap<String, Object> walletMap) {
		super.setTable("WL_USER");
		super.setRecord("tel", walletMap.getString("tel"));
		super.setRecord("phone", walletMap.getString("phone"));
		super.setRecord("zip", walletMap.getString("zip"));
		super.setRecord("addr1", walletMap.getString("addr1"));
		super.setRecord("addr2", walletMap.getString("addr2"));
		super.addWhere("walletId", walletMap.getString("walletId"), eq);
		super.update();
		super.initRecord();
		
	}

	public void updateShopWallet(SharedMap<String, Object> walletMap) {
		super.setTable("WL_USER");
		super.setRecord("identity", walletMap.getString("identity"));
		super.setRecord("compName", walletMap.getString("compName"));
		super.setRecord("ceo", walletMap.getString("ceo"));
		super.setRecord("tel", walletMap.getString("tel"));
		super.setRecord("phone", walletMap.getString("phone"));
		super.setRecord("zip", walletMap.getString("zip"));
		super.setRecord("addr1", walletMap.getString("addr1"));
		super.setRecord("addr2", walletMap.getString("addr2"));
		super.setRecord("email", walletMap.getString("email"));
		super.addWhere("walletId", walletMap.getString("walletId"),eq);
		super.update();
		super.initRecord();
		
	}
}
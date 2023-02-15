package com.pgmate.app.dao;

import com.pgmate.app.model.ajax.Data;
import com.pgmate.app.model.ajax.Page;
import com.pgmate.app.util.CPUtil;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Administrator
 *
 */
public class ReserveVactRateDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( ReserveVactRateDAO.class );
	private static final String TABLE = "VW_RESERVE_VACT_RATE";
	private static final String COLUMNS = "idx, member, title, parentId, fee, rate, distFee, distRate, agencyFee, agencyRate, beforeFee, beforeRate, beforeDistFee, beforeDistRate, beforeAgencyFee, beforeAgencyRate, pubDay, status, regId, regDay, regDate, feeType";

    public ReserveVactRateDAO() {
        super(TABLE,CPUtil.CP_DEBUG);
        super.setColumns(ReserveVactRateDAO.COLUMNS);
    }

    public RecordSet getByIdx(String idx){
        addWhere("idx",idx,eq);
        return search();
    }

    public RecordSet getByParentId(String parentId){
        addWhere("lower(parentId)",parentId.toLowerCase(),eq);
        return search();
    }

    public RecordSet search(List<Data> datas){
        CPUtil.setDAO(this, datas);			//DATA to CONDITION
        return super.search();				//단일 검색
    }

    //KJM : 가맹점 수수료 변경 예약 리스트
    public RecordSet list(List<Data> datas,Page page){
        page = CPUtil.correctPage(page);
        CPUtil.setDAO(this, datas);				//DATA to CONDITION
        return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색
    }
}

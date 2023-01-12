package com.pgmate.app.dao;

import com.pgmate.app.model.ajax.Data;
import com.pgmate.app.model.ajax.Page;
import com.pgmate.app.util.CPUtil;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.map.SharedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MchtTotalAuthDAO extends DAO {
    private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.MchtTotalAuthDAO.class );
    private static final String TABLE = "PG_MCHT_TOTAL_AUTH";
    private static final String COLUMNS = "*";

    public MchtTotalAuthDAO() {
        super(TABLE, CPUtil.CP_DEBUG);
        super.setColumns("*");
        super.setOrderBy("regDay desc");
    }

    public SharedMap<String,Object> getByMchtId(String mchtId){
        super.addWhere("mchtId",mchtId.toLowerCase(),eq);
        return super.search().getRow(0);
    }

    public RecordSet search(List<Data> datas){
        CPUtil.setDAO(this, datas);			//DATA to CONDITION
        return super.search();
    }

    public RecordSet list(List<Data> datas, Page page){
        page = CPUtil.correctPage(page);
        CPUtil.setDAO(this, datas);				//DATA to CONDITION
        return super.searchList(page.current, page.size,page.hash);	//LIST PAGING
    }
}

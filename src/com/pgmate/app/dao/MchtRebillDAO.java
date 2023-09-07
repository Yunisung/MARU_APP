package com.pgmate.app.dao;

import com.pgmate.app.util.CPUtil;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.util.map.SharedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MchtRebillDAO extends DAO {
    private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.MchtRebillDAO.class );
    private static final String TABLE = "PG_MCHT_REBILL";
    private static final String COLUMNS = "*";

    public MchtRebillDAO() {
        super(TABLE, CPUtil.CP_DEBUG);
        super.setColumns("*");
        super.setOrderBy("regDate desc");
    }

    public SharedMap<String,Object> getByMchtId(String mchtId){
        super.addWhere("mchtId",mchtId.toLowerCase(),eq);
        return super.search().getRow(0);
    }
}

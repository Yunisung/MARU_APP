package com.pgmate.app.dao;

import com.pgmate.app.util.CPUtil;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.util.map.SharedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MchtRentDAO extends DAO {
    private static Logger logger = LoggerFactory.getLogger( MchtRentDAO.class );
    private static final String TABLE = "PG_MCHT_RENT";
    private static final String COLUMNS = "*";

    public MchtRentDAO() {
        super(TABLE, CPUtil.CP_DEBUG);
        super.setColumns("*");
        super.setOrderBy("regDate desc");
    }

    public SharedMap<String,Object> getByMchtId(String mchtId){
        super.addWhere("mchtId",mchtId.toLowerCase(),eq);
        return super.search().getRow(0);
    }
}

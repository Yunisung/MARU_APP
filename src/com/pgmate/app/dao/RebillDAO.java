package com.pgmate.app.dao;

import com.pgmate.app.model.ajax.Data;
import com.pgmate.app.model.ajax.Page;
import com.pgmate.app.util.CPUtil;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;

import java.util.List;

public class RebillDAO extends DAO {


    public RecordSet regList(List<Data> datas, Page page) {
        super.setTable("PG_REBILL_REG");

        page = CPUtil.correctPage(page);
        CPUtil.setDAO(this, datas); //DATA to CONDITION
        return super.searchList(page.current, page.size, page.hash); //LIST PAGING
    }

    public RecordSet errList(List<Data> datas, Page page) {
        super.setTable("PG_REBILL_ERR");

        page = CPUtil.correctPage(page);
        CPUtil.setDAO(this, datas); //DATA to CONDITION
        return super.searchList(page.current, page.size, page.hash); //LIST PAGING
    }
}

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

    public RecordSet trxList(List<Data> datas, Page page) {
        super.setTable("PG_REBILL_PAY");

        page = CPUtil.correctPage(page);
        CPUtil.setDAO(this, datas);
        return super.searchList(page.current, page.size, page.hash);
    }

    public RecordSet trxSum(List<Data> datas,Page page) {
        //KJM : 금액의 합계를 amount 컬럼명으로 받겠다
        super.setColumns("SUM(amount) AS amount");
        super.addWhere("status", "승인");
        page = CPUtil.correctPage(page);
        CPUtil.setDAO(this, datas);				//DATA to CONDITION
        RecordSet rset =  super.search();
        super.initRecord();
        return rset;	//LIST PAGING 검색
    }

    public RecordSet getByTrxId(String trxId) {
        super.setTable("VW_REBILL_PAY");
        super.setColumns("*");
        super.addWhere("trxId", trxId);
        RecordSet rset = super.search();
        super.initRecord();
        return rset;
    }

    public RecordSet errList(List<Data> datas, Page page) {
        super.setTable("PG_REBILL_ERR");

        page = CPUtil.correctPage(page);
        CPUtil.setDAO(this, datas); //DATA to CONDITION
        return super.searchList(page.current, page.size, page.hash); //LIST PAGING
    }
}

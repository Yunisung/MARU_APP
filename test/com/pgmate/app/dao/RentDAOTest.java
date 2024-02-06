package com.pgmate.app.dao;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RentDAOTest {
    private static Logger logger = LoggerFactory.getLogger(RentDAOTest.class);

    private RentDAO rentDAO;

    @Before
    public void init() {
        rentDAO = new RentDAO();
    }

    @Test
    public void insertFirmReserveHistory() {
        String trxId = "CS240123048979";
        logger.info("히스토리 저장 결과 : {}", rentDAO.insertFirmReserveHistory(trxId));
    }
}

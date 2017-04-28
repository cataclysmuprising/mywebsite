/*
 * @author Mg Than Htike Aung {@literal <rage.cataclysm@gmail.com@address>}
 * @Since 1.0
 * 
 */
package com.mycom.products.mywebsite.core.unitTest.config;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.mycom.products.mywebsite.core.TestBase;
import com.mycom.products.mywebsite.core.api.InsertableTest;
import com.mycom.products.mywebsite.core.api.JoinedSelectableTest;
import com.mycom.products.mywebsite.core.bean.config.LoginHistoryBean;
import com.mycom.products.mywebsite.core.dao.config.LoginHistoryDao;
import com.mycom.products.mywebsite.core.util.FetchMode;

public class LoginHistoryUnitTest extends TestBase
		implements JoinedSelectableTest, InsertableTest {
	@Autowired
	private LoginHistoryDao loginHistoryDao;
	private Logger testLogger = Logger.getLogger(this.getClass());

	// --------------------------------- for fetching
	@Override
	@Test(groups = { "fetch" })
	public void testSelectAllWithLazyMode() throws Exception {
		List<LoginHistoryBean> results = loginHistoryDao.selectList(null, FetchMode.LAZY);
		showEntriesOfCollection(results);
		Assert.assertNotNull(results);
	}

	@Override
	@Test(groups = { "fetch" })
	public void testSelectAllWithEagerMode() throws Exception {
		List<LoginHistoryBean> results = loginHistoryDao.selectList(null, FetchMode.EAGER);
		showEntriesOfCollection(results);
		Assert.assertNotNull(results);
	}

	@Override
	@Test(groups = { "fetch" })
	public void testSelectAllCount() throws Exception {
		long count = loginHistoryDao.selectCounts(null, null);
		testLogger.info("Total counts ==> " + count);
		Assert.assertEquals(true, count > 0);
	}

	@Override
	@Test(groups = { "fetch" })
	public void testSelectCountWithLazyCriteria() throws Exception {
		HashMap<String, Object> criteria = new HashMap<>();
		criteria.put("id", 1);
		// criteria.put("userId", 1);
		long count = loginHistoryDao.selectCounts(criteria, FetchMode.LAZY);
		testLogger.info("Total counts ==> " + count);
	}

	@Override
	@Test(groups = { "fetch" })
	public void testSelectCountWithEagerCriteria() throws Exception {
		HashMap<String, Object> criteria = new HashMap<>();
		criteria.put("id", 1);
		// criteria.put("userId", 1);
		long count = loginHistoryDao.selectCounts(criteria, FetchMode.EAGER);
		testLogger.info("Total counts ==> " + count);
	}

	@Override
	@Test(groups = { "fetch" })
	public void testSelectMultiRecordByCriteriaWithLazyMode() throws Exception {
		HashMap<String, Object> criteria = new HashMap<>();
		criteria.put("id", 1);
		criteria.put("offset", 0);
		criteria.put("limit", 5);
		criteria.put("orderBy", "userId");
		criteria.put("orderAs", "desc");
		List<LoginHistoryBean> results = loginHistoryDao.selectList(criteria, FetchMode.LAZY);
		Assert.assertNotNull(results);
		showEntriesOfCollection(results);

	}

	@Override
	@Test(groups = { "fetch" })
	public void testSelectMultiRecordByCriteriaWithEagerMode() throws Exception {
		HashMap<String, Object> criteria = new HashMap<>();
		criteria.put("id", 1);
		criteria.put("offset", 0);
		criteria.put("limit", 5);
		criteria.put("orderBy", "userId");
		criteria.put("orderAs", "desc");
		List<LoginHistoryBean> results = loginHistoryDao.selectList(criteria, FetchMode.EAGER);
		Assert.assertNotNull(results);
		showEntriesOfCollection(results);

	}

	@Override
	@Test(groups = { "fetch" })
	public void testSelectByPrimaryKeyWithLazyMode() throws Exception {
		LoginHistoryBean loginHistory = loginHistoryDao.select(1, FetchMode.LAZY);
		Assert.assertNotNull(loginHistory);
		testLogger.info("LoginHistory ==> " + loginHistory);

	}

	@Override
	@Test(groups = { "fetch" })
	public void testSelectByPrimaryKeyWithEagerMode() throws Exception {
		LoginHistoryBean loginHistory = loginHistoryDao.select(1, FetchMode.EAGER);
		Assert.assertNotNull(loginHistory);
		testLogger.info("LoginHistory ==> " + loginHistory);
	}

	@Override
	@Test(groups = { "fetch" })
	public void testSelectSingleRecordByCriteriaWithLazyMode() throws Exception {
		HashMap<String, Object> criteria = new HashMap<>();
		criteria.put("id", 1);
		LoginHistoryBean loginHistory = loginHistoryDao.select(criteria, FetchMode.LAZY);
		Assert.assertNotNull(loginHistory);
		testLogger.info("LoginHistory ==> " + loginHistory);

	}

	@Override
	@Test(groups = { "fetch" })
	public void testSelectSingleRecordByCriteriaWithEagerMode() throws Exception {
		HashMap<String, Object> criteria = new HashMap<>();
		criteria.put("id", 1);
		LoginHistoryBean loginHistory = loginHistoryDao.select(criteria, FetchMode.EAGER);
		Assert.assertNotNull(loginHistory);
		testLogger.info("LoginHistory ==> " + loginHistory);
	}

	// --------------------------------- for insertion
	@Override
	@Test(groups = { "insert" })
	@Transactional
	@Rollback(true)
	public void testInsertSingleRecord() throws Exception {
		LoginHistoryBean loginHistory = new LoginHistoryBean();
		loginHistory.setIpAddress("192.168.0.1");
		loginHistory.setLoginDate(LocalDateTime.now());
		loginHistory.setOs("Window-8");
		loginHistory.setUserAgent("Firefox 50");
		loginHistory.setUserId(1);
		long lastInsertedId = loginHistoryDao.insert(loginHistory, TEST_CREATE_USER_ID);
		Assert.assertEquals(true, lastInsertedId > 0);
		testLogger.info("Last inserted ID = " + lastInsertedId);
	}

	@Override
	@Test(groups = { "insert" })
	@Transactional
	@Rollback(true)
	public void testInsertMultiRecords() throws Exception {
		List<LoginHistoryBean> records = new ArrayList<>();
		LoginHistoryBean record1 = new LoginHistoryBean();
		record1.setIpAddress("192.168.0.2");
		record1.setLoginDate(LocalDateTime.now());
		record1.setOs("Window-7");
		record1.setUserAgent("Chrome 32");
		record1.setUserId(1);
		records.add(record1);

		LoginHistoryBean record2 = new LoginHistoryBean();
		record2.setIpAddress("192.168.0.1");
		record2.setLoginDate(LocalDateTime.now());
		record2.setOs("Window-8");
		record2.setUserAgent("Firefox 50");
		record2.setUserId(1);
		records.add(record2);
		loginHistoryDao.insert(records, TEST_CREATE_USER_ID);
	}

}

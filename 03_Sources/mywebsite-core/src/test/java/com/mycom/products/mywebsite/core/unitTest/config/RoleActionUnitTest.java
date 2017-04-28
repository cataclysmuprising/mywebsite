/*
 * @author Mg Than Htike Aung {@literal <rage.cataclysm@gmail.com@address>}
 * @Since 1.0
 * 
 */
package com.mycom.products.mywebsite.core.unitTest.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.mycom.products.mywebsite.core.TestBase;
import com.mycom.products.mywebsite.core.api.XGenericTest;
import com.mycom.products.mywebsite.core.bean.config.RoleActionBean;
import com.mycom.products.mywebsite.core.dao.config.RoleActionDao;
import com.mycom.products.mywebsite.core.util.FetchMode;

public class RoleActionUnitTest extends TestBase implements XGenericTest {
	@Autowired
	private RoleActionDao roleActionDao;
	private Logger testLogger = Logger.getLogger(this.getClass());

	// --------------------------------- for fetching
	@Override
	@Test(groups = { "fetch" })
	public void testSelectAllWithLazyMode() throws Exception {
		List<RoleActionBean> results = roleActionDao.selectList(null, FetchMode.LAZY);
		showEntriesOfCollection(results);
		Assert.assertNotNull(results);
		Assert.assertEquals(true, results.size() > 0);
	}

	@Override
	@Test(groups = { "fetch" })
	public void testSelectAllWithEagerMode() throws Exception {
		List<RoleActionBean> results = roleActionDao.selectList(null, FetchMode.EAGER);
		showEntriesOfCollection(results);
		Assert.assertNotNull(results);
		Assert.assertEquals(true, results.size() > 0);
	}

	@Override
	@Test(groups = { "fetch" })
	public void testSelectAllCount() throws Exception {
		long count = roleActionDao.selectCounts(null);
		testLogger.info("Total counts ==> " + count);
		Assert.assertEquals(true, count > 0);
	}

	@Override
	@Test(groups = { "fetch" })
	public void testSelectRelatedKeysByKey1() throws Exception {
		List<Long> results = roleActionDao.selectByKey1(1);
		showEntriesOfCollection(results);
		Assert.assertNotNull(results);
		Assert.assertEquals(true, results.size() > 0);
	}

	@Override
	@Test(groups = { "fetch" })
	public void testSelectRelatedKeysByKey2() throws Exception {
		List<Long> results = roleActionDao.selectByKey2(1);
		showEntriesOfCollection(results);
		Assert.assertNotNull(results);
		Assert.assertEquals(true, results.size() > 0);
	}

	@Override
	@Test(groups = { "fetch" })
	public void testSelectByKeysWithLazyMode() throws Exception {
		RoleActionBean result = roleActionDao.select(1, 1, FetchMode.LAZY);
		testLogger.info("RoleAction ==> " + result);
		Assert.assertNotNull(result);
	}

	@Override
	@Test(groups = { "fetch" })
	public void testSelectByKeysWithEagerMode() throws Exception {
		RoleActionBean result = roleActionDao.select(1, 1, FetchMode.EAGER);
		testLogger.info("RoleAction ==> " + result);
		Assert.assertNotNull(result);
	}

	@Override
	@Test(groups = { "fetch" })
	public void testSelectMultiRecordByCriteriaWithLazyMode() throws Exception {
		HashMap<String, Object> criteria = new HashMap<>();
		criteria.put("roleId", 1);
		criteria.put("actionId", 1);
		criteria.put("offset", 0);
		criteria.put("limit", 5);
		criteria.put("orderBy", "roleId");
		criteria.put("orderAs", "desc");
		List<RoleActionBean> results = roleActionDao.selectList(criteria, FetchMode.LAZY);
		showEntriesOfCollection(results);
		Assert.assertNotNull(results);
		Assert.assertEquals(true, results.size() > 0);
	}

	@Override
	@Test(groups = { "fetch" })
	public void testSelectMultiRecordByCriteriaWithEagerMode() throws Exception {
		HashMap<String, Object> criteria = new HashMap<>();
		criteria.put("roleId", 1);
		criteria.put("actionId", 1);
		criteria.put("offset", 0);
		criteria.put("limit", 5);
		criteria.put("orderBy", "roleId");
		criteria.put("orderAs", "desc");
		List<RoleActionBean> results = roleActionDao.selectList(criteria, FetchMode.EAGER);
		showEntriesOfCollection(results);
		Assert.assertNotNull(results);
		Assert.assertEquals(true, results.size() > 0);
	}

	// --------------------------------- for insertion
	@Override
	@Test(groups = { "insert" })
	@Transactional
	@Rollback(true)
	public void testInsertSingleRecord() throws Exception {
		RoleActionBean roleAction = new RoleActionBean();
		roleAction.setRoleId(1001);
		roleAction.setActionId(2002);
		roleActionDao.insert(roleAction, TEST_CREATE_USER_ID);
	}

	@Override
	@Test(groups = { "insert" })
	@Transactional
	@Rollback(true)
	public void testInsertMultiRecords() throws Exception {
		List<RoleActionBean> records = new ArrayList<>();
		RoleActionBean record1 = new RoleActionBean();
		record1.setRoleId(1001);
		record1.setActionId(2002);
		records.add(record1);

		RoleActionBean record2 = new RoleActionBean();
		record2.setRoleId(3003);
		record2.setActionId(4004);
		records.add(record2);
		roleActionDao.insert(records, TEST_CREATE_USER_ID);
	}

	@Override
	@Test(groups = { "insert" })
	@Transactional
	@Rollback(true)
	public void testInsertWithKeys() throws Exception {
		roleActionDao.insert(5005, 6006, TEST_CREATE_USER_ID);
	}

	// --------------------------------- for update
	@Override
	@Test(groups = { "update" })
	@Transactional
	@Rollback(true)
	public void testMerge() throws Exception {
		roleActionDao.merge(1, Arrays.asList(new Long[] { 2l, 3l, 4l, 1001l }), TEST_UPDATE_USER_ID);

	}

	// --------------------------------- for delete

	@Override
	@Test(groups = { "delete" })
	@Transactional
	@Rollback(true)
	public void testDeleteByKeys() throws Exception {
		long totalEffectedRows = roleActionDao.delete(1, 1, TEST_UPDATE_USER_ID);
		Assert.assertEquals(true, totalEffectedRows > 0);
		testLogger.info("Total effected rows = " + totalEffectedRows);

	}

	@Override
	@Test(groups = { "delete" })
	@Transactional
	@Rollback(true)
	public void testDeleteByCriteria() throws Exception {
		HashMap<String, Object> criteria = new HashMap<>();
		criteria.put("roleId", 1);
		criteria.put("actionId", 1);
		long totalEffectedRows = roleActionDao.delete(criteria, TEST_UPDATE_USER_ID);
		Assert.assertEquals(true, totalEffectedRows > 0);
		testLogger.info("Total effected rows = " + totalEffectedRows);

	}
}

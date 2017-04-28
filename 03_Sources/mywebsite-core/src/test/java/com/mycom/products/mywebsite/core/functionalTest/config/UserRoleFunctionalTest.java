/*
 * @author Mg Than Htike Aung {@literal <rage.cataclysm@gmail.com@address>}
 * @Since 1.0
 * 
 */
package com.mycom.products.mywebsite.core.functionalTest.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.mycom.products.mywebsite.core.TestBase;
import com.mycom.products.mywebsite.core.api.XGenericTest;
import com.mycom.products.mywebsite.core.bean.config.UserRoleBean;
import com.mycom.products.mywebsite.core.service.config.api.UserRoleService;
import com.mycom.products.mywebsite.core.util.FetchMode;

public class UserRoleFunctionalTest extends TestBase implements XGenericTest {

	@Autowired
	private UserRoleService userRoleService;
	private Logger testLogger = Logger.getLogger(this.getClass());
	// --------------------------------- for fetching

	@Override
	@Test(groups = { "fetch" })
	public void testSelectAllWithLazyMode() throws Exception {
		List<UserRoleBean> results = userRoleService.selectList(null, FetchMode.LAZY);
		showEntriesOfCollection(results);
		Assert.assertNotNull(results);
		Assert.assertEquals(true, results.size() > 0);
	}

	@Override
	@Test(groups = { "fetch" })
	public void testSelectAllWithEagerMode() throws Exception {
		List<UserRoleBean> results = userRoleService.selectList(null, FetchMode.EAGER);
		showEntriesOfCollection(results);
		Assert.assertNotNull(results);
		Assert.assertEquals(true, results.size() > 0);
	}

	@Override
	@Test(groups = { "fetch" })
	public void testSelectAllCount() throws Exception {
		long count = userRoleService.selectCounts(null);
		testLogger.info("Total counts ==> " + count);
		Assert.assertEquals(true, count > 0);
	}

	@Override
	@Test(groups = { "fetch" })
	public void testSelectRelatedKeysByKey1() throws Exception {
		List<Long> results = userRoleService.selectByKey1(1);
		showEntriesOfCollection(results);
		Assert.assertNotNull(results);
		Assert.assertEquals(true, results.size() > 0);
	}

	@Override
	@Test(groups = { "fetch" })
	public void testSelectRelatedKeysByKey2() throws Exception {
		List<Long> results = userRoleService.selectByKey2(1);
		showEntriesOfCollection(results);
		Assert.assertNotNull(results);
		Assert.assertEquals(true, results.size() > 0);
	}

	@Override
	@Test(groups = { "fetch" })
	public void testSelectByKeysWithLazyMode() throws Exception {
		UserRoleBean result = userRoleService.select(1, 1, FetchMode.LAZY);
		testLogger.info("UserRole ==> " + result);
		Assert.assertNotNull(result);
	}

	@Override
	@Test(groups = { "fetch" })
	public void testSelectByKeysWithEagerMode() throws Exception {
		UserRoleBean result = userRoleService.select(1, 1, FetchMode.EAGER);
		testLogger.info("UserRole ==> " + result);
		Assert.assertNotNull(result);
	}

	@Override
	@Test(groups = { "fetch" })
	public void testSelectMultiRecordByCriteriaWithLazyMode() throws Exception {
		HashMap<String, Object> criteria = new HashMap<>();
		criteria.put("userId", 1);
		criteria.put("roleId", 1);
		criteria.put("offset", 0);
		criteria.put("limit", 5);
		criteria.put("orderBy", "roleId");
		criteria.put("orderAs", "desc");
		List<UserRoleBean> results = userRoleService.selectList(criteria, FetchMode.LAZY);
		showEntriesOfCollection(results);
		Assert.assertNotNull(results);
		Assert.assertEquals(true, results.size() > 0);
	}

	@Override
	@Test(groups = { "fetch" })
	public void testSelectMultiRecordByCriteriaWithEagerMode() throws Exception {
		HashMap<String, Object> criteria = new HashMap<>();
		criteria.put("userId", 1);
		criteria.put("roleId", 1);
		criteria.put("offset", 0);
		criteria.put("limit", 5);
		criteria.put("orderBy", "roleId");
		criteria.put("orderAs", "desc");
		List<UserRoleBean> results = userRoleService.selectList(criteria, FetchMode.EAGER);
		showEntriesOfCollection(results);
		Assert.assertNotNull(results);
		Assert.assertEquals(true, results.size() > 0);
	}

	// --------------------------------- for insertion

	@Override
	@Test(groups = { "insert" })
	public void testInsertSingleRecord() throws Exception {
		UserRoleBean userRole = new UserRoleBean();
		userRole.setUserId(1001);
		userRole.setRoleId(2002);
		userRoleService.insert(userRole, TEST_CREATE_USER_ID);
	}

	@Override
	@Test(groups = { "insert" })
	public void testInsertMultiRecords() throws Exception {
		List<UserRoleBean> records = new ArrayList<>();
		UserRoleBean record1 = new UserRoleBean();
		record1.setUserId(1001);
		record1.setRoleId(2002);
		records.add(record1);

		UserRoleBean record2 = new UserRoleBean();
		record2.setUserId(3003);
		record2.setRoleId(4004);
		records.add(record2);
		userRoleService.insert(records, TEST_CREATE_USER_ID);
	}

	@Override
	@Test(groups = { "insert" })
	public void testInsertWithKeys() throws Exception {
		userRoleService.insert(5005, 6006, TEST_CREATE_USER_ID);
	}

	// --------------------------------- for update

	@Override
	@Test(groups = { "update" })
	public void testMerge() throws Exception {
		userRoleService.merge(1, Arrays.asList(new Long[] { 12l, 13l, 14l }), TEST_UPDATE_USER_ID);

	}

	// --------------------------------- for delete

	@Override
	@Test(groups = { "delete" })
	public void testDeleteByKeys() throws Exception {
		long totalEffectedRows = userRoleService.delete(1, 1, TEST_UPDATE_USER_ID);
		Assert.assertEquals(true, totalEffectedRows > 0);
		testLogger.info("Total effected rows = " + totalEffectedRows);

	}

	@Override
	@Test(groups = { "delete" })
	public void testDeleteByCriteria() throws Exception {
		HashMap<String, Object> criteria = new HashMap<>();
		criteria.put("userId", 1);
		criteria.put("roleId", 1);
		long totalEffectedRows = userRoleService.delete(criteria, TEST_UPDATE_USER_ID);
		Assert.assertEquals(true, totalEffectedRows > 0);
		testLogger.info("Total effected rows = " + totalEffectedRows);

	}
}

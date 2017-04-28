/*
 * @author Mg Than Htike Aung {@literal <rage.cataclysm@gmail.com@address>}
 * @Since 1.0
 * 
 */
package com.mycom.products.mywebsite.core.functionalTest.master;

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
import com.mycom.products.mywebsite.core.api.CommonGenericTest;
import com.mycom.products.mywebsite.core.api.StandAloneSelectableTest;
import com.mycom.products.mywebsite.core.bean.master.SettingBean;
import com.mycom.products.mywebsite.core.service.master.api.SettingService;

public class SettingFunctionalTest extends TestBase implements StandAloneSelectableTest, CommonGenericTest {
    @Autowired
    private SettingService settingService;
    private Logger testLogger = Logger.getLogger(this.getClass());
    // --------------------------------- for fetching

    @Override
    @Test(groups = { "fetch" })
    public void testSelectAll() throws Exception {
	List<SettingBean> results = settingService.selectList(null);
	showEntriesOfCollection(results);
	Assert.assertNotNull(results);
	Assert.assertEquals(true, results.size() > 0);
    }

    @Override
    @Test(groups = { "fetch" })
    public void testSelectAllCount() throws Exception {
	long count = settingService.selectCounts(null);
	testLogger.info("Total counts ==> " + count);
	Assert.assertEquals(true, count > 0);
    }

    @Override
    @Test(groups = { "fetch" })
    public void testSelectCountByCriteria() throws Exception {
	HashMap<String, Object> criteria = new HashMap<>();
	criteria.put("id", 1);
	// criteria.put("includeIds", Arrays.asList(new Integer[] { 1, 2, 3 }));
	// criteria.put("excludeIds", Arrays.asList(new Integer[] { 4, 5, 6 }));
	// criteria.put("name", "UploadPath");
	// criteria.put("type", "String");
	// criteria.put("group", "Application");
	// criteria.put("subGroup", "File Directory");
	// criteria.put("value", "MMK");
	// criteria.put("word", "currency");
	long count = settingService.selectCounts(criteria);
	testLogger.info("Total counts ==> " + count);
	Assert.assertEquals(true, count > 0);
    }

    @Override
    @Test(groups = { "fetch" })
    public void testSelectMultiRecordByCriteria() throws Exception {
	HashMap<String, Object> criteria = new HashMap<>();
	criteria.put("id", 1);
	// criteria.put("includeIds", Arrays.asList(new Integer[] { 1, 2, 3 }));
	// criteria.put("excludeIds", Arrays.asList(new Integer[] { 4, 5, 6 }));
	// criteria.put("name", "UploadPath");
	// criteria.put("type", "String");
	// criteria.put("group", "Application");
	// criteria.put("subGroup", "File Directory");
	// criteria.put("value", "MMK");
	// criteria.put("word", "currency");
	criteria.put("offset", 0);
	criteria.put("limit", 5);
	criteria.put("orderBy", "group");
	criteria.put("orderAs", "desc");
	List<SettingBean> results = settingService.selectList(criteria);
	Assert.assertNotNull(results);
	Assert.assertEquals(true, results.size() > 0);
	showEntriesOfCollection(results);
    }

    @Override
    @Test(groups = { "fetch" })
    public void testSelectByPrimaryKey() throws Exception {
	SettingBean setting = settingService.select(1);
	Assert.assertNotNull(setting);
	testLogger.info("Setting ==> " + setting);
    }

    @Override
    @Test(groups = { "fetch" })
    public void testSelectSingleRecordByCriteria() throws Exception {
	HashMap<String, Object> criteria = new HashMap<>();
	criteria.put("id", 1);
	SettingBean setting = settingService.select(criteria);
	Assert.assertNotNull(setting);
	testLogger.info("Setting ==> " + setting);
    }

    // --------------------------------- for insertion

    @Override
    @Test(groups = { "insert" })
    @Transactional
    @Rollback(true)
    public void testInsertSingleRecord() throws Exception {
	SettingBean setting = new SettingBean();
	setting.setName("test_name");
	setting.setValue("test_value");
	setting.setType("test_type");
	setting.setGroup("test_group");
	setting.setSubGroup("test_subgroup");
	long lastInsertedId = settingService.insert(setting, TEST_CREATE_USER_ID);
	Assert.assertEquals(true, lastInsertedId > 0);
	testLogger.info("Last inserted ID = " + lastInsertedId);
    }

    @Override
    @Test(groups = { "insert" })
    @Transactional
    @Rollback(true)
    public void testInsertMultiRecords() throws Exception {
	List<SettingBean> records = new ArrayList<>();
	SettingBean record1 = new SettingBean();
	record1.setName("test_name");
	record1.setValue("test_value");
	record1.setType("test_type");
	record1.setGroup("test_group");
	record1.setSubGroup("test_subgroup");
	records.add(record1);

	SettingBean record2 = new SettingBean();
	record2.setName("test_name2");
	record2.setValue("test_value2");
	record2.setType("test_type2");
	record2.setGroup("test_group2");
	record2.setSubGroup("test_subgroup2");
	records.add(record2);
	settingService.insert(records, TEST_CREATE_USER_ID);
    }

    // --------------------------------- for update

    @Override
    @Test(groups = { "update" })
    @Transactional
    @Rollback(true)
    public void testSingleRecordUpdate() throws Exception {
	SettingBean setting = new SettingBean();
	setting.setId(1);
	setting.setName("test_name");
	setting.setValue("test_value");
	setting.setType("test_type");
	setting.setGroup("test_group");
	setting.setSubGroup("test_subgroup");
	long totalEffectedRows = settingService.update(setting, TEST_UPDATE_USER_ID);
	testLogger.info("Total effected rows = " + totalEffectedRows);
    }

    @Override
    @Test(groups = { "update" })
    @Transactional
    @Rollback(true)
    public void testMultiRecordsUpdate() throws Exception {
	List<SettingBean> records = new ArrayList<>();
	SettingBean record1 = new SettingBean();
	record1.setId(1);
	record1.setName("test_name");
	record1.setValue("test_value");
	record1.setType("test_type");
	record1.setGroup("test_group");
	record1.setSubGroup("test_subgroup");
	records.add(record1);

	SettingBean record2 = new SettingBean();
	record2.setId(2);
	record2.setName("test_name2");
	record2.setValue("test_value2");
	record2.setType("test_type2");
	record2.setGroup("test_group2");
	record2.setSubGroup("test_subgroup2");
	records.add(record2);
	settingService.update(records, TEST_UPDATE_USER_ID);
    }

    @Override
    @Test(groups = { "update" })
    @Transactional
    @Rollback(true)
    public void testUpdateByCriteria() throws Exception {
	HashMap<String, Object> criteria = new HashMap<>();
	criteria.put("id", 280);
	// criteria.put("includeIds", Arrays.asList(new Integer[] { 1, 2, 3 }));
	// criteria.put("excludeIds", Arrays.asList(new Integer[] { 4, 5, 6 }));
	// criteria.put("name", "UploadPath");
	// criteria.put("type", "String");
	// criteria.put("group", "Application");
	// criteria.put("subGroup", "File Directory");
	// criteria.put("value", "MMK");

	HashMap<String, Object> updateItems = new HashMap<>();
	updateItems.put("name", "test_name");
	updateItems.put("value", "test_value");
	updateItems.put("type", "test_type");
	updateItems.put("group", "test_group");
	updateItems.put("subGroup", "test_subgroup");

	settingService.update(criteria, updateItems, TEST_UPDATE_USER_ID);
    }

    // --------------------------------- for delete

    @Override
    @Test(groups = { "delete" })
    @Transactional
    @Rollback(true)
    public void testDeleteByPrimaryKey() throws Exception {
	long totalEffectedRows = settingService.delete(1, TEST_UPDATE_USER_ID);
	Assert.assertEquals(true, totalEffectedRows > 0);
	testLogger.info("Total effected rows = " + totalEffectedRows);
    }

    @Override
    @Test(groups = { "delete" })
    @Transactional
    @Rollback(true)
    public void testDeleteByCriteria() throws Exception {
	HashMap<String, Object> criteria = new HashMap<>();
	criteria.put("id", 280);
	// criteria.put("includeIds", Arrays.asList(new Integer[] { 1, 2, 3 }));
	// criteria.put("excludeIds", Arrays.asList(new Integer[] { 4, 5, 6 }));
	// criteria.put("name", "UploadPath");
	// criteria.put("type", "String");
	// criteria.put("group", "Application");
	// criteria.put("subGroup", "File Directory");
	// criteria.put("value", "MMK");

	long totalEffectedRows = settingService.delete(criteria, TEST_UPDATE_USER_ID);
	Assert.assertEquals(true, totalEffectedRows > 0);
	testLogger.info("Total effected rows = " + totalEffectedRows);
    }
}

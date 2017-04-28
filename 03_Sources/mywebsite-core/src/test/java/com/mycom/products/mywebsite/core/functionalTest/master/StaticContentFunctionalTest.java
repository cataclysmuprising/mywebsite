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
import com.mycom.products.mywebsite.core.bean.master.StaticContentBean;
import com.mycom.products.mywebsite.core.bean.master.StaticContentBean.FileType;
import com.mycom.products.mywebsite.core.service.master.api.StaticContentService;

public class StaticContentFunctionalTest extends TestBase implements StandAloneSelectableTest, CommonGenericTest {
    @Autowired
    private StaticContentService staticContentService;
    private Logger testLogger = Logger.getLogger(this.getClass());

    // --------------------------------- for fetching

    @Override
    @Test(groups = { "fetch" })
    public void testSelectAll() throws Exception {
	List<StaticContentBean> results = staticContentService.selectList(null);
	showEntriesOfCollection(results);
	Assert.assertNotNull(results);
	Assert.assertEquals(true, results.size() > 0);
    }

    @Override
    @Test(groups = { "fetch" })
    public void testSelectAllCount() throws Exception {
	long count = staticContentService.selectCounts(null);
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
	// criteria.put("fileType", FileType.IMAGE);
	// criteria.put("fileName", "super_user.jpg");
	// criteria.put("fileSize", "10KB");
	// criteria.put("word", "avatar");
	long count = staticContentService.selectCounts(criteria);
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
	// criteria.put("fileType", FileType.IMAGE);
	// criteria.put("fileName", "super_user.jpg");
	// criteria.put("fileSize", "10KB");
	// criteria.put("word", "avatar");
	criteria.put("offset", 0);
	criteria.put("limit", 5);
	criteria.put("orderBy", "fileName");
	criteria.put("orderAs", "desc");
	List<StaticContentBean> results = staticContentService.selectList(criteria);
	Assert.assertNotNull(results);
	Assert.assertEquals(true, results.size() > 0);
	showEntriesOfCollection(results);
    }

    @Override
    @Test(groups = { "fetch" })
    public void testSelectByPrimaryKey() throws Exception {
	StaticContentBean staticContent = staticContentService.select(1);
	Assert.assertNotNull(staticContent);
	testLogger.info("StaticContent ==> " + staticContent);
    }

    @Override
    @Test(groups = { "fetch" })
    public void testSelectSingleRecordByCriteria() throws Exception {
	HashMap<String, Object> criteria = new HashMap<>();
	criteria.put("id", 1);
	StaticContentBean staticContent = staticContentService.select(criteria);
	Assert.assertNotNull(staticContent);
	testLogger.info("StaticContent ==> " + staticContent);
    }

    // --------------------------------- for insertion

    @Override
    @Test(groups = { "insert" })
    @Transactional
    @Rollback(true)
    public void testInsertSingleRecord() throws Exception {
	StaticContentBean content = new StaticContentBean();
	content.setFileType(FileType.IMAGE);
	content.setFileName("favicon.ico");
	content.setFileSize("120KB");
	content.setFilePath("D:/web-resources/mywebsite/uploadFiles/avatar/favicon_123.jpg");
	long lastInsertedId = staticContentService.insert(content, TEST_CREATE_USER_ID);
	Assert.assertEquals(true, lastInsertedId > 0);
	testLogger.info("Last inserted ID = " + lastInsertedId);
    }

    @Override
    @Test(groups = { "insert" })
    @Transactional
    @Rollback(true)
    public void testInsertMultiRecords() throws Exception {
	List<StaticContentBean> records = new ArrayList<>();
	StaticContentBean record1 = new StaticContentBean();
	record1.setFileType(FileType.IMAGE);
	record1.setFileName("favicon.ico");
	record1.setFileSize("120KB");
	record1.setFilePath("D:/web-resources/mywebsite/uploadFiles/avatar/favicon_123.jpg");
	records.add(record1);

	StaticContentBean record2 = new StaticContentBean();
	record2.setFileType(FileType.PDF);
	record2.setFileName("user_manual.pdf");
	record2.setFileSize("3MB");
	record2.setFilePath("D:/web-resources/mywebsite/uploadFiles/guides/user_manual_321.pdf");
	records.add(record2);
	staticContentService.insert(records, TEST_CREATE_USER_ID);
    }

    // --------------------------------- for update

    @Override
    @Test(groups = { "update" })
    @Transactional
    @Rollback(true)
    public void testSingleRecordUpdate() throws Exception {
	StaticContentBean content = new StaticContentBean();
	content.setId(1);
	content.setFileType(FileType.IMAGE);
	content.setFileName("favicon.ico");
	content.setFileSize("120KB");
	content.setFilePath("D:/web-resources/mywebsite/uploadFiles/avatar/favicon_123.jpg");
	long totalEffectedRows = staticContentService.update(content, TEST_UPDATE_USER_ID);
	testLogger.info("Total effected rows = " + totalEffectedRows);
    }

    @Override
    @Test(groups = { "update" })
    @Transactional
    @Rollback(true)
    public void testMultiRecordsUpdate() throws Exception {
	List<StaticContentBean> records = new ArrayList<>();
	StaticContentBean record1 = new StaticContentBean();
	record1.setId(1);
	record1.setFileType(FileType.IMAGE);
	record1.setFileName("favicon.ico");
	record1.setFileSize("120KB");
	record1.setFilePath("D:/web-resources/mywebsite/uploadFiles/avatar/favicon_123.jpg");
	records.add(record1);

	StaticContentBean record2 = new StaticContentBean();
	record2.setId(1);
	record2.setFileType(FileType.PDF);
	record2.setFileName("user_manual.pdf");
	record2.setFileSize("3MB");
	record2.setFilePath("D:/web-resources/mywebsite/uploadFiles/guides/user_manual_321.pdf");
	records.add(record2);
	staticContentService.update(records, TEST_UPDATE_USER_ID);
    }

    @Override
    @Test(groups = { "update" })
    @Transactional
    @Rollback(true)
    public void testUpdateByCriteria() throws Exception {
	HashMap<String, Object> criteria = new HashMap<>();
	criteria.put("id", 1);
	// criteria.put("includeIds", Arrays.asList(new Integer[] { 1, 2, 3 }));
	// criteria.put("excludeIds", Arrays.asList(new Integer[] { 4, 5, 6 }));
	// criteria.put("fileType", FileType.IMAGE);
	// criteria.put("fileName", "super_user.jpg");
	// criteria.put("fileSize", "10KB");

	HashMap<String, Object> updateItems = new HashMap<>();
	updateItems.put("fileType", FileType.IMAGE);
	updateItems.put("fileName", "logo.png");
	updateItems.put("fileSize", "50KB");

	staticContentService.update(criteria, updateItems, TEST_UPDATE_USER_ID);
    }

    // --------------------------------- for delete

    @Override
    @Test(groups = { "delete" })
    @Transactional
    @Rollback(true)
    public void testDeleteByPrimaryKey() throws Exception {
	long totalEffectedRows = staticContentService.delete(1, TEST_UPDATE_USER_ID);
	Assert.assertEquals(true, totalEffectedRows > 0);
	testLogger.info("Total effected rows = " + totalEffectedRows);
    }

    @Override
    @Test(groups = { "delete" })
    @Transactional
    @Rollback(true)
    public void testDeleteByCriteria() throws Exception {
	HashMap<String, Object> criteria = new HashMap<>();
	criteria.put("id", 1);
	// criteria.put("includeIds", Arrays.asList(new Integer[] { 1, 2, 3 }));
	// criteria.put("excludeIds", Arrays.asList(new Integer[] { 4, 5, 6 }));
	// criteria.put("fileType", FileType.IMAGE);
	// criteria.put("fileName", "super_user.jpg");
	// criteria.put("fileSize", "10KB");

	long totalEffectedRows = staticContentService.delete(criteria, TEST_UPDATE_USER_ID);
	Assert.assertEquals(true, totalEffectedRows > 0);
	testLogger.info("Total effected rows = " + totalEffectedRows);
    }

}

/*
 * @author Mg Than Htike Aung {@literal <rage.cataclysm@gmail.com@address>}
 * @Since 1.0
 * 
 */
package com.mycom.products.mywebsite.core.unitTest.config;

import java.time.LocalDate;
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
import com.mycom.products.mywebsite.core.api.JoinedSelectableTest;
import com.mycom.products.mywebsite.core.bean.config.UserBean;
import com.mycom.products.mywebsite.core.bean.config.UserBean.Gender;
import com.mycom.products.mywebsite.core.dao.config.UserDao;
import com.mycom.products.mywebsite.core.util.Cryptographic;
import com.mycom.products.mywebsite.core.util.FetchMode;

public class UserUnitTest extends TestBase implements JoinedSelectableTest, CommonGenericTest {
    @Autowired
    private UserDao userDao;
    private Logger testLogger = Logger.getLogger(this.getClass());

    // --------------------------------- for fetching
    @Override
    @Test(groups = { "fetch" })
    public void testSelectAllWithLazyMode() throws Exception {
	List<UserBean> results = userDao.selectList(null, FetchMode.LAZY);
	showEntriesOfCollection(results);
	Assert.assertNotNull(results);
	Assert.assertEquals(true, results.size() > 0);
    }

    @Override
    @Test(groups = { "fetch" })
    public void testSelectAllWithEagerMode() throws Exception {
	List<UserBean> results = userDao.selectList(null, FetchMode.EAGER);
	showEntriesOfCollection(results);
	Assert.assertNotNull(results);
	Assert.assertEquals(true, results.size() > 0);
    }

    @Override
    @Test(groups = { "fetch" })
    public void testSelectAllCount() throws Exception {
	long count = userDao.selectCounts(null, null);
	testLogger.info("Total counts ==> " + count);
	Assert.assertEquals(true, count > 0);
    }

    @Override
    @Test(groups = { "fetch" })
    public void testSelectCountWithLazyCriteria() throws Exception {
	HashMap<String, Object> criteria = new HashMap<>();
	criteria.put("id", 1);
	// criteria.put("includeIds", Arrays.asList(new Integer[] { 1, 2, 3 }));
	// criteria.put("excludeIds", Arrays.asList(new Integer[] { 4, 5, 6 }));
	// criteria.put("loginId", "super-user");
	// criteria.put("name", "Super User");
	// criteria.put("email", "superuser@gmail.com");
	// criteria.put("nrc", "12/KMY(N)123455");
	// criteria.put("phone", "09779988530");
	// criteria.put("gender", Gender.MALE);
	// criteria.put("word", "user");
	long count = userDao.selectCounts(criteria, FetchMode.LAZY);
	testLogger.info("Total counts ==> " + count);
	Assert.assertEquals(true, count > 0);
    }

    @Override
    @Test(groups = { "fetch" })
    public void testSelectCountWithEagerCriteria() throws Exception {
	HashMap<String, Object> criteria = new HashMap<>();
	criteria.put("roleId", 1);
	// criteria.put("roleName", "SUPER_USER");
	// criteria.put("contentId", 1);
	// criteria.put("id", 1);
	// criteria.put("includeIds", Arrays.asList(new Integer[] { 1, 2, 3 }));
	// criteria.put("excludeIds", Arrays.asList(new Integer[] { 4, 5, 6 }));
	// criteria.put("loginId", "super-user");
	// criteria.put("name", "Super User");
	// criteria.put("email", "superuser@gmail.com");
	// criteria.put("nrc", "12/KMY(N)123455");
	// criteria.put("phone", "09779988530");
	// criteria.put("gender", Gender.MALE);
	// criteria.put("word", "user");
	long count = userDao.selectCounts(criteria, FetchMode.EAGER);
	testLogger.info("Total counts ==> " + count);
	Assert.assertEquals(true, count > 0);
    }

    @Override
    @Test(groups = { "fetch" })
    public void testSelectMultiRecordByCriteriaWithLazyMode() throws Exception {
	HashMap<String, Object> criteria = new HashMap<>();
	criteria.put("id", 1);
	// criteria.put("includeIds", Arrays.asList(new Integer[] { 1, 2, 3 }));
	// criteria.put("excludeIds", Arrays.asList(new Integer[] { 4, 5, 6 }));
	// criteria.put("age", 27);
	// criteria.put("loginId", "super-user");
	// criteria.put("name", "Super User");
	// criteria.put("email", "superuser@gmail.com");
	// criteria.put("nrc", "12/KMY(N)123455");
	// criteria.put("phone", "09-000000001");
	// criteria.put("gender", Gender.MALE);
	// criteria.put("word", "user");
	criteria.put("offset", 0);
	criteria.put("limit", 5);
	criteria.put("orderBy", "loginDate");
	criteria.put("orderAs", "desc");
	List<UserBean> results = userDao.selectList(criteria, FetchMode.LAZY);
	Assert.assertNotNull(results);
	Assert.assertEquals(true, results.size() > 0);
	showEntriesOfCollection(results);
    }

    @Override
    @Test(groups = { "fetch" })
    public void testSelectMultiRecordByCriteriaWithEagerMode() throws Exception {
	HashMap<String, Object> criteria = new HashMap<>();
	criteria.put("roleId", 1);
	// criteria.put("roleName", "SUPER_USER");
	// criteria.put("contentId", 1);
	// criteria.put("id", 1);
	// criteria.put("includeIds", Arrays.asList(new Integer[] { 1, 2, 3 }));
	// criteria.put("excludeIds", Arrays.asList(new Integer[] { 4, 5, 6 }));
	// criteria.put("age", 27);
	// criteria.put("loginId", "super-user");
	// criteria.put("name", "Super User");
	// criteria.put("email", "superuser@gmail.com");
	// criteria.put("nrc", "12/KMY(N)123455");
	// criteria.put("phone", "09-000000001");
	// criteria.put("gender", Gender.MALE);
	// criteria.put("word", "user");
	criteria.put("offset", 0);
	criteria.put("limit", 5);
	criteria.put("orderBy", "loginDate");
	criteria.put("orderAs", "desc");
	List<UserBean> results = userDao.selectList(criteria, FetchMode.EAGER);
	Assert.assertNotNull(results);
	Assert.assertEquals(true, results.size() > 0);
	showEntriesOfCollection(results);
    }

    @Override
    @Test(groups = { "fetch" })
    public void testSelectByPrimaryKeyWithLazyMode() throws Exception {
	UserBean user = userDao.select(1, FetchMode.LAZY);
	Assert.assertNotNull(user);
	testLogger.info("User ==> " + user);
    }

    @Override
    @Test(groups = { "fetch" })
    public void testSelectByPrimaryKeyWithEagerMode() throws Exception {
	UserBean user = userDao.select(1, FetchMode.EAGER);
	Assert.assertNotNull(user);
	testLogger.info("User ==> " + user);
    }

    @Override
    @Test(groups = { "fetch" })
    public void testSelectSingleRecordByCriteriaWithLazyMode() throws Exception {
	HashMap<String, Object> criteria = new HashMap<>();
	criteria.put("id", 1);
	UserBean user = userDao.select(criteria, FetchMode.LAZY);
	Assert.assertNotNull(user);
	testLogger.info("User ==> " + user);
    }

    @Override
    @Test(groups = { "fetch" })
    public void testSelectSingleRecordByCriteriaWithEagerMode() throws Exception {
	HashMap<String, Object> criteria = new HashMap<>();
	criteria.put("id", 1);
	UserBean user = userDao.select(criteria, FetchMode.EAGER);
	Assert.assertNotNull(user);
	testLogger.info("User ==> " + user);
    }

    // --------------------------------- for insertion
    @Override
    @Test(groups = { "insert" })
    @Transactional
    @Rollback(true)
    public void testInsertSingleRecord() throws Exception {
	UserBean user = new UserBean();
	user.setLoginId("admin");
	user.setContentId(2);
	user.setAge(25);
	user.setName("John");
	user.setGender(Gender.MALE);
	user.setEmail("john@gmail.com");
	user.setPassword(Cryptographic.getSha256CheckSum("admin-pwd"));
	user.setNrc("12/YGN 123456");
	user.setPhone("+95-9000000001");
	user.setDob(LocalDate.now());
	user.setAddress("California");
	long lastInsertedId = userDao.insert(user, TEST_CREATE_USER_ID);
	Assert.assertEquals(true, lastInsertedId > 0);
	testLogger.info("Last inserted ID = " + lastInsertedId);
    }

    @Override
    @Test(groups = { "insert" })
    @Transactional
    @Rollback(true)
    public void testInsertMultiRecords() throws Exception {
	List<UserBean> records = new ArrayList<>();
	UserBean record1 = new UserBean();
	record1.setLoginId("admin");
	record1.setContentId(2);
	record1.setAge(25);
	record1.setName("John");
	record1.setGender(Gender.MALE);
	record1.setEmail("john@gmail.com");
	record1.setPassword(Cryptographic.getSha256CheckSum("admin-pwd"));
	record1.setNrc("12/YGN 123456");
	record1.setPhone("+95-9000000001");
	record1.setDob(LocalDate.now());
	record1.setAddress("California");
	records.add(record1);

	UserBean record2 = new UserBean();
	record2.setLoginId("user");
	record2.setContentId(3);
	record2.setAge(24);
	record2.setName("Sarah");
	record2.setGender(Gender.MALE);
	record2.setEmail("sarah@gmail.com");
	record2.setPassword(Cryptographic.getSha256CheckSum("user-pwd"));
	record2.setNrc("12/YGN 654321");
	record2.setPhone("+95-9000000002");
	record2.setDob(LocalDate.now());
	record2.setAddress("Washington");
	records.add(record2);
	userDao.insert(records, TEST_CREATE_USER_ID);
    }

    // --------------------------------- for update
    @Override
    @Test(groups = { "update" })
    @Transactional
    @Rollback(true)
    public void testSingleRecordUpdate() throws Exception {
	UserBean user = new UserBean();
	user.setId(1);
	user.setContentId(2);
	user.setAge(25);
	user.setName("John");
	user.setGender(Gender.MALE);
	user.setNrc("12/YGN 123456");
	user.setPhone("+95-9000000001");
	user.setDob(LocalDate.now());
	user.setAddress("California");
	long totalEffectedRows = userDao.update(user, TEST_UPDATE_USER_ID);
	testLogger.info("Total effected rows = " + totalEffectedRows);
    }

    @Override
    @Test(groups = { "update" })
    @Transactional
    @Rollback(true)
    public void testMultiRecordsUpdate() throws Exception {
	List<UserBean> records = new ArrayList<>();
	UserBean record1 = new UserBean();
	record1.setId(1);
	record1.setContentId(2);
	record1.setAge(25);
	record1.setName("John");
	record1.setGender(Gender.MALE);
	record1.setNrc("12/YGN 123456");
	record1.setPhone("+95-9000000001");
	record1.setDob(LocalDate.now());
	record1.setAddress("California");
	records.add(record1);

	UserBean record2 = new UserBean();
	record2.setId(1);
	record2.setContentId(3);
	record2.setAge(24);
	record2.setName("Sarah");
	record2.setGender(Gender.MALE);
	record2.setNrc("12/YGN 654321");
	record2.setPhone("+95-9000000002");
	record2.setDob(LocalDate.now());
	record2.setAddress("Washington");
	records.add(record2);
	userDao.update(records, TEST_UPDATE_USER_ID);
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
	criteria.put("age", 27);
	criteria.put("loginId", "super-user");
	criteria.put("name", "Super User");
	criteria.put("email", "superuser@gmail.com");
	criteria.put("nrc", "12/KMY(N)123455");
	criteria.put("phone", "09-000000001");
	criteria.put("gender", Gender.MALE);

	HashMap<String, Object> updateItems = new HashMap<>();
	updateItems.put("name", "USER");
	updateItems.put("password", Cryptographic.getSha256CheckSum("user-pwd"));

	userDao.update(criteria, updateItems, TEST_UPDATE_USER_ID);
    }

    // --------------------------------- for delete

    @Override
    @Test(groups = { "delete" })
    @Transactional
    @Rollback(true)
    public void testDeleteByPrimaryKey() throws Exception {
	long totalEffectedRows = userDao.delete(1, TEST_UPDATE_USER_ID);
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
	criteria.put("age", 27);
	criteria.put("loginId", "super-user");
	criteria.put("name", "Super User");
	criteria.put("email", "superuser@gmail.com");
	criteria.put("nrc", "12/KMY(N)123455");
	criteria.put("phone", "09-000000001");
	criteria.put("gender", Gender.MALE);

	long totalEffectedRows = userDao.delete(criteria, TEST_UPDATE_USER_ID);
	Assert.assertEquals(true, totalEffectedRows > 0);
	testLogger.info("Total effected rows = " + totalEffectedRows);
    }

}

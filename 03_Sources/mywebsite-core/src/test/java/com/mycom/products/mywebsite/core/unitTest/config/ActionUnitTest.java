/*
 * @author Mg Than Htike Aung {@literal <rage.cataclysm@gmail.com@address>}
 * @Since 1.0
 * 
 */
package com.mycom.products.mywebsite.core.unitTest.config;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.mycom.products.mywebsite.core.TestBase;
import com.mycom.products.mywebsite.core.api.JoinedSelectableTest;
import com.mycom.products.mywebsite.core.bean.config.ActionBean;
import com.mycom.products.mywebsite.core.dao.config.ActionDao;
import com.mycom.products.mywebsite.core.util.FetchMode;

public class ActionUnitTest extends TestBase implements JoinedSelectableTest {
    @Autowired
    private ActionDao actionDao;
    private Logger testLogger = Logger.getLogger(this.getClass());

    @Override
    @Test(groups = { "fetch" })
    public void testSelectAllWithLazyMode() throws Exception {
	List<ActionBean> results = actionDao.selectList(null, FetchMode.LAZY);
	showEntriesOfCollection(results);
	Assert.assertNotNull(results);
	Assert.assertEquals(true, results.size() > 0);
    }

    @Override
    @Test(groups = { "fetch" })
    public void testSelectAllWithEagerMode() throws Exception {
	List<ActionBean> results = actionDao.selectList(null, FetchMode.EAGER);
	showEntriesOfCollection(results);
	Assert.assertNotNull(results);
	Assert.assertEquals(true, results.size() > 0);
    }

    @Override
    @Test(groups = { "fetch" })
    public void testSelectAllCount() throws Exception {
	long count = actionDao.selectCounts(null, null);
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
	// criteria.put("page", "User");
	// criteria.put("actionName", "userAdd");
	// criteria.put("actionType", ActionType.SUB);
	// criteria.put("word", "user");
	long count = actionDao.selectCounts(criteria, FetchMode.LAZY);
	testLogger.info("Total counts ==> " + count);
	Assert.assertEquals(true, count > 0);
    }

    @Override
    @Test(groups = { "fetch" })
    public void testSelectCountWithEagerCriteria() throws Exception {
	HashMap<String, Object> criteria = new HashMap<>();
	criteria.put("roleId", 1);
	// criteria.put("id", 1);
	// criteria.put("includeIds", Arrays.asList(new Integer[] { 1, 2, 3 }));
	// criteria.put("excludeIds", Arrays.asList(new Integer[] { 4, 5, 6 }));
	// criteria.put("page", "User");
	// criteria.put("actionName", "userAdd");
	// criteria.put("actionType", ActionType.SUB);
	// criteria.put("word", "user");
	long count = actionDao.selectCounts(criteria, FetchMode.EAGER);
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
	// criteria.put("page", "User");
	// criteria.put("actionName", "userAdd");
	// criteria.put("actionType", ActionType.SUB);
	// criteria.put("word", "user");
	criteria.put("offset", 0);
	criteria.put("limit", 5);
	criteria.put("orderBy", "page");
	criteria.put("orderAs", "desc");
	List<ActionBean> results = actionDao.selectList(criteria, FetchMode.LAZY);
	Assert.assertNotNull(results);
	Assert.assertEquals(true, results.size() > 0);
	showEntriesOfCollection(results);
    }

    @Override
    @Test(groups = { "fetch" })
    public void testSelectMultiRecordByCriteriaWithEagerMode() throws Exception {
	HashMap<String, Object> criteria = new HashMap<>();
	criteria.put("roleId", 1);
	// criteria.put("id", 1);
	// criteria.put("includeIds", Arrays.asList(new Integer[] { 1, 2, 3 }));
	// criteria.put("excludeIds", Arrays.asList(new Integer[] { 4, 5, 6 }));
	// criteria.put("page", "User");
	// criteria.put("actionName", "userAdd");
	// criteria.put("actionType", ActionType.SUB);
	// criteria.put("word", "user");
	criteria.put("offset", 0);
	criteria.put("limit", 5);
	criteria.put("orderBy", "page");
	criteria.put("orderAs", "desc");
	List<ActionBean> results = actionDao.selectList(criteria, FetchMode.EAGER);
	Assert.assertNotNull(results);
	Assert.assertEquals(true, results.size() > 0);
	showEntriesOfCollection(results);
    }

    @Override
    @Test(groups = { "fetch" })
    public void testSelectByPrimaryKeyWithLazyMode() throws Exception {
	ActionBean action = actionDao.select(1, FetchMode.LAZY);
	Assert.assertNotNull(action);
	testLogger.info("Action ==> " + action);
    }

    @Override
    @Test(groups = { "fetch" })
    public void testSelectByPrimaryKeyWithEagerMode() throws Exception {
	ActionBean action = actionDao.select(1, FetchMode.EAGER);
	Assert.assertNotNull(action);
	testLogger.info("Action ==> " + action);
    }

    @Override
    @Test(groups = { "fetch" })
    public void testSelectSingleRecordByCriteriaWithLazyMode() throws Exception {
	HashMap<String, Object> criteria = new HashMap<>();
	criteria.put("id", 1);
	ActionBean action = actionDao.select(criteria, FetchMode.LAZY);
	Assert.assertNotNull(action);
	testLogger.info("Action ==> " + action);
    }

    @Override
    @Test(groups = { "fetch" })
    public void testSelectSingleRecordByCriteriaWithEagerMode() throws Exception {
	HashMap<String, Object> criteria = new HashMap<>();
	criteria.put("id", 1);
	ActionBean action = actionDao.select(criteria, FetchMode.EAGER);
	Assert.assertNotNull(action);
	testLogger.info("Action ==> " + action);
    }

    @Test(groups = { "fetch" })
    public void testSelectAllPageNamesByModule() throws Exception {
	List<String> pageNames = actionDao.selectPageNamesByModule("back-end");
	Assert.assertNotNull(pageNames);
	Assert.assertEquals(true, pageNames.size() > 0);
	showEntriesOfCollection(pageNames);
    }

}
/*
 * @author Mg Than Htike Aung {@literal <rage.cataclysm@gmail.com@address>}
 * @Since 1.0
 * 
 */
package com.mycom.products.mywebsite.core;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import com.mycom.products.mywebsite.core.mapper.BaseMapper;

@ContextConfiguration(locations = { "classpath:spring-test-context.xml" })
public class TestBase extends AbstractTransactionalTestNGSpringContextTests {
	private static final Logger testLogger = Logger.getLogger("testLogs." + TestBase.class.getName());
	protected static final int TEST_CREATE_USER_ID = 10009;
	protected static final int TEST_UPDATE_USER_ID = 90001;

	@Autowired
	private BaseMapper baseMapper;

	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		baseMapper.disableAllConstraints();
	}

	@AfterClass(alwaysRun = true)
	public void afterClass() {
		baseMapper.enableAllConstraints();
	}

	@BeforeMethod
	public void beforeMethod(Method method) {
		testLogger.info("***** DAO-TEST : Testing method '" + method.getName() + "' has started. *****");
	}

	@AfterMethod
	public void afterMethod(Method method) {
		testLogger.info("----- DAO-TEST : Testing method '" + method.getName() + "' has finished. -----");
	}

	protected <T> void showEntriesOfCollection(Collection<T> collection) {
		if (collection != null) {
			Iterator<?> iterator = collection.iterator();
			while (iterator.hasNext()) {
				Object obj = iterator.next();
				testLogger.info(" >>> " + obj.toString());
			}
		}
	}

}
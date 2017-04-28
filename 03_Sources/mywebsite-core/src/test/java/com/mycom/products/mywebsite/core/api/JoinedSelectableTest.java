/*
 * @author Mg Than Htike Aung {@literal <rage.cataclysm@gmail.com@address>}
 * @Since 1.0
 * 
 */
package com.mycom.products.mywebsite.core.api;

public interface JoinedSelectableTest {

	public void testSelectAllWithLazyMode() throws Exception;

	public void testSelectAllWithEagerMode() throws Exception;

	public void testSelectAllCount() throws Exception;

	public void testSelectCountWithLazyCriteria() throws Exception;

	public void testSelectCountWithEagerCriteria() throws Exception;

	public void testSelectMultiRecordByCriteriaWithLazyMode() throws Exception;

	public void testSelectMultiRecordByCriteriaWithEagerMode() throws Exception;

	public void testSelectByPrimaryKeyWithLazyMode() throws Exception;

	public void testSelectByPrimaryKeyWithEagerMode() throws Exception;

	public void testSelectSingleRecordByCriteriaWithLazyMode() throws Exception;

	public void testSelectSingleRecordByCriteriaWithEagerMode() throws Exception;

}

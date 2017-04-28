/*
 * @author Mg Than Htike Aung {@literal <rage.cataclysm@gmail.com@address>}
 * @Since 1.0
 * 
 */
package com.mycom.products.mywebsite.core.api;

public interface XSelectableTest {

	public void testSelectAllWithLazyMode() throws Exception;

	public void testSelectAllWithEagerMode() throws Exception;

	public void testSelectAllCount() throws Exception;

	public void testSelectRelatedKeysByKey1() throws Exception;

	public void testSelectRelatedKeysByKey2() throws Exception;

	public void testSelectByKeysWithLazyMode() throws Exception;

	public void testSelectByKeysWithEagerMode() throws Exception;

	public void testSelectMultiRecordByCriteriaWithLazyMode() throws Exception;

	public void testSelectMultiRecordByCriteriaWithEagerMode() throws Exception;

}

/*
 * @author Mg Than Htike Aung {@literal <rage.cataclysm@gmail.com@address>}
 * @Since 1.0
 * 
 */
package com.mycom.products.mywebsite.core.api;

public interface XGenericTest extends InsertableTest, XSelectableTest {

	public void testInsertWithKeys() throws Exception;

	public void testDeleteByKeys() throws Exception;

	public void testDeleteByCriteria() throws Exception;

	public void testMerge() throws Exception;

}

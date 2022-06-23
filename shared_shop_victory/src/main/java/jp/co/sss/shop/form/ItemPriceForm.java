package jp.co.sss.shop.form;

import javax.validation.constraints.NotNull;

/**
 * 商品情報のフォーム
 *
 * @author SystemShared
 */
public class ItemPriceForm {

	/**
	 * 価格
	 */

	@NotNull
	private int itemMinPrice;
	public int getItemMinPrice() {
		return itemMinPrice;
	}
	public void setItemMinPrice(int itemMinPrice) {
		this.itemMinPrice = itemMinPrice;
	}
	
	@NotNull
	private int itemMaxPrice;
	
	public int getItemMaxPrice() {
		return itemMaxPrice;
	}
	public void setItemMaxPrice(int itemMaxPrice) {
		this.itemMaxPrice = itemMaxPrice;
	}
}

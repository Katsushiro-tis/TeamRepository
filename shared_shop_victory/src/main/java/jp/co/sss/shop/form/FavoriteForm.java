package jp.co.sss.shop.form;

import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.entity.User;

/**
 * 商品情報のフォーム
 *
 * @author SystemShared
 */
public class FavoriteForm {

	/**
	 * ID
	 */
	private String id;
	/**
	 * username
	 */
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	private User user;
	private Item item;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}
}
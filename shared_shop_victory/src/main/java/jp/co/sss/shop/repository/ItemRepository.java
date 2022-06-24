package jp.co.sss.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jp.co.sss.shop.entity.Category;
import jp.co.sss.shop.entity.Item;

/**
 * itemsテーブル用リポジトリ
 *
 * @author System Shared
 */
@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

	/**
	 * 商品情報を新着順で検索
	 * 
	 * @param pageable
	 */
	public List<Item> findByDeleteFlagOrderByInsertDateDescIdAsc(int deleteFlag);

	public List<Item> findByCategoryAndDeleteFlag(Category category, Integer deleteFlag);

	@Query(value = "select i.id, i.ID, i.NAME, i.PRICE, i.DESCRIPTION, i.STOCK, i.IMAGE, i.CATEGORY_ID, i.DELETE_FLAG, i.INSERT_DATE, sum(oi.quantity) \r\n"
			+ "from order_items oi left join items i on oi.item_id = i.id\r\n" + "where i.delete_flag = 0\r\n"
			+ "group by i.id, i.ID, i.NAME, i.PRICE, i.DESCRIPTION, i.STOCK, i.IMAGE, i.CATEGORY_ID, i.DELETE_FLAG, i.INSERT_DATE\r\n"
			+ "order by sum(oi.quantity) desc" + "", nativeQuery = true)

	public List<Item> sortSQL();

	public Item findByIdAndDeleteFlag(int id, int deleteFlag);

	public Item findByName(String itemName);

	public Item findByNameLikeAndDeleteFlag(String string, Integer deleteFlag);
	
	public List<Item> findAllByName(String itemName);

	public List<Item> findByDeleteFlagOrderByPriceDesc(Integer deleteFlag);
	public List<Item> findByDeleteFlagOrderByPriceAsc(Integer deleteFlag);

	public char[] findByPrice(int itemPrice);
}

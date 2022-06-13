package jp.co.sss.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.entity.OrderItem;

/**
 * order_itemsテーブル用リポジトリ
 *
 * @author System Shared
 */
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
	
	public List<OrderItem> findAllByOrderByQuantityDesc();
	
	
	public List<OrderItem> findByItem(Item item);
	 
	
//	@Query("SELECT oi FROM OrderItem oi WHERE oi.id=1"
//			+ "LEFT JOIN oi.item i "
//			+ "WHERE i.deleteFlag = 0"
//			+ "GROUP BY oi"
//			+ "")
//	public List<OrderItem> sortSQL();
	
	@Query(value = "select oi.ID, oi.QUANTITY, oi.ORDER_ID, oi.ITEM_ID, oi.PRICE \r\n"
			+ "FROM order_items oi "
			+ "LEFT OUTER JOIN items item ON oi.item_id = item.id \r\n"
			+ "WHERE item.DELETE_FLAG = 0 \r\n"
			+ "GROUP BY oi.ID, oi.QUANTITY, oi.ORDER_ID, oi.ITEM_ID, oi.PRICE \r\n"
			+ "ORDER BY SUM(oi.QUANTITY) DESC"
			+ "", nativeQuery = true)
	public List<OrderItem> sortSQL();
}

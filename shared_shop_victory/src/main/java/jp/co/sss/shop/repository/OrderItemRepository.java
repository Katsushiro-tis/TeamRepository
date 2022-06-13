package jp.co.sss.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

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
	 
	List<OrderItem> findByItemId(Integer item_id);
	
//	@Query("SELECT oi.item"
////			+ ", sum(oi.quantity) "
//			+ "FROM OrderItem oi "
////			+ "LEFT JOIN item i ON oi.item = i.id"
////			+ "WHERE i.deleteFlag GROUP BY oi.item"
////			+ "ORDER BY sum(oi.quantity)"
//			+ "")
//	public List<OrderItem> sortSql();
}

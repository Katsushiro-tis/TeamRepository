package jp.co.sss.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.sss.shop.bean.OrderItemBean;
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

	public void save(OrderItemBean orderItemBean);

}

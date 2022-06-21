package jp.co.sss.shop.service;

import java.util.ArrayList;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.sss.shop.bean.OrderItemBean;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.entity.Order;
import jp.co.sss.shop.entity.OrderItem;
import jp.co.sss.shop.repository.ItemRepository;
import jp.co.sss.shop.repository.OrderItemRepository;
import jp.co.sss.shop.repository.OrderRepository;

/**
 * 
 * @author user データベース登録用クラス
 * 
 */

@Service
public class OrderSaveService {

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	ItemRepository itemRepository;

	@Autowired
	OrderItemRepository orderItemRepository;

	@Transactional
	public void orderSave(Order order, ArrayList<OrderItemBean> orderItemList) {

		// 注文情報をorderテーブルへ登録
		orderRepository.save(order);

		// orderItemListの数だけ以下の処理を繰り返す。
		for (int i = 0; i < orderItemList.size(); i++) {
			// iのオーダーを取得
			orderItemList.get(i);
			// OrderItemBean型のorderItemBeanを作成
			OrderItemBean orderItemBean = new OrderItemBean();
			// orderItemBeanへ i のオーダーを代入
			orderItemBean = orderItemList.get(i);
			// OrderItem型のorderItemeを作成
			OrderItem orderIteme = new OrderItem();
			// orderItemeに注文数と値段、Orderの情報を入れる
			orderIteme.setQuantity(orderItemBean.getQuantity());
			orderIteme.setPrice(orderItemBean.getPrice());
			orderIteme.setOrder(order);

			// itemエンティティを作り、Idをもとに商品情報を持ってきて入れる
			Item item = itemRepository.getById(orderItemBean.getId());
			// orderItemeにItem情報を入れる
			orderIteme.setItem(item);
			// orderItemeに入れた情報を保存
			orderItemRepository.save(orderIteme);

			// 注文した商品を在庫から引く
			int num = item.getStock() - orderIteme.getQuantity();
			// 残りの在庫をStockに入れる
			item.setStock(num);

			// 在庫から注文数を引いた残りの在庫を保存
			itemRepository.save(item);
		}
	}

}

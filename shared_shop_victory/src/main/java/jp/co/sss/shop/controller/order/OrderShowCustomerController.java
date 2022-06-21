package jp.co.sss.shop.controller.order;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jp.co.sss.shop.bean.OrderBean;
import jp.co.sss.shop.bean.OrderItemBean;
import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.Order;
import jp.co.sss.shop.entity.OrderItem;
import jp.co.sss.shop.repository.OrderRepository;
import jp.co.sss.shop.repository.UserRepository;
import jp.co.sss.shop.util.PriceCalc;

/**
 * 
 * @author kenji_haga
 * 
 *         注文一覧、詳細表示用コントローラクラス(一般会員用)
 *
 */
@Controller
public class OrderShowCustomerController {

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	UserRepository userRepository;

	/**
	 * 
	 * @param session セッション情報
	 * @param model   Viewとの値受け渡し
	 * @return "order/list/order_list" 注文一覧画面へ
	 */
	@GetMapping("order/list")
	public String showOrderList(HttpSession session, Model model) {
		Integer userId = ((UserBean) session.getAttribute("user")).getId();

		List<Order> orderList = orderRepository.findByUserIdOrderByInsertDateDescIdAsc(userId);

		// 注文情報リストを生成
		List<OrderBean> orderBeanList = new ArrayList<OrderBean>();
		for (Order order : orderList) {
			OrderBean orderBean = new OrderBean();
			orderBean.setId(order.getId());
			orderBean.setInsertDate(order.getInsertDate().toString());
			orderBean.setPayMethod(order.getPayMethod());

			// 注文時点の単価を合計するための一時変数
			int total = 0;

			// orderレコードから紐づくOrderItemのListを取り出す
			List<OrderItem> orderItemList = order.getOrderItemsList();

			for (OrderItem orderItem : orderItemList) {

				// 購入時単価 * 買った個数をもとめて、合計に加算
				total += (orderItem.getPrice() * orderItem.getQuantity());
			}
			// Orderに改めて注文時点の単価をセット
			orderBean.setTotal(total);

			orderBeanList.add(orderBean);
		}

		// 注文情報リストをViewへ渡す
		model.addAttribute("orders", orderBeanList);
		// model.addAttribute("url", "/order/list/admin");

		return "order/list/order_list";
	}

	/**
	 * 
	 * @param id      注文詳細ID(ordersテーブルのIDに相当)
	 * @param model   Viewとの値受け渡し
	 * @param session セッション情報
	 * @return "order/detail/order_detail" 注文詳細画面へ
	 */
	@GetMapping("order/detail/{id}")
	public String showOrder(@PathVariable int id, Model model, HttpSession session) {

		Order order = orderRepository.getById(id);

		// URLを直接たたかれた場合にユーザーと注文詳細の注文者が一致するか確認
		// 一致しなければ一覧にリダイレクト
		UserBean userBean = (UserBean) session.getAttribute("user");
		if (userBean.getId() != order.getUser().getId()) {
			return "redirect:/order/list";
		}

		// 指定された注文IDの注文情報をOrderBeanにセット
		OrderBean orderBean = new OrderBean();
		BeanUtils.copyProperties(order, orderBean);
		orderBean.setInsertDate(order.getInsertDate().toString());

		// 注文商品のリストを生成
		List<OrderItemBean> orderItemBeanList = new ArrayList<OrderItemBean>();
		for (OrderItem orderItem : order.getOrderItemsList()) {
			OrderItemBean orderItemBean = new OrderItemBean();

			orderItemBean.setId(orderItem.getItem().getId());
			orderItemBean.setPrice(orderItem.getPrice());
			orderItemBean.setName(orderItem.getItem().getName());
			orderItemBean.setOrderNum(orderItem.getQuantity());

			// 商品ごとの小計を計算
			int subtotal = orderItem.getPrice() * orderItem.getQuantity();
			orderItemBean.setSubtotal(subtotal);

			orderItemBeanList.add(orderItemBean);
		}

		// 注文の合計金額を計算
		int total = PriceCalc.orderItemBeanPriceTotal(orderItemBeanList);

		model.addAttribute("orderItemList", orderItemBeanList);
		model.addAttribute("order", orderBean);
		model.addAttribute("total", total);

		return "order/detail/order_detail";
	}
}

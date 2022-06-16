package jp.co.sss.shop.controller.order;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.bean.BasketBean;
import jp.co.sss.shop.bean.OrderBean;
import jp.co.sss.shop.bean.OrderItemBean;
import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.entity.Order;
import jp.co.sss.shop.entity.OrderItem;
import jp.co.sss.shop.entity.User;
import jp.co.sss.shop.form.AddressForm;
import jp.co.sss.shop.repository.ItemRepository;
import jp.co.sss.shop.repository.OrderItemRepository;
import jp.co.sss.shop.repository.OrderRepository;
import jp.co.sss.shop.repository.UserRepository;

/**
 * 注文登録用コントローラクラス
 * 
 * @author kenji_haga
 * @author rei_uruma
 */

@Controller
public class OrderRegistCustomerController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	ItemRepository itemRepository;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	OrderItemRepository orderItemRepository;

	// 届け先入力画面へ
	@RequestMapping(path = "/address/input", method = RequestMethod.POST)

	public String inputAddress(@ModelAttribute AddressForm addressForm, HttpSession session, Model model,
			boolean backflg) {

		// 戻るボタンからの遷移でない場合の処理
		if (!backflg) {

			UserBean sessionUser = (UserBean) session.getAttribute("user");
			User user = userRepository.getById(sessionUser.getId());
			UserBean userBean = new UserBean();

			// Userエンティティの各フィールドの値をUserBeanにコピー
			BeanUtils.copyProperties(user, userBean);

			// 会員情報をViewに渡す
			model.addAttribute("userDetail", userBean);
		}

		// 戻るボタンからの遷移の場合の処理
		else {

			model.addAttribute("userDetail", addressForm);
		}
		return "order/regist/order_address_input";
	}

	// 入力チェックで引っかかった時の処理
	@GetMapping("/address/input")
	public String inputAddressRedirect() {
		return "order/regist/order_address_input";
	}

	// 支払い方法選択画面へ
	@RequestMapping(path = "/payment/input", method = RequestMethod.POST)
	public String inputPayment(@Valid @ModelAttribute AddressForm addressForm, BindingResult result, Model model,
			boolean backflg) {

		// 入力した住所を登録（確認画面での表示）
		model.addAttribute("userDetail", addressForm);

		// 戻るボタンからの遷移処理
		if (backflg) {
			return "order/regist/order_payment_input";
		}

		// 入力エラーがあった場合の処理
		if (result.hasErrors()) {
			return "order/regist/order_address_input";
		}

		return "order/regist/order_payment_input";
	}

	// 注文登録確認画面
	@RequestMapping(path = "/order/check", method = RequestMethod.POST)
	public String checkOrder(OrderBean orderBean, HttpSession session, Model model) {

		// 買い物かごにある商品の情報を取得
		ArrayList<OrderItemBean> orderItemList = new ArrayList<OrderItemBean>();
		ArrayList<BasketBean> basketList = (ArrayList<BasketBean>) session.getAttribute("basket");
		int total = 0;

		// 在庫不足時のメッセージリスト
		ArrayList<String> shotageList = new ArrayList<String>();

		for (BasketBean basketBean : basketList) {
			Item item = itemRepository.getById(basketBean.getId());

			// 注文数
			int orderNum = basketBean.getOrderNum();
			// 在庫
			int stock = item.getStock();
			// 小計
			int subtotal = 0;

			// 確定時点での在庫が0の場合、注文不可
			if (stock <= 0) {
				shotageList.add(item.getName() + "は在庫切れのため、注文できません。");
				continue;
			}

			OrderItemBean orderItemBean = new OrderItemBean();
			BeanUtils.copyProperties(item, orderItemBean);

			// 在庫数 < 注文数の場合は在庫数で注文
			if (stock < orderNum) {
				shotageList.add(item.getName() + "は在庫不足のため、在庫数分のみ注文できます。");
				orderItemBean.setQuantity(stock);
				subtotal += item.getPrice() * stock;
			}
			// 在庫数 >= 注文数の場合は注文数で注文
			else {
				orderItemBean.setQuantity(orderNum);
				subtotal += item.getPrice() * orderNum;
			}

			orderItemBean.setSubtotal(subtotal);

			total += subtotal;
			orderItemList.add(orderItemBean);
		}

		session.setAttribute("orderItemList", orderItemList);
		model.addAttribute("register", orderBean);
		model.addAttribute("shotageList", shotageList);
		model.addAttribute("total", total);

		return "order/regist/order_check";
	}

	// 注文登録完了画面
	@RequestMapping(path = "/order/complete")
	public String completeOrder(Order order, HttpSession session) {

		// ordersテーブルへの登録処理
		// viewから受け取った情報にセッションスコープのユーザー情報を加えて登録
		User user = new User();
		UserBean userBean = (UserBean) session.getAttribute("user");

		BeanUtils.copyProperties(userBean, user);
		order.setUser(user);

		orderRepository.save(order);

		// order_itemsテーブルへの登録処理
		// セッションスコープから注文商品情報のリストを取得
		ArrayList<OrderItemBean> orderItemList = (ArrayList<OrderItemBean>) session.getAttribute("orderItemList");

		// 商品ごとにテーブルへ登録
		for (OrderItemBean orderItemBean : orderItemList) {
			OrderItem orderItem = new OrderItem();
			Item item = itemRepository.getById(orderItemBean.getId());

			orderItem.setOrder(order);
			orderItem.setPrice(orderItemBean.getPrice());
			orderItem.setQuantity(orderItemBean.getQuantity());
			orderItem.setItem(item);

			orderItemRepository.save(orderItem);

			// 登録した商品の在庫を変更
			int updateStock = item.getStock() - orderItem.getQuantity();
			item.setStock(updateStock);
			itemRepository.save(item);
		}

		// 買い物かごの情報を削除
		session.removeAttribute("basket");
		session.removeAttribute("ordeItemList");

		return "order/regist/order_complete";
	}

}

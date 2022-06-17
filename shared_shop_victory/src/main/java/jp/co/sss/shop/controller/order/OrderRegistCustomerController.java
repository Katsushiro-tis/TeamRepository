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
import org.springframework.web.bind.annotation.PostMapping;

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
	OrderRepository oderRepository;
	@Autowired
	OrderItemRepository oderItemRepository;

	@Autowired
	ItemRepository itemRepository;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	OrderItemRepository orderItemRepository;

	// 届け先入力画面へ
	@PostMapping("address/input")
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
	@PostMapping("/payment/input")
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
	@PostMapping("/order/check")
	public String checkOrder(@ModelAttribute OrderForm orderForm, HttpSession session, Model model) {

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
	@GetMapping("/order/complete")
	public String completeOrder(AddressForm addressForm, Order order, HttpSession session) {

		// ユーザ情報を取得し、 userBeanに入れる
		UserBean userBean = (UserBean) session.getAttribute("user");
		// User型のuserを作成
		User user = new User();
		// userBeanをuserにコピー
		BeanUtils.copyProperties(userBean, user);
		// Orderエンティティにユーザ情報を入れる
		order.setUser(user);
		// オーダー情報を保存
		oderRepository.save(order);

		// オーダーアイテムリストを取得し、orderItemListに入れる
		ArrayList<OrderItemBean> orderItemList = (ArrayList<OrderItemBean>) session.getAttribute("orderItemList");
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
			oderItemRepository.save(orderIteme);

			// 注文した商品を在庫から引く
			int num = item.getStock() - orderIteme.getQuantity();
			// 残りの在庫をStockに入れる
			item.setStock(num);
			// 在庫から注文数を引いた残りの在庫を保存
			itemRepository.save(item);
		}

		// sessionに登録した情報を削除
		session.removeAttribute("orderItemList");
		session.removeAttribute("basket");

		return "order/regist/order_complete";

	}

}

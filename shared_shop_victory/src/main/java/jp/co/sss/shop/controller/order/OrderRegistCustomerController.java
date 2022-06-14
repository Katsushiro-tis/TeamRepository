package jp.co.sss.shop.controller.order;

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

import jp.co.sss.shop.bean.OrderItemBean;
import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.Order;
import jp.co.sss.shop.entity.User;
import jp.co.sss.shop.form.AddressForm;
import jp.co.sss.shop.form.ItemForm;
import jp.co.sss.shop.form.OrderForm;
import jp.co.sss.shop.repository.OrderItemRepository;
import jp.co.sss.shop.repository.OrderRepository;
import jp.co.sss.shop.repository.UserRepository;

@Controller
public class OrderRegistCustomerController {

	@Autowired
	UserRepository userRepository;
	@Autowired
	OrderRepository oderRepository;
	@Autowired
	OrderItemRepository oderItemRepository;

	// 届け先入力画面へ
	@RequestMapping(path = "/address/input", method = RequestMethod.POST)

	public String inputAddress(@ModelAttribute AddressForm addressForm, BindingResult result, HttpSession session,
			Model model, boolean backflg) {

		// 戻るボタンからの遷移でない場合の処理
		if (!backflg) {

			UserBean sessionUser = (UserBean) session.getAttribute("user");
			User user = userRepository.getById(sessionUser.getId());
			// UserBean userBean = new UserBean();

			// Userエンティティの各フィールドの値をUserBeanにコピー
			// BeanUtils.copyProperties(user, userBean);
			addressForm.setAddress(user.getAddress());
			addressForm.setName(user.getName());
			addressForm.setPhoneNumber(user.getPhoneNumber());
			addressForm.setPostalCode(user.getPostalCode());

			// 会員情報をViewに渡す
			model.addAttribute("userDetail", addressForm);
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
		model.addAttribute("register", addressForm);
		model.addAttribute("userDetail", addressForm);

		if (backflg) {
			return "order/regist/order_payment_input";
		}

		if (result.hasErrors()) {
			return "order/regist/order_address_input";
		}
		return "order/regist/order_payment_input";
	}

	// 注文登録確認画面
	@RequestMapping(path = "/order/check", method = RequestMethod.POST)
	public String checkOrder(@ModelAttribute OrderForm orderform, Model model, String selectedRadio) {

		// 選んだ支払い方法を登録（確認画面での表示）
		model.addAttribute("register", orderform);
		model.addAttribute("payMethod", selectedRadio);

		return "order/regist/order_check";
	}

	// 注文登録完了画面
	@RequestMapping(path = "/order/complete", method = RequestMethod.POST)
	public String completeOrder(Model model, OrderForm orderform, ItemForm itemForm, HttpSession session) {

		Order order = new Order();
		order.setPostalCode(orderform.getPostalCode());
		order.setAddress(orderform.getAddress());
		order.setName(orderform.getName());
		order.setPhoneNumber(orderform.getPhoneNumber());
		order.setPayMethod(orderform.getPayMethod());
		order.setUser((User) session.getAttribute("user"));
		oderRepository.save(order);

		OrderItemBean orderItemBean = new OrderItemBean();
		BeanUtils.copyProperties(order, orderItemBean);
		orderItemBean.setName(orderItemBean.getName());
		orderItemBean.setPrice(orderItemBean.getPrice());
		orderItemBean.setQuantity(orderItemBean.getQuantity());
		oderItemRepository.save(orderItemBean);

		return "order/regist/order_complete";
	}

}

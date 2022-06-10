package jp.co.sss.shop.controller.order;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.User;
import jp.co.sss.shop.form.AddressForm;
import jp.co.sss.shop.form.OrderForm;
import jp.co.sss.shop.repository.UserRepository;

@Controller
public class OrderRegistCustomerController {

	@Autowired
	UserRepository userRepository;

	// 届け先入力画面へ
	@RequestMapping(path = "/address/input", method = RequestMethod.POST)
	public String ShopOrderRegist(@Valid @ModelAttribute AddressForm addressForm, BindingResult result,
			HttpSession session, Model model, boolean backflag) {

		// 戻るボタンからの遷移でない場合の処理
		if (!backflag) {

			UserBean sessionUser = (UserBean) session.getAttribute("user");
			User user = userRepository.getById(sessionUser.getId());
			// UserBean userBean = new UserBean();

			//// Userエンティティの各フィールドの値をUserBeanにコピー
			// BeanUtils.copyProperties(user, userBean);

			AddressForm form = new AddressForm();
			form.setAddress(user.getAddress());
			form.setName(user.getName());
			form.setPhoneNumber(user.getPhoneNumber());
			form.setPostalCode(user.getPostalCode());

			// 会員情報をViewに渡す
			model.addAttribute("userDetail", form);
		}
		// 戻るボタンからの遷移の場合の処理
		else {

		}
		return "order/regist/order_address_input";
	}

	// 支払い方法選択画面へ
	@RequestMapping(path = "/payment/input", method = RequestMethod.POST)
	public String PaymentInput(@Valid @ModelAttribute AddressForm addressForm, BindingResult result, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("userDetail", addressForm);
			return "regist/order_address_input";
		}

		return "order/regist/order_payment_input";
	}

	// 注文登録確認画面
	@RequestMapping(path = "/order/check", method = RequestMethod.POST)
	public String OrderCheck(OrderForm oderform, HttpSession session) {

		return "order/regist/order_check";
	}

	// 注文登録完了画面
	@RequestMapping(path = "/order/complete")
	public String OrderComplete() {
		return "order/regist/order_complete";
	}

	// トップ画面へ (とりあえず）
	@RequestMapping(path = "/go/top")
	public String GoTop() {
		return "redirect:/";
	}

}

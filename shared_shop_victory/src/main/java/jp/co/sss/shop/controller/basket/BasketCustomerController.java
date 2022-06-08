package jp.co.sss.shop.controller.basket;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.form.AddressForm;
import jp.co.sss.shop.form.OrderForm;

@Controller
public class BasketCustomerController {

//	　　　　　買い物かごコントローラー 

	// 買い物かご画面
	@RequestMapping(path = "/basket")
	public String ShopBasket() {
		return "basket/shopping_basket";
	}

	// 届け先入力画面
	@RequestMapping(path = "/address/input", method = RequestMethod.POST)
	public String ShopOrderRegist(AddressForm addressform, HttpSession session) {
		session.setAttribute("address", addressform.getAddress());
		return "order/regist/order_address_input";
	}

	// 支払い方法選択画面
	@RequestMapping(path = "/payment/input", method = RequestMethod.POST)
	public String PaymentInput(OrderForm orderform, HttpSession session) {
		session.setAttribute("payment", orderform.getPayMethod());
		return "order/regist/order_payment_input";
	}

	// 注文登録確認画面
	@RequestMapping(path = "/order/check")
	public String OrderCheck() {
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

package jp.co.sss.shop.controller.basket;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.bean.BasketBean;
import jp.co.sss.shop.form.AddressForm;
import jp.co.sss.shop.form.BasketForm;
import jp.co.sss.shop.form.OrderForm;

@Controller
public class BasketCustomerController {

//	　　　　　買い物かごコントローラー 

// 商品追加処理
	@PostMapping("/basket/add")
	public String addItem(HttpSession session) {

		return "basket/shopping_basket";
	}

	// 買い物かご画面(ナビゲーションバーから遷移)
	@GetMapping("/basket/list")
	public String basketListGet(HttpSession session) {

		return "basket/shopping_basket";
	}

	// 買い物かご画面(各種ボタンから遷移)
	@PostMapping("/basket/list")
	public String basketList(HttpSession session) {

		return "basket/shopping_basket";
	}

	@PostMapping("/basket/delete")
	public String deleteItem(HttpSession session, BasketForm form) {
		int deleteId = form.getId();
		ArrayList<BasketBean> basketList = (ArrayList<BasketBean>) session.getAttribute("basket");
		for (BasketBean bean : basketList) {
			int index = 0;
			if (bean.getId() == deleteId) {
				basketList.remove(index);
				break;
			}
			index++;
		}
		session.setAttribute("basketList", basketList);
		return "basket/shopping_basket";
	}

	@PostMapping("basket/deleteAll")
	public String deleteAll(HttpSession session) {
		ArrayList<BasketBean> basketList = (ArrayList<BasketBean>) session.getAttribute("basket");
		basketList.clear();
		session.setAttribute("basket", basketList);

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

	@PostMapping("/basket/test")
	public String test(HttpSession session) {
		// 画面確認用のbean生成

		BasketBean bean = new BasketBean(1, "りんご", 30, 1);
		BasketBean bean2 = new BasketBean(2, "辞書", 5, 1);
		ArrayList<BasketBean> basketList = new ArrayList<>();
		basketList.add(bean);
		basketList.add(bean2);
		session.setAttribute("basket", basketList);

		return "basket/shopping_basket";
	}
}

package jp.co.sss.shop.controller.basket;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.sss.shop.bean.BasketBean;

@Controller
public class BasketCustomerController {

//	　　　　　買い物かごコントローラー 

// 商品追加処理
	@PostMapping("/basket/add")
	public String addItem(HttpSession session) {

		return "basket/list";
	}

	// 買い物かご画面(ナビゲーションバーから遷移)
	@GetMapping("/basket/list")
	public String basketListGet(HttpSession session) {

		// 確認用のbean生成
		BasketBean bean = new BasketBean(10, "りんご", 100, 1);
		BasketBean bean2 = new BasketBean(10, "いちご", 100, 1);
		ArrayList<BasketBean> basket = new ArrayList<>();
		basket.add(bean);
		basket.add(bean2);
		session.setAttribute("basketBean", basket);

		return "basket/shopping_basket";
	}

	// 買い物かご画面(各種ボタンから遷移)
	@PostMapping("/basket/list")
	public String basketList(HttpSession session) {

		// 画面確認用のbean生成
		BasketBean bean = new BasketBean(10, "りんご", 100, 1);
		BasketBean bean2 = new BasketBean(10, "いちご", 100, 1);
		ArrayList<BasketBean> basket = new ArrayList<>();
		basket.add(bean);
		basket.add(bean2);
		session.setAttribute("basketBean", basket);

		return "basket/shopping_basket";
	}

	@PostMapping("/basket/delete")
	public String deleteItem(HttpSession session, Model model) {
		Integer deleteId = (Integer) model.getAttribute("orderId");
		ArrayList<BasketBean> basket = (ArrayList<BasketBean>) session.getAttribute("basketList");
		// basket.remove(basket.indexOf());

		session.setAttribute("basketBean", basket);
		return "basket/shopping_basket";
	}

	@PostMapping("basket/deleteAll")
	public String deleteAll() {
		return "basket/shopping_basket";
	}

	// 届け先入力画面
	@RequestMapping(path = "/address/input")
	public String ShopOrderRegist() {
		return "order/regist/order_address_input";
	}

	// 支払い方法選択画面
	@RequestMapping(path = "/payment/input")
	public String PaymentInput() {
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

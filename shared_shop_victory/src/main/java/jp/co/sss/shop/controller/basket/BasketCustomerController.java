package jp.co.sss.shop.controller.basket;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.bean.BasketBean;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.form.AddressForm;
import jp.co.sss.shop.form.OrderForm;
import jp.co.sss.shop.repository.ItemRepository;

@Controller
public class BasketCustomerController {

	@Autowired
	ItemRepository itemRepository;

//	　　　　　買い物かごコントローラー 

// 商品追加処理
	@PostMapping("/basket/add")
	public String addItem(int id, HttpSession session) {
		// sessionに買い物かご情報があるか確認。なければ作成
		ArrayList<BasketBean> basket = (ArrayList<BasketBean>) session.getAttribute("basketBean");
		if (basket == null) {
			basket = new ArrayList<>();
		}
		// 追加対象商品の在庫数確認
		Item item = itemRepository.getById(id);
		// 追加できる数ならば、買い物かごに追加(この時点では、在庫数が減らない為、買い物かご数は在庫数を超えてはいけない)
		// 買い物かご個数
		int basketStock = 0;
		// 配列番号カウント
		int count = 0;
		for (BasketBean bask : basket) {
			if (bask.getId() == id) {
				basketStock = bask.getOrderNum() + 1;
				if(basketStock <= item.getStock()) {
					bask.setOrderNum(basketStock);
					basket.set(count, bask);
				}
				
			}
			count++;
		}
		
		if (basketStock > item.getStock()) {
			System.out.println("エラー");
			return "basket/shopping_basket";
		} else { // 買い物かごをセット
			session.setAttribute("basketBean", basket);
			if(basketStock == 0) {
				// 値を登録
				BasketBean bean = new BasketBean(item.getId(), item.getName(), item.getPrice(), 1);
				// 買い物かごに追加
				basket.add(bean);
			}
			
			return "basket/shopping_basket";
		}

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

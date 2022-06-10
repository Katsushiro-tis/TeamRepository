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
import jp.co.sss.shop.form.BasketForm;
import jp.co.sss.shop.form.OrderForm;
import jp.co.sss.shop.repository.ItemRepository;
import jp.co.sss.shop.repository.OrderRepository;

@Controller
public class BasketCustomerController {
	@Autowired
	OrderRepository orderRepository;

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

		return "basket/shopping_basket";
	}

	// 買い物かご画面(各種ボタンから遷移)
	@PostMapping("/basket/list")
	public String basketList(HttpSession session) {

		return "basket/shopping_basket";
	}

	@PostMapping("/basket/delete")
	public String deleteItem(HttpSession session, Model model, BasketForm form) {
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

	// 届け先入力画面へ
	@RequestMapping(path = "/address/input", method = RequestMethod.POST)
	public String ShopOrderRegist() {
		return "order/regist/order_address_input";
	}

	// 支払い方法選択画面へ
	@RequestMapping(path = "/payment/input", method = RequestMethod.POST)
	public String PaymentInput(AddressForm addressform, HttpSession session) {
//		session.setAttribute("payment", orderform.getPayMethod());

//		Order order = new Order();
//		order.setPostalCode(addressform.getPostalCode());
//		order.setAddress(addressform.getAddress());
//		order.setName(addressform.getName());
//		order.setPhoneNumber(addressform.getPhoneNumber());
//		orderRepository.save(order);
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

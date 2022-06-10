package jp.co.sss.shop.controller.basket;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.bean.BasketBean;
import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.entity.Order;
import jp.co.sss.shop.entity.User;
import jp.co.sss.shop.form.AddressForm;
import jp.co.sss.shop.form.OrderForm;
import jp.co.sss.shop.repository.ItemRepository;
import jp.co.sss.shop.repository.OrderRepository;
import jp.co.sss.shop.repository.UserRepository;

@Controller
public class BasketCustomerController {
	@Autowired
	OrderRepository orderRepository;

	@Autowired
	ItemRepository itemRepository;

	@Autowired
	UserRepository userRepository;

//	　　　　　買い物かごコントローラー 

// 商品追加処理
	@PostMapping("/basket/add")
	public String addItem(int id, HttpSession session, Model model) {
		// sessionに買い物かご情報があるか確認。なければ作成
		ArrayList<BasketBean> basketList = (ArrayList<BasketBean>) session.getAttribute("basket");
		if (basketList == null) {
			basketList = new ArrayList<>();
		}
		// 追加対象商品の在庫数確認
		Item item = itemRepository.getById(id);
		// 追加できる数ならば、買い物かごに追加(この時点では、在庫数が減らない為、買い物かご数は在庫数を超えてはいけない)
		// 買い物かご個数
		int basketStock = 0;
		// 配列番号カウント
		int count = 0;
		for (BasketBean bask : basketList) {
			if (bask.getId() == id) {
				basketStock = bask.getOrderNum() + 1;
				if (basketStock <= item.getStock()) {
					bask.setOrderNum(basketStock);
					basketList.set(count, bask);
				}

			}
			count++;
		}

		if (basketStock > item.getStock()) {
			System.out.println("エラー");
			model.addAttribute("notEnoughName", item.getName());
			// return "basket/shopping_basket";
		} else { // 買い物かごをセット
			// session.setAttribute("basket", basketList);
			if (basketStock == 0) {
				// 値を登録
				BasketBean bean = new BasketBean(item.getId(), item.getName(), item.getPrice(), 1);
				// 買い物かごに追加
				basketList.add(bean);
			}
			// return "basket/shopping_basket";
		}

		session.setAttribute("basket", basketList);
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

	// 商品削除（個別）
	@PostMapping("/basket/delete")
	public String deleteItem(HttpSession session, int id) {
		ArrayList<BasketBean> basketList = (ArrayList<BasketBean>) session.getAttribute("basket");
		for (BasketBean bean : basketList) {
			int index = 0;
			if (bean.getId() == id) {
				int orderNum = bean.getOrderNum();
				if (--orderNum <= 0) {
					basketList.remove(index);
				} else {
					bean.setOrderNum(orderNum);
				}
				break;
			}
			index++;
		}
		session.setAttribute("basket", basketList);
		return "basket/shopping_basket";
	}

	// 商品全削除
	@PostMapping("basket/deleteAll")
	public String deleteAll(HttpSession session) {
		ArrayList<BasketBean> basketList = (ArrayList<BasketBean>) session.getAttribute("basket");
		basketList.clear();
		session.setAttribute("basket", basketList);

		return "basket/shopping_basket";
	}

	// 届け先入力画面へ
	@RequestMapping(path = "/address/input", method = RequestMethod.POST)
	public String ShopOrderRegist(HttpSession session, Model model, boolean backflag) {
		if (!backflag) {

			UserBean sessionUser = (UserBean) session.getAttribute("user");
			User user = userRepository.getById(sessionUser.getId());
			UserBean userBean = new UserBean();

			// Userエンティティの各フィールドの値をUserBeanにコピー
			BeanUtils.copyProperties(user, userBean);

			// 会員情報をViewに渡す
			model.addAttribute("userDetail", userBean);
		} else {

		}
		return "order/regist/order_address_input";
	}

	// 支払い方法選択画面へ
	@RequestMapping(path = "/payment/input", method = RequestMethod.POST)
	public String PaymentInput(@Valid @ModelAttribute AddressForm addressform, BindingResult result,
			HttpSession session) {

		Order order = new Order();
		order.setPostalCode(addressform.getPostalCode());
		order.setAddress(addressform.getAddress());
		order.setName(addressform.getName());
		order.setPhoneNumber(addressform.getPhoneNumber());

		session.setAttribute("address", order);

		if (result.hasErrors()) {
			return "order/regist/order_address_input";
		}
		return "order/regist/order_payment_input";
	}

	// 注文登録確認画面へ
	@RequestMapping(path = "/order/check", method = RequestMethod.POST)
	public String OrderCheck(OrderForm oderform, HttpSession session) {

		Order order = new Order();
		order.setPayMethod(oderform.getPayMethod());

		session.setAttribute("payment", oderform.getPayMethod());
		return "order/regist/order_check";
	}

	// 注文登録完了画面
	@RequestMapping(path = "/order/complete")
	public String OrderComplete() {

		return "order/regist/order_complete";
	}

	@PostMapping("/basket/test")
	public String test(HttpSession session) {
		// 画面確認用のbean生成

		BasketBean bean = new BasketBean(1, "りんご", 30, 3);
		BasketBean bean2 = new BasketBean(2, "辞書", 5, 1);
		ArrayList<BasketBean> basketList = new ArrayList<>();
		basketList.add(bean);
		basketList.add(bean2);
		session.setAttribute("basket", basketList);

		return "basket/shopping_basket";
	}
}

package jp.co.sss.shop.controller.basket;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jp.co.sss.shop.bean.BasketBean;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.repository.ItemRepository;
import jp.co.sss.shop.repository.OrderRepository;
import jp.co.sss.shop.repository.UserRepository;

/**
 * 買い物かご操作用コントローラクラス
 * 
 * @author kenji_haga
 * @author makito_hiraoka
 *
 */
@Controller
public class BasketCustomerController {
	@Autowired
	OrderRepository orderRepository;

	@Autowired
	ItemRepository itemRepository;

	@Autowired
	UserRepository userRepository;

	// 商品追加処理
	@PostMapping("/basket/add")
	public String addItem(int id, HttpSession session, Model model) {
		// sessionに買い物かご情報があるか確認。なければ作成
		@SuppressWarnings("unchecked")
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

		if (basketStock > item.getStock() || item.getStock() <= 0) {
			System.out.println("エラー");
			model.addAttribute("notEnoughName", item.getName());
			// return "basket/shopping_basket";
		} else { // 買い物かごをセット
			// session.setAttribute("basket", basketList);
			if (basketStock == 0) {
				// 値を登録
				BasketBean bean = new BasketBean(item.getId(), item.getName(), item.getStock(), 1);
				// 買い物かごに追加
				basketList.add(bean);
			}
			// return "basket/shopping_basket";
		}

		session.setAttribute("basket", basketList);
		return "forward:/basket/list";
	}

	// 買い物かご画面(ナビゲーションバーから遷移)
	@GetMapping("/basket/list")
	public String basketListGet(HttpSession session) {
		return "/basket/shopping_basket";
	}

	// 買い物かご画面(各種ボタンから遷移)
	@PostMapping("/basket/list")
	public String basketList(HttpSession session) {
		return "/basket/shopping_basket";
	}

	// 商品削除（個別）
	@PostMapping("/basket/delete")
	public String deleteItem(HttpSession session, int id) {
		@SuppressWarnings("unchecked")
		ArrayList<BasketBean> basketList = (ArrayList<BasketBean>) session.getAttribute("basket");

		// かごの中の商品を検索、指定のIDの商品を減らして0以下なら削除
		for (BasketBean bean : basketList) {
			int index = 0;
			if (bean.getId() == id) {
				int orderNum = bean.getOrderNum();
				// 注文数を1減らして0以下なら削除
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
		return "forward:/basket/list";
	}

	// 商品全削除
	@PostMapping("basket/deleteAll")
	public String deleteAll(HttpSession session) {
		@SuppressWarnings("unchecked")
		ArrayList<BasketBean> basketList = (ArrayList<BasketBean>) session.getAttribute("basket");
		basketList.clear();
		session.setAttribute("basket", basketList);

		return "forward:/basket/list";
	}
}
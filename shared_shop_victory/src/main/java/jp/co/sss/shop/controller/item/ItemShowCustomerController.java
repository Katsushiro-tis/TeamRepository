package jp.co.sss.shop.controller.item;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.bean.FavoriteBean;
import jp.co.sss.shop.bean.ItemBean;
import jp.co.sss.shop.bean.OrderItemBean;
import jp.co.sss.shop.entity.Category;
import jp.co.sss.shop.entity.Favorite;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.entity.OrderItem;
import jp.co.sss.shop.form.ItemForm;
import jp.co.sss.shop.repository.CategoryRepository;
import jp.co.sss.shop.repository.FavoriteRepository;
import jp.co.sss.shop.repository.ItemRepository;
import jp.co.sss.shop.repository.OrderItemRepository;
import jp.co.sss.shop.util.BeanCopy;
import jp.co.sss.shop.util.Constant;

/**
 * 商品管理 一覧表示機能(一般会員用)のコントローラクラス
 *
 * @author SystemShared
 */
@Controller
public class ItemShowCustomerController {
	/**
	 * 商品情報
	 */
	@Autowired
	ItemRepository itemRepository;
	@Autowired
	CategoryRepository categoryRepository;
	@Autowired
	OrderItemRepository orderItemRepository;
	@Autowired
	FavoriteRepository favoriteRepository;

	/**
	 * トップ画面 表示処理
	 *
	 * @param model Viewとの値受渡し
	 * @return "/" トップ画面へ
	 */
	@RequestMapping(path = "/")
	public String index(Model model) {

		// 商品情報を全件検索(新着順)
		List<Item> itemList = itemRepository.findByDeleteFlagOrderByInsertDateDescIdAsc(Constant.NOT_DELETED);

		// エンティティ内の検索結果をJavaBeansにコピー
		List<ItemBean> itemBeanList = BeanCopy.copyEntityToItemBean(itemList);

		// 商品情報をViewへ渡す
		model.addAttribute("items", itemBeanList);

		return "index";
	}

	@RequestMapping(path = "/item/list")
	public String item_list(Model model) {

		// 商品情報を全件検索(新着順)
		List<Item> itemList = itemRepository.findByDeleteFlagOrderByInsertDateDescIdAsc(Constant.NOT_DELETED);

		// エンティティ内の検索結果をJavaBeansにコピー
		List<ItemBean> itemBeanList = BeanCopy.copyEntityToItemBean(itemList);

		// 商品情報をViewへ渡す
		model.addAttribute("items", itemBeanList);
		model.addAttribute("url", "/item/list/");
		return "/item/list/item_list";
	}

	@RequestMapping(path = "/item/detail/{id}")
	public String showItem(@PathVariable int id, Model model) {

		// 商品IDに該当する商品情報を取得
		Item item = itemRepository.getById(id);

		ItemBean itemBean = new ItemBean();

		// Itemエンティティの各フィールドの値をItemBeanにコピー
		BeanUtils.copyProperties(item, itemBean);

		// 商品情報にカテゴリ名を設定
		itemBean.setCategoryName(item.getCategory().getName());

		// 商品情報をViewへ渡す
		model.addAttribute("item", itemBean);

		return "item/detail/item_detail";
	}

	@RequestMapping(path = "/item/list/category/{sortType}", method = RequestMethod.GET)
	public String showCategoryList(int categoryId, Model model) {

		Category category = new Category();

		category.setId(categoryId);

		List<Item> items = itemRepository.findByCategory(category);

		List<ItemBean> itemBeanList = BeanCopy.copyEntityToItemBean(items);

		model.addAttribute("items", itemBeanList);
		model.addAttribute("url", "/item/list/");

		return "/item/list/item_list";
	}

	@RequestMapping(path = "/item/list/{sortType}", method = RequestMethod.GET)
	public String showNewerList(Model model) {

		List<OrderItem> oderitems = orderItemRepository.findAllByOrderByQuantityDesc();

		List<OrderItemBean> itemBeanList2 = BeanCopy.copyEntityToOrderItemBean(oderitems);

		int num1 = 0;
		int num2 = 0;
		int num3 = 0;

		int[] num = new int[3];

		OrderItem oderitems111 = orderItemRepository.getById(1);
		OrderItem oderitems222 = orderItemRepository.getById(1);
		OrderItem oderitems333 = orderItemRepository.getById(1);
		for (OrderItemBean value0 : itemBeanList2) {

			for (OrderItemBean value : itemBeanList2) {
				if (value.getItem().getId() == 1) {
					num1 += value.getQuantity();
				}
				if (value.getItem().getId() == 2) {
					num2 += value.getQuantity();
				}
				if (value.getItem().getId() == 3) {
					num3 += value.getQuantity();
				}
			}

			if (num1 > num2) {
				oderitems111 = orderItemRepository.getById(1);
				if (num2 > num3) {
					oderitems222 = orderItemRepository.getById(2);
					oderitems333 = orderItemRepository.getById(3);
				} else if (num3 > num2) {
					oderitems222 = orderItemRepository.getById(3);
					oderitems333 = orderItemRepository.getById(2);
				}
			} else if (num2 > num3) {
				oderitems111 = orderItemRepository.getById(2);
				if (num1 > num3) {
					oderitems222 = orderItemRepository.getById(1);
					oderitems333 = orderItemRepository.getById(3);
				} else if (num3 > num1) {
					oderitems222 = orderItemRepository.getById(3);
					oderitems333 = orderItemRepository.getById(2);
				}
			} else {
				oderitems111 = orderItemRepository.getById(3);
				if (num1 > num2) {
					oderitems222 = orderItemRepository.getById(1);
					oderitems333 = orderItemRepository.getById(2);
				} else if (num2 > num1) {
					oderitems222 = orderItemRepository.getById(2);
					oderitems333 = orderItemRepository.getById(1);
				}
			}

			num[0] = num1;
			num[1] = num2;
			num[2] = num3;
			for (int f = 0, l = num.length - 1; f < l; f++, l--) {
				int temp = num[f];
				num[f] = num[l];
				num[l] = temp;
			}

		}

		/*
		 * System.out.println(num[0]); System.out.println(num[1]);
		 * System.out.println(num[2]);
		 */
//
//		OrderItem oderitems111 = orderItemRepository.getById(1);
		System.out.println(oderitems111.getItem().getName());
		System.out.println(oderitems222.getItem().getName());
		System.out.println(oderitems333.getItem().getName());
		/* なんかうまいことしてnumの順番に応じて並び替えられないかな？ */
		/* numの順位付け＋リストに順次入れていく */

		model.addAttribute("items", oderitems111);
		model.addAttribute("items2", oderitems222);
		model.addAttribute("items3", oderitems333);

//		model.addAttribute("items", itemBeanList2);

		/* いったんitem_favoritに渡してます。のちのちはitem_listへ */
		return "/item/list/item_new";

	}

	@RequestMapping(path = "/favorite/list", method = RequestMethod.GET)
	public String showFavoriteList(Model model, ItemForm form) {

		Item item = new Item();
		item.setId(Integer.valueOf(form.getId())); 
		item.setName(form.getName());
		
		System.out.println(form.getId());
		System.out.println(form.getName());
		
		
		List<Favorite> favoriteitems = favoriteRepository.findAll();

		List<FavoriteBean> itemBeanList3 = BeanCopy.copyEntityToFavoriteBean(favoriteitems);

		model.addAttribute("items", itemBeanList3);

		return "/item/list/item_favorite";
	}

}

package jp.co.sss.shop.controller.item;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.bean.ItemBean;
import jp.co.sss.shop.bean.OrderItemBean;
import jp.co.sss.shop.entity.Category;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.entity.OrderItem;
import jp.co.sss.shop.repository.CategoryRepository;
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
		
		model.addAttribute("items", itemBeanList2);
		
		/* いったんitem_favoritに渡してます。のちのちはitem_listへ */
		return "/item/list/item_favorite";
	}

	@RequestMapping(path = "/favorite/list", method = RequestMethod.GET)
	public String showFavoriteList(Model model) {

		return "/item/list/item_favorite";
	}
	
	
//	@GetMapping("/item/list/{sortType}")
//	public String itemListSortType() {
//		return "item_list";
//	}

}

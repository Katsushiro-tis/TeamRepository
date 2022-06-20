package jp.co.sss.shop.controller.item;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.bean.ItemBean;
import jp.co.sss.shop.entity.Category;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.repository.CategoryRepository;
import jp.co.sss.shop.repository.FavoriteRepository;
import jp.co.sss.shop.repository.ItemRepository;
import jp.co.sss.shop.repository.UserRepository;
import jp.co.sss.shop.util.BeanCopy;
import jp.co.sss.shop.util.Constant;

/**
 * 商品管理 一覧表示機能(一般会員用)のコントローラクラス
 *
 */

@Controller
public class ItemShowCustomerController {
	private static final Pageable Pageabel = null;
	/**
	 * 商品情報
	 */
	@Autowired
	ItemRepository itemRepository;
	@Autowired
	CategoryRepository categoryRepository;

//	@Autowired
//	OrderItemRepository orderItemRepository;
	@Autowired
	FavoriteRepository favoriteRepository;
	@Autowired
	UserRepository userRepository;

	/**
	 * トップ画面 表示処理
	 *
	 * @param model Viewとの値受渡し
	 * @return "/" トップ画面へ
	 */
	
//新着順に変更
	@RequestMapping(path = "/")
	public String index(Model model) {

		// 商品情報を全件検索(新着順)
		List<Item> itemList = itemRepository.findByDeleteFlagOrderByInsertDateDescIdAsc(Constant.NOT_DELETED);

		// エンティティ内の検索結果をJavaBeansにコピー
		List<ItemBean> itemBeanList = BeanCopy.copyEntityToItemBean(itemList);

		// 商品情報をViewへ渡す(itemsに検索結果を詰めてHTMLに渡す）
		model.addAttribute("items", itemBeanList);

		return "index";
	}

//アイテム詳細
	@RequestMapping(path = "/item/detail/{id}")
	public String showItem(@PathVariable int id, Model model) {
		// 商品IDに該当する商品情報を取得
		Item item = itemRepository.getById(id);
		ItemBean itemBean = new ItemBean();
		// Itemエンティティの各フィールドの値をItemBeanにコピー
		BeanUtils.copyProperties(item, itemBean);
		// 商品情報にカテゴリ名を設定
		//itemBean.setCategoryName(item.getCategory().getName());
		// 商品情報をViewへ渡す
		model.addAttribute("item", itemBean);
		return "item/detail/item_detail";
	}	

//カテゴリ検索
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

//あいまい検索
	@RequestMapping(path = "/item/list/itemlike", method = RequestMethod.GET)
	public String showLikeItem(String itemName, Model model) {
		System.out.println(itemName);
		
		List<Item> items = itemRepository.findByNameLike("%" +itemName + "%");

		List<ItemBean> itemBeanList = BeanCopy.copyEntityToItemBean(items);
		
		model.addAttribute("items", itemBeanList);
		model.addAttribute("url", "/item/list/");
		
		System.out.println(items);
		
		
		return "/item/list/item_list";
	}
		
//商品検索
	@RequestMapping(path = "/item/list/{sortType}", method = RequestMethod.GET)
	public String showNewerList(Model model, @PathVariable int sortType) {

		List<ItemBean> itemBeanList;
		List<Item> itemList = null;

		if (sortType == 1) {
			// 商品情報を全件検索(新着順)
			itemList = itemRepository.findByDeleteFlagOrderByInsertDateDescIdAsc(Constant.NOT_DELETED);
			model.addAttribute("sort", 1);
		} else if (sortType == 2) {
			// 商品情報を売上順で検索
			itemList = itemRepository.sortSQL();
			model.addAttribute("sort", 2);
			
			for(Item value : itemList) {
			
			System.out.println(value.getName());
		
			}
			
			//商品情報を安い順で検索
		}else if(sortType == 3) {
			itemList = itemRepository.findAllByOrderByPriceAsc();
			
			
			model.addAttribute("sort", 3);
			
		}else if(sortType == 4) {
			itemList = itemRepository.findAllByOrderByPriceDesc();
		model.addAttribute("sort", 4);
	}
		

		// エンティティ内の検索結果をJavaBeansにコピー
		itemBeanList = BeanCopy.copyEntityToItemBean(itemList);
		// 商品情報をViewへ渡す
		model.addAttribute("items", itemBeanList);
		model.addAttribute("url", "/item/list/");
		return "/item/list/item_list";
	}
}

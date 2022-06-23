package jp.co.sss.shop.controller.item;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.bean.ItemBean;
import jp.co.sss.shop.entity.Category;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.form.ItemPriceForm;
import jp.co.sss.shop.form.LikeForm;
import jp.co.sss.shop.repository.CategoryRepository;
import jp.co.sss.shop.repository.ItemRepository;
import jp.co.sss.shop.repository.UserRepository;
import jp.co.sss.shop.util.BeanCopy;
import jp.co.sss.shop.util.Constant;

/**
 * 商品管理 一覧表示機能(一般会員用)のコントローラクラス
 *
 * @author SystemShared
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
	@Autowired
	UserRepository userRepository;

	/**
	 * トップ画面 表示処理
	 *
	 * @param model Viewとの値受渡し
	 * @return "/" トップ画面へ
	 */
	@GetMapping("/")
	public String index(Model model, Pageable pageable) {

		// 商品情報を全件検索(新着順)
		List<Item> itemList = itemRepository.findByDeleteFlagOrderByInsertDateDescIdAsc(Constant.NOT_DELETED);

/// エンティティ内の検索結果をJavaBeansにコピー
		List<ItemBean> itemBeanList = BeanCopy.copyEntityToItemBean(itemList);

		// 商品情報をViewへ渡す
		model.addAttribute("items", itemBeanList);

		return "index";
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

	@GetMapping("/item/list/category/{sortType}")
	public String showCategoryList(int categoryId, Model model) {

		Category category = new Category();

		category.setId(categoryId);

		List<Item> items = itemRepository.findByCategory(category);

		List<ItemBean> itemBeanList = BeanCopy.copyEntityToItemBean(items);

		model.addAttribute("items", itemBeanList);
		model.addAttribute("url", "/item/list/");

		return "item/list/item_list";
	}

	@GetMapping("/item/list/{sortType}")
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
			for (Item i : itemList) {
				System.out.println(i.getName());
			}
			model.addAttribute("sort", 2);
		} else if (sortType == 3) {
			itemList = itemRepository.findAllByOrderByPriceDesc();
			model.addAttribute("sort", 3);
		} else if (sortType == 4) {
			itemList = itemRepository.findAllByOrderByPriceAsc();
			model.addAttribute("sort", 4);
		}
	
		// エンティティ内の検索結果をJavaBeansにコピー
		itemBeanList = BeanCopy.copyEntityToItemBean(itemList);
		// 商品情報をViewへ渡す
		model.addAttribute("items", itemBeanList);
		model.addAttribute("url", "/item/list/");
		return "item/list/item_list";
	}

//あいまい検索	
	@RequestMapping(path = "/item/list/findByItemName")
	//public String showItemListByName(@Valid @ModelAttribute ItemForm form, BindingResult result, Model model) {
		public String showItemListByName(@Valid @ModelAttribute LikeForm likeform, BindingResult result, Model model) {
		
		if (result.hasErrors()) {
			System.out.println("エラー");
			return "item/list/item_list";
		}
		
		System.out.println(likeform.getName());
		Item item = itemRepository.findByNameLike("%" + likeform.getName() + "%");
		
		ItemBean itemBean = new ItemBean();
		// Itemエンティティの各フィールドの値をItemBeanにコピー
		BeanUtils.copyProperties(item, itemBean);
		itemBean.setName(item.getName());
		// 商品情報をViewへ渡す
		model.addAttribute("items", itemBean);
	
		return "item/list/item_list";

	}
	
	@RequestMapping(path = "/item/list/findByItemPrice")
	public String showItemListByPrice(@Valid @ModelAttribute ItemPriceForm pform, BindingResult result, Model model) {

		if (result.hasErrors()) {
			System.out.println("エラー");
			return "item/list/item_list";
	
		}
		
		List<ItemBean> itemBeanList = new ArrayList<ItemBean>();
		List<Item> item;

		// 商品情報を全件検索(新着順)
		List<Item> itemList = itemRepository.findByDeleteFlagOrderByInsertDateDescIdAsc(Constant.NOT_DELETED);

		for (Item i : itemList) {
			if (pform.getItemMinPrice() <= i.getPrice() && pform.getItemMaxPrice() >= i.getPrice()) {

				item = itemRepository.findAllByName(i.getName());

				/// エンティティ内の検索結果をJavaBeansにコピー
				itemBeanList.addAll(BeanCopy.copyEntityToItemBean(item));

			}
		}
		// 商品情報をViewへ渡す
		model.addAttribute("items", itemBeanList);

		return "item/list/item_list";
		
	}

	
	 //商品の値段別検索
	  
	  @RequestMapping(path = "/item/list/pricearray", method = RequestMethod.GET)
	  public String priceViewList(int pricearray, Model model) {
	  System.out.println(pricearray);
	  
	  //安い順で全件検索
	  List<Item> item = itemRepository.findAllByOrderByPriceAsc();
	  List<ItemBean> itemBeanList = BeanCopy.copyEntityToItemBean(item);
	  
	  
	  List<Item> prices = new ArrayList<Item>();
	  
	  //配列を表示してプライスの金額を表示する。
	  for( Item price : item){
	  System.out.println(price.getPrice());
	  
	  //1000円以下のアイテムを表示
	  	if (pricearray == 1 && price.getPrice() <= 1000) {
	  	prices.add(price);
	  
	  
	  	}else if (pricearray == 2 && price.getPrice() >= 1001 && price.getPrice() <=2000) { prices.add(price);

	  	}else if (pricearray == 3 && price.getPrice() >= 2001 && price.getPrice() <=3000) { prices.add(price);
	  
	  	}else if (pricearray == 4 && price.getPrice() >= 3001 && price.getPrice() <= 4000) { prices.add(price);
	 
	  	}else if (pricearray == 5 && price.getPrice() >= 4001 && price.getPrice() <= 5000) { prices.add(price);
	  
	  	}else if (pricearray == 6 && price.getPrice() >= 5001 && price.getPrice() <=6000) { prices.add(price);
	  
	  	}else if (pricearray == 7 && price.getPrice() >= 6001 && price.getPrice() <=7000) { prices.add(price);
	  
	  	}else if (pricearray == 8 && price.getPrice() >= 7001 && price.getPrice() <= 8000) { prices.add(price);
	  
	  	}else if (pricearray == 9 && price.getPrice() >= 8001 && price.getPrice() <=9000) { prices.add(price);
	  
	  	}else if (pricearray == 10 && price.getPrice() >= 9001 && price.getPrice() <= 10000) {
	  
	  		prices.add(price);
	 
	  
	  	}else if (pricearray == 11 && price.getPrice() > 10000) {	 
	  	prices.add(price);
	  	} 
	  }
	  
	  itemBeanList = BeanCopy.copyEntityToItemBean(prices);
	  model.addAttribute("items", itemBeanList);
	  return "item/list/item_list"; 
}

	  
	//int strt;
	//int end;

		//if (pricearray == strt && price.getPrice() >= 9001 && price.getPrice() <= end) {
	 
}
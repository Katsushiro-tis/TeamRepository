package jp.co.sss.shop.controller.item;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.bean.FavoriteBean;
import jp.co.sss.shop.bean.ItemBean;
import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.Category;
import jp.co.sss.shop.entity.Favorite;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.entity.User;
import jp.co.sss.shop.form.FavoriteForm;
import jp.co.sss.shop.form.UserForm;
import jp.co.sss.shop.repository.CategoryRepository;
import jp.co.sss.shop.repository.FavoriteRepository;
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
		}

		// エンティティ内の検索結果をJavaBeansにコピー
		itemBeanList = BeanCopy.copyEntityToItemBean(itemList);
		// 商品情報をViewへ渡す
		model.addAttribute("items", itemBeanList);
		model.addAttribute("url", "/item/list/");
		return "/item/list/item_list";
	}

	@RequestMapping(path = "/favorite/add", method = RequestMethod.GET)
	public String addFavoriteList(Model model, FavoriteForm form, UserForm userform, HttpSession session) {

		// 商品IDに該当する商品情報を取得
		Item item = itemRepository.getById(Integer.valueOf(form.getId()));

		// userIDに該当する商品情報を取得
		UserBean sessionUser = (UserBean) session.getAttribute("user");
		User user = userRepository.getById(sessionUser.getId());

		// favorite二セーブ
		Favorite favorite = new Favorite();

		favorite.setItem(item);
		favorite.setUser(user);

		favoriteRepository.save(favorite);

		return "redirect:/favorite/list";
	}

	@RequestMapping(path = "/favorite/list", method = RequestMethod.GET)
	public String showFavoriteList(Model model) {
		List<Favorite> favoriteitems = favoriteRepository.findAll();

		List<FavoriteBean> itemBeanList3 = BeanCopy.copyEntityToFavoriteBean(favoriteitems);

		model.addAttribute("items", itemBeanList3);
		return "/item/list/item_favorite";
	}

	@PostMapping("/favorite/delete")
	public String deleteFavoriteItem(HttpSession session, FavoriteForm favoriteForm) {
		favoriteRepository.deleteById(Integer.valueOf(favoriteForm.getId()));
		return "redirect:/favorite/list";
	}

}

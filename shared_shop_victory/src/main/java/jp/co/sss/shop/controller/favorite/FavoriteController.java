package jp.co.sss.shop.controller.favorite;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jp.co.sss.shop.bean.FavoriteBean;
import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.Category;
import jp.co.sss.shop.entity.Favorite;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.entity.User;
import jp.co.sss.shop.form.FavoriteForm;
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
public class FavoriteController {

	@Autowired
	ItemRepository itemRepository;
	@Autowired
	CategoryRepository categoryRepository;
	@Autowired
	FavoriteRepository favoriteRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	HttpSession session;

	@PostMapping("/favorite/add")
	public String addFavoriteList(Model model, FavoriteForm form) {

		// 商品IDに該当する商品情報を取得
		Item item = itemRepository.getById(Integer.valueOf(form.getId()));

		// userIDに該当する商品情報を取得
		UserBean sessionUser = (UserBean) session.getAttribute("user");
		User user = userRepository.getById(sessionUser.getId());

		// favoriteにセーブ
		Favorite favorite = new Favorite();

		favorite.setItem(item);
		favorite.setUser(user);

		List<Favorite> favoriteadd = favoriteRepository.findByItemAndUser(item, user);
		List<FavoriteBean> itemBeanList4 = BeanCopy.copyEntityToFavoriteBean(favoriteadd);

		for (FavoriteBean f : itemBeanList4) {
			// すでに保存されているItem/Userと今回addしたItem/Userが同じならsaveせずにリダイレクト
			if (item.getName() == f.getItem().getName() && user.getName() == f.getUser().getName()) {
				return "redirect:/favorite/list";
			}

		}
		favoriteRepository.save(favorite);

		return "redirect:/favorite/list";
	}

	@PostMapping("/favorite/delete")
	public String deleteFavoriteItem(FavoriteForm favoriteForm) {
		favoriteRepository.deleteById(Integer.valueOf(favoriteForm.getId()));
		return "redirect:/favorite/list";
	}

	@GetMapping("/favorite/list")
	public String showFavoriteList(Model model) {
		List<Favorite> favoriteitems = favoriteRepository.findAll();

		List<FavoriteBean> itemBeanList3 = BeanCopy.copyEntityToFavoriteBean(favoriteitems);

		for (FavoriteBean f : itemBeanList3) {
			System.out.println(f.getName());
		}

		// カテゴリ情報を取得する
		List<Category> categoryList = categoryRepository
				.findByDeleteFlagOrderByInsertDateDescIdAsc(Constant.NOT_DELETED);

		// カテゴリ情報をViewへ渡す
		model.addAttribute("categories", categoryList);

		model.addAttribute("items", itemBeanList3);

		return "favorite/item_favorite";
	}
}
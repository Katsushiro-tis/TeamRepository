package jp.co.sss.shop.controller.category;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.bean.CategoryBean;
import jp.co.sss.shop.entity.Category;
import jp.co.sss.shop.form.CategoryForm;
import jp.co.sss.shop.repository.CategoryRepository;
import jp.co.sss.shop.util.Constant;

/**
 * カテゴリ管理 一覧表示機能のコントローラクラス
 *
 * @author SystemShared
 */
@Controller
public class CategoryShowAdminController {

	/**
	 * カテゴリ情報
	 */
	@Autowired
	CategoryRepository categoryRepository;

	/**
	 * カテゴリ情報一覧表示処理
	 *
	 * @param model Viewとの値受渡し
	 * @param form カテゴリ情報
	 * @return "category/list/category_list" カテゴリ情報 一覧画面へ
	 */
	@RequestMapping(path = "/category/list", method = RequestMethod.GET)
	public String showCategoryList(Model model, @ModelAttribute CategoryForm form) {

		// カテゴリ情報を取得する
		List<Category> categoryList = categoryRepository
		        .findByDeleteFlagOrderByInsertDateDescIdAsc(Constant.NOT_DELETED);

		// カテゴリ情報をViewへ渡す
		model.addAttribute("categories", categoryList);
		model.addAttribute("url", "/category/list");

		return "category/list/category_list";
	}

	/**
	 * カテゴリ情報詳細表示処理
	 *
	 * @param model Viewとの値受渡し
	 * @param form カテゴリ情報
	 * @return "/category/detail/category_detail" カテゴリ情報 詳細画面へ
	 */
	@RequestMapping(path = "/category/detail/{id}")
	public String showCategory(@PathVariable int id, Model model, @ModelAttribute CategoryForm form) {

		// カテゴリIDに該当するカテゴリ情報を取得する
		Category category = categoryRepository.getById(form.getId());

		CategoryBean categoryBean = new CategoryBean();
		// Categoryエンティティの各フィールドの値をCategoryBeanにコピー
		BeanUtils.copyProperties(category, categoryBean);

		// カテゴリ情報をViewへ渡す
		model.addAttribute("category", categoryBean);

		return "category/detail/category_detail";
	}

}

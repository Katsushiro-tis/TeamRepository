package jp.co.sss.shop.controller.user;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.User;
import jp.co.sss.shop.form.UserForm;
import jp.co.sss.shop.repository.UserRepository;

/**
 * 会員管理　会員変更(一般会員)
 *
 * @author SystemShared
 */
@Controller
public class UserUpdateCustomerController {

	@Autowired
	UserRepository userRepository;
	
	@PostMapping("/user/update/input")	//会員変更入力画面へ
	public String userUpdateInput(boolean backFlg, Model model, HttpSession session, @ModelAttribute UserForm form) {
		
		//ログイン中のユーザ情報を設定
		UserBean sessionUser = (UserBean) session.getAttribute("user") ;
		form.setId(sessionUser.getId());
		form.setAuthority(sessionUser.getAuthority());
		
		// 戻るボタンかどうかを判定
		if (!backFlg) {

			// 変更対象の会員情報を取得
			User user = userRepository.getById(form.getId());
			UserBean userBean = new UserBean();

			// Userエンティティの各フィールドの値をUserBeanにコピー
			BeanUtils.copyProperties(user, userBean);

			// 会員情報をViewに渡す
			model.addAttribute("user", userBean);

		} else {

			UserBean userBean = new UserBean();
			// 入力値を会員情報にコピー
			BeanUtils.copyProperties(form, userBean);

			// 会員情報をViewに渡す
			model.addAttribute("user", userBean);

		}
		
		return "user/update/user_update_input";
	}
	
	
	@RequestMapping(path = "/user/update/check", method = RequestMethod.POST)
	public String updateCheck( Model model, @Valid @ModelAttribute UserForm form, HttpSession session, BindingResult result) {
		UserBean sessionUser = (UserBean) session.getAttribute("user") ;
		
		form.setId(sessionUser.getId());
		form.setAuthority(sessionUser.getAuthority());
		
		// 入力値にエラーがあった場合、会員情報 変更入力画面表示処理に戻る
		if (result.hasErrors()) {
			
			UserBean userBean = new UserBean();
			// 入力値を会員情報にコピー
			BeanUtils.copyProperties(form, userBean);

			// 会員情報をViewに渡す
			model.addAttribute("user", userBean);
			
			return "user/update/user_update_input";
		}

		return "user/update/user_update_check";
	}
	
	@PostMapping("/user/update/complete")	//会員変更完了画面へ
	public String userUpdateComplete() {
		return "user/update/user_update_complete";
	}
}

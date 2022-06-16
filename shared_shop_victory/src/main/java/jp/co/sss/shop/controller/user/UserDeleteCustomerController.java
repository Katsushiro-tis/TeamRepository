package jp.co.sss.shop.controller.user;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.User;
import jp.co.sss.shop.form.UserForm;
import jp.co.sss.shop.repository.UserRepository;
import jp.co.sss.shop.util.Constant;

/**
 * 会員管理　会員変更(一般会員)
 *
 * @author SystemShared
 */
@Controller
public class UserDeleteCustomerController {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	HttpSession session;
	
	@PostMapping("/user/delete/check")
	public String userDeleteCheck(Model model, @ModelAttribute UserForm form) {

		UserBean sessionUser = (UserBean) session.getAttribute("user") ;
		form.setId(sessionUser.getId());
		form.setAuthority(sessionUser.getAuthority());
		
		// 削除対象の会員情報を取得
		User user = userRepository.getById(form.getId());
		
		UserBean userBean = new UserBean();

		// Userエンティティの各フィールドの値をUserBeanにコピー
		BeanUtils.copyProperties(user, userBean);

		// 会員情報をViewに渡す
		model.addAttribute("user", userBean);

		return "user/delete/user_delete_check";
	}
	
	@PostMapping("/user/delete/complete")
	public String userDeleteComplete(@ModelAttribute UserForm form) {

		UserBean sessionUser = (UserBean) session.getAttribute("user") ;
		form.setId(sessionUser.getId());
		
		// 削除対象の会員情報を取得
		User user = userRepository.getById(form.getId());
		
		// 削除フラグを立てる
		user.setDeleteFlag(Constant.DELETED);
		
		// 会員情報を保存
		userRepository.save(user);

		return "redirect:/user/delete/complete";
	}
	
	
	@GetMapping("/user/delete/complete")
	public String userDeleteComplete() {
		return "/user/delete/user_delete_complete";
	}
}

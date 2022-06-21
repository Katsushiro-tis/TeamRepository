package jp.co.sss.shop.controller.user;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.User;
import jp.co.sss.shop.form.UserForm;
import jp.co.sss.shop.repository.UserRepository;

@Controller
public class UserRegistCustomerController {
	
	/**
	 * 会員情報
	 */
	@Autowired
	UserRepository userRepository;
	@Autowired
	HttpSession session;
	
	// 登録画面(新規登録画面からの遷移)
	@GetMapping("/user/regist/input")
	public String registInput(Model model) {
		System.out.println("新規登録");
		if (!model.containsAttribute("userForm")) {
			UserForm userForm = new UserForm();
			model.addAttribute("userForm", userForm);
		}
		return "user/regist/user_regist_input";
	}
	
	/**
	 * POSTメソッドを利用して会員情報入力画面に戻る処理
	 * 
	 * @param form 会員情報
	 * @return "user/regist/user_regist_input_admin" 会員情報 登録入力画面へ
	 */
	@PostMapping("/user/regist/input")
	public String registInput(UserForm form) {
		return "user/regist/user_regist_input";
	}
	
	
	// 登録情報確認画面
	@PostMapping("/user/regist/check")
	public String registCheck(@Valid @ModelAttribute UserForm form, BindingResult result) {

		// 入力値にエラーがあった場合、会員情報 入力画面表示処理に戻る
		if (result.hasErrors()) {
			return "user/regist/user_regist_input";
		}
		return "user/regist/user_regist_check";
	}
	
	// 登録完了画面
	@PostMapping("/user/regist/complete")
	public String registComplete(@ModelAttribute UserForm form) {
		// 会員情報の生成
				User user = new User();

				// 入力値を会員情報にコピー
				BeanUtils.copyProperties(form, user);

				// 会員情報を保存
				userRepository.save(user);
				
				UserBean userBean = new UserBean();

				userBean.setId(user.getId());
				userBean.setName(user.getName());
				userBean.setAuthority(user.getAuthority());

				// セッションスコープにログインしたユーザの情報を登録
				session.setAttribute("user", userBean);
		return "redirect:/user/regist/complete";
	}
	//リダイレクト
	@GetMapping("/user/regist/complete")
	public String registComplete() {
		return "user/regist/user_regist_complete";
	}

}

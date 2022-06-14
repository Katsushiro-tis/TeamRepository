package jp.co.sss.shop.controller.user;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
	@RequestMapping(path = "/user/regist/input", method = RequestMethod.GET)
	public String registInput(Model model) {
		System.out.println("新規登録");
		if (!model.containsAttribute("userForm")) {
			UserForm userForm = new UserForm();
//			if (user.getAuthority() == 0) {
//				// システム管理者としてログイン中の場合、入力フォーム「権限」の初期値を0（システム管理者）に指定する。
//				userForm.setAuthority(user.getAuthority());
//			}
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
	@RequestMapping(path = "/user/regist/input", method = RequestMethod.POST)
	public String registInput(UserForm form) {

		return "user/regist/user_regist_input";
	}
	
	
	// 登録情報確認画面
	@RequestMapping(path = "/user/regist/check", method = RequestMethod.POST)
	public String registCheck(@Valid @ModelAttribute UserForm form, BindingResult result) {

		// 入力値にエラーがあった場合、会員情報 入力画面表示処理に戻る
		if (result.hasErrors()) {
			System.out.println("エラー");
			return "user/regist/user_regist_input";
		}
		System.out.println(form.getEmail());
		

		return "user/regist/user_regist_check";
	}
	
	// 登録完了画面
	@RequestMapping(path = "/user/regist/complete", method = RequestMethod.POST)
	public String registComplete(@ModelAttribute UserForm form) {
		System.out.println("会員登録");
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
	@RequestMapping(path = "/user/regist/complete", method = RequestMethod.GET)
	public String registComplete() {
		return "user/regist/user_regist_complete";
	}

}

package jp.co.sss.shop.controller.user;

import java.util.Date;

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
import jp.co.sss.shop.util.MailSend;

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
	
	@Autowired
	MailSend sender;
	
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

		//退会日を取得
		Date date = new Date();
//		sender.send();
		sender.send(user.getEmail(),"[Shared shop]退会申し込み確認", "----------------------------------------\n"
															+ "　[shared shop]退会申し込み確認メール\n"
															+ "----------------------------------------\n"
															+ "\n"
															+ "shared shopをご利用いただきありがとうございました。\n"
															+ "この度の退会の申し込み内容について、以下のとおりご案内いたします。\n"
															+ "またのご入会、ご利用を心よりお待ち申し上げます。\n"
															+ "\n"
															+ "----------------------------------------\n"
															+ "◇shared shop退会の申し込み受付内容\n"
															+ "----------------------------------------\n"
															+ "\n"
															+ "メールアドレス\n"
															+ user.getEmail() + "\n"
															+ "ユーザ名\n"
															+ user.getName() + "\n"
															+ "退会申込日\n"
															+ sender.getSendDate()
															+ "\n\n"
															+ "Shared shop\n"
															+ "http://localhost:55000/shared_shop/\n"
															+ "\n\n"
															+ "----------------------------------------\n"
															+ "※本メールは送信専用メールです。お問い合わせは下記のwebページよりお願いいたします。\n"
															+ "----------------------------------------\n"
															+ "資料の請求・お問い合わせ|【公式】東京ITスクール-株式会社システムシェアード\n"
															+ "https://tokyoitschool.jp/inquiry/ \n"
															+ "----------------------------------------\n");
		
		return "redirect:/user/delete/complete";
	}
	
	
	@GetMapping("/user/delete/complete")
	public String userDeleteComplete() {
		return "/user/delete/user_delete_complete";
	}
}

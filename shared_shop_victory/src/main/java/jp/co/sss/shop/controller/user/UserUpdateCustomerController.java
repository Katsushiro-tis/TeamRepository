package jp.co.sss.shop.controller.user;

import java.sql.Date;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.User;
import jp.co.sss.shop.form.UserForm;
import jp.co.sss.shop.repository.UserRepository;
import jp.co.sss.shop.util.MailSend;

/**
 * 会員管理　会員変更(一般会員)
 *
 * @author SystemShared
 */
@Controller
public class UserUpdateCustomerController {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	HttpSession session;
	
	@Autowired
	MailSend sender;
	
	@PostMapping("/user/update/input")	//会員変更入力画面へ
	public String userUpdateInput(boolean backFlg, Model model, @ModelAttribute UserForm form) {
		
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
	public String userUpdateCheck( Model model, @Valid @ModelAttribute UserForm form, BindingResult result) {
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
	
	
	@RequestMapping(path = "/user/update/complete", method = RequestMethod.POST)
	public String userUpdateComplete(Model model, @ModelAttribute UserForm form) {

		// 変更対象の会員情報を取得
		User user = userRepository.getById(form.getId());

		// 会員情報の削除フラグを取得
		Integer deleteFlag = user.getDeleteFlag();
		// 会員情報の登録日付を取得
		Date insertDate = user.getInsertDate();

		// 入力値をUserエンティティの各フィールドにコピー
		BeanUtils.copyProperties(form, user);

		// 削除フラグをセット
		user.setDeleteFlag(deleteFlag);
		// 登録日付をセット
		user.setInsertDate(insertDate);

		// 会員情報を保存
		userRepository.save(user);

		// セッションからログインユーザーの情報を取得
		UserBean userBean = (UserBean) session.getAttribute("user");
		// 変更対象の会員が、ログインユーザと一致していた場合セッション情報を変更
		if (user.getId().equals(userBean.getId())) {
			// Userエンティティの各フィールドの値をUserBeanにコピー
			BeanUtils.copyProperties(form, userBean);
			// 会員情報をViewに渡す
			session.setAttribute("user", userBean);
		}
		
		
		sender.send(user.getEmail(),"[Shared shop]会員情報変更の確認", "----------------------------------------\n"
																+ "　[shared shop]会員情報変更の確認メール\n"
																+ "----------------------------------------\n"
																+ "\n"
																+ "この度はshared shopをご利用いただきありがとうございます。\n"
																+ "会員情報の変更が完了しました。"
																+ "サイトへログイン後会員詳細ページより変更後の情報をご確認いただけます。\n"
																+ "変更申し込み日\n"
																+ sender.getSendDate()
																+ "\n\n"
																+ "Shared shopログインページ\n"
																+ "http://localhost:55000/shared_shop/login\n"
																+ "\n\n"
																+ "----------------------------------------\n"
																+ "※本メールは送信専用メールです。お問い合わせは下記のwebページよりお願いいたします。\n"
																+ "----------------------------------------\n"
																+ "資料の請求・お問い合わせ|【公式】東京ITスクール-株式会社システムシェアード\n"
																+ "https://tokyoitschool.jp/inquiry/ \n"
																+ "----------------------------------------\n");

		return "redirect:/user/update/complete";
	}
	
	@GetMapping("/user/update/complete")	//会員変更完了画面へ
	public String userUpdateComplete() {
		return "user/update/user_update_complete";
	}
}

package jp.co.sss.shop.controller.user;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.User;
import jp.co.sss.shop.repository.UserRepository;
import jp.co.sss.shop.util.MailSend;

/**
 * 会員管理　会員詳細表示(一般会員)
 *
 * @author SystemShared
 */
@Controller
public class UserShowCustomerController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	MailSend sender;
	
	@Autowired
	HttpSession session;
	
	//会員詳細画面用
	@GetMapping("/user/detail")		
	public String userShowCustomer(Model model) {
		// 表示対象の会員情報を取得
		UserBean sessionUser = (UserBean) session.getAttribute("user") ;
		User user = userRepository.getById(sessionUser.getId());

		UserBean userBean = new UserBean();

		// Userエンティティの各フィールドの値をUserBeanにコピー
		BeanUtils.copyProperties(user, userBean);

		// 会員情報をViewに渡す
		model.addAttribute("user", userBean);
		
		return "user/detail/user_detail";
	}
	
}

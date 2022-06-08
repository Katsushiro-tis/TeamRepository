package jp.co.sss.shop.controller.user;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.User;
import jp.co.sss.shop.repository.UserRepository;

/**
 * 会員管理　会員詳細表示(一般会員)
 *
 * @author SystemShared
 */
@Controller
public class UserShowCustomerControlle {

	@Autowired
	UserRepository userRepository;
	
	@GetMapping("/user/detail")		//会員詳細画面用
	public String userShowCustomer(HttpSession session) {
		// 表示対象の会員情報を取得
		User user = userRepository.getById(3);

		UserBean userBean = new UserBean();

		// Userエンティティの各フィールドの値をUserBeanにコピー
		BeanUtils.copyProperties(user, userBean);

		// 会員情報をViewに渡す
		session.setAttribute("user", userBean);
		
		return "user/detail/user_detail";
	}
	
}

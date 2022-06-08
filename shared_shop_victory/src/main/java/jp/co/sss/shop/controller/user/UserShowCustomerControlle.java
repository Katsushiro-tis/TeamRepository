package jp.co.sss.shop.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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
	public String userShowCustomer() {
		return "user/detail/user_detail";
	}
	
}

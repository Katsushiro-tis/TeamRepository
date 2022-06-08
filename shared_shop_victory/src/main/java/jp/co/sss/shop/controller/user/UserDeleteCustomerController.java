package jp.co.sss.shop.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import jp.co.sss.shop.repository.UserRepository;

/**
 * 会員管理　会員変更(一般会員)
 *
 * @author SystemShared
 */
@Controller
public class UserDeleteCustomerController {
	@Autowired
	UserRepository userRepository;
	
	@PostMapping("/user/delete/check")
	public String userDeleteCheck() {
		return "/user/delete/user_delete_check";
	}
	
	@PostMapping("/user/delete/complete")
	public String userDeleteComplete() {
		return "/user/delete/user_delete_complete";
	}
}

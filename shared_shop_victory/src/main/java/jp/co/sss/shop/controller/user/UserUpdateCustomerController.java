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
public class UserUpdateCustomerController {

	@Autowired
	UserRepository userRepository;
	
	@PostMapping("/user/update/input")	//会員変更入力画面へ
	public String userUpdateInput() {
		return "user/update/user_update_input";
	}
	
	@PostMapping("/user/update/check")	//会員変更確認画面へ
	public String userUpdateCheck() {
		return "user/update/user_update_check";
	}
	
	@PostMapping("/user/update/complete")	//会員変更完了画面へ
	public String userUpdateComplete() {
		return "user/update/user_update_complete";
	}
}

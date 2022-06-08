package jp.co.sss.shop.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

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
}

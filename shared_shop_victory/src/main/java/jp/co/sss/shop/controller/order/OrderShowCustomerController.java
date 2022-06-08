package jp.co.sss.shop.controller.order;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrderShowCustomerController {

	// 注文情報のリストを表示
	@GetMapping("order/list")
	public String showOrderList() {
		return "order/list/order_list";
	}

	@GetMapping("order/detail")
	public String showOrder() {

		return "order/detail/order_detail";
	}

}

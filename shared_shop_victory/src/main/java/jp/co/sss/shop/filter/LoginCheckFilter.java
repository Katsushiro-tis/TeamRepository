
package jp.co.sss.shop.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;

import jp.co.sss.shop.util.URLCheck;

/**
 * ログインチェック用フィルタ
 * 
 * @author System Shared
 */

@Component
public class LoginCheckFilter implements Filter {
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// リクエスト情報を取得
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		if (checkRequestURL(httpRequest)) {

			// セッション情報を取得
			HttpSession session = httpRequest.getSession();

			if (session.getAttribute("user") == null) {
				// 不正アクセスの場合、ログイン画面にリダイレクト
				System.out.println("非会員");
				// レスポンス情報を取得
				HttpServletResponse httpResponse = (HttpServletResponse) response;

				// ログイン画面へリダイレクト
				httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
			} else {
				chain.doFilter(request, response);
			}
		} else {
			chain.doFilter(request, response);
		}
	}

	/**
	 * リクエストURLがチェック対象であるかを判定
	 * 
	 * @param requestURL リクエストURL
	 * @return true：チェック対象、false：チェック対象外
	 */
	private boolean checkRequestURL(HttpServletRequest httpRequest) {
		// リクエストURLを取得
		String requestURL = httpRequest.getRequestURI();
		if (!URLCheck.checkURLForStaticFile(requestURL) && !requestURL.endsWith("/login")
				&& !requestURL.endsWith(httpRequest.getContextPath() + "/")
				&& (requestURL.indexOf("/item/list/") == -1 || requestURL.indexOf("/admin") != -1)
				&& (requestURL.indexOf("/item/detail/") == -1 || requestURL.indexOf("/admin") != -1)
				&& !requestURL.endsWith("/user/regist/input") && !requestURL.endsWith("/user/regist/check")
				&& !requestURL.endsWith("/user/regist/complete") || requestURL.indexOf("/basket") != -1
				|| requestURL.indexOf("/order") != -1) {
<<<<<<< HEAD
			System.out.println("非会員用実行対象だよ : " + requestURL);
			// URLのリクエスト先がフィルタ実行対象である場合
=======
			// URLのリクエスト先がフィルタ実行対象の場合場合
>>>>>>> 5a89f4dd09768eb761352a589c7157ae35ff3d94
			return true;
		} else {
			// URLのリクエスト先がフィルタ実行対象ではない場合
			return false;
		}
	}
}
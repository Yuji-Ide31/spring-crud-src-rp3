package jp.co.sss.crud.filter;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import jp.co.sss.crud.entity.Employee;

/**
 * 権限認証用フィルタ
 * 
 * @author System Shared
 */
public class AdminAccountCheckFilter extends HttpFilter {

	@Override
	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		// URIと送信方式を取得する
		String requestURI = request.getRequestURI();
		String requestMethod = request.getMethod();

		// 完了画面はフィルターを通過させる
		if (requestURI.contains("/complete") && requestMethod.equals("GET")) {
			chain.doFilter(request, response);
			return;
		}

		// セッションからユーザー情報を取得
		HttpSession session = request.getSession();

		// セッションユーザーのIDと権限の変数をそれぞれ初期化
		Employee sessionUser = (Employee) session.getAttribute("user");
		Integer sessionUserAuthoriry = Integer.valueOf(-1);
		Integer sessionUserEmpId = Integer.valueOf(-1);

		// セッションユーザーがNULLでない場合
		if (sessionUser != null) {
			// セッションユーザーからID、権限を取得して変数に代入
			sessionUserAuthoriry = sessionUser.getAuthority();
			sessionUserEmpId = sessionUser.getEmpId();
		}

		// 更新対象の社員IDをリクエストから取得
		String param = request.getParameter("empId");
		Integer requestEmpId = null;

		// 社員IDがNULLでない場合
		if (param != null) {
			// 社員IDを整数型に変換
			requestEmpId = Integer.valueOf(Integer.parseInt(param));
		}

		// フィルター通過のフラグを初期化 true:フィルター通過 false:ログイン画面へ戻す
		boolean accessFlg = false;

		// 管理者(セッションユーザーのIDが2)の場合、アクセス許可
		if (sessionUserAuthoriry.intValue() == 2) {
			accessFlg = true;
			// ログインユーザ自身(セッションユーザのIDと変更リクエストの社員IDが一致)の画面はアクセス許可
		} else if (sessionUserEmpId == requestEmpId) {
			accessFlg = true;
		}

		// accessFlgが立っていない場合はログイン画面へリダイレクトし、処理を終了する
		if (!accessFlg) {
			// レスポンス情報を取得
			HttpServletResponse httpResponse = response;

			// ログイン画面へリダイレクト
			httpResponse.sendRedirect(request.getContextPath());

			// 処理を終了
			return;
		}

		chain.doFilter(request, response);
		return;

	}

}

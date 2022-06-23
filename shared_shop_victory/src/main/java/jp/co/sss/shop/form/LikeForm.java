package jp.co.sss.shop.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class LikeForm {
	
//曖昧検索のフォーム

	@NotNull
	@NotBlank
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}

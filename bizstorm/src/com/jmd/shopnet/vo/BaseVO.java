package com.jmd.shopnet.vo;

import lombok.Getter;

public class BaseVO {
	@Getter final private Long id;

	public BaseVO(Long id) {
		this.id = id;
	}
}

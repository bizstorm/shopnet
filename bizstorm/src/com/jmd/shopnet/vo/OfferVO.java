package com.jmd.shopnet.vo;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import com.jmd.shopnet.entity.OfferComments;

public class OfferVO extends BaseVO{

	public OfferVO(final Long id) {
		super(id);
	}
	
	@Getter @Setter private ProductVO productDetails;
	@Getter @Setter private BusinessVO businessDetails;
	@Getter @Setter private float price;
	@Getter @Setter private int quantity;
	@Getter @Setter private String offerType;
	@Getter @Setter private String message;
	@Getter @Setter private int customerRatingAvg;
	@Getter @Setter private int blacklistedCount;
	@Getter @Setter private Date createdDate;
	@Getter @Setter private Date modifiedDate;
	@Getter @Setter private List<OfferComments> comments;

}

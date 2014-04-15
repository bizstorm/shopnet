package com.jmd.shopnet.api;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.inject.Inject;
import com.jmd.shopnet.entity.Product;
import com.jmd.shopnet.service.ProductService;
import com.jmd.shopnet.utils.Ids;

@Api(name = "products"
,version = "v1"
,clientIds = {Ids.WEB_CLIENT_ID, com.google.api.server.spi.Constant.API_EXPLORER_CLIENT_ID}
,scopes = {Ids.SCOPES}
)
public class ProductAPI {
	private ProductService productService;
	
	@ApiMethod(name="createproduct", httpMethod = "POST" , path="new")
	public Product createProduct(				
			Product product) {
		
		return productService.createProduct(product);
	}
	
	@ApiMethod(name="updateproduct", httpMethod = "POST" , path="update")
	public void updateProduct(				
				Product product) {
		
		productService.updateProduct(product);
	}
	
	@ApiMethod(name="deleteproduct", httpMethod = "POST" , path="delete/{id}")
	public void deleteProduct(				
			@Named("id") Long productId ) {
		
		 productService.deleteProduct(productId);
	}
			
	@Inject
	public void setProductService(ProductService productService) {
		this.productService = productService;
	}
}

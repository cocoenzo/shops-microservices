package com.gft.db.shops.data;

public class ResponseData {

	public static final String ACTION_NEW = "new";
	public static final String ACTION_UPDATE ="update";
	public static final String ACTION_REMOVE = "remove";
	public static final String ACTION_ERROR ="error";
	private String result;
	private Shop shop;
	
	public ResponseData(){
		this.result = "";
		this.shop = new Shop();
	}
	public ResponseData(final String result, final Shop shop){
		this.result=result;
		this.shop=shop;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public Shop getShop() {
		return shop;
	}
	public void setShop(Shop shop) {
		this.shop = shop;
	}
	
}

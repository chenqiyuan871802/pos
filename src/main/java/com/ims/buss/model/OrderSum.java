package com.ims.buss.model;

/**
 * 
 * 类名:com.ims.buss.model.OrderSum 
 * 描述:订单汇总信息 
 * 编写者:陈骑元 
 * 创建时间:2019年11月10日 下午1:28:05
 * 修改说明:
 */
public class OrderSum {

	/**
	 * 订单日期
	 */
	private String orderDate;
	/**
	 * 现金收取总金额
	 */
	private int cashTotalAmount;
	/**
	 * 信用卡收取总金额
	 */
	private int cardTotalAmount;
	/**
	 * 其他总金额
	 */
	private int otherTotalAmount;
	/**
	 * 优惠券
	 */
	private int couponsTotalAmount;
	

	/**
	 * 预测金额
	 */
	private int predictAmount;
	

	/**
	 * 总金额
	 */
	private int totalAmount;
	/**
	 * 菜单品总金额
	 */
	private int menuAmount;
	/**
	 * 吃饭总人数
	 */
	private int eatNum;
	/**
	 * 人均消费金额
	 */
	private int avgAmount;
	/**
	 * 桌位费总消费总金额
	 * 
	 */
	private int deskAmount;
	/**
	 * 10%税金总金额（包含桌位费税金）
	 */
	private int taxAmount;

	/**
	 * 8%（税金金额）
	 */
	private int outTaxAmount;
	/**
	 * 10%菜品金额
	 */
	private int tenMenuAmount;

	/**
	 * 8%菜品金额
	 */
	private int eightMenuAmount;
	/**
	 * 10%纯菜品金额税金
	 */
	private int tenTaxAmount;
	/**
	 * 初金
	 */
	private int beginAmount;
	
	/**
	 * 入金
	 */
	private int inAmount;
	
	/**
	 * 出金
	 */
	private int outAmount;
	/**
	 * 消费税
	 */
	private int consumeTax;
	
	/**
	 * 支付订单数量
	 */
	private int payOrderCount;
	
	/**
	 * 总订单数量
	 */
	private int orderCount;
	
	private int deskNum;
	
	public int getDeskNum() {
		return deskNum;
	}

	public void setDeskNum(int deskNum) {
		this.deskNum = deskNum;
	}

	public int getConsumeTax() {
		return consumeTax;
	}

	public void setConsumeTax(int consumeTax) {
		this.consumeTax = consumeTax;
	}

	
	public int getPayOrderCount() {
		return payOrderCount;
	}

	public void setPayOrderCount(int payOrderCount) {
		this.payOrderCount = payOrderCount;
	}


	public int getPredictAmount() {
		return predictAmount;
	}

	public void setPredictAmount(int predictAmount) {
		this.predictAmount = predictAmount;
	}

	public int getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(int orderCount) {
		this.orderCount = orderCount;
	}
	

	public int getBeginAmount() {
		return beginAmount;
	}

	public void setBeginAmount(int beginAmount) {
		this.beginAmount = beginAmount;
	}

	public int getInAmount() {
		return inAmount;
	}

	public void setInAmount(int inAmount) {
		this.inAmount = inAmount;
	}

	public int getOutAmount() {
		return outAmount;
	}

	public void setOutAmount(int outAmount) {
		this.outAmount = outAmount;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public int getCashTotalAmount() {
		return cashTotalAmount;
	}

	public void setCashTotalAmount(int cashTotalAmount) {
		this.cashTotalAmount = cashTotalAmount;
	}

	public int getCardTotalAmount() {
		return cardTotalAmount;
	}

	public void setCardTotalAmount(int cardTotalAmount) {
		this.cardTotalAmount = cardTotalAmount;
	}

	public int getOtherTotalAmount() {
		return otherTotalAmount;
	}

	public void setOtherTotalAmount(int otherTotalAmount) {
		this.otherTotalAmount = otherTotalAmount;
	}

	public int getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(int totalAmount) {
		this.totalAmount = totalAmount;
	}

	public int getMenuAmount() {
		return menuAmount;
	}

	public void setMenuAmount(int menuAmount) {
		this.menuAmount = menuAmount;
	}

	public int getEatNum() {
		return eatNum;
	}

	public void setEatNum(int eatNum) {
		this.eatNum = eatNum;
	}

	public int getAvgAmount() {
		return avgAmount;
	}

	public void setAvgAmount(int avgAmount) {
		this.avgAmount = avgAmount;
	}

	public int getDeskAmount() {
		return deskAmount;
	}

	public void setDeskAmount(int deskAmount) {
		this.deskAmount = deskAmount;
	}

	public int getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(int taxAmount) {
		this.taxAmount = taxAmount;
	}

	public int getOutTaxAmount() {
		return outTaxAmount;
	}

	public void setOutTaxAmount(int outTaxAmount) {
		this.outTaxAmount = outTaxAmount;
	}

	public int getTenMenuAmount() {
		return tenMenuAmount;
	}

	public void setTenMenuAmount(int tenMenuAmount) {
		this.tenMenuAmount = tenMenuAmount;
	}

	public int getEightMenuAmount() {
		return eightMenuAmount;
	}

	public void setEightMenuAmount(int eightMenuAmount) {
		this.eightMenuAmount = eightMenuAmount;
	}

	public int getTenTaxAmount() {
		return tenTaxAmount;
	}

	public void setTenTaxAmount(int tenTaxAmount) {
		this.tenTaxAmount = tenTaxAmount;
	}
	public int getCouponsTotalAmount() {
		return couponsTotalAmount;
	}

	public void setCouponsTotalAmount(int couponsTotalAmount) {
		this.couponsTotalAmount = couponsTotalAmount;
	}
}

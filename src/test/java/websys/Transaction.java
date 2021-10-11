package websys;

public class Transaction {
	
	private String OrderID;
	
	private Integer Amount;
    
	private Integer Tax;
	
	private String[] PayMethods;

	public String getOrderID() {
		return OrderID;
	}

	public void setOrderID(String orderID) {
		OrderID = orderID;
	}

	public Integer getAmount() {
		return Amount;
	}

	public void setAmount(Integer amount) {
		Amount = amount;
	}

	public Integer getTax() {
		return Tax;
	}

	public void setTax(Integer tax) {
		Tax = tax;
	}

	public String[] getPayMethods() {
		return PayMethods;
	}

	public void setPayMethods(String[] payMethods) {
		PayMethods = payMethods;
	}
}

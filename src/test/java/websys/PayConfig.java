package websys;

public class PayConfig {
	
	private String configid;
	
	public String getConfigid() {
		return configid;
	}

	public void setConfigid(String configid) {
		this.configid = configid;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	private Transaction transaction;

}

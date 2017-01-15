package cz.vervasinova.ptracker.model;

public class PaymentRecord {
	private String currency;
	private int amount;

	public PaymentRecord(String currency, int amount) {
		this.currency = currency;
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	@Override
    public String toString() {
        return currency + " " + amount;
    }
}

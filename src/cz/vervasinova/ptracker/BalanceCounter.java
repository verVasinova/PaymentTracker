package cz.vervasinova.ptracker;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cz.vervasinova.ptracker.model.Currency;
import cz.vervasinova.ptracker.model.PaymentRecord;

public class BalanceCounter implements Runnable {
	private List<PaymentRecord> payments;
	private Map<String, Integer> balance;

	private static final String USD = "USD";

	private volatile boolean running = true;

	public BalanceCounter(List<PaymentRecord> payments) {
		this.payments = payments;
		this.balance = new HashMap<>();
	}

	public BalanceCounter() {
		this.payments = new ArrayList<>();
		this.balance = new HashMap<>();
	}

	@Override
	public void run() {
		while (running) {
			try {
				doPayments();
				Thread.sleep(60000); // 1 minute
			} catch (InterruptedException e) {
				// System.out.println("Thread was interrupted.");
				running = false;
			}
		}

	}

	public void terminate() {
		running = false;
	}

	private void doPayments() {
		if (!payments.isEmpty()) {
			for (PaymentRecord payment : payments) {
				refreshBalance(payment.getCurrency(), payment.getAmount());
			}
			payments.clear();
		}
		System.out.println("Balance: \n" + printBalance());
	}

	private void refreshBalance(String currency, int amount) {
		if (balance.containsKey(currency)) {
			int sum = balance.get(currency) + amount;
			balance.put(currency, sum);
		} else
			balance.put(currency, amount);
	}

	private String printBalance() {
		StringBuilder result = new StringBuilder();
		for (Entry<String, Integer> in : balance.entrySet()) {
			if (in.getValue() != 0) {
				result.append(in).append(getUSD(in)).append("\n");
			}
		}

		return result.toString();
	}

	private boolean containsCurrency(String currency) {
		for (Currency rate : Currency.values()) {
			if (rate.name().equals(currency))
				return true;
		}
		return false;
	}

	private String getUSD(Entry<String, Integer> in) {
		String result = "";
		DecimalFormat decFormat = new DecimalFormat(".##");

		if (in.getKey().equals(USD))
			return result;

		else if (containsCurrency(in.getKey())) {
			double rate = Currency.valueOf(in.getKey()).getValue();
			result = " (USD " + String.valueOf(decFormat.format(in.getValue() * rate)) + ")";
		}

		return result;
	}
}

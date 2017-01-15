package cz.vervasinova.ptracker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import cz.vervasinova.ptracker.model.PaymentRecord;

public class PaymentTracker {

	public PaymentTracker() {
	}

	public void start() {
		System.out.println("Payment tracker\n-------------- ");
		List<PaymentRecord> input = loadPayments("payments.txt");

		BalanceCounter balanceCounter;
		if (!input.isEmpty())
			balanceCounter = new BalanceCounter(input);
		else
			balanceCounter = new BalanceCounter();

		Thread t = new Thread(balanceCounter);
		t.start();

		Scanner scanner = new Scanner(System.in);
		String consoleInput;

		while (true) {
			consoleInput = scanner.nextLine();

			if (consoleInput.equals("quit"))
				break;
			else if (isValid(consoleInput))
				input.add(createPaymentRecord(consoleInput));
			else
				System.out.println("Wrong format! \nCorrect format is: \"XXX amount\", where XXX is currency e.g. USD");
		}

		balanceCounter.terminate();
		t.interrupt();
		System.out.println("Payment tracker finished!");
		scanner.close();

	}

	private List<PaymentRecord> loadPayments(String fileName) {
		List<PaymentRecord> result = new ArrayList<>();
		Stream<String> stream = null;
		try {
			stream = Files.lines(Paths.get(fileName));
			stream.filter(line -> testFormat(line)).map(i -> createPaymentRecord(i)).forEach(p -> result.add(p));

		} catch (IOException e) {
			System.out.println("No file!");

		} finally {
			if (stream != null) {
				System.out.println("Payments loaded.");
				stream.close();
			}
		}

		return result;
	}

	private PaymentRecord createPaymentRecord(String line) {
		String[] parts = line.split(" ");
		PaymentRecord result = new PaymentRecord(parts[0], Integer.parseInt(parts[1]));
		return result;
	}

	private boolean isValid(String input) {
		Pattern p = Pattern.compile("[A-Z]{3} -??\\d+"); // USD -100
		return (p.matcher(input)).matches();
	}

	private boolean testFormat(String input) {
		if (isValid(input)) {
			return true;
		} else {
			System.out.println("Wrong format: " + input);
			return false;
		}

	}

}

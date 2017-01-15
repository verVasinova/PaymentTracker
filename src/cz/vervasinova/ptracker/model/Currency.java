package cz.vervasinova.ptracker.model;

public enum Currency {
	HKD("0.128954"), RMB("0.154632"), CZK("0.0392710"), EUR("1.06100"), GBP("0.83246") ;

    private String value;

    Currency(String s) {
        value = s;
    }

    public Double getValue() {
        return Double.valueOf(value);
    }

}

package final_group_project;

public class CurrencyConverter implements Converter{
    public CurrencyConverter() {

    }

    @Override
    public double converter(double money, double rate) {
        return money * rate;
    }
}

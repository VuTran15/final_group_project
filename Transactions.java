package final_group_project;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Transactions extends Bank{
    private JFrame f;
    private JLabel l1,l2,l3,l4,l5,l6,l7,l8,l9,l10,err;
    private LinkedList<String> transactionHistory;

    private JPanel p1, p2, p3;

    private JTextField j1, j2;
    public Transactions(double total, double USDollar, double austoralianDollar, double canadianDollar, double yen, double euro, double peso, double poundSterling, double dong, double interestRate) {
        super(total, USDollar, austoralianDollar, canadianDollar, yen, euro, peso, poundSterling, dong, interestRate);
        transactionHistory = new LinkedList<>();
        // create JFrame Object
        f = new JFrame("BANK");

        // create panels
        p1 = new JPanel();
        p2 = new JPanel();

        // add data to labels
        l1 = new JLabel("Total: " + total);
        l2 = new JLabel("USDollar: " + USDollar);
        l3 = new JLabel("Australian Dollar: " + austoralianDollar);
        l4 = new JLabel("Canadian Dollar: " + canadianDollar);
        l5 = new JLabel("Yen: " + yen);
        l6 = new JLabel("Euro: " + euro);
        l7 = new JLabel( "Peso: " + peso);
        l8 = new JLabel("Pound Sterling: " + poundSterling);
        l9 = new JLabel("Dong" + dong);
        l10 = new JLabel("Interest Rate: " + interestRate);
        err = new JLabel();

    }

    @Override
    public void calculateInterest(int year) {
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        double interest = this.getTotal() * (this.getInterestRate()/100) * (thisYear - year);
    }

    @Override
    public void deposit(double money) {
        // add money to total available in bank
        this.setTotal(this.getTotal() + money);
        // record transaction
        transactionHistory.add("Deposit of " + money + ". Current total: " + this.getTotal());

        // update labels
        updateBank();

        // show items
        showBank();
    }

    @Override
    public void withdraw(double money) {
        // subtract money from total available in bank
        this.setTotal(this.getTotal() - money);
        // record transaction
        transactionHistory.add("Withdrawal of " + money + ". Current total: " + this.getTotal());

        // update labels
        updateBank();

        // show items
        showBank();
    }

    @Override
    public void showTotal() {
        // show total on GUI
        l1.setText(String.valueOf(this.getTotal()));
        err.setText("Total: " + this.getTotal());
        err.setBounds(50, 600, 300, 30);
        f.revalidate();
        f.repaint();
    }

    @Override
    public void record() {
        // write transactions to file
        try {
            FileWriter myWriter = new FileWriter("transactions.txt");
            for (int i=0; i<transactionHistory.size(); i++)
                myWriter.write(transactionHistory.get(i) + "\n");
            myWriter.close();

            err.setText("File written successfully");
            err.setBounds(50, 600, 300, 30);
            f.revalidate();
            f.repaint();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    @Override
    public void description() {
        // read from file
        try {
            File myObj = new File("transactions.txt");
            Scanner myReader = new Scanner(myObj);

            p3.removeAll();
            int count = 0;
            while (myReader.hasNextLine()) {
                p3.add(new JLabel(myReader.nextLine()));
                count++;
            }

            myReader.close();

            p3.setBounds(50, 620, 450, 40 * count);
            f.revalidate();
            f.repaint();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            err.setText("Error reading file");
            err.setBounds(50, 600, 300, 30);
            f.revalidate();
            f.repaint();
        }
    }

    @Override
    public void transitFromOtherBank(double money, double balance, int bank) {
        this.setTotal(money - balance);
    }

    @Override
    public void transitToOtherBank(double money, int bank, Bank bank2) {
        bank2.setTotal(money);
    }

    @Override
    public int compareTo(Bank bank2) {
        if(this.getTotal() == bank2.getTotal())
            return 0;
        else if(this.getTotal() > bank2.getTotal())
            return 1;
        else
            return -1;
    }

    @Override
    public void GUI() {
        // Default method for closing the frame
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e)
            {
                System.exit(0);
            }
        });

        // button to show bank info
        JButton showBtn = new JButton("SHOW BANK");
        showBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // show if panel is empty
//                p1 = new JPanel();
                showBank();
            }
        });

        showBtn.setBounds(50, 20, 150, 30);
        p2.setBounds(50, 50, 500, 300);

        // create withdrawal jtesxtfield and jbutton
        j1 = new JTextField("Enter amount");
        JButton withdrawBtn = new JButton("WITHDRAW");
        withdrawBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // get amount to withdraw
                String s1 = j1.getText();

                // convert to double. catch exception if input is not a number
                double amount = 0;
                try {
                    amount = Double.parseDouble(s1);
                    withdraw(amount);
                } catch (Exception exception){
                    err.setText("Invalid input for amount");
                    err.setBounds(50, 600, 300, 30);
                    f.revalidate();
                    f.repaint();
                }
            }
        });
        p2.add(j1);
        p2.add(withdrawBtn);

        // create deposit jtesxtfield and jbutton
        j2 = new JTextField("Enter amount");
        JButton depositBtn = new JButton("DEPOSIT");
        depositBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // get amount to withdraw
                String s1 = j2.getText();

                // convert to double. catch exception if input is not a number
                double amount = 0;
                try {
                    amount = Double.parseDouble(s1);
                    deposit(amount);
                } catch (Exception exception){
                    err.setText("Invalid input for amount");
                    err.setBounds(50, 600, 200, 30);
                    f.revalidate();
                    f.repaint();
                }
            }
        });
        p2.add(j2);
        p2.add(depositBtn);

        // add write to file button
        JButton fileWriter = new JButton("WRITE TRANSACTIONS");
        fileWriter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!transactionHistory.isEmpty())
                    record();
                else{
                    err.setText("No transactions yet");
                    err.setBounds(50, 600, 300, 30);
                    f.revalidate();
                    f.repaint();
                }
            }
        });
        p2.add(new JLabel("Write to file"));
        p2.add(fileWriter);

        // read from file button
        JButton fileReader = new JButton("READ TRANSACTIONS");
        fileReader.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                description();
            }
        });
        p2.add(new JLabel("Read from file"));
        p2.add(fileReader);

        // convert random btn
        JButton convertBtn = new JButton("CONVERT RANDOM");
        convertBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                convertRandom();
            }
        });

        p2.add(new JLabel("Convert Random Currency"));
        p2.add(convertBtn);

        p2.setLayout(new GridLayout(5, 2));

        // Adding the created objects
        f.add(showBtn);
        f.add(p1);
        f.add(p2);
        p3 = new JPanel();
        f.add(p3);
        f.add(err);

        f.setLayout(null);
        f.setSize(600, 700);
        f.setVisible(true);
    }

    private void showBank() {
        p2.setBounds(50, 250, 500, 300);
        p1.setBounds(50, 50, 300, 200);


        p1.add(l1);
        p1.add(l2);
        p1.add(l3);
        p1.add(l4);
        p1.add(l5);
        p1.add(l6);
        p1.add(l7);
        p1.add(l8);
        p1.add(l9);
        p1.add(l10);

        p1.setLayout(new GridLayout(5, 3));
        f.revalidate();
        f.repaint();
    }

    private void updateBank(){

        l1.setText("Total: " + this.getTotal());
        l2.setText("USDollar: " + this.getUSDollar());
        l3.setText("Australian Dollar: " + this.getAustoralianDollar());
        l4.setText("Canadian Dollar: " + this.getCanadianDollar());
        l5.setText("Yen: " + this.getYen());
        l6.setText("Euro: " + this.getEuro());
        l7.setText("Peso: " + this.getPeso());
        l8.setText("Pound Sterling: " + this.getPoundSterling());
        l9.setText("Dong" + this.getDong());
        l10.setText("Interest Rate: " + this.getInterestRate());
    }

    private void convertRandom() {
        // put currencies in hashmap
        HashMap<String, Double> currencies = new HashMap<>();
        currencies.put("USDollar", this.getUSDollar());
        currencies.put("Australian Dollar", this.getUSDollar());
        currencies.put("Canadian Dollar", this.getUSDollar());
        currencies.put("Yen", this.getUSDollar());
        currencies.put("Euro", this.getUSDollar());
        currencies.put("Peso", this.getUSDollar());
        currencies.put("Pound Sterling", this.getUSDollar());
        currencies.put("Dong", this.getUSDollar());
        currencies.put("Interest Rate", this.getUSDollar());

        // get random number from 0 to 9
        int randomInt = new Random().nextInt(9);

        // convert random currency
        int count = 0;
        for (String i:currencies.keySet()){
            if (count == randomInt){
                CurrencyConverter currencyConverter = new CurrencyConverter();
                double converted = currencyConverter.converter(currencies.get(i), this.getInterestRate());
                err.setBounds(50, 600, 300, 30);
                err.setText( i + ": " + currencies.get(i) + " Converted: " + converted );
                f.revalidate();
                f.repaint();
            }
            count++;
        }

    }
}

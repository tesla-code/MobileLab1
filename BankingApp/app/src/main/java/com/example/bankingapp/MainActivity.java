package com.example.bankingapp;

/*  Req: at least sdk 28. */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;

import java.util.Locale;
import java.util.Random;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // Volatile keyword is used for extrea feature to make sure
    // the variable is more thread safe
    public static /* volatile */ int  balance = 0;
    public static /* volatile */ int cents = 100;
    public static final int  MAX_EURO = 110;
    public static final int  MIN_EURO = 90;

    public static final int REQUEST_CODE_TRANSFER = 101;
    static ArrayList<Transaction> transactionsList = new ArrayList<Transaction>();
    TextView textView;

    // for the drop down drop-down
    final String[] recipientList = new String[]{"Alice", "Bob", "Charlie", "Dawn", "Elivs", "Frode"};

    private Generator m_generator = new Generator();

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // If you want to use the foundrasier class
        // uncomment the lines below and also the class, + notifyFunction
        // + uncomment the voletile keywords infront of cents and balance variables.

        // FoundsRaiser f  = new FoundsRaiser();
        // f.start();

                balance = m_generator.generateBalance();
        if(balance != MAX_EURO) {
            cents = m_generator.generateCents();
        }
        else
        {
            cents = 0;
        }

        Log.i("tag", "Number Generated = " + balance + "," + cents);
        if (balance > MAX_EURO || balance < MIN_EURO) {
            String assertionMessage = String.format("Balance euros not in range (%d, %d)",MIN_EURO, MAX_EURO);
            throw new AssertionError(assertionMessage);
        }

        Transaction transaction1 = new Transaction("Angel", balance, cents, balance, cents);
        transactionsList.add(transaction1);
        transactionsList.get(0).prepareCents();

        Log.i("Angel: ", transactionsList.get(0).toString());

        // Display the balance on the android screen
        textView = findViewById(R.id.lbl_balance);
        textView.setText(String.format(Locale.getDefault(), "%d,%d", balance, cents));

        Button btn = findViewById(R.id.btn_transfer);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Transfer variable to another activity
                Intent intent = new Intent(MainActivity.this, TransferActivity.class);
                intent.putExtra("EURO_ID", balance);
                intent.putExtra("CENT_ID", cents);
                intent.putExtra("RECIPIENT_LIST", recipientList);

                startActivityForResult(intent, REQUEST_CODE_TRANSFER);
            }
        });

        Button btn2 = findViewById(R.id.btn_transactions);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] listOftransactions = new String[transactionsList.size()];

                for(int i = 0; i < transactionsList.size(); i++)
                {
                    transactionsList.get(i).prepareCents();
                    listOftransactions[i] = transactionsList.get(i).toString();
                }

                Bundle b=new Bundle();
                b.putStringArray("Tlist", listOftransactions);
                Intent intent = new Intent(MainActivity.this, TransactionsActivity.class);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
    }

    /**
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_TRANSFER) {
            if (resultCode == RESULT_OK) {

                // Get the transaction value
                int euroEntered = data.getIntExtra("balance", 0);
                int centsEntered = data.getIntExtra("cents", 0);

                Log.i("Euro entered: ", Integer.toString(euroEntered));
                Log.i("cents entered: ", Integer.toString(centsEntered));

                // Get the name of the recipient
                String recipient = data.getStringExtra("recipient");

                // Update balance
                int[] res = updateBalance(euroEntered, centsEntered);

                Transaction transaction = new Transaction(recipient, euroEntered, centsEntered, res[0], res[1]);
                transactionsList.add(transaction);

                // Display the new balance
                textView.setText(String.format(Locale.getDefault(), "%d,%d", balance, cents));
            }
        }
    }

    /**
     *
     * @param i_euros
     * @param i_cents
     * @return the remaining balance after subtracting transaction
     */
    public static int[] updateBalance(int i_euros, int i_cents)
    {
        // ret 0 euro
        //     1 cents
        int[] ret = new int[2];
        int ones = i_cents % 100;
        cents -= ones;

        if(cents < 0)
        {
            balance--;
            cents += 100;
        }

        balance -= i_euros;
        ret[0] = balance;
        ret[1] = cents;

        return ret;
    }


    /**
     *  This class was simply to made to have an additional feature.
     *  FoundsRaiser class starts a thread that randomly adds Euros to
     *  The current balance. 1-10 euros in a random time interval


    class FoundsRaiser extends Thread
    {
        private Random m_generator = new Random();
        int eurosRaised = 0;
        int centsRaised = 0;

        @Override
        public void run() {

            while(true)
            {
                try {
                    Thread.sleep(1000 * ((m_generator.nextInt(100)) + 1) );
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                // Fund raiser raises between 1 and 10 euros
                eurosRaised = m_generator.nextInt(10) + 1;

                balance += eurosRaised;
                Transaction transaction = new Transaction("Angel (FoundRaised)", eurosRaised, 0, balance, cents);
                transactionsList.add(transaction);

                noitfyViewChange();
                // Display the balance on the android screen

            }
        }
    }


    /**
     * This function is used by the foundRaiser extra feature to update the view
     * of the balance with out the programming crashing =)
     */
    /*
    public synchronized void noitfyViewChange()
    {
        textView.post(new Runnable() {
            public void run() {
                textView.setText(String.format(Locale.getDefault(), "%d,%d", balance, cents));
            }
        });
    }

    */
}


/**
 * The generator class is used generate random initial balances
 *
 * @author victor
 * @version 1
 * @since 18/01/2019
 */
class Generator
{
    private Random m_generator = new Random();

    /**
     * The method is used to generate an initial balance
     * @return a value between 90 and 110
     */
    protected int generateBalance()
    {
        return m_generator.nextInt(21) + 90;
    }

    protected int generateCents()
    {
        return m_generator.nextInt(100);
    }
}


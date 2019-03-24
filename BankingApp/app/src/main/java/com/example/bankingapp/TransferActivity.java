package com.example.bankingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class TransferActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        //get the spinner from the xml.
        // if something is messed up remove final
        Spinner dropdown = findViewById(R.id.spinner_friends);

        String[] items = getIntent().getStringArrayExtra("RECIPIENT_LIST");
        //create a list of items for the spinner.
        //String[] items = new String[]{"Alice", "Bob", "Charlie", "Dawn", "Elivs", "Frode"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        final Button button = findViewById(R.id.btn_pay);
        button.setEnabled(false);

        // Just for logging purposes
        int sessionId = getIntent().getIntExtra("EXTRA_BALANCE_ID", 0);
        Log.i("********",Integer.toString(sessionId));

        // txt_amount
        final EditText txt_amount = findViewById(R.id.txt_amount);

        // final since we access this field in an inner class later on
        final TextView lbl_amount_check = findViewById(R.id.lbl_amount_check);

        txt_amount.addTextChangedListener(new TextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count)
            {
                String input = s.toString();
                int i_euro = 0;
                int i_cents = 0;

                // Store the input inn to the correct variables
                if(input.contains("."))
                {
                    String[] splitStrings = input.split("\\.");
                    Log.i("splitStrings has length of : ",Integer.toString(splitStrings.length));

                    if(splitStrings.length == 1)
                    {
                        Log.i("splitStrings[0] contains: ", splitStrings[0]);
                        i_euro = Integer.parseInt(splitStrings[0]);
                        i_cents = Integer.parseInt(splitStrings[0]);

                    }
                    if(splitStrings.length == 2)
                    {
                        Log.i("splitStrings[1] contains: ", splitStrings[1]);

                        if(splitStrings[1].length() >= 1 )
                        {
                            i_cents = Character.getNumericValue(splitStrings[1].charAt(0));
                        }
                        else
                        {
                            i_cents = Character.getNumericValue(splitStrings[1].charAt(0)) +
                                      (10 * Character.getNumericValue(splitStrings[1].charAt(1)));
                        }
                    }
                }
                else
                {
                    try
                    {
                        i_euro = Integer.parseInt(s.toString());
                    }catch (NumberFormatException e)
                    {
                        e.printStackTrace();
                    }
                }

                Log.i("num1: " , input);
                int currentEuro = getIntent().getIntExtra("EURO_ID", 0);
                int currentCent = getIntent().getIntExtra("CENT_ID", 0);

                Log.i("currentEuros: ", Integer.toString(currentEuro));
                Log.i("currentCents: ", Integer.toString(currentCent));
                Log.i("current i_euro: ", Integer.toString(i_euro));

                // Check if the transaction amount is with inn bounds.
                    if (i_euro <= 0) {

                        if(i_cents <= 0 && i_euro > currentEuro)
                        {
                            lbl_amount_check.setText("Amount is outside of bounds, to low");
                            button.setEnabled(false);
                        }
                        else
                        {
                            // check if cent is larger than current cents amount
                            // and that theres not enough euros to subtract
                            if(i_cents > currentCent && i_euro >= currentEuro)
                            {
                                lbl_amount_check.setText("Amount is outside of bounds, to high");
                                button.setEnabled(false);
                            }
                            else
                            {
                                lbl_amount_check.setText("Ready to transfer");
                                button.setEnabled(true);
                            }
                        }
                    }
                    else if (i_euro > currentEuro)
                    {
                        lbl_amount_check.setText("Amount is outside of bounds, to high");
                        button.setEnabled(false);
                    } else
                        {
                        lbl_amount_check.setText("Ready to transfer");
                        button.setEnabled(true);
                    }
                }
        });


        /**
         *  When the user click the button, the amount is subtracted from the users
         *  balance.
         *
         *  Also a new transaction is made to the recipient selected from the drop down menu.
         *
         *  This new transaction is added to the list, subsequently visible in the
         *  TransactionsActivity. After the payment is triggered the user is moved
         *  back to the MainActivity.
         */
        button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent data = new Intent(); // getIntent();
                    String input = txt_amount.getText().toString();
                    String[] args = input.split("\\.");

                    int euro = 0;
                    if(args.length != 0)
                    {
                        Log.i("args[0] = ", args[0]);

                        euro = Integer.parseInt(args[0]);
                    }

                    int cents = 100;
                    if(args.length == 2)
                    {
                        Log.i("args[1] = ", args[1]);

                        if(args[1].length() == 2)
                        {
                            cents += 10 * Character.getNumericValue(args[1].charAt(0));
                            cents += Character.getNumericValue(args[1].charAt(1));
                        }
                        else   // else the number contains no cents
                        {
                            cents += 10 * Character.getNumericValue(args[1].charAt(0));
                        }
                    }

                    data.putExtra("balance", euro);
                    data.putExtra("cents", cents);

                    // Return the string
                    Spinner mySpinner = findViewById(R.id.spinner_friends);
                    String recipient = mySpinner.getSelectedItem().toString();

                    Log.i("recipient ", recipient);

                    data.putExtra("recipient", recipient);
                    setResult(RESULT_OK, data);

                    finish();
                }
        });
    }
}
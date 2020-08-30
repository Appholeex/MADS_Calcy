package com.appholeex.madscalcy.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;


import com.appholeex.madscalcy.R;
import com.appholeex.madscalcy.databinding.ActivityHomeBinding;
import com.appholeex.madscalcy.utils.PrefUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class HomeActivity extends BaseActivity implements View.OnClickListener {

    ActivityHomeBinding binding;
    private String TAG = "HomeActivity";
    private static Stack<Double> valueStack;
    private static Stack<Character> opretorStack;
    private List<String> lastTenResults;
    private boolean isLogin;
    private String mobile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        prefManager.connectDB();
        isLogin = prefManager.getBoolean(PrefUtils.IS_LOGIN);
        mobile = prefManager.getString(PrefUtils.MOBILE);
        prefManager.closeDB();


        lastTenResults = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            binding.edtInput.setTextAppearance(android.R.style.TextAppearance_Material_Display2);
        }else{
            binding.edtInput.setTextAppearance(this,android.R.style.TextAppearance_Material_Display2);
            binding.finalResult.setTextAppearance(this,android.R.style.TextAppearance_Material_Display1);
        }

        binding.btn0.setOnClickListener(this);
        binding.btn1.setOnClickListener(this);
        binding.btn2.setOnClickListener(this);
        binding.btn3.setOnClickListener(this);
        binding.btn4.setOnClickListener(this);
        binding.btn5.setOnClickListener(this);
        binding.btn6.setOnClickListener(this);
        binding.btn7.setOnClickListener(this);
        binding.btn8.setOnClickListener(this);
        binding.btn9.setOnClickListener(this);
        binding.btnDot.setOnClickListener(this);
        binding.btnAddition.setOnClickListener(this);
        binding.btnMinus.setOnClickListener(this);
        binding.btnMultipy.setOnClickListener(this);
        binding.btnDivide.setOnClickListener(this);
        binding.btnClearAll.setOnClickListener(this);
        binding.btnErase.setOnClickListener(this);
        binding.btnEqualTo.setOnClickListener(this);
        binding.btn6.setOnClickListener(this);


        binding.edtInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
            }
        });

        binding.edtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                binding.edtInput.setSelection(binding.edtInput.getText().length());

            }
        });

        binding.showHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isLogin) {
                    showHistory();
                }else{
                    Toast.makeText(HomeActivity.this, R.string.olny_login_user_view, Toast.LENGTH_LONG).show();
                }

            }
        });

        if (isLogin) {
            binding.imgLogout.setVisibility(View.VISIBLE);
        }else{
            binding.imgLogout.setVisibility(View.GONE);
        }

        binding.imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                logoutDialog();
            }
        });

    }

    private void showHistory() {

        lastTenResults = new ArrayList<>();
        databaseReference = firebaseDatabase.getReference("History").child(mobile);
        Query query = databaseReference.orderByKey().limitToLast(10);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    Log.d("User key", child.getKey());
                    lastTenResults.add(child.getValue().toString());
                }

                if (lastTenResults.isEmpty())
                    Toast.makeText(HomeActivity.this, R.string.no_history, Toast.LENGTH_LONG).show();
                else
                    setResultList();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: "+databaseError.getMessage());

            }
        });;
    }

    private void logoutDialog() {

        new MaterialAlertDialogBuilder(this)
                .setMessage(R.string.logout_msg)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        FirebaseAuth.getInstance().signOut();

                        prefManager.connectDB();
                        prefManager.setBoolean(PrefUtils.IS_LOGIN,false);
                        prefManager.setString(PrefUtils.MOBILE,"");
                        prefManager.closeDB();

                        startActivity( new Intent(HomeActivity.this, LoginActivity.class));
                        finish();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.btn0:
                binding.edtInput.setText(binding.edtInput.getText().toString() + "0");
                binding.finalResult.setVisibility(View.INVISIBLE);
                break;

            case R.id.btn1:
                binding.edtInput.setText(binding.edtInput.getText().toString() + "1");
                binding.finalResult.setVisibility(View.INVISIBLE);


                break;

            case R.id.btn2:
                binding.edtInput.setText(binding.edtInput.getText().toString() + "2");
                binding.finalResult.setVisibility(View.INVISIBLE);

                break;

            case R.id.btn3:
                binding.edtInput.setText(binding.edtInput.getText().toString() + "3");
                binding.finalResult.setVisibility(View.INVISIBLE);

                break;

            case R.id.btn4:
                binding.edtInput.setText(binding.edtInput.getText().toString() + "4");
                binding.finalResult.setVisibility(View.INVISIBLE);

                break;

            case R.id.btn5:
                binding.edtInput.setText(binding.edtInput.getText().toString() + "5");
                binding.finalResult.setVisibility(View.INVISIBLE);

                break;

            case R.id.btn6:
                binding.edtInput.setText(binding.edtInput.getText().toString() + "6");
                binding.finalResult.setVisibility(View.INVISIBLE);

                break;

            case R.id.btn7:
                binding.edtInput.setText(binding.edtInput.getText().toString() + "7");
                binding.finalResult.setVisibility(View.INVISIBLE);

                break;

            case R.id.btn8:
                binding.edtInput.setText(binding.edtInput.getText().toString() + "8");
                binding.finalResult.setVisibility(View.INVISIBLE);

                break;

            case R.id.btn9:
                binding.edtInput.setText(binding.edtInput.getText().toString() + "9");
                binding.finalResult.setVisibility(View.INVISIBLE);

                break;

            case R.id.btnDot:
                binding.edtInput.setText(binding.edtInput.getText().toString() + ".");
                binding.finalResult.setVisibility(View.INVISIBLE);

                break;

            case R.id.btnAddition:
                binding.edtInput.setText(binding.edtInput.getText().toString() + "+");
                binding.finalResult.setVisibility(View.INVISIBLE);

                break;

            case R.id.btnMinus:
                binding.edtInput.setText(binding.edtInput.getText().toString() + "-");
                binding.finalResult.setVisibility(View.INVISIBLE);

                break;

            case R.id.btnMultipy:
                binding.edtInput.setText(binding.edtInput.getText() +  "*");
                binding.finalResult.setVisibility(View.INVISIBLE);

                break;

            case R.id.btnDivide:
                binding.edtInput.setText(binding.edtInput.getText().toString() + "/");
                binding.finalResult.setVisibility(View.INVISIBLE);

                break;

            case R.id.btnClearAll:
                binding.edtInput.setText("");
                break;

            case R.id.btnErase:
                if (binding.edtInput.getText().length() >0) {
                binding.edtInput.getText().delete(binding.edtInput.getText().length()-1,binding.edtInput.getText().length() );
                }
                break;

            case R.id.btnEqualTo:

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    binding.finalResult.setTextAppearance(android.R.style.TextAppearance_Material_Display2);
                    binding.edtInput.setTextAppearance(android.R.style.TextAppearance_Material_Display1);

                }else{
                    binding.finalResult.setTextAppearance(this,android.R.style.TextAppearance_Material_Display2);
                    binding.edtInput.setTextAppearance(this,android.R.style.TextAppearance_Material_Display1);
                }

                String expression = (binding.edtInput.getText().toString());
                double ans = evaluate(expression);
                binding.finalResult.setVisibility(View.VISIBLE);
                binding.finalResult.setText(""+ans);


                storeHistory();

                binding.edtInput.setText("");
                binding.edtInput.setHint("0");

                setResultList();
                break;

        }
    }

    private void storeHistory()
    {
        if (lastTenResults.size() < 10) {
            lastTenResults.add(binding.edtInput.getText().toString() +" = "+binding.finalResult.getText().toString());

            if (isLogin) {
                storeHistoryInDB(lastTenResults);
            }
        }else {
            lastTenResults.remove(0);
            lastTenResults.add(binding.edtInput.getText().toString() +" = "+binding.finalResult.getText().toString());

            if (isLogin) {
                storeHistoryInDB(lastTenResults);
            }
        }
    }

    private void storeHistoryInDB(List<String> lastTenResults) {
        for (int i=0; i<lastTenResults.size(); i++) {
            String key = getNewChildKey();
            databaseReference = firebaseDatabase.getReference("History").child(mobile);
            databaseReference.child(""+i).setValue(lastTenResults.get(i));
        }

    }

    private void setResultList() {

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.row_textview,R.id.txtView, lastTenResults);
        binding.rvResultList.setAdapter(arrayAdapter);
    }

    public static double evaluate(String expression)
    {
        char[] input = expression.toCharArray();

        valueStack = new Stack<Double>();
        opretorStack = new Stack<Character>();

        for (int i = 0; i < input.length; i++)
        {
            // Current token is a whitespace, skip it
            if (input[i] == ' ')
                continue;

            // Current token is a number, push it to stack for numbers
            if (input[i] >= '0' && input[i] <= '9')
            {
                StringBuffer sbuf = new StringBuffer();
                // There may be more than one digits in number
                while (i < input.length && input[i] >= '0' && input[i] <= '9')
                    sbuf.append(input[i++]);
                valueStack.push(Double.parseDouble((sbuf.toString())));
                i--;
            }

            // Current token is an operator.
            else if (input[i] == '+' || input[i] == '-' ||
                    input[i] == '*' || input[i] == '/')
            {
                // While top of 'opretorStack' has same or greater precedence to current
                // token, which is an operator. Apply operator on top of 'opretorStack'
                // to top two elements in valueStack stack
                while (!opretorStack.empty() && hasPrecedence(input[i], opretorStack.peek()))
                    valueStack.push(applyOp(opretorStack.pop(), valueStack.pop(), valueStack.pop()));

                // Push current token to 'opretorStack'.
                opretorStack.push(input[i]);
            }
            System.out.println("value   = "+valueStack +"\t oprator = "+opretorStack);
        }

        // Entire expression has been parsed at this point, apply remaining
        // opretorStack to remaining valueStack
        while (!opretorStack.empty())
            valueStack.push(applyOp(opretorStack.pop(), valueStack.pop(), valueStack.pop()));

        // Top of 'valueStack' contains result, return it
        return valueStack.pop();
    }

    // Returns true if 'op2' has higher or same precedence as 'op1', otherwise returns false.
    public static boolean hasPrecedence(char op1, char op2)
    {
        int tempOp1 = precedenceLevel(op1);
        int tempOp2 = precedenceLevel(op2);

        if (tempOp2 > tempOp1 || tempOp2 == tempOp1)
            return true;

        else
            return false;
    }

    /*Return the precedence value for MADS Operation */
    public static int precedenceLevel(char op)
    {
        switch (op) {
            case '*':
                return 3;
            case '+':
                return 2;
            case '/':
                return 1;
            case '-':
                return 0;
            default:
                throw new IllegalArgumentException("Operator unknown: " + op);
        }
    }

    // A utility method to apply an operator 'op' on operands 'a'
    // and 'b'. Return the result.
    public static double applyOp(char op, double b, double a)
    {
        switch (op)
        {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0)
                    throw new
                            UnsupportedOperationException("Cannot divide by zero");
                return a / b;
        }
        return 0;
    }

}
package com.mb.kibeti;


import static com.mb.kibeti.LoginActivity.EMAIL;
import static com.mb.kibeti.LoginActivity.MY_PREFERENCES;
import static com.mb.kibeti.LoginActivity.USERNAME;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MyExpensesFragment extends Fragment implements AdapterView.OnItemClickListener {

    View view;
    ProgressBar progressBar;
    String strUsername, strEmailAddress, strSetupStatus, strBusinessStatus;
    PipeLines Genetics = new PipeLines();
    ImageView imageView,imageView1;
    String strBusinessNamePh = Genetics.Earth;
    SharedPreferences sharedPreferences;
    String strBusinessName = "";
    String strUsernamePh = Genetics.Mercury;
    String strEmailAddressPh = Genetics.Venus;

    Cursor cur;
    SQLiteDatabase db;
    ExpensesCLVA adapter;
    ListView lvMyExpenses;
    DecimalFormat formatter;
    TextView tvCumulativeExpenses;
    ArrayList<FeedbackRowItem> initialList;
    String think_big = Genetics.think_big;
    String my_expenses_table = Genetics.my_expenses_table;
    String my_expenses_table_columns = Genetics.my_expenses_table_columns;

    String create_table = Genetics.create_table;
    String select_from = Genetics.select_from;

    String strAppName = Genetics.America;
    String strAppDescription = Genetics.Europe;
    String strTabColBizNam = Genetics.strTabColBizNam;
    String strTabColExpenseAmount = Genetics.strTabColExpenseAmount;

    String strItemName, strAmount, strMerge;

    FloatingActionButton floatingActionButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.my_expenses_beta, container, false);

        if(getArguments()!=null)
        {
            strBusinessName = getArguments().getString(strBusinessNamePh,null);
        }
        sharedPreferences = getContext().getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        strEmailAddress = sharedPreferences.getString(EMAIL, "");
        strUsername = sharedPreferences.getString(USERNAME, "");

        imageView = view.findViewById(R.id.imageView);
        imageView1 = view.findViewById(R.id.imageView1);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewExpense ();
            }
        });

        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewExpense ();
            }
        });
//        getActivity().setTitle("My Budget - Inflow");

//        floatingActionButton=view.findViewById(R.id.floating_action_button);
//        floatingActionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(view.getContext(), ClientNew.class);
//                view.getContext().startActivity(intent);
//            }
//        });

        formatter = new DecimalFormat("#,###,###,###");
        //openOrCreateDatabase "globally":
        db = getActivity().openOrCreateDatabase(think_big, Context.MODE_PRIVATE, null);
        db.execSQL(create_table + my_expenses_table + my_expenses_table_columns);

        tvCumulativeExpenses = view.findViewById(R.id.tvCumulativeExpenses);

        adapter = new ExpensesCLVA(getContext(), R.layout.list_item_expenses, retrieveExpenses());
        lvMyExpenses = view.findViewById(R.id.lvMyExpenses);
        lvMyExpenses.setAdapter(adapter);
        lvMyExpenses.setOnItemClickListener(this);

        return view;
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView tvItemName = view.findViewById(R.id.tvBeta);
        TextView tvAmount = view.findViewById(R.id.tvGamma);

        strItemName = tvItemName.getText().toString();
        strAmount = tvAmount.getText().toString();

        //strMerge = "\n" + strItemName + "\n\n" + strAmount + "\n";
        strMerge = "\nDelete or edit:\n\nWork in progress\n";
        //Toast.makeText(this, strMerge, Toast.LENGTH_SHORT).show();

//        //Intent nextInt = new Intent(this, TurifayKanana.class);
//        Intent nextInt = new Intent(getContext(), EditExpense.class);
//        nextInt.putExtra(strUsernamePh, strUsername);
//        nextInt.putExtra(strEmailAddressPh, strEmailAddress);
//        nextInt.putExtra(strBusinessNamePh, strBusinessName);
//        nextInt.putExtra("1", strItemName);
//        nextInt.putExtra("2", strAmount);
//        startActivity(nextInt);
//        getActivity().finish();
    }
    private ArrayList<FeedbackRowItem> retrieveExpenses() {
        initialList = new ArrayList<FeedbackRowItem>();
//        cur = db.rawQuery(select_from + " " + my_expenses_table +
//                " WHERE " + strTabColBizNam + " = '" + strBusinessName + "'" +
//                " AND " + strTabColExpenseAmount + " != 'Star'" +
//                " AND " + strTabColExpenseAmount + " != '0'", null);
        cur = db.rawQuery(select_from + " " + my_expenses_table+" WHERE " + strTabColBizNam + " = '" + strBusinessName+"'", null);

        int intCounter = 0;
        int intCumulativeExpenses = 0;
        while (cur.moveToNext()) {
            intCounter++;
            strItemName = cur.getString(3);
            strAmount = cur.getString(4);

//            int intAmount = Integer.parseInt(strAmount);

            int intAmount = 0;
            if(strAmount!=""){
                intAmount = Integer.parseInt(strAmount);
            }

            String strAmount2 = formatter.format(intAmount);

//            Toast.makeText(this, "Expense amount "+strAmount, Toast.LENGTH_SHORT).show();

            intCumulativeExpenses += intAmount;
            initialList.add(new FeedbackRowItem("" + intCounter, strItemName, strAmount2, "", "", ""));
            //initialList.add(new FeedbackRowItem("" + intCounter, strItemName, "Kshs. " + strAmount2 + "/=", "", "", ""));
        }

        String strCumulativeExpenses = formatter.format(intCumulativeExpenses);
        //tvCumulativeExpenses.setText("Kshs. " + strCumulativeExpenses + "/=");
        tvCumulativeExpenses.setText(strCumulativeExpenses);

        return initialList;
    }
    private void addNewExpense () {
//        //Toast.makeText(this, "\nAdd new expense:\n\nWork in progress\n", Toast.LENGTH_SHORT).show();
//        Intent nextInt = new Intent(getContext(), MyExpensesAlpha.class);
//        nextInt.putExtra(strUsernamePh, strUsername);
//        nextInt.putExtra(strEmailAddressPh, strEmailAddress);
//        nextInt.putExtra(strBusinessNamePh, strBusinessName);
//        startActivity(nextInt);
////        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
//        progressBar.setVisibility(View.VISIBLE);

    }

}
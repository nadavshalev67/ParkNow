package com.mypark.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mypark.R;
import com.mypark.models.CreditCard;
import com.mypark.recycler.RecyclerViewCreditCard;
import com.mypark.utilities.Utilites;

import java.util.ArrayList;
import java.util.List;


public class PaymentFragment extends Fragment {

    private View mFragment;
    private ActivitytFragmentListener mListener;
    private RecyclerViewCreditCard mAdapter;
    private Button mAddCreditCard;
    private RecyclerView mRecyclerView;

    private static final int CARD_NUMBER_TOTAL_SYMBOLS = 19; // size of pattern 0000-0000-0000-0000
    private static final int CARD_NUMBER_TOTAL_DIGITS = 16; // max numbers of digits in pattern: 0000 x 4
    private static final int CARD_NUMBER_DIVIDER_MODULO = 5; // means divider position is every 5th symbol beginning with 1
    private static final int CARD_NUMBER_DIVIDER_POSITION = CARD_NUMBER_DIVIDER_MODULO - 1; // means divider position is every 4th symbol beginning with 0
    private static final char CARD_NUMBER_DIVIDER = '-';
    private static final int CARD_DATE_TOTAL_SYMBOLS = 5; // size of pattern MM/YY
    private static final int CARD_DATE_TOTAL_DIGITS = 4; // max numbers of digits in pattern: MM + YY
    private static final int CARD_DATE_DIVIDER_MODULO = 3; // means divider position is every 3rd symbol beginning with 1
    private static final int CARD_DATE_DIVIDER_POSITION = CARD_DATE_DIVIDER_MODULO - 1; // means divider position is every 2nd symbol beginning with 0
    private static final char CARD_DATE_DIVIDER = '/';
    private static final int CARD_CVC_TOTAL_SYMBOLS = 3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragment = inflater.inflate(R.layout.payment_fragment, container, false);
        mAddCreditCard = mFragment.findViewById(R.id.add_card);
        mAddCreditCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialogButtonClicked();
            }
        });
        initRecyclerView(mFragment);

        return mFragment;
    }

    private void initRecyclerView(View mFragment) {
        mRecyclerView = mFragment.findViewById(R.id.recycler_view_credit_cards);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
        mAdapter = new RecyclerViewCreditCard(getActivity().getBaseContext(), mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        try {
            List<CreditCard> creditCards = Utilites.getCardListToSharedPrefence(getActivity().getBaseContext());
            mAdapter.setNewList(creditCards);
        } catch (Exception e) {

        }


    }

    @Override
    public void onStart() {
        super.onStart();
        mListener = (ActivitytFragmentListener) getContext();
    }

    public void showAlertDialogButtonClicked() {
        // Create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add Card Payment");
        final View customLayout = getLayoutInflater().inflate(R.layout.dialog_credit_card, null);
        builder.setView(customLayout);
        final EditText cardNumberEditText = customLayout.findViewById(R.id.cardNumberEditText);
        cardNumberEditText.addTextChangedListener(first);
        final EditText cardDateEditText = customLayout.findViewById(R.id.cardDateEditText);
        cardDateEditText.addTextChangedListener(second);
        final EditText cardCVCEditText = customLayout.findViewById(R.id.cardCVCEditText);
        cardCVCEditText.addTextChangedListener(third);
        builder.setPositiveButton("Add Card", null);
        builder.setNegativeButton("Cancel", null);

        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog2) {
                Button b = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (TextUtils.getTrimmedLength(cardNumberEditText.getText().toString()) != 19 ||
                                TextUtils.getTrimmedLength(cardDateEditText.getText().toString()) != 5 ||
                                TextUtils.getTrimmedLength(cardCVCEditText.getText().toString()) != 3) {
                            Toast.makeText(getContext(), "Input in not valid", Toast.LENGTH_SHORT).show();
                        } else {
                            saveCard(Integer.valueOf(cardNumberEditText.getText().toString().substring(15, 19)));
                            dialog.dismiss();
                            Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });
        dialog.show();
    }


    private boolean isInputCorrect(Editable s, int size, int dividerPosition, char divider) {
        boolean isCorrect = s.length() <= size;
        for (int i = 0; i < s.length(); i++) {
            if (i > 0 && (i + 1) % dividerPosition == 0) {
                isCorrect &= divider == s.charAt(i);
            } else {
                isCorrect &= Character.isDigit(s.charAt(i));
            }
        }
        return isCorrect;
    }

    private String concatString(char[] digits, int dividerPosition, char divider) {
        final StringBuilder formatted = new StringBuilder();

        for (int i = 0; i < digits.length; i++) {
            if (digits[i] != 0) {
                formatted.append(digits[i]);
                if ((i > 0) && (i < (digits.length - 1)) && (((i + 1) % dividerPosition) == 0)) {
                    formatted.append(divider);
                }
            }
        }

        return formatted.toString();
    }

    private char[] getDigitArray(final Editable s, final int size) {
        char[] digits = new char[size];
        int index = 0;
        for (int i = 0; i < s.length() && index < size; i++) {
            char current = s.charAt(i);
            if (Character.isDigit(current)) {
                digits[index] = current;
                index++;
            }
        }
        return digits;
    }

    private void saveCard(int numbers) {
        CreditCard card = new CreditCard(numbers, mAdapter.getCreditCardList().size() == 0);
        mAdapter.getCreditCardList().add(card);
        Utilites.saveCardListToSharedPrefence(getActivity().getBaseContext(), mAdapter.getCreditCardList());
        mAdapter.notifyDataSetChanged();

    }

    private final TextWatcher first = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!isInputCorrect(s, CARD_NUMBER_TOTAL_SYMBOLS, CARD_NUMBER_DIVIDER_MODULO, CARD_NUMBER_DIVIDER)) {
                s.replace(0, s.length(), concatString(getDigitArray(s, CARD_NUMBER_TOTAL_DIGITS), CARD_NUMBER_DIVIDER_POSITION, CARD_NUMBER_DIVIDER));
            }
        }
    };

    private final TextWatcher second = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!isInputCorrect(s, CARD_DATE_TOTAL_SYMBOLS, CARD_DATE_DIVIDER_MODULO, CARD_DATE_DIVIDER)) {
                s.replace(0, s.length(), concatString(getDigitArray(s, CARD_DATE_TOTAL_DIGITS), CARD_DATE_DIVIDER_POSITION, CARD_DATE_DIVIDER));
            }
        }
    };

    private final TextWatcher third = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > CARD_CVC_TOTAL_SYMBOLS) {
                s.delete(CARD_CVC_TOTAL_SYMBOLS, s.length());
            }
        }
    };


}

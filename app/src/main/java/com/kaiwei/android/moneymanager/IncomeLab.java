package com.kaiwei.android.moneymanager;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class IncomeLab {
    private static IncomeLab sIncomeLab;

    private List<Income> mIncomes;

    public static IncomeLab get(Context context){
        if(sIncomeLab == null){
            sIncomeLab = new IncomeLab(context);
        }
        return sIncomeLab;
    }

    private IncomeLab(Context context){
        mIncomes = new ArrayList<>();
        for(int i = 0; i<50; i++){
            Income income = new Income();
            income.setIncomeTotal(Double.valueOf(i));
            income.setIncomeCategory("Test");
            mIncomes.add(income);
        }
    }

    public List<Income>getIncomes(){
        return mIncomes;
    }

    public Income getIncome(UUID id){
        for(Income income : mIncomes){
            if(income.getIncomeId().equals(id)){
                return income;
            }
        }
        return null;
    }
}

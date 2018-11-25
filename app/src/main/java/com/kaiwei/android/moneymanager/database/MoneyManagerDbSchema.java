package com.kaiwei.android.moneymanager.database;

public class MoneyManagerDbSchema {
    public static final class CategoryTable{
        public static final String NAME = "income_category";

        public static final class Cols{
            public static final String NAME = "category_name";
            public static final String TYPE = "category_type";
        }
    }

    public static final class IncomeTable{
        public static final String NAME = "income_record";

        public static final class Cols{
            public static final String UUID = "income_uuid";
            public static final String DATE = "income_date";
            public static final String AMOUNT = "income_amount";
            public static final String NOTE = "income_note";
            public static final String CATEGORY= "income_category";
        }
    }
}

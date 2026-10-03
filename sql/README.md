# SQL scripts

Run in order against the Equity Trading App database (PostgreSQL 12+):

```bash
psql -d postgres -f 01_create_customer_table.sql
psql -d postgres -f 02_create_trade_table.sql
psql -d postgres -f 03_insert_customer_data.sql   # 100 sample customers
psql -d postgres -f 04_insert_trade_data.sql      # 1,148 sample trades
```

`03` and `04` are fictitious sample data and are safe to re-run (existing rows are skipped).

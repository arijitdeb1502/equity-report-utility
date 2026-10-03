# SQL scripts

Run in order against the Equity Trading App database (PostgreSQL 12+):

```bash
psql -d postgres -f 01_create_customer_table.sql
psql -d postgres -f 02_create_trade_table.sql
```

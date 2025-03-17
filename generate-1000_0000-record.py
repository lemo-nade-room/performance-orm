import sqlite3
import datetime

def main():
    conn = sqlite3.connect('test.db')
    cur = conn.cursor()
    
    cur.execute('DROP TABLE IF EXISTS testdata')
    cur.execute('CREATE TABLE testdata (id INTEGER, datetime TEXT)')

    # 挿入高速化のための設定
    conn.execute('PRAGMA synchronous = OFF')
    conn.execute('PRAGMA journal_mode = MEMORY')
    
    conn.execute('BEGIN TRANSACTION')
    
    start_date = datetime.datetime(2000, 1, 1)
    total_rows = 10_000_000
    batch_size = 10_000
    rows = []
    
    # i 秒ずつ加算すると 1000 万秒 = 約 115.74 日なので、範囲オーバーはしない
    for i in range(total_rows):
        dt = start_date + datetime.timedelta(seconds=i)
        date_str = dt.strftime('%Y-%m-%d %H:%M:%S')
        rows.append((i+1, date_str))  # id は 1 からにするなら (i+1)
        
        if (i+1) % batch_size == 0:
            cur.executemany('INSERT INTO testdata (id, datetime) VALUES (?, ?)', rows)
            rows.clear()
    
    if rows:
        cur.executemany('INSERT INTO testdata (id, datetime) VALUES (?, ?)', rows)
    
    conn.commit()
    conn.close()
    print(f'Inserted {total_rows} rows.')

if __name__ == '__main__':
    main()

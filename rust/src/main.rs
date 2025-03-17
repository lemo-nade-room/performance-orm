use rusqlite::{Connection, Result, Error, types::Type};
use chrono::{NaiveDateTime};
use std::time::Instant;

#[derive(Debug)]
struct Record {
    id: i32,
    datetime: NaiveDateTime,
}

fn main() -> Result<()> {
    let conn = Connection::open("../test.db")?;

    let start = Instant::now();

    let mut stmt = conn.prepare("SELECT id, datetime FROM testdata")?;

    let records_iter = stmt.query_map([], |row| {
        let id_val: i32      = row.get(0)?;
        let dt_str: String   = row.get(1)?;

        let dt_parsed = chrono::NaiveDateTime::parse_from_str(&dt_str, "%Y-%m-%d %H:%M:%S")
            .map_err(|e| {
                Error::FromSqlConversionFailure(
                    1,
                    Type::Text,
                    Box::new(e)
                )
            })?;

        Ok(Record { id: id_val, datetime: dt_parsed })
    })?;

    let mut records = Vec::new();
    for row_result in records_iter {
        let record = row_result?;
        records.push(record);
    }

    let duration = start.elapsed();
    println!("Read {} records in {:.3?}", records.len(), duration);

    if let Some(first) = records.first() {
        println!("First row => id: {}, datetime: {}", first.id, first.datetime);
    }

    Ok(())
}

import { Database } from "bun:sqlite";

const db = new Database("../test.db");
const query = db.query("SELECT * FROM testdata")
const records = query.all().map(r => ({ ...r, datetime: new Date(r.datetime)}))
console.log(`read ${records.length} records, first: ${JSON.stringify(records[0])}`);
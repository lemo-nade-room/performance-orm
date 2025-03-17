# Performance ORM

ORMの読み取り性能を計測します。

## 実行手順

### 初期SQLiteファイルセットアップ

1000万レコードを作成します。

```shell
python3 generate-1000_0000-record.py
```

### Swift Fluent

#### 実行スクリプト

```shell
cd swift
swift build -c release
time ./.build/release/performance-orm
```

#### 結果スナップショット

`1908.30s user 10.64s system 98% cpu 32:19.81 total`

### Swift Fluent on Docker

#### 実行スクリプト

```shell
cd swift
docker compose up -d
docker compose exec app bash
time ./performance-orm
```

#### 結果スナップショット

```
real    13m39.274s
user    13m41.585s
sys     0m2.580s
```

### Go GORM

#### 実行スクリプト

```shell
cd go
go build -o performance-orm main.go
time ./performance-orm
```

#### 結果スナップショット

3時間以上かけても終わらない

### TypeScript Bun

#### 実行スクリプト

```shell
cd bun
time bun run main.ts
```

#### 結果スナップショット

`3.27s user 0.51s system 127% cpu 2.976 total`

### Rust rusqlite

#### 実行スクリプト

```shell
cd rust
cargo build --release
time ./target/release/rust
```

#### 結果スナップショット

`2.62s user 0.10s system 93% cpu 2.919 total`

### Scala Slick

#### 実行スクリプト

```shell
cd scala
sbt assembly && time java -jar target/scala-2.13/performance-orm-scala-assembly-0.1.0.jar
```

#### 結果スナップショット

`14.78s user 0.87s system 149% cpu 10.449 total`

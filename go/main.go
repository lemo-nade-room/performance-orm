package main

import (
	"fmt"
	"log"
	"time"

	"gorm.io/driver/sqlite"
	"gorm.io/gorm"
)

type Record struct {
	ID       int       `gorm:"column:id"`
	DateTime time.Time `gorm:"column:datetime"`
}

func main() {
	db, err := gorm.Open(sqlite.Open("../test.db"), &gorm.Config{})
	if err != nil {
		log.Fatalf("failed to connect database: %v", err)
	}

	var records []Record
	start := time.Now()
	if err := db.Table("testdata").Find(&records).Error; err != nil {
		log.Fatalf("query failed: %v", err)
	}
	elapsed := time.Since(start)

	fmt.Printf("Read %d records in %v\n", len(records), elapsed)
}

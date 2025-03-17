import Fluent
import FluentSQLiteDriver
import Foundation
import NIOCore
import NIOPosix

func main() async throws {
    // Arrange
    let eventLoopGroup = MultiThreadedEventLoopGroup.singleton
    let threadPool = NIOThreadPool(numberOfThreads: System.coreCount)
    try await threadPool.shutdownGracefully()
    threadPool.start()
    let databases = Databases(threadPool: threadPool, on: eventLoopGroup)
    databases.use(.sqlite(.file("../test.db")), as: .sqlite)
    let logger = Logger(label: "Performance ORM")
    let db =
        databases
        .database(logger: logger, on: eventLoopGroup.any())! as! SQLDatabase

    struct Record: Codable {
        var id: Int
        var datetime: Date
    }

    // Act
    do {
        let records =
            try await db
            .raw("SELECT * FROM testdata")
            .all(decoding: Record.self)
        print("count: \(records.count), first: \(records[0])")
    } catch {
        print(String(reflecting: error))
    }

    await databases.shutdownAsync()
    try await threadPool.shutdownGracefully()
}
try await main()

## About

A POC for integration tests with and without a related repository.

For repository testing, `testcontainers` is used, which is built specifically for the tests and is detached from the
application repositories.

### Notes

The `testcontainers` interface uses `PostgreSQL` as its implementation; however, the implementor could be swapped for
any other database system.
package uk.co.mulecode.ddd.test.infrastructure

import org.springframework.beans.factory.annotation.Autowired
import uk.co.mulecode.ddd.IntegrationTest

import javax.sql.DataSource
import java.sql.Connection

class JpaMysqlIntegrationTest extends IntegrationTest {

    private static final int TOTAL_TABLES = 6

    @Autowired
    private DataSource dataSource

    def "Migration Files Applied Successfully"() {
        given:
        def databaseSql = "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'test'"
        int tableCount = 0

        when:
        try (Connection connection = dataSource.getConnection()) {
            println "Connected to schema: " + dataSource.getConnection().getCatalog()
            def statement = connection.createStatement()
            def resultSet = statement.executeQuery(databaseSql)
            if (resultSet.next()) {
                tableCount = resultSet.getInt(1)
            }
        }

        then:
        assert tableCount == TOTAL_TABLES: "Expected at least ${TOTAL_TABLES} tables, but found ${tableCount}. Migration might have failed."
    }
}

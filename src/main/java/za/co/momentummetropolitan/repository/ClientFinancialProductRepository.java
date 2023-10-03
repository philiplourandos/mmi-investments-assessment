package za.co.momentummetropolitan.repository;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import za.co.momentummetropolitan.dto.ClientFinancialProducts;
import za.co.momentummetropolitan.enums.FinancialProductsEnum;

@Repository
public class ClientFinancialProductRepository {
    private static final String CLIENT_PRODUCT_ID = "id";
    private static final String CLIENT_PRODUCT_NAME = "name";
    private static final String CLIENT_PRODUCT_TYPE = "type";
    private static final String CLIENT_PRODUCT_BALANCE = "balance";
    
    private final JdbcTemplate jdbc;

    public ClientFinancialProductRepository(final JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<ClientFinancialProducts> getClientProducts(final Long id) {
        return jdbc.query(
                """
                SELECT
                  cp.id AS id,
                  fp.name AS name,
                  fp.type AS type,
                  cp.balance AS balance
                FROM
                  FINANCIAL_PRODUCTS fp,
                  CLIENT_PRODUCTS cp,
                  CLIENTS c
                WHERE
                  cp.client_id = c.id
                  AND
                  cp.financial_product_id = fp.id
                  AND
                  c.id = ?
                ORDER BY
                  cp.id DESC
                """, (pss) -> pss.setLong(1, id), (rs, i) -> {
                    return new ClientFinancialProducts(
                            rs.getLong(CLIENT_PRODUCT_ID),
                            rs.getString(CLIENT_PRODUCT_NAME), 
                            FinancialProductsEnum.valueOf(rs.getString(CLIENT_PRODUCT_TYPE)),
                            rs.getBigDecimal(CLIENT_PRODUCT_BALANCE));
                });
    }
}

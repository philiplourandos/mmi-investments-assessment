package za.co.momentummetropolitan.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import za.co.momentummetropolitan.dto.ClientFinancialProduct;
import za.co.momentummetropolitan.entities.ClientProduct;
import za.co.momentummetropolitan.enums.FinancialProductsEnum;

@Repository
public class ClientFinancialProductRepository {
    private static final String CLIENT_PRODUCT_ID = "id";
    private static final String CLIENT_PRODUCT_NAME = "name";
    private static final String CLIENT_PRODUCT_TYPE = "type";
    private static final String CLIENT_PRODUCT_BALANCE = "balance";
    private static final String CLIENT_PRODUCT_FINANCIAL_PRODUCT_ID = "financial_product_id";
    private static final String CLIENT_PRODUCT_CLIENT_ID = "client_id";

    private final JdbcTemplate jdbc;

    public ClientFinancialProductRepository(final JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<ClientFinancialProduct> findClientProductsByClientId(final Long clientId) {
        return jdbc.query("""
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
                """, (pss) -> pss.setLong(1, clientId), (rs, i) -> {
                    return new ClientFinancialProduct(
                            rs.getLong(CLIENT_PRODUCT_ID),
                            rs.getString(CLIENT_PRODUCT_NAME), 
                            FinancialProductsEnum.valueOf(rs.getString(CLIENT_PRODUCT_TYPE)),
                            rs.getBigDecimal(CLIENT_PRODUCT_BALANCE));
                });
    }

    public Optional<ClientProduct> findById(final Long id) {
        final List<ClientProduct> products = jdbc.query(
                """
                SELECT
                    cp.id AS id,
                    cp.client_id AS client_id,
                    cp.financial_product_id AS financial_product_id,
                    cp.balance AS balance
                FROM
                    CLIENT_PRODUCTS cp
                WHERE
                    cp.id = ?
                """, (pss) -> pss.setLong(1, id), (rs, i) -> {
                    return new ClientProduct(rs.getLong(CLIENT_PRODUCT_ID), 
                            rs.getLong(CLIENT_PRODUCT_CLIENT_ID), 
                            rs.getLong(CLIENT_PRODUCT_FINANCIAL_PRODUCT_ID), 
                            rs.getBigDecimal(CLIENT_PRODUCT_BALANCE));
                });

        if (products.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(products.get(0));
        }
    }
}

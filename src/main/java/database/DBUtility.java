package database;

import config.ConfigReader;
import utilities.LogUtility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JDBC validation layer for WMS/ERP data consistency checks.
 */
public final class DBUtility {

    private static final org.apache.logging.log4j.Logger LOG = LogUtility.getLogger(DBUtility.class);

    private DBUtility() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                ConfigReader.get("db.url"),
                ConfigReader.get("db.username"),
                ConfigReader.get("db.password"));
    }

    public static List<Map<String, Object>> executeQuery(String sql, Object... params) {
        List<Map<String, Object>> rows = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            bindParams(ps, params);
            try (ResultSet rs = ps.executeQuery()) {
                int columnCount = rs.getMetaData().getColumnCount();
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        row.put(rs.getMetaData().getColumnLabel(i), rs.getObject(i));
                    }
                    rows.add(row);
                }
            }
        } catch (SQLException ex) {
            LOG.error("Query execution failed: {}", sql, ex);
            throw new IllegalStateException("DB query failed", ex);
        }
        return rows;
    }

    public static int executeUpdate(String sql, Object... params) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            bindParams(ps, params);
            return ps.executeUpdate();
        } catch (SQLException ex) {
            LOG.error("Update failed: {}", sql, ex);
            throw new IllegalStateException("DB update failed", ex);
        }
    }

    public static boolean exists(String sql, Object... params) {
        return !executeQuery(sql, params).isEmpty();
    }

    private static void bindParams(PreparedStatement ps, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            ps.setObject(i + 1, params[i]);
        }
    }

    // Sample WMS validation queries
    public static final String SQL_PRODUCT_BY_SKU =
            "SELECT id, sku, name, status FROM products WHERE sku = ?";
    public static final String SQL_INVENTORY_BY_PRODUCT =
            "SELECT warehouse_id, location_id, qty_available, qty_blocked, qty_damaged " +
            "FROM inventory WHERE product_id = ? ORDER BY created_at";
    public static final String SQL_PICKLIST_BY_ID =
            "SELECT id, status, picking_rule, warehouse_id FROM picklists WHERE id = ?";
    public static final String SQL_SERIAL_BY_NUMBER =
            "SELECT serial_number, product_id, status FROM serial_inventory WHERE serial_number = ?";
    public static final String SQL_BATCH_BY_NUMBER =
            "SELECT batch_number, expiry_date, qty_available FROM batch_inventory WHERE batch_number = ?";
    public static final String SQL_ASN_LINKAGE =
            "SELECT asn_id, product_id, qty_expected, qty_received FROM asn_lines WHERE asn_id = ?";
    public static final String SQL_AUDIT_TRAIL =
            "SELECT entity_type, entity_id, action, performed_by, performed_at " +
            "FROM audit_log WHERE entity_id = ? ORDER BY performed_at DESC";
}

-- LogixERP WMS sample validation queries (adjust table/column names per environment schema)

-- Product creation
SELECT id, sku, name, status, warehouse_id
FROM products
WHERE sku = ?;

-- Inventory allocation
SELECT warehouse_id, location_id, qty_available, qty_blocked, qty_damaged, qty_expired
FROM inventory
WHERE product_id = ?
ORDER BY created_at ASC;

-- Picklist generation
SELECT id, status, picking_rule, warehouse_id, created_at
FROM picklists
WHERE id = ?;

-- Serial mapping
SELECT serial_number, product_id, status, warehouse_id
FROM serial_inventory
WHERE serial_number = ?;

-- Batch mapping
SELECT batch_number, expiry_date, qty_available, warehouse_id
FROM batch_inventory
WHERE batch_number = ?;

-- ASN linkage
SELECT asn_id, product_id, qty_expected, qty_received, status
FROM asn_lines
WHERE asn_id = ?;

-- Audit trail
SELECT entity_type, entity_id, action, performed_by, performed_at
FROM audit_log
WHERE entity_id = ?
ORDER BY performed_at DESC;

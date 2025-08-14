package org.example;

import io.jmix.core.metamodel.annotation.JmixEntity;

@JmixEntity
public class Item {
    private String supplierId;
    private String supplierName;
    private String supplierTaxCode;

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    private String branchCode;

    public Item(String supplierId, String supplierName, String supplierTaxCode) {
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.supplierTaxCode = supplierTaxCode;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public String getSupplierTaxCode() {
        return supplierTaxCode;
    }
}

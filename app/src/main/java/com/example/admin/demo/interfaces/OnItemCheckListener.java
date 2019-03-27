package com.example.admin.demo.interfaces;

import com.example.admin.demo.item.TaxRateItem;

public interface OnItemCheckListener {
    void onItemCheck(TaxRateItem item);
    void onItemUncheck(TaxRateItem item);
}
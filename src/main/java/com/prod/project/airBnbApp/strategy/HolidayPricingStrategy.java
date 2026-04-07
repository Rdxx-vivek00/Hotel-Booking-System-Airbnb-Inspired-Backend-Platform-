package com.prod.project.airBnbApp.strategy;

import com.prod.project.airBnbApp.entity.Inventory;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class HolidayPricingStrategy implements PricingStrategy{
    private final PricingStrategy wrapped;

    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        BigDecimal price=wrapped.calculatePrice(inventory);
        boolean isHoliday=true;
        if(isHoliday)
        {
            price=price.multiply(BigDecimal.valueOf(1.25));

        }
        return price;
    }
}

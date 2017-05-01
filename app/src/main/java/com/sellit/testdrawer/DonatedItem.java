package com.sellit.testdrawer;

/**
 * Created by Colton Jacobson on 4/29/2017.
 */

public class DonatedItem extends Donation
{
    public DonatedItem(String name, String price, String description, int rating, String uid) {

        super(name, "0", description, rating, uid, false);

    }

    public DonatedItem() {
    }
}

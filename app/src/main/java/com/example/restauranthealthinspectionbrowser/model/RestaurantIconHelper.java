package com.example.restauranthealthinspectionbrowser.model;

import com.example.restauranthealthinspectionbrowser.R;

/**
 * A helper class for setting custom restaurant icons.
 */
public class RestaurantIconHelper {

    public int getIconResId(String title) {

        if (title.contains("A&W")) {
            return R.drawable.aandw;
        }
        else if (title.contains("7-Eleven")) {
            return R.drawable.seven_eleven;
        }
        else if (title.contains("Blenz")) {
            return R.drawable.blenz_coffee;
        }
        else if (title.contains("Booster Juice")) {
            return R.drawable.booster_juice;
        }
        else if (title.contains("Boston Pizza")) {
            return R.drawable.boston_pizza;
        }
        else if (title.contains("Burger King")) {
            return R.drawable.burger_king;
        }
        else if (title.contains("Church's Chicken")) {
            return R.drawable.churchs_chicken;
        }
        else if (title.contains("COBS Bread")) {
            return R.drawable.cobs_bread;
        }
        else if (title.contains("Dairy Queen")) {
            return R.drawable.dairy_queen;
        }
        else if (title.contains("Domino's Pizza")) {
            return R.drawable.dominos_pizza;
        }
//            else if (title.contains("Elements Casino")) {
//                return R.drawable.elements_casino);
//            }
        else if (title.contains("Freshslice")) {
            return R.drawable.fresh_slice;
        }
        else if (title.contains("McDonald's")) {
            return R.drawable.mcdonalds;
        }
        else if (title.contains("Panago")) {
            return R.drawable.panago;
        }
        else if (title.contains("Pizza Hut")) {
            return R.drawable.pizza_hut;
        }
        else if (title.contains("Safeway")) {
            return R.drawable.safeway;
        }
        else if (title.contains("Starbucks")) {
            return R.drawable.starbucks;
        }
        else if (title.contains("Subway")) {
            return R.drawable.subway;
        }
        else if (title.contains("Tim horton's")) {
            return R.drawable.tim_hortons;
        }
        else {
            return R.drawable.restaurant_icon;
        }
    }
}

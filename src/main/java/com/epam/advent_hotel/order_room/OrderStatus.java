package com.epam.advent_hotel.order_room;

/**
 * Created by Elizaveta Kapitonova on 01.02.16.
 */
public enum OrderStatus {
    REQUESTED {
        @Override
        public String toString() {
            return "REQUESTED";
        }
    }, IN_DISCUSSION {
        @Override
        public String toString() {
            return "IN_DISCUSSION";
        }
    }, APPROVED {
        @Override
        public String toString() {
            return "APPROVED";
        }
    }, PAID {
        @Override
        public String toString() {
            return "PAID";
        }
    }, REJECTED {
        @Override
        public String toString() {
            return "REJECTED";
        }
    };


}

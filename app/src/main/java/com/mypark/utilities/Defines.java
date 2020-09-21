package com.mypark.utilities;

import com.google.android.gms.maps.model.LatLng;

public class Defines {

    //TableNames
    public class TableNames {
        public static final String USERS = "Users";
        public static final String PARKING_CREATE = "ParkingCreate";
        public static final String PARKING_OCCUPIED = "Parking_Occupied";
    }

    //TableFields
    public class TableFields {
        public static final String USERS_USERNAME = "user_name";
        public static final String USERS_FAMILY_NAME = "family_name";
        public static final String USERS_FIRST_NAME = "private_name";
        public static final String USERS_ID_PIC = "id_pic";
        public static final String USERS_LICENSE_PIC = "license_pic";

        public static final String PARKING_CREATE_START_TIME = "start_time";
        public static final String PARKING_CREATE_FINISH_TIME = "finish_time";
        public static final String PARKING_CREATE_PRICE = "price";
        public static final String PARKING_CREATE_LOCATION_LNG = "location_lag";
        public static final String PARKING_CREATE_LOCATION_LAT = "location_lat";
    }

    public class Intent {
        public static final String KEY_INTENT_SOURCE = "source";
        public static final String VALUE_INTENT_SOURCE_REGISTER = "Register";
    }

    public static class Location {
        public static final LatLng tlvLocation = new LatLng(32.083010, 34.779690);
    }


}

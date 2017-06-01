package com.ensatus.truqartmerchant.app;

/**
 * Created by Praveen on 25-11-2016.
 */

public class EndPoints {

    // URLS
    //private static final String BASE_URL = "http://54.175.46.109/truqart_v100";/*amazon server*/
    // public static final String BASE_URL = "http://192.168.1.120:8080/truqart_v100";/*vishu sir lap*/
    // private static final String BASE_URL = "http://192.168.1.100:8080/truqart_v100";/*office local*/
     //private static final String BASE_URL = "http://130.211.245.178:8080/truqart_v100";/*Google Server  version 1*/
    private static final String BASE_URL = "http://130.211.245.178:8080/Truqartv3";/*Google Server version 3*/

    public static final String PRODUCT_URL = BASE_URL+"/gmp";
    public static final String UPLOAD_PRODUCT_URL = BASE_URL+"/npa";
    public static final String CATEGORY_URL = BASE_URL+"/gcm";
    public static final String LOGIN_URL =BASE_URL+"/ml";
    public static final String MAIN_SUMMARY= BASE_URL+"/oi";
    public static final String REGISTRATION_URL = BASE_URL+"/nmr";
    public static final String UPDATE_MERCHANT_URL = BASE_URL+"/upm";
    public static final String EDIT_PRODUCT_URL = BASE_URL+"/upp";
    public static final String UPLOAD_MERCHANT_CONTACTS = BASE_URL+"/umc";
    public static final String GET_MERCHANT_CONTACTS= BASE_URL+"/gmcus";
    public static final String SEND_INVITATION = BASE_URL+"/si";
    public static final String ADD_EXHIBITION_URL = BASE_URL+"/aee";
    public static final String GET_EXHIBITION_URL = BASE_URL+"/getExhibition";
    public static final String EDIT_EXHIBITION_URL = BASE_URL+"/UpdateExhibition";
    public static final String UNCAT_PRODUCT_URL = BASE_URL+"/getUnCategorizedProducts";
    public static final String ORDERS_BY_MER_URL = BASE_URL+"/go";

    // common constants
    public static final String STATUS = "status";
    public static final String MESSAGE = "msg";
    public static final String MERCHANT_ID = "mid";
    public static final String CATEGORY_ID = "cid";
    public static final String POSITION = "pos";
    public static final String VISIBLE = "visible";
    public static final String COMMENTS = "comments";



    //merchant constants
    public static final String KEY_USER_ID = "id";
    public static final String KEY_USER_NAME = "name";
    public static final String KEY_USER_EMAIL = "email";
    public static final String KEY_USER_PHONE = "mobile";
    public static final String KEY_USER_PASSWORD = "pwd";
    public static final String KEY_USER_SHOP = "shopname";
    public static final String KEY_USER_ADDRESS1 = "addr1";
    public static final String KEY_USER_ADDRESS2 = "addr2";
    public static final String KEY_USER_DEVICE_ID = "deviceid";
    public static final String KEY_USER_LAT = "latitude";
    public static final String KEY_USER_LONG = "longitude";
    public static final String KEY_USER_SHOP_PHOTO1 = "shopphoto1";
    public static final String KEY_USER_SHOP_PHOTO2 = "shopphoto2";
    public static final String KEY_USER_SHOP_PHOTO3 = "shopphoto3";
    public static final String KEY_USER_SHOP_PHOTO4 = "shopphoto4";
    public static final String KET_USER_C_DATE = "createddate";
    public static final String KEY_USER_ICON = "icon";
    public static final String SHOP_ICON = "shopicon";
    public static final String MERCHANT = "merchant";




    //Login Constants
    public static final String  MERCHANT_LOGIN = "merchantlogin";
    public static final String DEVICE_ID = "deviceid";
    public static final String MOBILE = "mobile";
    public static final String PASSWORD = "pwd";

    //products Constants
    public static final String ALL_PRODUCTS = "Productlist";
    public static final String PRODUCT = "product";
    public static final String PROD_NAME = "name";
    public static final String PROD_PRICE = "price";
    public static final String PROD_IMAGE1 = "image1";
    public static final String PROD_IMAGE2 = "image2";
    public static final String PROD_IMAGE3 = "image3";
    public static final String PROD_IMAGE4 = "image4";
    public static final String PROD_PID = "pid";
    public static final String PROD_DESC = "desc";
    public static final String PROD_LIKES = "likes";
    public static final String MERCHANT_PROD_ID = "skuid";
    public static final String T_COUNT = "Tlcount";
    public static final String UNIT = "unit";

    public static final String PROD_ID = "id";// used in uncategorized


    //category constants
    public static final String CATEGORY = "Category";
    public static final String CAT_ID = "catid";
    public static final String CAT_NAME = "name";

    // contacts constants
    public static final String MOBILES = "mobiles";
    public static final String CONTACTS = "Contacts";
    public static  final String NUMBER = "number";

    //exhibition constants
    public static final String EVENT_NAME ="eventname" ;
    public static final String EVENT_DESC = "textdesc" ;
    public static final String EVENT_ADDRESS = "location";
    public static final String START_DATE = "startdate";
    public static final String END_DATE = "enddate";
    public static final String EVENT_INVI_IMAGE = "image";
    public static final String EXHIBITION_INFO = "exhibition_info";
    public static final String EVENT_ID = "exid";
    public static final String DISCOUNT = "discount";
    public static final String ORDER_INFO = "order_info";
    public static final String PROD_UNIT = "unit";


    //invitation constants
}

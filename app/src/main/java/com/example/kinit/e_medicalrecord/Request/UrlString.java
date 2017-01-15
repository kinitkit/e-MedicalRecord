package com.example.kinit.e_medicalrecord.Request;
public class UrlString {
    public static final int POST = 1;
    public static final String IMAGE_URL = "http://192.168.0.146:80/Medical%20Record/";
    //public static final String URL = "http://192.168.56.2:80/Medical%20Record/main.php";
    //public static final String URL = "http://192.168.0.104:80/Medical%20Record/main.php"; // sa balay
    public static final String URL = "http://192.168.0.146:80/Medical%20Record/main.php"; // sa balay
    //public static final String URL = "http://192.168.0.113:80/Medical%20Record/main.php"; // ila jason
    //public static final String URL = "http://192.168.43.19:80/Medical%20Record/main.php"; // sa cp
    //public static final String URL = "http://172.16.8.152:80/Medical%20Record/main.php"; // sa hcdc
    //public static final String URL = "http://192.168.8.100:80/Medical%20Record/main.php"; // caloy

    public static String getImageUrl(String url){
        return IMAGE_URL + url;
    }
}

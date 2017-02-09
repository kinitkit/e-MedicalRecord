package com.example.kinit.e_medicalrecord.General.Request;

public class UrlString {

    public static final int POST = 1;

    //private static final String ipAddress = "http://192.168.56.2:80/medical_record/"; //sa Virtual Box
    //private static final String ipAddress = "http://192.168.0.146:80/medical_record/"; //sa balay
    //static final String ipAddress = "http://192.168.43.19:80/medical_record/"; //sa cp
    private static final String ipAddress = "http://emr.bsitcapstone.com/medical_record/"; //sa online

    public static final String URL = ipAddress + "main.php";
    public static final String URL_ADMISSION = ipAddress + "mobile_functions/admission.php";
    public static final String URL_REGISTRATION = ipAddress + "mobile_functions/registration.php";
    public static final String URL_SEARCH = ipAddress + "mobile_functions/search.php";
    public static final String URL_USER_INFORMATION = ipAddress + "mobile_functions/user_information.php";
    public static final String URL_MEDICAL_PRESCRIPTION = ipAddress + "mobile_functions/medical_prescription.php";
    public static final String URL_LABORATORY = ipAddress + "mobile_functions/laboratory.php";
    public static final String URL_MY_PHYSICIAN = ipAddress + "mobile_functions/my_physician.php";
    public static final String URL_ALLERGY = ipAddress + "mobile_functions/allergy.php";
    public static final String URL_SETTINGS = ipAddress + "mobile_functions/settings.php";
    public static final String URL_VACCINATION = ipAddress + "mobile_functions/vaccination.php";
    public static final String URL_PAST_MEDICAL = ipAddress + "mobile_functions/past_medical_history.php";
    public static final String URL_SURGICAL = ipAddress + "mobile_functions/surgical_history.php";
    public static final String URL_FAMILY = ipAddress + "mobile_functions/family_history.php";
    public static final String URL_SOCIAL = ipAddress + "mobile_functions/social_history.php";
    public static final String URL_CONSULTATION = ipAddress + "mobile_functions/consultation.php";

    //public static final String URL = "http://192.168.0.104:80/Medical%20Record/main.php"; // sa balay
    //public static final String URL = "http://192.168.0.146:80/Medical%20Record/main.php"; // sa balay
    //public static final String URL = "http://192.168.0.113:80/Medical%20Record/main.php"; // ila jason
    //public static final String URL = "http://192.168.43.19:80/Medical%20Record/main.php"; // sa cp
    //public static final String URL = "http://172.16.8.152:80/Medical%20Record/main.php"; // sa hcdc
    //public static final String URL = "http://192.168.8.100:80/Medical%20Record/main.php"; // caloy

    public static String getImageUrl(String url) {
        return ipAddress + url;
    }
}

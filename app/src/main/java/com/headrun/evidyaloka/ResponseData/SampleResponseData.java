package com.headrun.evidyaloka.ResponseData;

import android.net.Uri;

import com.headrun.evidyaloka.config.ApiEndpoints;

/**
 * Created by sujith on 10/1/17.
 */

public class SampleResponseData {

    public static String data1 = "{\"data\": [{\"description\": \"Managundi is a Village in Dharwad Taluk in Dharwad District of Karnata.. <span style=\"color:#2EC7F0;cursor:pointer\">Know more >></span>\", \"title\":\"Managundi - Vajragalu\", \"image\": \"/static/uploads/center/IMG_2159_255x170.JPG\", \"tags\": {\"subjects\": [\"English_Foundation\", \"Science\"], \"months\": [\"June\"]}, \"pending_courses\": 2, \"running_courses\": 2, \"id\": 37},{\"description\": \"Managundi is a Village in Dharwad Taluk in Dharwad District of Karnata.. <span style=\"color:#2EC7F0;cursor:pointer\">Know more >></span>\", \"title\":\"Managundi - Vajragalu\", \"image\": \"/static/uploads/center/IMG_2159_255x170.JPG\", \"tags\": {\"subjects\": [\"English_Foundation\", \"Science\"], \"months\": [\"June\"]}, \"pending_courses\": 2, \"running_courses\": 2, \"id\": 37}]}";
    public static String data = "{\"data\": [{\"description\": \"Managundi is a Village in Dharwad Taluk in Dharwad District of Karnata.. <span style=color:#2EC7F0;cursor:pointer>Know more >></span>\", \"title\":\"Managundi - Vajragalu\", \"image\": \"/static/uploads/center/IMG_2159_255x170.JPG\", \"tags\": {\"subjects\": [\"English_Foundation\", \"Science\"], \"months\": [\"June\"]}, \"pending_courses\": 2, \"running_courses\": 2, \"id\": 37},{\"description\": \"Managundi is a Village in Dharwad Taluk in Dharwad District of Karnata.. <span style=color:#2EC7F0;cursor:pointer>Know more >></span>\", \"title\":\"Managundi - Vajragalu\", \"image\": \"/static/uploads/center/IMG_2159_255x170.JPG\", \"tags\": {\"subjects\": [\"English_Foundation\", \"Science\"], \"months\": [\"June\"]}, \"pending_courses\": 2, \"running_courses\": 2, \"id\": 37},{\"description\": \"Managundi is a Village in Dharwad Taluk in Dharwad District of Karnata.. <span style=color:#2EC7F0;cursor:pointer>Know more >></span>\", \"title\":\"Managundi - Vajragalu\", \"image\": \"/static/uploads/center/IMG_2159_255x170.JPG\", \"tags\": {\"subjects\": [\"English_Foundation\", \"Science\"], \"months\": [\"June\"]}, \"pending_courses\": 2, \"running_courses\": 2, \"id\": 37},{\"description\": \"Managundi is a Village in Dharwad Taluk in Dharwad District of Karnata.. <span style=color:#2EC7F0;cursor:pointer>Know more >></span>\", \"title\":\"Managundi - Vajragalu\", \"image\": \"/static/uploads/center/IMG_2159_255x170.JPG\", \"tags\": {\"subjects\": [\"English_Foundation\", \"Science\"], \"months\": [\"June\"]}, \"pending_courses\": 2, \"running_courses\": 2, \"id\": 37}]}";

    public static Uri image = Uri.parse(ApiEndpoints.BASE_URL + "static/images/main_page1.jpg");
    public static Uri image1 = Uri.parse(ApiEndpoints.BASE_URL + "static/uploads/center/2014-07-25_12-09-48.672_1_255x170.jpg");
    public static Uri image2 = Uri.parse(ApiEndpoints.BASE_URL + "static/uploads/center/2017-01-02_12-58-42.653_255x170.jpg");
    public static Uri image3 = Uri.parse(ApiEndpoints.BASE_URL + "static/uploads/center/Marewad_255x170.jpg");

    public static Uri[] images_uri = {image, image1, image2, image3};

    public static String SchoolDetails_img = "{\"data\": [{\"image\": \"/static/uploads/center/IMG_2159_255x170.JPG\", \"name\":\"Teacher Name\"},{\"image\": \"/static/uploads/center/IMG_2159_255x170.JPG\", \"name\":\"Teacher Name\"},{\"image\": \"/static/uploads/center/IMG_2159_255x170.JPG\", \"name\":\"Teacher Name\"}]}";
    public static String FilterResponse = "{\"languages\": [\"Tamil\", \"Hindi\", \"Telugu\", \"Kannada\", \"Marathi\"], \"states\": [\"Tamil Nadu\", \"Andhra Pradesh\", \"Jharkhand\", \"Karnataka\", \"Assam\", \"West Bengal\", \"Maharashtra\", \"Tamilnadu\"]}";

    public static String Sessions_data = "{\"next_page\": 1,\"total\": 20,\"role\": \"Super Admin\",\"results\": [{\"status\": \"Completed\",\"center\": \"Mallela - Muthyalu\",\"grade\": \"7th class\",\"teacher\": \"harika rathode\",\"topic\": \"14.Area and Perimeter\",\"time\": \"3 : 30 PM\", \"date\": \"15/02/2017\",\"id\": 75552,\"subject\": \"Maths\"},{\"status\": \"Completed\",\"center\": \"Mallela - Muthyalu\",\"grade\": \"7th class\",\"teacher\": \"harika rathode\",\"topic\": \"14.Area and Perimeter\",\"time\": \"3 : 30 PM\", \"date\": \"15/02/2017\",\"id\": 75552,\"subject\": \"Maths\"},{\"status\": \"Completed\",\"center\": \"Mallela - Muthyalu\",\"grade\": \"7th class\",\"teacher\": \"harika rathode\",\"topic\": \"14.Area and Perimeter\",\"time\": \"3 : 30 PM\", \"date\": \"15/02/2017\",\"id\": 75552,\"subject\": \"Maths\"},{\"status\": \"Completed\",\"center\": \"Mallela - Muthyalu\",\"grade\": \"7th class\",\"teacher\": \"harika rathode\",\"topic\": \"14.Area and Perimeter\",\"time\": \"3 : 30 PM\", \"date\": \"15/02/2017\",\"id\": 75552,\"subject\": \"Maths\"}]}";
}

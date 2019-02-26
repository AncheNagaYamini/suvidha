package com.user.alltronics.ruralpro;

/**
 * Created by Belal on 10/24/2015.
 */
public class Config {

    //Address of our scripts of the CRUD
    public static final String URL_ADD="https://pro10997.000webhostapp.com/RuralPro/rural/addProb.php";
    public static final String URL_GET_ALL = "https://pro10997.000webhostapp.com/RuralPro/rural/getAllProb.php";
    public static final String URL_GET_ALL_FILT = "https://pro10997.000webhostapp.com/RuralPro/rural/getAllProbFilt.php?d=";
    public static final String URL_GET_PROB = "https://pro10997.000webhostapp.com/RuralPro/rural/getProb.php?id=";
    public static final String URL_UPDATE_EMP = "http://10.0.0.4/Android/CRUD/emp/updateEmp.php";
    public static final String URL_DELETE_EMP = "http://10.0.0.4/Android/CRUD/emp/deleteEmp.php?id=";

    public static final String URL_ADD_USER="https://pro10997.000webhostapp.com/RuralPro/rural/addUserSolution.php";
    public static final String URL_GET_USER = "https://pro10997.000webhostapp.com/RuralPro/rural/getAllUserSolutions.php?id=";

    //Keys that will be used to send the request to php scripts
    public static final String KEY_EMP_ID = "id";
    public static final String KEY_EMP_SUBJECT = "subject";
    public static final String KEY_EMP_DESCRIPTION = "description";
    public static final String KEY_EMP_SAL = "salary";

    public static final String KEY_EMP_SAL2 = "salary";

    public static final String KEY_EMP_SOL_ID = "solution_id";
    public static final String KEY_EMP_PROB_ID = "problem_id";
    public static final String KEY_EMP_SOLUTION = "solution";

    public static final String KEY_EMP_TYPE_ID = "type_id";
    public static final String KEY_EMP_PROB_SUBJECT = "problem_subject";

    //JSON Tags
    public static final String TAG_JSON_ARRAY="result";
    public static final String TAG_ID = "id";
    /*public static final String TAG_NAME = "name";
    public static final String TAG_DESG = "desg";
    public static final String TAG_SAL = "salary";*/

    //employee id to pass with intent
    public static final String EMP_ID = "emp_id";



    //JSON URL
    public static final String DATA_URL = "https://pro10997.000webhostapp.com/RuralPro/123_st/json.php";
    public static final String DATA_URL_PROBLEMS = "https://pro10997.000webhostapp.com/RuralPro/123_st/json_problems.php";

    public static final String DATA_URL_MANDALS = "https://pro10997.000webhostapp.com/RuralPro/123_st/json_mandals.php?id=";

    public static final String DATA_URL_VILLAGES = "https://pro10997.000webhostapp.com/RuralPro/123_st/json_villages.php?id=";


    public static final String TAG_DISTRICT_ID = "district_id";
    public static final String TAG_DISTRICT_NAME = "district_name";

    public static final String TAG_MANDAL_ID = "mandal_id";
    public static final String TAG_MANDAL_NAME = "mandal_name";

    public static final String TAG_VILLAGE_ID = "village_id";
    public static final String TAG_VILLAGE_NAME = "village_name";

    public static final String TAG_SOLUTION = "solution";
    public static final String TAG_INITIATIVE = "initiative";
    public static final String TAG_INITIATIVE_STATUS = "initiative_status";

    //JSON array name
    public static final String JSON_ARRAY = "result";
}

package com.mb.kibeti;

public class PipeLines {

    public String America = "My Business Partner";
    public String Europe = "App Description";
    public String Mercury = "Username";
    public String Venus = "EmailAddress";
    public String Earth = "BusinessName";
    public String strSelectedMonthPh = "SelectedMonth";
    public String strSelectedDatePh = "SelectedDate";
    public String strSelectedDayPh = "SelectedDay";
    public String strClientNamePh = "ClientName";
    public String strRevenueGeneratedPh="RevenueGenerated";
    public String strRevenueTargetPh="RevenueTarget";
    public String strSoftLanding = "Loading text";
    public String strFeedbackIB = "Brief intro in feedback";
    public String strDetailAlpha = "Feedback hint 1";
    public String strDetailBeta = "Feedback hint 2";
    public String strDetailGamma = "Feedback hint 3";
    public String strDetailDelta = "Feedback hint 4";
    public String strIntroductoryBriefNew = "Set and achieve your business targets";
    public String strIntroductoryBriefNew1 = "My Business Partner is designed to give you clarity on how to set and meet your revenue targets\n\n" +
            "Through a powerful variance analysis function, it stores valuable lessons along your journey and also gives recommendations on how" +
            " to manage your clients and grow your business";
    public String strResetConfirmAlpha = "Are you sure you want to delete ";
    public String strResetConfirmBeta = "I accept that this process irreversible.\n\nData once deleted cannot be recovered.";
    public String strOrigin = "Activity Name";
    public String strHowItWorks = "How It Works";
    public String strNorthWest = "One-off set up: ";
    public String strNorthEast = "\n1) Key expenses (To advise you on minimum expenses to work with)\n" +
            "\n2) Set revenue target for the year";
    public String strSouthWest = "Daily Routine: ";
    public String strSouthEast = "\n3) Create clients to bring you business\n" +
            "\n4) Input actual revenue per day\n" +
            "\n5) View daily performance and record lessons\n" +
            "\n6) After navigating deep into the App: The home button located at the top right leads you back to the dashboard";
    public String IBPrefix = "Select business to review";
    public String strReferralAppreciation = "";
    public String strAchievedRevenueStopperPrefix = "Achieved Revenue Stopper Prefix";
    public String strAchievedRevenueStopperPostfix = "Achieved Revenue Stopper Postfix";
    public String strTargetsNotSetPrefix = "Revenue target not set for ";
    public String strTargetsNotSetPostfix = ".\nPlease set target by click \"Set Target \"";
    public String strErrorToast = "Error Message";
    public String strCharLimit = "Character Limit Error";


    //--------------Database details------------//
    public String think_big = "my_hustle_db";
    public String select_from = "SELECT * FROM ";
    public String create_table = " CREATE TABLE IF NOT EXISTS ";

    public String update_table = " UPDATE ";
    public String insert_into = " INSERT INTO ";
    public String my_profile_table = "my_profile_table";
    public String my_profile_table_columns = "( user_id INTEGER PRIMARY KEY AUTOINCREMENT, username   TEXT,  PhoneNumber  TEXT, EmailAddress TEXT, password TEXT)";

    public String notifications_table = "notifications_table";
    public String notifications_table_column = "( notification_id INTEGER PRIMARY KEY AUTOINCREMENT, prefered_time   TEXT )";


    public String my_businesses_table = "my_businesses_table";
    public String my_businesses_table_columns = "( business_id INTEGER PRIMARY KEY AUTOINCREMENT, business_name   TEXT ,business_date   TEXT ,ValueOfBusiness   TEXT ,clientName   TEXT ,phoneNumber   TEXT)";
    public String strTabColBizNam = " business_name ";

    public String table_in_use = "table_in_use";
    public String table_in_use_columns = "( data_id INTEGER PRIMARY KEY AUTOINCREMENT, business_name   TEXT , data_value TEXT)";

    public String my_pipelines_table = "my_pipelines_table";
    public String my_pipelines_table_columns = "( pipeline_id INTEGER PRIMARY KEY AUTOINCREMENT, business_name   TEXT ,business_date   TEXT ,ValueOfBusiness   TEXT ,clientName   TEXT ,phoneNumber   TEXT)";


    public String daily_target_table = " daily_targets_table ";
    public String daily_target_table_columns = " ( target_id INTEGER PRIMARY KEY AUTOINCREMENT, business_name   TEXT , daily_amount TEXT) ";

    public String strTabColExpenseAmount = " expense_amount ";
    public String my_expenses_table = " my_expenses_table ";
    public String strColPriority = " priority ";
    public String strColExpense = " expense_name ";
    public String strColAmount = " expense_amount ";
    public String my_expenses_table_columns = " ( expenses_id INTEGER PRIMARY KEY AUTOINCREMENT, "+strTabColBizNam+" TEXT, "+strColPriority+" TEXT,"
            +strColExpense+" TEXT ,"+strColAmount+" TEXT) ";
    public String weekday_targets_table = " weekday_targets_table ";
    public String weekday_targets_table_columns = " ( target_id INTEGER PRIMARY KEY AUTOINCREMENT, business_name   TEXT ,  start_date TEXT,Sundays TEXT, Mondays TEXT,Tuesdays TEXT,Wednesdays TEXT" +
            ",Thursdays TEXT,Fridays TEXT,Saturdays TEXT) ";
    public String monthly_targets_table = " monthly_targets_table ";
    public String monthly_targets_table_columns = " ( target_id INTEGER PRIMARY KEY AUTOINCREMENT, business_name   TEXT , start_date TEXT," +
            " January  TEXT , February  TEXT , March  TEXT , April  TEXT , May  TEXT ,June  TEXT ,July  TEXT ,August  TEXT , " +
            " September  TEXT , October  TEXT , November  TEXT , December  TEXT ) ";

    public String quarterly_targets_table = " quarterly_targets_table ";
    public String quarterly_targets_table_columns = " ( target_id INTEGER PRIMARY KEY AUTOINCREMENT, business_name   TEXT , start_date TEXT, quarterly_amount TEXT) ";


    public String yearly_target_table = " yearly_targets_table ";
    public String yearly_target_table_columns = " ( target_id INTEGER PRIMARY KEY AUTOINCREMENT, business_name   TEXT , start_date TEXT, yearly_amount TEXT) ";


    public String drop_table = " DROP TABLE IF EXISTS ";
    public String delete_from = " DELETE FROM ";
    public String order_by = " ORDER BY ";


    ///--------------URLs Details--------------//

    public String PARENT_URL = "https://mwalimubiashara.com/";
    //BaseURL
    public String BASEURL = "https://mwalimubiashara.com/app";

    //Get goal list
    public String LOGIN_URL = BASEURL+"/applogin.php";
    public String LOGIN_GOOGLE_URL = BASEURL+"/applogin_with_google.php";
    public String ADD_WHATSAPP_NUMBER_URL = BASEURL+"/add_whatsapp_number.php";
    public String GET_GOAL_LIST_URL = BASEURL+"/goal_list.php";
    public String RESET_ACCOUNT_URL = BASEURL+"/reset.php";
    public String GET_ACCOUNT_URL = BASEURL+"/get_account.php";
    public String MAKE_PAYMENT_URL = BASEURL+"/payment.php";
    public String GET_PAYMENT_STATUS_URL = BASEURL+"/getpayment.php";

    public String UPDATE_OUTFLOW_URL = BASEURL+"/update_outflow.php";
    public String GET_ACTUAL_EXPENSE_LIST_URL = BASEURL+"/get_actual_expenses.php";
    public String GET_EXPENSE_LINE_URL = BASEURL+"/get_expense_lines.php";
    public String UPDATE_ALLOCATED_TRANSCACTION = BASEURL+"/update_allocated_transaction.php";
    public String GET_MPESA_STATUS = BASEURL+"/get_mpesa_status.php";
    public String GET_MPESA_ALLOCATED_STATUS = BASEURL+"/get_mpesa_allocated_status.php";
    public String GET_ACTUAL_DOWNLOAD = BASEURL+"/actuals_download.php";
    public String GET_WORTH_URL = BASEURL+"/get_worth.php";
    public String GET_OUTFLOW_URL = BASEURL+"/get_outflow.php";
    public String GET_CASHFLOW_DISPLAY = BASEURL+"/cashflow_display.php";
    public String ADD_ACTUAL_EXPENSES_URL = BASEURL+"/add_actual_expenses.php";
    public String ADD_ACTUAL_INFLOW_URL = BASEURL+"/add_actual_inflow.php";
    public String ADD_MPESA_TRANS_URL = BASEURL+"/add_mpesa_trans.php";
    public String GET_YOUR_GOAL_URL = BASEURL+"/get_yourgoal.php";
    public String DELETE_INFLOW_URL=BASEURL+"/delete_inflow.php";
    public String SEND_WHATSAPP_MESSAGE_URL = BASEURL+"/send_whatsapp.php";
    public String SAVE_PLAN_URL = BASEURL+"/save_plan.php";
    public String READ_SMS_NOT_ALLOWED_URL = BASEURL+"/user_not_allowed_sms  .php";


    public String URL_OPERATION_REFERRALS = "https://mwalimubiashara.com/bp_app/get_actual_inflow.php";
    public String URL_ALL_BUSINESSES_RETRIEVE ="https://mwalimubiashara.com/bp_app/get_bus_data.php";
    public String URL_OPERATION_REGISTER = "https://mwalimubiashara.com/bp_app/register.php";
    public String URL_OPERATION_REGISTER_BUSINESS = "https://mwalimubiashara.com/bp_app/register_business.php";
    public String URL_OPERATION_GET_BUSINESS_UPDATES = "https://mwalimubiashara.com/bp_app/get_businesses.php";
    public String URL_OPERATION_LOGIN = "https://mwalimubiashara.com/bp_app/login.php";
    public String URL_OPERATION_PIPELINE_REGISTER = "https://mwalimubiashara.com/bp_app/register_client.php";
    public String URL_OPERATION_UPDATE_PIPELINE = "https://mwalimubiashara.com/bp_app/update_client.php";
    public String URL_OPERATION_UPLOAD_MONTHLY_TARGETS = "https://mwalimubiashara.com/bp_app/bp_set_target_monthly.php";
    public String URL_OPERATION_UPLOAD_WEEKDAY_TARGETS = "https://mwalimubiashara.com/bp_app/bp_set_target_weekly.php";
    public String URL_OPERATION_UPLOAD_YEARLY_TARGET = "https://mwalimubiashara.com/bp_app/bp_set_target_yearly.php";
    public String URL_OPERATION_UPLOAD_DAILY_TARGET = "https://mwalimubiashara.com/bp_app/bp_set_target_daily.php";
    public String URL_OPERATION_RETRIEVE_PERFORMANCE_REPORT_PDF = "https://mwalimubiashara.com/bp_app/cashflow_download.php";
    public String URL_OPERATION_RETRIEVE_PERFORMANCE_REPORT_XLS = "https://mwalimubiashara.com/bp_app/get_actual_inflow.php";
    public String URL_OPERATION_RETRIEVE_TARGETS = "https://mwalimubiashara.com/bp_app/get_revenue_target.php";
    public String URL_OPERATION_ACTUAL_VARIANCE_UPDATE = "https://mwalimubiashara.com/bp_app/update_actual_variance.php";
    public String URL_OPERATION_FEEDBACK_RETRIEVE = "https://mwalimubiashara.com/bp_app/get_actual_inflow.php";
    public String URL_OPERATION_PIPELINES_RETRIEVE = "https://mwalimubiashara.com/bp_app/get_clients.php";
    public String URL_OPERATION_DELETE_CLIENT = "https://mwalimubiashara.com/bp_app/del_client.php";
    public String URL_OPERATION_PIPELINES_TODAY_RETRIEVE = "https://mwalimubiashara.com/bp_app/get_clients_today.php";
    public String URL_OPERATION_INSERT_EXPENSES = "https://mwalimubiashara.com/bp_app/get_actual_inflow.php";
    public String URL_OPERATION_FEEDBACK_SEND = "https://mwalimubiashara.com/bp_app/get_actual_inflow.php";
    public String URL_OPERATION_RESET_ALL = "https://mwalimubiashara.com/bp_app/bp_reset.php";


    public String strDestinationRevenueTargets = "Destination Revenue Targets";
    public String strDestinationClients = " clients";
    public String strDestinationAchievedRevenue = "Destination Achieved Revenue";
    public String strDestinationPerformance = "Destination Performance";
    public String strDestinationKeyExpenses = " expenses";

    public String strInvalidDate = "Invalid date text";
    public String strInvalidYear = "Invalid date text";
    public String strMyPipelineAlpha = "\nMy clients: Walking the talk.\n";
    public String strMyPipelineBeta = "\n\nIn order to achieve the set targets, who are your actual/ potential clients through which you will generate revenue?\n";
    public String strSortingPreferencePh = "Sorting clients preferences";
    public String strAddContactManually = "Or alternatively, add contact manually";
    public String strEuniceCynthia = " ";
    public String strNotificationAlpha = "Business Partner";
    public String strNotificationBeta = "Remember to review your performance at ";
    public String strIntroductoryBriefBusiness = "Brief intro about the app";
    public String strExportToPdf = "Export to PDF Message";
    public String strExportToExcel = "Export to Excel Message";
    public String strPipColDate = " business_date ";
    public String strPipColClient = " clientName ";
    public String strPipColAmount = " ValueOfBusiness ";
    public String strSortByDateAsc ="Sort by date Ascending";
    public String strSortByDateDesc = "Sort by date descending";
    public String strSortByAmountAsc ="Sort by amount Ascending";
    public String strSortByAmountDesc = "Sort by amount descending";
    public String strSortByClientAsc = "Sort by client Ascending";
    public String strSortByClientDesc = "Sort by client descending";

    public String strWebViewPdfAlpha = "UNKNOWN";
    public String strWebViewPdfBeta = "UNKNOWN";
    public String strWebViewXlsAlpha = "UNKNOWN";
    public String strWebViewXlsBeta = "UNKNOWN";
    public String strExpense1Key = "Expense1Key";
    public String strExpense2Key = "Expense2Key";
    public String strExpense3Key = "Expense3Key";
    public String strExpense4Key = "Expense4Key";
    public String strExpense5Key = "Expense5Key";
    public String strExpense6Key = "Expense6Key";
    public String strExpense7Key = "Expense7Key";
    public String strAmount1Key = "Amount1Key";
    public String strAmount2Key = "Amount2Key";
    public String strAmount3Key = "Amount3Key";
    public String strAmount4Key = "Amount4Key";
    public String strAmount5Key = "Amount5Key";
    public String strAmount6Key = "Amount6Key";
    public String strAmount7Key = "Amount7Key";

    public String strExpense1 = "Rent & Utilities";
    public String strExpense2 = "Salaries & Wages";
    public String strExpense3 = "Marketing & Ads";
    public String strExpense4 = "Transport & travel";
    public String strExpense5 = "Internet";
    public String strExpense6 = "Supplies";
    public String strExpense7 = "Labour";
    public String strExpense8 = "Direct overheads";
    public String strExpense9 = "Start-up costs";
    public String strExpense10 = "Licenses";
    public String strExpense11 = "Welfare";
    public String strExpense12 = "Commissions";
    public String strExpense13 = "Entertainment";
    public String strExpense14 = "Computer";
    public String strExpense15 = "Printing & Stationery";
    public String strExpense16 = "Repairs & maintenance";
    public String strExpense17 = "Legal fees";
    public String strExpense18 = "Training";
    public String strExpense19 = "Consultancy";
    public String strExpense20 = "Bad debts";
    public String strExpense21 = "Emergency";
    public String strExpense22 = "Insurance";
    public String strExpense23 = "Mpesa & bank charges";
    public String strExpense24 = "Loan interest";
    public String strExpense25 = "CSR & charity";
    public String strExpense26 = "Miscellaneous";
    public String strExpense27 = "Taxes & Levies";
    public String strExpense28 = "Other ";
    public String strExpense29 = "Other ";
    public String strExpense30 = "Other ";
    public String strExpense31 = "Other ";
    public String strExpense32 = "Other ";
    public String strExpense33 = "Telephone";

    public String strVal1 = "trVal1";
    public String strVal2 = "trVal2";
    public String strVal3 = "trVal3";
    public String strVal4 = "trVal4";
    public String strVal5 = "trVal5";
    public String strVal6 = "trVal6";
    public String strVal7 = "trVal7";
    public String strVal8 = "trVal8";
    public String strVal9 = "trVal9";
    public String strVal10 = "trVal10";
    public String strVal11 = "trVal11";
    public String strVal12 = "trVal12";
    public String strVal13 = "trVal13";
    public String strVal14 = "trVal14";
    public String strVal15 = "trVal15";
    public String strVal16 = "trVal16";
    public String strVal17 = "trVal17";
    public String strVal18 = "trVal18";
    public String strVal19 = "trVal19";
    public String strVal20 = "trVal20";
    public String strVal21 = "trVal21";
    public String strVal22 = "trVal22";
    public String strVal23 = "trVal23";
    public String strVal24 = "trVal24";
    public String strVal25 = "trVal25";
    public String strVal26 = "trVal26";
    public String strVal27 = "trVal27";
    public String strVal28 = "trVal28";
    public String strVal29 = "trVal29";
    public String strVal30 = "trVal30";
    public String strVal31 = "trVal31";
    public String strVal32 = "trVal32";
    public String strVal33 = "trVal33";


    public String strSurplusProductSolution1 = "We researched well on the product/ market fit";
    public String strSurplusProductSolution2 = "We defined the value of our solutions";
    public String strSurplusProductSolution3 = "We understood our solutions well";
    public String strSurplusProductSolution4 = "We communicated our solutions well";
    public String strSurplusCustomer1 = "Our key customer(s) came through ";
    public String strSurplusCustomer2 = "We identified our ideal customers";
    public String strSurplusCustomer3 = "We reached our ideal customers";
    public String strSurplusCustomer4 = "We had enough money reach our clients";
    public String strSurplusCustomer5 = "We used our money well to reach our clients";
    public String strSurplusPeople1 = "I shared my vision well";
    public String strSurplusPeople2 = "My team has caught the vision";
    public String strSurplusPeople3 = "My managers/supervisors are effective";
    public String strSurplusPeople4 = "My staff is engaged and did their work well ";
    public String strSurplusPeople5 = "We have the discipline to reach our goals";
    public String strSurplusMarket1 = "We anticipated and responded to competition";
    public String strSurplusMarket2 = "We applied the right technology";
    public String strSurplusMarket3 = "We knew where to meet our customers";
    public String strSurplusMarket4 = "We caught our customer’s attention";
    public String strSurplusMarket5 = "We had a good sales funnel - develop relationship, " +
            "tell them what you are about, pitch your products or services";
    public String strSurplusMarket6 = "We anticipated and responded to regulatory changes";
    public String strSurplusOrganization1 = "We had a good system to reach our customers";
    public String strSurplusOrganization2 = "We had a good system to measure performance";
    public String strSurplusOrganization3 = "Our brand is trusted";


    public String strDeficitProductSolution1 = "We have not researched well on the product/market fit";
    public String strDeficitProductSolution2 = "We have not defined the value of our solutions";
    public String strDeficitProductSolution3 = "We have not understood our solutions well";
    public String strDeficitProductSolution4 = "We have not communicated our solutions well";
    public String strDeficitCustomer1 = "Our key customer(s) did not come through";
    public String strDeficitCustomer2 = "We have not identified our ideal customers";
    public String strDeficitCustomer3 = "We have not reached our ideal customers";
    public String strDeficitCustomer4 = "We did not have enough money reach our clients";
    public String strDeficitCustomer5 = "We did not use our money well to reach our clients";


    public String strDeficitPeople1 = "I did not share my vision well";
    public String strDeficitPeople2 = "My team did not catch my vision well";
    public String strDeficitPeople3 = "My managers/supervisors let me down";
    public String strDeficitPeople4 = "My staff did not do their work well";
    public String strDeficitPeople5 = "We lacked the discipline to reach our goals";


    public String strDeficitMarket1 = "We did not anticipate or respond to competition";
    public String strDeficitMarket2 = "We have not applied the right technology";
    public String strDeficitMarket3 = "We did not know where to meet our customers";
    public String strDeficitMarket4 = "We did not catch our customer’s attention";
    public String strDeficitMarket5 = "We did not have a good sales funnel - develop " +
            "relationship, tell them what you are about, pitch your products or services";
    public String strDeficitMarket6 = "We did not anticipate or respond to regulatory changes";


    public String strDeficitOrganization1 = "We do not have a good system to reach our customers";
    public String strDeficitOrganization2 = "We do not have a good system to measure performance";
    public String strDeficitOrganization3 = "We have not yet become a trusted brand";
}
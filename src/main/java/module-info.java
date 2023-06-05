module com.distributedlabacademy.sha1.banddtechs_task3 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.home.sha1 to javafx.fxml;
    exports com.home.sha1;
}
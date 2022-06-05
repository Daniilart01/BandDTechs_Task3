package com.distributedlabacademy.sha1.banddtechs_task3;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class AppController {
    @FXML
    private Label result;

    @FXML
    private TextArea message;

    @FXML
    public void buttonPressed() {
        result.setText(SHA1.getHash(message.getText()));
    }
}
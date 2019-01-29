/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.events.JFXDialogEvent;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

/**
 *
 * @author DevMan
 */
public class AjouterCourrierController implements Initializable{
    
    String query;
    
    @FXML
    private JFXDatePicker date_arrivee;
    @FXML
    private JFXTextField pre_reference;
    @FXML
    private JFXDatePicker date_pre_reference;
    @FXML
    private JFXTextField origine;
    @FXML
    private JFXTextField reference;
    @FXML
    private JFXDatePicker date_courrier;
    @FXML
    private JFXTextArea objet;
    @FXML
    private JFXTextField classement;
    @FXML
    private JFXDatePicker date_traitement;
    @FXML
    private StackPane rootPane;
    @FXML
    private BorderPane root;
	
    BoxBlur blur = new BoxBlur(3,3,3);
    
    String idUtilConnecter;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    
    @FXML
    public void enregistrerCourrier(){
        // Ajouter un courrier
        try{
            if(!"".equals(objet.getText()) && !"".equals(origine.getText()) && !"".equals(date_arrivee.getValue()) && !"".equals(date_courrier.getValue()) && !"".equals(date_pre_reference.getValue()) && !"".equals(date_traitement.getValue())){
                
                query = "INSERT INTO courrier SET date_arrivee=?,pre_reference=?,date_pre_reference=?,origine=?,reference=?,date_courrier=?,objet=?,classement=?,date_traitement=?,utilisateur=?";
                 
                PreparedStatement prepare = DevsConnexion.getInstance().prepareStatement(query);
                prepare.setString(1, date_arrivee.getValue().toString());
                prepare.setString(2, pre_reference.getText());
                prepare.setString(3, date_pre_reference.getValue().toString());
                prepare.setString(4, origine.getText());
                prepare.setString(5, reference.getText());
                prepare.setString(6, date_courrier.getValue().toString());
                prepare.setString(7, objet.getText());
                prepare.setString(8, classement.getText());
                prepare.setString(9, date_traitement.getValue().toString());
                prepare.setString(10, idUtilConnecter);
                prepare.executeUpdate();
                JFXDialogLayout dialogLayout = new JFXDialogLayout();
                JFXButton button = new JFXButton("Okay");
                JFXDialog dialog = new JFXDialog(rootPane,dialogLayout,JFXDialog.DialogTransition.CENTER);
                button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent)->{
                	root.setEffect(blur);
                	dialog.close();
                });
                button.getStyleClass().add("dialog-button");
                
                dialogLayout.setBody(new Text("Le courrier a été ajouté"));
                dialogLayout.setActions(button);
                dialog.show();
                dialog.setOnDialogClosed((JFXDialogEvent event1)->{
                	root.setEffect(null);
                });
            }else{
            	JFXDialogLayout dialogLayout = new JFXDialogLayout();
                JFXButton button = new JFXButton("Okay");
                JFXDialog dialog = new JFXDialog(rootPane,dialogLayout,JFXDialog.DialogTransition.CENTER);
                button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent)->{
                	root.setEffect(blur);
                	dialog.close();
                });
                button.getStyleClass().add("dialog-button");
                
                dialogLayout.setHeading(new Label("Warning"));
                dialogLayout.setBody(new Text("Veuillez remplir les champs requis"));
                dialogLayout.setActions(button);
                dialog.show();
                dialog.setOnDialogClosed((JFXDialogEvent event1)->{
                	root.setEffect(null);
                });
            }
        }catch(SQLException e){
            JFXDialogLayout dialogLayout = new JFXDialogLayout();
            JFXButton button = new JFXButton("Okay");
            JFXDialog dialog = new JFXDialog(rootPane,dialogLayout,JFXDialog.DialogTransition.CENTER);
            button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent)->{
            	root.setEffect(blur);
            	dialog.close();
            });
            button.getStyleClass().add("dialog-button");
            
            dialogLayout.setHeading(new Label("Warning"));
            dialogLayout.setBody(new Text("Veuillez remplir tous les champs"));
            dialogLayout.setActions(button);
            dialog.show();
            dialog.setOnDialogClosed((JFXDialogEvent event1)->{
            	root.setEffect(null);
            });
        }
    }
    
}

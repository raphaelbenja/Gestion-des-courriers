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
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javax.swing.JOptionPane;
/**
 *
 * @author DevMan
 */
public class EditerCourrierController implements Initializable{

    String query;
    
    @FXML
    private JFXTextField id;
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
    private Label creer_par;
    @FXML
    private Label mod;
    @FXML
    private Label modifier_par;
    @FXML
    private StackPane rootPane;
    @FXML
    private BorderPane root;
    @FXML
    private JFXButton edit;
	
    BoxBlur blur = new BoxBlur(3,3,3);
    
    String idUtilConnecter;
    int fonctionUtilConnecter;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mod.setVisible(false);
        modifier_par.setVisible(false);
    }
    
    public void obtenirInfoCourrier(String idCourrier){
        
    	if(fonctionUtilConnecter == 0 || fonctionUtilConnecter == 3){
            edit.setVisible(false);
        }
        
        try{
            query = "SELECT * FROM courrier LEFT JOIN utilisateur ON utilisateur.id_utilisateur=courrier.utilisateur WHERE id_courrier=?";
            PreparedStatement prepare = DevsConnexion.getInstance().prepareStatement(query);
            prepare.setString(1, idCourrier);
            ResultSet res = prepare.executeQuery();
            res.first();
            id.setText(res.getString("id_courrier"));
            date_arrivee.setValue(res.getDate("date_arrivee").toLocalDate());
            pre_reference.setText(res.getString("pre_reference"));
            date_pre_reference.setValue(res.getDate("date_pre_reference").toLocalDate());
            origine.setText(res.getString("origine"));
            reference.setText(res.getString("reference"));
            date_courrier.setValue(res.getDate("date_courrier").toLocalDate());
            objet.setText(res.getString("objet"));
            classement.setText(res.getString("classement"));
            date_traitement.setValue(res.getDate("date_traitement").toLocalDate());
            if(!Objects.isNull(res.getString("nom_util")) || !Objects.isNull(res.getString("prenom_util"))){
                creer_par.setText(res.getString("nom_util")+" "+res.getString("prenom_util"));
            }else if(!"".equals(res.getString("pseudo_util"))){
                creer_par.setText(res.getString("pseudo_util").toUpperCase());
            }else{
                creer_par.setText("L'utilisateur n'a aucun Nom ou Prénom");
            }
            
            
            if(!Objects.isNull(res.getString("modifier_par")) && !res.getString("modifier_par").equals(res.getString("id_utilisateur"))){
                mod.setVisible(true);
                modifier_par.setVisible(true);
                modifier_par.setText(obtenirNomUtilisateur(res.getString("modifier_par")));
            }
            
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erreur lors de la connexion à la base de donnée");
        }
    }
     
    @FXML
    private void btnModifierCourrier() {
        // Modifier le courrier
        try{
            if(!"".equals(objet.getText()) && !"".equals(origine.getText()) && !"".equals(date_arrivee.getValue()) && !"".equals(date_courrier.getValue()) && !"".equals(date_pre_reference.getValue()) && !"".equals(date_traitement.getValue())){
                
                query = "UPDATE courrier SET date_arrivee=?,pre_reference=?,date_pre_reference=?,origine=?,reference=?,date_courrier=?,objet=?,classement=?,date_traitement=?,modifier_par=? WHERE id_courrier=?";
                 
                PreparedStatement prepare = DevsConnexion.getInstance().prepareStatement(query);
                prepare.setString(1, date_arrivee.getValue().toString());
                prepare.setString(2,pre_reference.getText());
                prepare.setString(3, date_pre_reference.getValue().toString());
                prepare.setString(4, origine.getText());
                prepare.setString(5, reference.getText());
                prepare.setString(6, date_courrier.getValue().toString());
                prepare.setString(7, objet.getText());
                prepare.setString(8, classement.getText());
                prepare.setString(9, date_traitement.getValue().toString());
                prepare.setString(10, idUtilConnecter);
                prepare.setString(11, id.getText());
                prepare.executeUpdate();
                JFXDialogLayout dialogLayout = new JFXDialogLayout();
                JFXButton button = new JFXButton("Okay");
                JFXDialog dialog = new JFXDialog(rootPane,dialogLayout,JFXDialog.DialogTransition.CENTER);
                button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent)->{
                	root.setEffect(blur);
                	dialog.close();
                });
                button.getStyleClass().add("dialog-button");
                
                dialogLayout.setBody(new Text("Le courrier été modifié"));
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
                dialogLayout.setBody(new Text("Veuillez remplir les champs"));
                dialogLayout.setActions(button);
                dialog.show();
                dialog.setOnDialogClosed((JFXDialogEvent event1)->{
                	root.setEffect(null);
                });
            }
        }catch(Exception e){
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

    private String obtenirNomUtilisateur(String id) {
        String nom="";
        query = "SELECT pseudo_util, nom_util, prenom_util FROM utilisateur WHERE id_utilisateur='"+id+"'";
         try {
            Statement state = DevsConnexion.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet res = state.executeQuery(query);
            res.first();
            
            if(!Objects.isNull(res.getString("nom_util")) || !Objects.isNull(res.getString("prenom_util"))){
                nom = res.getString("nom_util")+" "+res.getString("prenom_util");
            }else if(!"".equals(res.getString("pseudo_util"))){
                nom = res.getString("pseudo_util").toUpperCase();
            }
            
         } catch (Exception e) {
             
         }   
        return nom;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.events.JFXDialogEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 *
 * @author DevMan
 */
public class ProfileController implements Initializable{

    String query;
    
    @FXML
    private JFXTextField id;
    @FXML
    private JFXTextField pseudo;
    @FXML
    private JFXTextField nom;
    @FXML
    private JFXTextField prenom;
    @FXML
    private JFXTextField matricule;
    @FXML
    private JFXComboBox direction;
    @FXML
    private JFXComboBox fonction;
    @FXML
    private Label titre;
    @FXML
    private JFXButton mettre_a_jour;
    @FXML
    private JFXButton editer;
    @FXML
    private JFXButton changementMdp;
    
    @FXML
    private StackPane rootPane;
    
    @FXML
    private BorderPane root;
    
    BoxBlur blur = new BoxBlur(3,3,3);
    
    String idUtilConnecter;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        mettre_a_jour.setVisible(false);
        editer.setVisible(false);
        changementMdp.setVisible(false);
        
        obtenirDirection();
        
        fonction.getItems().add("Administrateur");
        fonction.getItems().add("Secretaire");
        fonction.getItems().add("Autre");
    }
    
    private void obtenirDirection() {
        try{
            String query = "SELECT * FROM direction";
            Statement state = DevsConnexion.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            ResultSet res = state.executeQuery(query);
            while(res.next()){
                direction.getItems().add(res.getString("nom_direction"));
            }
            
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private int obtenirIdDirection(String nomDirection) {
        int i=0;
        try{
            String query = "SELECT id_direction FROM direction WHERE nom_direction='"+nomDirection+"'";
            Statement state = DevsConnexion.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            ResultSet res = state.executeQuery(query);
            if(res.first()){
                i = res.getInt("id_direction");
            }
            
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        
        return i;
    }
    
   public void obtenirInfoProfilUtil(String idU){
        try{
            query = "SELECT * FROM utilisateur LEFT JOIN direction ON utilisateur.direction_util=direction.id_direction WHERE id_utilisateur=?";
            PreparedStatement prepare = DevsConnexion.getInstance().prepareStatement(query);
            prepare.setString(1, idU);
            ResultSet res = prepare.executeQuery();
            res.first();
            id.setText(res.getString("id_utilisateur"));
            pseudo.setText(res.getString("pseudo_util"));
            nom.setText(res.getString("nom_util"));
            prenom.setText(res.getString("prenom_util"));
            matricule.setText(res.getString("matricule_util"));
            direction.getSelectionModel().select(res.getString("nom_direction"));
            fonction.getSelectionModel().select(res.getString("fonction_util"));
            
            titre.setText("PROFILE DE "+res.getString("pseudo_util").toUpperCase());
            
            if(idU.equals(idUtilConnecter)){
                changementMdp.setVisible(true);
                editer.setVisible(true);
            }
            
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erreur lors de la connexion à la base de donnée");
        }
    }
    
    @FXML
    private void activeBtnMettre(){
        mettre_a_jour.setVisible(true);
    }
    
    @FXML
    private void btnMotdePasse() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Changer_mdp.fxml"));
        Parent root = loader.load();
                                   
        PasswordController controller = (PasswordController) loader.getController();
        controller.idUtilConnecter=idUtilConnecter;
                                   
        Stage load = new Stage();
        load.initModality(Modality.APPLICATION_MODAL);
        load.setResizable(false);
        load.setScene(new Scene(root));
        load.show();
    }
    
    @FXML
    private void btnMettreaJourUtilisateur() {
        // Modifier l'utilisateur
        try{
            if(!"".equals(pseudo.getText()) && !"".equals(id.getText())){
                
                String query = "UPDATE utilisateur SET pseudo_util=?,nom_util=?,prenom_util=?,matricule_util=? WHERE id_utilisateur =? ";
                
                PreparedStatement prepare = DevsConnexion.getInstance().prepareStatement(query);
                prepare.setString(1, pseudo.getText());
                prepare.setString(2, nom.getText());
                prepare.setString(3, prenom.getText());
                prepare.setString(4, matricule.getText());
                
                prepare.setString(5, id.getText());
                
                    prepare.executeUpdate();
                    JFXDialogLayout dialogLayout = new JFXDialogLayout();
                    JFXButton button = new JFXButton("Okay");
                    JFXDialog dialog = new JFXDialog(rootPane,dialogLayout,JFXDialog.DialogTransition.CENTER);
                    button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent)->{
                            root.setEffect(blur);
                            dialog.close();
                    });
                    button.getStyleClass().add("dialog-button");

                    dialogLayout.setBody(new Text("Les informations ont été mise à jour"));
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
                dialogLayout.setBody(new Text("Veuillez remplir le champs"));
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
    
}

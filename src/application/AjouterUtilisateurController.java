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
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.events.JFXDialogEvent;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javax.swing.JOptionPane;
import sun.misc.BASE64Encoder;

/**
 *
 * @author DevMan
 */
public class AjouterUtilisateurController implements Initializable{

    @FXML
    private JFXTextField pseudo;
    @FXML
    private JFXPasswordField mot_de_passe;
    @FXML
    private JFXComboBox direction;
    @FXML
    private JFXComboBox fonction;
	
    @FXML
    private StackPane rootPane;
    
    @FXML
    private AnchorPane root;
	
    BoxBlur blur = new BoxBlur(3,3,3);
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        obtenirDirection();
        
        fonction.getItems().add("Administrateur");
        fonction.getItems().add("Secretaire");
        fonction.getItems().add("Autre");
    }
    
    @FXML
	private void ajouterUtilisateur() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        // Ajouter un utilisateur
        try{
            if(!"".equals(pseudo.getText()) && !"".equals(mot_de_passe.getText())){
                
                String encoding="UTF-8",hash,plaintext=mot_de_passe.getText();
            
                MessageDigest msgDigest = MessageDigest.getInstance("SHA-1");
                msgDigest.update(plaintext.getBytes(encoding));
                byte rawByte[] = msgDigest.digest();
                hash = (new BASE64Encoder().encode(rawByte));
                
                String query = "INSERT INTO utilisateur(pseudo_util,mot_de_passe_util,direction_util,fonction_util,acces_util) VALUES(?,?,?,?,?) ";
                
                PreparedStatement prepare = DevsConnexion.getInstance().prepareStatement(query);
                prepare.setString(1, pseudo.getText());
                prepare.setString(2, hash);
                prepare.setInt(3, obtenirIdDirection(direction.getSelectionModel().getSelectedItem().toString()));
                prepare.setString(4, fonction.getSelectionModel().getSelectedItem().toString());
                String fct = fonction.getSelectionModel().getSelectedItem().toString();
                if("Administrateur".equals(fct)){
                  prepare.setInt(5, 1);  
                }else if("Secretaire".equals(fct)){
                    prepare.setInt(5, 2);
                }else if("Autre".equals(fct)){
                    prepare.setInt(5, 3);
                }else{ 
                    prepare.setInt(5, 3);
                }
                
                int nbr_secretaire = verifSecretaireDirection((int) obtenirIdDirection(direction.getSelectionModel().getSelectedItem().toString()),fct);
                
                if(nbr_secretaire > 3 || nbr_secretaire == 3){
                    JFXDialogLayout dialogLayout = new JFXDialogLayout();
                    JFXButton button = new JFXButton("Okay");
                    JFXDialog dialog = new JFXDialog(rootPane,dialogLayout,JFXDialog.DialogTransition.CENTER);
                    button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent)->{
                            root.setEffect(blur);
                            dialog.close();
                    });
                    button.getStyleClass().add("dialog-button");

                    dialogLayout.setBody(new Text("La sécretaire dans cette direction ne peut pas dépassé 3 personne"));
                    dialogLayout.setActions(button);
                    dialog.show();
                    dialog.setOnDialogClosed((JFXDialogEvent event1)->{
                        root.setEffect(null);
                    });
                }else if(nbr_secretaire < 3){
                    prepare.executeUpdate();
                    JFXDialogLayout dialogLayout = new JFXDialogLayout();
                    JFXButton button = new JFXButton("Okay");
                    JFXDialog dialog = new JFXDialog(rootPane,dialogLayout,JFXDialog.DialogTransition.CENTER);
                    button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent)->{
                            root.setEffect(blur);
                            dialog.close();
                    });
                    button.getStyleClass().add("dialog-button");

                    dialogLayout.setBody(new Text("L'utilisateur a été ajouté"));
                    dialogLayout.setActions(button);
                    dialog.show();
                    dialog.setOnDialogClosed((JFXDialogEvent event1)->{
                            root.setEffect(null);
                            actualiser();
                    });
                }
                    
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
            dialogLayout.setBody(new Text("Erreur interne"));
            dialogLayout.setActions(button);
            dialog.show();
            dialog.setOnDialogClosed((JFXDialogEvent event1)->{
            	root.setEffect(null);
            });
        }
    }
	
     /**
     * Reinitialise les champs
     */
    private void actualiser(){
        pseudo.setText("");
     }
    
    @FXML
    private void closeCurrentWindow() {
    	pseudo.getScene().getWindow().hide();
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

    private int verifSecretaireDirection(int idDirection,String fonction) {
        int i =0;
        try{
            String query = "SELECT fonction_util FROM utilisateur WHERE direction_util='"+idDirection+"' AND fonction_util='"+fonction+"'";
            Statement state = DevsConnexion.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            ResultSet res = state.executeQuery(query);
            res.last();
            i = res.getRow();
            res.beforeFirst();
           
            
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        
        return i;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.events.JFXDialogEvent;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author DevMan
 */
public class UtilisateurController implements Initializable {

    private String query;
    double xOffset,yOffset;
	
    @FXML
    private JFXTextField txtSearch;
	
    @FXML
    private TableView<UtilisateurController.Utilisateur> tab;
    @FXML
    private TableColumn<UtilisateurController.Utilisateur, String> id_utilisateurColumn;
    @FXML
    private TableColumn<UtilisateurController.Utilisateur, String> nom_utilColumn;
    @FXML
    private TableColumn<UtilisateurController.Utilisateur, String> prenom_utilColumn;
    @FXML
    private TableColumn<UtilisateurController.Utilisateur, String> matricule_utilColumn;
    @FXML
    private TableColumn<UtilisateurController.Utilisateur, String> direction_utilColumn;
    @FXML
    private TableColumn<UtilisateurController.Utilisateur, String> fonction_utilColumn;
    
    @FXML
    private StackPane rootPane;
    @FXML
    private BorderPane root;
	
    BoxBlur blur = new BoxBlur(3,3,3);
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        chargerColonne();
        chargerUtilisateur();
    } 
    
    public void chargerUtilisateur() {
            query = "SELECT * FROM utilisateur LEFT JOIN direction ON utilisateur.direction_util = direction.id_direction";
            ObservableList<UtilisateurController.Utilisateur> list = FXCollections.observableArrayList();
            try {
            
            Statement state = DevsConnexion.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet res = state.executeQuery(query);
            ResultSetMetaData meta = (ResultSetMetaData) res.getMetaData();
            
            for(int i = 1 ; i <= meta.getColumnCount(); i++){
    		tab.getColumns().setAll(id_utilisateurColumn,nom_utilColumn,prenom_utilColumn,matricule_utilColumn,direction_utilColumn,fonction_utilColumn);
            }
            
            while(res.next()){	
            	
            	String fonction = res.getString("fonction_util");
            	String direction = res.getString("nom_direction");
            	String matricule = res.getString("matricule_util");
            	String prenom = res.getString("prenom_util");
            	String nom = res.getString("nom_util");
            	String id = res.getString("id_utilisateur");
            	
            	list.add(new UtilisateurController.Utilisateur(id,nom,prenom,matricule,direction,fonction));     
            }
            res.close();
            state.close();
            
         } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "ERREUR ! ", JOptionPane.ERROR_MESSAGE);
         } 
		tab.getItems().setAll(list);
	}
	
	@FXML
	public void refreshData() {
            chargerUtilisateur();
	}

	private void chargerColonne() {
            id_utilisateurColumn.setCellValueFactory(new PropertyValueFactory<>("id_utilisateur"));
            nom_utilColumn.setCellValueFactory(new PropertyValueFactory<>("Nom_util"));
	    prenom_utilColumn.setCellValueFactory(new PropertyValueFactory<>("prenom_util"));
	    matricule_utilColumn.setCellValueFactory(new PropertyValueFactory<>("matricule_util"));
	    direction_utilColumn.setCellValueFactory(new PropertyValueFactory<>("direction_util"));
	    fonction_utilColumn.setCellValueFactory(new PropertyValueFactory<>("fonction_util"));
	}
        
        @FXML
        public void supprimerUtilisateur(ActionEvent event) throws Exception {

            ObservableList<UtilisateurController.Utilisateur> select;
            select = tab.getSelectionModel().getSelectedItems();

            if(!tab.getItems().isEmpty()){
                 if(tab.getSelectionModel().getSelectedIndex() != -1){
                      if(JOptionPane.showConfirmDialog(null, "attention!!! etes-vous sure de"
                    + " vouloir supprimer ce utilisateur ?","Supprimer un utilisateur",JOptionPane.YES_NO_OPTION)
                    == JOptionPane.OK_OPTION){
                        String id = select.get(0).getId_utilisateur();
                        query = "DELETE FROM utilisateur WHERE id_utilisateur=? LIMIT 1";
                        PreparedStatement prepare;
                        prepare = DevsConnexion.getInstance().prepareStatement(query);
                        prepare.setString(1, id);
                        prepare.executeUpdate();
                        JFXDialogLayout dialogLayout = new JFXDialogLayout();
                        JFXButton button = new JFXButton("Okay");
                        JFXDialog dialog = new JFXDialog(rootPane,dialogLayout,JFXDialog.DialogTransition.CENTER);
                        button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent)->{
                           root.setEffect(blur);
                           dialog.close();
                        });
                        button.getStyleClass().add("dialog-button");
                        dialogLayout.setBody(new Text("Suppression avec success"));
                        dialogLayout.setActions(button);
                        dialog.show();
                        dialog.setOnDialogClosed((JFXDialogEvent event1)->{
                           root.setEffect(null);
                           chargerUtilisateur();
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
                     dialogLayout.setBody(new Text("Aucune ligne selectionne"));
                     dialogLayout.setActions(button);
                     dialog.show();
                     dialog.setOnDialogClosed((JFXDialogEvent event1)->{
                           root.setEffect(null);
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
                dialogLayout.setBody(new Text("Aucune donnee"));
                dialogLayout.setActions(button);
                dialog.show();
                dialog.setOnDialogClosed((JFXDialogEvent event1)->{
                   root.setEffect(null);
                });
            }
        }
        
        @FXML
        public void rechercherUtilisateur() {
            try { 
                query = "SELECT * FROM utilisateur LEFT JOIN direction ON utilisateur.direction_util=direction.id_direction WHERE id_utilisateur LIKE '%"+txtSearch.getText()+"%' OR nom_util LIKE '%"
                        +txtSearch.getText()+"%' OR prenom_util LIKE '"+txtSearch.getText()+"%' OR nom_direction LIKE '%"+txtSearch.getText()+"%' OR fonction_util LIKE '%"+txtSearch.getText()+"%'";
                Statement st = DevsConnexion.getInstance().createStatement();
                ResultSet res = st.executeQuery(query);

                ResultSetMetaData meta = res.getMetaData();

                for(int i = 1 ; i <= meta.getColumnCount(); i++){
                    tab.getColumns().setAll(id_utilisateurColumn,nom_utilColumn,prenom_utilColumn,matricule_utilColumn,direction_utilColumn,fonction_utilColumn);
                }

                ObservableList<Utilisateur> list = FXCollections.observableArrayList();
                 while(res.next()){

                  	String fonction = res.getString("fonction_util");
                        String direction = res.getString("nom_direction");
                        String matricule = res.getString("matricule_util");
                        String prenom = res.getString("prenom_util");
                        String nom = res.getString("nom_util");
                        String id = res.getString("id_utilisateur");

                        list.add(new UtilisateurController.Utilisateur(id,nom,prenom,matricule,direction,fonction));     
                }

                tab.getItems().setAll(list);

                if (tab.getItems().isEmpty()){
                    JFXDialogLayout dialogLayout = new JFXDialogLayout();
                    JFXButton button = new JFXButton("Okay");
                    JFXDialog dialog = new JFXDialog(rootPane,dialogLayout,JFXDialog.DialogTransition.CENTER);
                    button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent)->{
                           root.setEffect(blur);
                           dialog.close();
                    });
                    button.getStyleClass().add("dialog-button");

                    dialogLayout.setHeading(new Label("Information"));
                    dialogLayout.setBody(new Text("Aucun utilisateur trouvee"));
                    dialogLayout.setActions(button);
                    dialog.show();
                    dialog.setOnDialogClosed((JFXDialogEvent event1)->{
                           root.setEffect(null);
                    });
                }

                tab.refresh();

            }catch (SQLException m){
                System.err.println(m);}
        }
        
        @FXML
        public void AjouterUtilisateur(ActionEvent event) throws Exception {

            Parent root = FXMLLoader.load(getClass().getResource("Ajouter_utilisateur.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            root.setOnMousePressed((MouseEvent e) -> {
                xOffset = e.getSceneX();
                yOffset = e.getSceneY();
            });
            root.setOnMouseDragged((MouseEvent e) -> {
                stage.setX(e.getScreenX() - xOffset);
                stage.setY(e.getScreenY() - yOffset);
            });
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Nouveau utilisateur");
            //stage.getIcons().add(new Image("/images/logo.png"));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.show();
        }
        
        @FXML
        public void editerUtilisateur() throws Exception {
            if(!tab.getItems().isEmpty()){
                 if(tab.getSelectionModel().getSelectedIndex() != -1){
                    String id = tab.getSelectionModel().getSelectedItems().get(0).getId_utilisateur();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Editer_utilisateur.fxml"));
                    Parent root = loader.load();
                    EditerUtilisateurController controller = (EditerUtilisateurController) loader.getController();
                    controller.obtenirInfoUtilisateur(id);
                    Scene scene = new Scene(root);
                    Stage stage = new Stage();
                    root.setOnMousePressed((MouseEvent e) -> {
                        xOffset = e.getSceneX();
                        yOffset = e.getSceneY();
                    });
                    root.setOnMouseDragged((MouseEvent e) -> {
                        stage.setX(e.getScreenX() - xOffset);
                        stage.setY(e.getScreenY() - yOffset);
                    });
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setTitle("Editer un utilisateur");
                    //stage.getIcons().add(new Image("/images/logo.png"));
                    stage.initStyle(StageStyle.UNDECORATED);
                    stage.setScene(scene);
                    stage.show();
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
                     dialogLayout.setBody(new Text("Aucune ligne selectionne"));
                     dialogLayout.setActions(button);
                     dialog.show();
                     dialog.setOnDialogClosed((JFXDialogEvent event1)->{
                           root.setEffect(null);
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
                dialogLayout.setBody(new Text("Aucune donnee"));
                dialogLayout.setActions(button);
                dialog.show();
                dialog.setOnDialogClosed((JFXDialogEvent event1)->{
                   root.setEffect(null);
                });
            }
        }
        
        @FXML
        public void voirProfilUtilisateur() throws Exception {
            if(!tab.getItems().isEmpty()){
                 if(tab.getSelectionModel().getSelectedIndex() != -1){
                    String id = tab.getSelectionModel().getSelectedItems().get(0).getId_utilisateur();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Profile.fxml"));
                    Parent root = loader.load();
                    ProfileController controller = (ProfileController) loader.getController();
                    controller.obtenirInfoProfilUtil(id);
                    Scene scene = new Scene(root);
                    Stage stage = new Stage();
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setTitle("Profil d'un utilisateur");
                    //stage.getIcons().add(new Image("/images/logo.png"));
                    stage.setScene(scene);
                    stage.show();
                    //txtSearch.getScene().getWindow().hide();
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
                     dialogLayout.setBody(new Text("Aucune ligne selectionne"));
                     dialogLayout.setActions(button);
                     dialog.show();
                     dialog.setOnDialogClosed((JFXDialogEvent event1)->{
                           root.setEffect(null);
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
                dialogLayout.setBody(new Text("Aucune donnee"));
                dialogLayout.setActions(button);
                dialog.show();
                dialog.setOnDialogClosed((JFXDialogEvent event1)->{
                   root.setEffect(null);
                });
            }
        }
        
        public class Utilisateur{
		
		private final SimpleStringProperty id_utilisateur;
		private final SimpleStringProperty nom_util;
		private final SimpleStringProperty prenom_util;
		private final SimpleStringProperty matricule_util;
		private final SimpleStringProperty direction_util;
		private final SimpleStringProperty fonction_util;

                public Utilisateur(String id_utilisateur, String nom_util, String prenom_util, String matricule_util, String direction_util, String fonction_util) {
                    this.id_utilisateur = new SimpleStringProperty(id_utilisateur);
                    this.nom_util = new SimpleStringProperty(nom_util);
                    this.prenom_util = new SimpleStringProperty(prenom_util);
                    this.matricule_util = new SimpleStringProperty(matricule_util);
                    this.direction_util = new SimpleStringProperty(direction_util);
                    this.fonction_util = new SimpleStringProperty(fonction_util);
                }
                
                 public String getId_utilisateur() {
                    return id_utilisateur.get();
                }

                public String getNom_util() {
                    return nom_util.get();
                }

                public String getPrenom_util() {
                    return prenom_util.get();
                }

                public String getMatricule_util() {
                    return matricule_util.get();
                }

                public String getDirection_util() {
                    return direction_util.get();
                }

                public String getFonction_util() {
                    return fonction_util.get();
                }

	}
    
}

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
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author DevMan
 */
public class CourrierController implements Initializable {

    private String query;
    double xOffset,yOffset;
	
    @FXML
    private JFXTextField txtSearch;
	
    @FXML
    private TableView<CourrierController.Courrier> tab;
    @FXML
    private TableColumn<CourrierController.Courrier, String> id_courrierColumn;
    @FXML
    private TableColumn<CourrierController.Courrier, String> date_arriveeColumn;
    @FXML
    private TableColumn<CourrierController.Courrier, String> pre_referenceColumn;
    @FXML
    private TableColumn<CourrierController.Courrier, String> date_pre_referenceColumn;
    @FXML
    private TableColumn<CourrierController.Courrier, String> origineColumn;
    @FXML
    private TableColumn<CourrierController.Courrier, String> referenceColumn;
    @FXML
    private TableColumn<CourrierController.Courrier, String> date_courrierColumn;
    @FXML
    private TableColumn<CourrierController.Courrier, String> objetColumn;
    @FXML
    private TableColumn<CourrierController.Courrier, String> classementColumn;
    @FXML
    private TableColumn<CourrierController.Courrier, String> date_traitementColumn;
    
    @FXML
    private StackPane rootPane;
    @FXML
    private BorderPane root;
    @FXML
    private JFXComboBox direction;
    
    @FXML
    private JFXButton btnNouveau;
    @FXML
    private JFXButton btnEditer;
    @FXML
    private JFXButton btnActualiser;
    @FXML
    private JFXButton btnSupprimer;
	
    BoxBlur blur = new BoxBlur(3,3,3);
    
    String idUtilConnecter;
    int fonctionUtilConnecter;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadColumn();
        loadDirection();
    } 
    
    @FXML
    public void pageAjoutCourrier() throws Exception {
 		 FXMLLoader loader = new FXMLLoader(getClass().getResource("Ajouter_courrier.fxml"));
                 Parent root = loader.load();
                                   
                 AjouterCourrierController controller = (AjouterCourrierController) loader.getController();
                 controller.idUtilConnecter=idUtilConnecter;
                                   
                 Stage load = new Stage();
                 load.setScene(new Scene(root));
                 load.setTitle("Nouveau courrier");
 		 load.initModality(Modality.APPLICATION_MODAL);
                 load.show();
 		
    }
    
    @FXML
    public void pageEditerCourrier() throws Exception {
 	if(!tab.getItems().isEmpty()){
                 if(tab.getSelectionModel().getSelectedIndex() != -1){
                    String id = tab.getSelectionModel().getSelectedItems().get(0).getId_courrier();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Editer_courrier.fxml"));
                    Parent root = loader.load();
                    EditerCourrierController controller = (EditerCourrierController) loader.getController();
                    controller.idUtilConnecter = idUtilConnecter;
                    controller.fonctionUtilConnecter = fonctionUtilConnecter;
                    controller.obtenirInfoCourrier(id);
                    Scene scene = new Scene(root);
                    Stage stage = new Stage();
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setTitle("Editer un courrier");
                    //stage.getIcons().add(new Image("/images/logo.png"));
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
    public void pageVoirCourrier() throws Exception {
        
 	if(!tab.getItems().isEmpty()){
                 if(tab.getSelectionModel().getSelectedIndex() != -1){
                    String id = tab.getSelectionModel().getSelectedItems().get(0).getId_courrier();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Editer_courrier.fxml"));
                    Parent root = loader.load();
                    EditerCourrierController controller = (EditerCourrierController) loader.getController();
                    controller.idUtilConnecter = idUtilConnecter;
                    controller.fonctionUtilConnecter = fonctionUtilConnecter;
                    controller.obtenirInfoCourrier(id);
                    Scene scene = new Scene(root);
                    Stage stage = new Stage();
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setTitle("Visualisation du courrier");
                    //stage.getIcons().add(new Image("/images/logo.png"));
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
    
    public void loadCourrier() {
        
            if(fonctionUtilConnecter == 2){
                query = "SELECT * FROM courrier LEFT JOIN utilisateur ON courrier.utilisateur=utilisateur.id_utilisateur WHERE direction_util='"+obtenirIdDirectionUtil(idUtilConnecter)+"' ORDER BY id_courrier DESC";
                direction.getSelectionModel().select(obtenirNomDirection(obtenirIdDirectionUtil(idUtilConnecter)));
            }else{
                query = "SELECT * FROM courrier ORDER BY id_courrier DESC";
            }
            
            ObservableList<CourrierController.Courrier> list = FXCollections.observableArrayList();
            try {
            
            Statement state = DevsConnexion.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet res = state.executeQuery(query);
            ResultSetMetaData meta = (ResultSetMetaData) res.getMetaData();
            
            for(int i = 1 ; i <= meta.getColumnCount(); i++){
    		tab.getColumns().setAll(id_courrierColumn,date_arriveeColumn,pre_referenceColumn,date_pre_referenceColumn,origineColumn,referenceColumn,date_courrierColumn,objetColumn,classementColumn,date_traitementColumn);
            }
            
            while(res.next()){	
            	
            	String date_arrivee = res.getString("date_arrivee");
            	String pre_reference = res.getString("pre_reference");
            	String date_pre_reference = res.getString("date_pre_reference");
            	String origine = res.getString("origine");
            	String reference = res.getString("reference");
            	String date_courrier = res.getString("date_courrier");
            	String id = res.getString("id_courrier");
                String objet = res.getString("objet");
                String classement = res.getString("classement");
                String date_traitement = res.getString("date_traitement");
            	
            	list.add(new CourrierController.Courrier(id,date_arrivee,pre_reference,date_pre_reference,origine,reference,date_courrier,objet,classement,date_traitement));     
            }
            res.close();
            state.close();
            
         } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "ERREUR ! ", JOptionPane.ERROR_MESSAGE);
         } 
		tab.getItems().setAll(list);
	}
    
    
        public void loadDirection() {
            query = "SELECT * FROM direction";
            try {
            
                Statement state = DevsConnexion.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet res = state.executeQuery(query);
                ResultSetMetaData meta = (ResultSetMetaData) res.getMetaData();

                for(int i = 1 ; i <= meta.getColumnCount(); i++){
                    tab.getColumns().setAll(id_courrierColumn,date_arriveeColumn,pre_referenceColumn,date_pre_referenceColumn,origineColumn,referenceColumn,date_courrierColumn,objetColumn,classementColumn,date_traitementColumn);
                }

                while(res.next()){	
                    direction.getItems().add(res.getString("nom_direction"));
                }
                res.close();
                state.close();
            
            } catch (SQLException e) {
               JOptionPane.showMessageDialog(null, e.getMessage(), "ERREUR ! ", JOptionPane.ERROR_MESSAGE);
            } 
	}
	
	@FXML
	public void refreshData() {
            loadCourrier();
	}

	private void loadColumn() {
            id_courrierColumn.setCellValueFactory(new PropertyValueFactory<>("Id_courrier"));
            date_arriveeColumn.setCellValueFactory(new PropertyValueFactory<>("Date_arrivee"));
	    pre_referenceColumn.setCellValueFactory(new PropertyValueFactory<>("Pre_reference"));
	    date_pre_referenceColumn.setCellValueFactory(new PropertyValueFactory<>("Date_pre_reference"));
	    origineColumn.setCellValueFactory(new PropertyValueFactory<>("Origine"));
	    referenceColumn.setCellValueFactory(new PropertyValueFactory<>("Reference"));
	    date_courrierColumn.setCellValueFactory(new PropertyValueFactory<>("Date_courrier"));
            objetColumn.setCellValueFactory(new PropertyValueFactory<>("Objet"));
            classementColumn.setCellValueFactory(new PropertyValueFactory<>("Classement"));
            date_traitementColumn.setCellValueFactory(new PropertyValueFactory<>("Date_traitement"));
	}
        
        @FXML
        public void deleteAction(ActionEvent event) throws Exception {

            ObservableList<CourrierController.Courrier> select;
            select = tab.getSelectionModel().getSelectedItems();

            if(!tab.getItems().isEmpty()){
                 if(tab.getSelectionModel().getSelectedIndex() != -1){
                      if(JOptionPane.showConfirmDialog(null, "attention!!! etes-vous sure de"
                    + " vouloir supprimer ce courrier ?","Supprimer un courrier",JOptionPane.YES_NO_OPTION)
                    == JOptionPane.OK_OPTION){
                        String id = select.get(0).getId_courrier();
                        String objet = select.get(0).getObjet();
                        query = "DELETE FROM courrier WHERE id_courrier=? AND objet=? LIMIT 1";
                        PreparedStatement prepare;
                        prepare = DevsConnexion.getInstance().prepareStatement(query);
                        prepare.setString(1, id);
                        prepare.setString(2, objet);
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
                           loadCourrier();
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
        
        private int obtenirIdDirection(String nomDirection) {
            int i=0;
            try{
                String query = "SELECT id_direction FROM direction WHERE nom_direction='"+nomDirection+"'";
                Statement state = DevsConnexion.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

                ResultSet res = state.executeQuery(query);
                if(res.first()){
                    i = res.getInt("id_direction");
                }

            }catch(SQLException e){
                JOptionPane.showMessageDialog(null, e.getMessage());
            }

            return i;
        }
        
         private int obtenirIdDirectionUtil(String idUtil) {
            int i=0;
            try{
                String query = "SELECT direction_util FROM utilisateur WHERE id_utilisateur='"+idUtil+"'";
                Statement state = DevsConnexion.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

                ResultSet res = state.executeQuery(query);
                if(res.first()){
                    i = res.getInt("direction_util");
                }

            }catch(SQLException e){
                JOptionPane.showMessageDialog(null, e.getMessage());
            }

            return i;
        }
         
        private String obtenirNomDirection(int idDirection) {
            String nom = "";
            try{
                String query = "SELECT nom_direction FROM direction WHERE id_direction='"+idDirection+"'";
                Statement state = DevsConnexion.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

                ResultSet res = state.executeQuery(query);
                if(res.first()){
                    nom = res.getString("nom_direction");
                }

            }catch(SQLException e){
                JOptionPane.showMessageDialog(null, e.getMessage());
            }

            return nom;
        }

        @FXML
        public void searchCourrier() {
            try { 
                if(direction.getSelectionModel().getSelectedIndex() != -1 && fonctionUtilConnecter == 2){
                    query = "SELECT * FROM courrier LEFT JOIN utilisateur ON courrier.utilisateur=utilisateur.id_utilisateur WHERE (origine LIKE '%"+txtSearch.getText()+"%' OR date_courrier LIKE '%"
                        +txtSearch.getText()+"%' OR date_arrivee LIKE '%"+txtSearch.getText()+"%' OR id_courrier LIKE '%"+txtSearch.getText()+"%') AND direction_util='"+obtenirIdDirectionUtil(idUtilConnecter)+"' ORDER BY id_courrier DESC";
                }else if(direction.getSelectionModel().getSelectedIndex() != -1){
                    query = "SELECT * FROM courrier LEFT JOIN utilisateur ON courrier.utilisateur=utilisateur.id_utilisateur WHERE direction_util='"+obtenirIdDirection(direction.getSelectionModel().getSelectedItem().toString())+"'";
                } else{
                    query = "SELECT * FROM courrier LEFT JOIN utilisateur ON courrier.utilisateur=utilisateur.id_utilisateur WHERE origine LIKE '%"+txtSearch.getText()+"%' OR date_courrier LIKE '%"
                        +txtSearch.getText()+"%' OR date_arrivee LIKE '%"+txtSearch.getText()+"%' OR id_courrier LIKE '%"+txtSearch.getText()+"%'";
                }
                
                Statement st = DevsConnexion.getInstance().createStatement();
                ResultSet res = st.executeQuery(query);

                ResultSetMetaData meta = res.getMetaData();

                for(int i = 1 ; i <= meta.getColumnCount(); i++){
                    tab.getColumns().setAll(id_courrierColumn,date_arriveeColumn,pre_referenceColumn,date_pre_referenceColumn,origineColumn,referenceColumn,date_courrierColumn,objetColumn,classementColumn,date_traitementColumn);
                }

                ObservableList<Courrier> list = FXCollections.observableArrayList();
                 while(res.next()){

                    String date_arrivee = res.getString("date_arrivee");
                    String pre_reference = res.getString("pre_reference");
                    String date_pre_reference = res.getString("date_pre_reference");
                    String origine = res.getString("origine");
                    String reference = res.getString("reference");
                    String date_courrier = res.getString("date_courrier");
                    String id = res.getString("id_courrier");
                    String objet = res.getString("objet");
                    String classement = res.getString("classement");
                    String date_traitement = res.getString("date_traitement");

                    list.add(new CourrierController.Courrier(id,date_arrivee,pre_reference,date_pre_reference,origine,reference,date_courrier,objet,classement,date_traitement));        
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
                    dialogLayout.setBody(new Text("Aucun courrier trouvee"));
                    dialogLayout.setActions(button);
                    dialog.show();
                    dialog.setOnDialogClosed((JFXDialogEvent event1)->{
                           root.setEffect(null);
                    });
                }

                tab.refresh();

            }catch (SQLException m){
                   JOptionPane.showMessageDialog(null, ""+m.getMessage());
            }
        }
        
        public void verifierAccesBtn(){
            if(fonctionUtilConnecter == 0 || fonctionUtilConnecter == 3){
                btnNouveau.setVisible(false);
                btnEditer.setVisible(false);
                btnSupprimer.setVisible(false);
                btnActualiser.setVisible(false);
            }else if(fonctionUtilConnecter == 2){
                direction.setDisable(true);
            }
        }
        
        public class Courrier{
		
		private final SimpleStringProperty id_courrier;
		private final SimpleStringProperty date_arrivee;
		private final SimpleStringProperty pre_reference;
		private final SimpleStringProperty date_pre_reference;
		private final SimpleStringProperty origine;
		private final SimpleStringProperty reference;
		private final SimpleStringProperty date_courrier;
		private final SimpleStringProperty objet;
                private final SimpleStringProperty classement;
                private final SimpleStringProperty date_traitement;

                public Courrier(String id_courrier, String date_arrivee, String pre_reference, String date_pre_reference, String origine, String reference, String date_courrier, String objet, String classement, String date_traitement) {
                    this.id_courrier = new SimpleStringProperty(id_courrier);
                    this.date_arrivee = new SimpleStringProperty(date_arrivee);
                    this.pre_reference = new SimpleStringProperty(pre_reference);
                    this.date_pre_reference = new SimpleStringProperty(date_pre_reference);
                    this.origine = new SimpleStringProperty(origine);
                    this.reference = new SimpleStringProperty(reference);
                    this.date_courrier = new SimpleStringProperty(date_courrier);
                    this.objet = new SimpleStringProperty(objet);
                    this.classement = new SimpleStringProperty(classement);
                    this.date_traitement = new SimpleStringProperty(date_traitement);
                }

                public String getId_courrier() {
                    return id_courrier.get();
                }

                public String getDate_arrivee() {
                    return date_arrivee.get();
                }

                public String getPre_reference() {
                    return pre_reference.get();
                }

                public String getDate_pre_reference() {
                    return date_pre_reference.get();
                }

                public String getOrigine() {
                    return origine.get();
                }

                public String getReference() {
                    return reference.get();
                }

                public String getDate_courrier() {
                    return date_courrier.get();
                }

                public String getObjet() {
                    return objet.get();
                }

                public String getClassement() {
                    return classement.get();
                }

                public String getDate_traitement() {
                    return date_traitement.get();
                }
	}
}

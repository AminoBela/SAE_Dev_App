package org.javafxapp.sae_dev_app_project.classComponent;

import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import org.javafxapp.sae_dev_app_project.importExport.Export;
import org.javafxapp.sae_dev_app_project.importExport.SingleClassLoader;


/**
 * Classe qui représente un attribut d'une classe
 */
public class Attribute extends ClassComponent {

    // Attributs
    private String type; // Type de l'attribut



    /**
     * Constructeur de la classe
     * @param modifier Type d'accès à l'attribut
     * @param name Nom de l'attribut
     * @param type Type de l'attribut
     */
    public Attribute(String modifier, String type, String name) {
        this.modifier = modifier;
        this.name = name;
        this.type = type;
        this.hidden = false; // Pas caché par défaut
    }



    /**
     * Méthode qui retourne l'affichage de l'attribut dans l'interface
     * @return L'affichage de l'attribut dans l'interface
     */
    @Override
    public HBox getDisplay() {
        // Création de la HBox
        HBox hBox = new HBox();

        // Ajout du type d'accès
        Text modifierText = new Text(Export.convertModifier(modifier));
        hBox.getChildren().add(modifierText);

        // Ajout du nom
        Text nameText = new Text(name);
        hBox.getChildren().add(nameText);

        // Ajout du type
        Text typeText = new Text(" : " + Export.removePackageName(type)); // remove package name pour enlever le nom des packages a chaque fois

        System.out.printf("Attribute: %s\n", Export.removePackageName(type));

        hBox.getChildren().add(typeText);

        return hBox;
    }



    /*
     * ### GETTERS ###
     */
    public String getType() {
        return type;
    }



    @Override
    public String toString() {
        return Export.convertModifier(getModifier()) + " " + Export.removePackageName(getName()) + " : " + Export.removePackageName(getType());
    }


}

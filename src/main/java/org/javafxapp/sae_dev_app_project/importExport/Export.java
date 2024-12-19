package org.javafxapp.sae_dev_app_project.importExport;

import javafx.scene.image.WritableImage;
import org.javafxapp.sae_dev_app_project.subjects.ModelClass;
import org.javafxapp.sae_dev_app_project.views.ViewAllClasses;

import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import javax.imageio.ImageIO;


/**
 * Classe qui contient toutes les méthodes pour exporter le diagramme sous différents formats
 */
public class Export {


    /**
     * Méthode qui exporte le squelette Java d'une ou plusieurs classes
     * @param view Vue contenant les classes à traiter
     */
    public static void exportInJava(ViewAllClasses view) {

        // Affichage d'un FileChooser pour que l'utilisateur choisisse le chemin et le nom du fichier
        FileChooserHandler fileChooserHandler = new FileChooserHandler();
        File directory = fileChooserHandler.openRepositoryPath();

        String codeJava;

        try {

            // Boucle pour parcourir toutes les classes du diagramme dans la vue
            for (int i = 0; i < view.getAllClasses().size(); i++) {

                String className = view.getAllClasses().get(i).getName(); // Supposons que `getName()` donne le nom de la classe
                String fileName = className + ".java";

                File file = new File(directory, fileName);

                Writer writer = new FileWriter(file);

                // Ouverture du fichier en mode écriture
                BufferedWriter bufferedWriter = new BufferedWriter(writer);

                // On récupère le code puml de la classe actuellement traitée dans la boucle
                codeJava = getJavaFrameCode(view.getAllClasses().get(i));

                // Ecriture du code puml récupéré de la classe
                bufferedWriter.write(codeJava);

                // Fermeture du fichier
                bufferedWriter.close();

            }

        } catch (IOException e){
            System.out.println(e.getMessage());
        }


    }



    /**
     * Méthode qui créé le squelette de la classe Java à partir d'un modèle
     * @param modelClass Modèle d'une classe
     * @return Un String contenant le squelette Java
     */
    public static String getJavaFrameCode(ModelClass modelClass) {

        // Initialisation de l'affichage final
        StringBuffer aff = new StringBuffer();

        // Intitué de la classe
        aff.append("class " + modelClass.getName() + " {\n");

        // On ferme la classe
        aff.append("}");

        return aff.toString();

    }



    /**
     * Méthode qui exporte une capture d'écran de l'application au format PNG
     * @param view Vue contenant toutes les classes
     */
    public static void exportInPNG(ViewAllClasses view) {

        // Affichage d'un FileChooser pour que l'utilisateur choisisse le chemin et le nom du fichier
        FileChooserHandler fileChooserHandler = new FileChooserHandler();
        File file = fileChooserHandler.openRepositoryPathAndFileNameChooser("png");

        // On prend un screenshot de l'application
        WritableImage image = view.snapshot(null, null);

        // Convertir WritableImage en BufferedImage
        BufferedImage bufferedImage = new BufferedImage(
                (int) image.getWidth(),
                (int) image.getHeight(),
                BufferedImage.TYPE_INT_ARGB
        );

        // Conversion du WritableImage en BufferedImage
        for (int y = 0; y < (int) image.getHeight(); y++) {
            for (int x = 0; x < (int) image.getWidth(); x++) {
                int argb = image.getPixelReader().getArgb(x, y);
                bufferedImage.setRGB(x, y, argb);
            }
        }

        try {
            // Enregistrer l'image au format PNG
            ImageIO.write(bufferedImage, "png", file);
        }
        catch (IOException e) {
            System.out.println("erreur");
        }
        catch (IllegalArgumentException ignored) {}


    }



    /**
     * Méthode qui construit un fichier PlantUml à partir d'une liste de classes Java récupérée de la vue
     * @param view Vue qui contient la liste de toutes les classes
     */
    public static void exportInPUml(ViewAllClasses view) {

        // Affichage d'un FileChooser pour que l'utilisateur choisisse le chemin et le nom du fichier
        FileChooserHandler fileChooserHandler = new FileChooserHandler();
        File file = fileChooserHandler.openRepositoryPathAndFileNameChooser("codepuml");

        String codepuml;

        try {

            Writer writer = new FileWriter(file);
            // Ouverture du fichier en mode écriture
            BufferedWriter bufferedWriter = new BufferedWriter(writer);

            // On ajoute le "@startuml"
            bufferedWriter.write("@startuml");

            // Retour à la ligne
            bufferedWriter.newLine();

            // Boucle pour parcourir toutes les classes du diagramme dans la vue
            for (int i = 0; i < view.getAllClasses().size(); i++) {

                // On récupère le code puml de la classe actuellement traitée dans la boucle
                codepuml = getPUmlCode(view.getAllClasses().get(i));

                // Ecriture du code puml récupéré de la classe
                bufferedWriter.write(codepuml);

                // Retour à la ligne
                bufferedWriter.newLine();

            }

            // On ajoute le "@enduml"
            bufferedWriter.write("@enduml");

            // Fermeture du fichier
            bufferedWriter.close();

        } catch (IOException e){
            System.out.println(e.getMessage());
        }

    }



    /**
     * Méthode qui récupère le code plantuml balisé d'une classe
     * @param modelClass La classe de type ModelClass que l'on veut traiter
     * @return Le code plantuml en String
     */
    private static String getPUmlCode(ModelClass modelClass){


        String className = modelClass.getName();

        // Initialisation de l'affichage final
        StringBuffer aff = new StringBuffer();

        // A MODIFIER APRES ITERATION //

        // On affiche le type de classe
        aff.append("class ");

        // On affiche le nom de la classe
        aff.append(className);

        // Accolades
        aff.append("{\n");
        aff.append("}\n");

        //----------------------------//

        return aff.toString();


    }



    /**
     * Méthode qui retourne le bon caractère pour représenter l'accès en UML (+, -, #, {abstract}, {static})
     * @param accessInt Entier qui représente le type d'accès
     * @return Le bon caratère représentant l'accès
     */
    private static String convertPUmlAccess(int accessInt) {

        String access = Modifier.toString(accessInt);
        StringBuffer res = new StringBuffer();

        // Si l'attribut (ou la méthode) est private
        if (access.contains("private")) {
            // On commence par "- "
            res.append("- ");
        }
        // Sinon si l'attribut (ou la méthode) est protected
        else if (access.contains("protected")) {
            // On commence par "# "
            res.append("# ");
        }
        // Sinon
        else {
            // On commence par "+ "
            res.append("+ ");
        }

        // Si l'attribut (ou la méthode) est static
        if (access.contains("static")) {
            // On ajoute "{static} "
            res.append("{static} ");
        }

        // Si la méthode est abstract
        if (access.contains("abstract")) {
            // On ajoute "{abstract} "
            res.append("{abstract} ");
        }

        // On retourne le résultat
        return res.toString();

    }



    /**
     * Méthode qui retire le package du nom
     * @param txt La chaîne à traiter
     * @return Le nom traité
     */
    private static String removePackageName(String txt) {

        // Si le text contient des "<>"
        if (txt.contains("<") && txt.contains(">")) {
            // On récupère la classe principale
            String classePrincipale = txt.substring(0, txt.indexOf("<"));

            // On récupère la classe entre "<>"
            int indexDeb = txt.indexOf("<");
            int indexFin = txt.indexOf(">");
            String classeUtilisee = txt.substring(indexDeb, indexFin);

            // On récupère le nom seul de la classe principale
            String[] res1 = classePrincipale.split("\\.");
            classePrincipale = res1[res1.length - 1];

            // On récupère le nom seul de la classe entre "<>"
            String[] res2 = classeUtilisee.split("\\.");
            classeUtilisee = res2[res2.length - 1];

            // On retourne le nom simple de la classe principale et de la classe utilisée
            return classePrincipale + "<" + classeUtilisee + ">";
        }
        // Sinon
        else {
            // On retourne le nom seul de la classe
            String[] res = txt.split("\\.");
            return res[res.length - 1];
        }

    }



    /**
     * Méthode qui retire la dernière virgule d'une chaîne de caractère (et l'espace qui suit)
     * @param txt La chaîne à traiter
     */
    private static void removeLastComa(StringBuffer txt) {

        // Si le texte contient une virgule
        if (txt.toString().contains(",")) {
            // On récupère l'index là où se trouve la dernière virgule
            int index = txt.lastIndexOf(",");
            // On supprime cette virgule et l'espace qui suit
            txt.deleteCharAt(index);
            txt.deleteCharAt(index);
        }

    }

}

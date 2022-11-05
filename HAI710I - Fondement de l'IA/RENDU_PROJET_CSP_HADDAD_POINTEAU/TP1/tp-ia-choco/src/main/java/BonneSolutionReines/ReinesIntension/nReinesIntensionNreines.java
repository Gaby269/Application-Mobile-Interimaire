package BonneSolutionReines.ReinesIntension;
import java.util.Scanner;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;
//import org.chocosolver.solver.constraints.extension.Tuples;


public class nReinesIntensionNreines{

	public static void main(String[] args) {
		
		// Création du modele
		Model model = new Model("nReine");
		
		//Donner un entier pour faire le nombre de reines : 
		System.out.println("Veuillez entrée un nombre de reines : ");		//je demande a ce qu'on entre une solution
		@SuppressWarnings("resource")
		Scanner clavier = new Scanner(System.in);							//je recupère la valeur
		int n = clavier.nextInt();											//je le convertie en entier
		
		// Création des variables
		int nbReine = n;
		int nbCases = n;
		//on considere ici les reines � la ligne i placer dans les colonnes numerot� de 1 � n
		IntVar [] reines = model.intVarArray("Ri", nbReine, 1, nbCases);	
		
		
        
        /************************************************************************
         *                                                                      *
         *    Compléter en ajoutant les contraintes modélisant les contraintes  *
         *                                                                      *
         ************************************************************************/

		
		//Contraintes d'egalite : les reines ne doivent pas �tre sur la m�me colonne donc qu'elle soit �gal
		model.allDifferent(reines).post();
		
			
		
		
		//contraintes diago
		//a droite
		for (int i=0; i<n; i++) {		//la reines � la ligne i
			for (int j=i+1; j<n; j++) {	//la reine � la ligne j
				//Pour ne pas utiliser les valeurs absolue on choisis de tester tous les cas possible selon i et j
				model.arithm(reines[j], "-", reines[i], "!=", j-i).post();
				model.arithm(reines[j], "-", reines[i], "!=", i-j).post();
				model.arithm(reines[i], "-", reines[j], "!=", j-i).post();
				model.arithm(reines[i], "-", reines[j], "!=", i-j).post();
			}
		}
		
		
		
		
		
		
        // Affichage du réseau de contraintes créé
        System.out.println("*** Réseau Initial ***");
        System.out.println("Model : " + model);
        

        // Calcul de la première solution
        if(model.getSolver().solve()) {
        	System.out.println("\n\n*** Première solution ***");        
        	System.out.println("Sol "+ model.getSolver().getSolutionCount()+"\n"+model);
        }

        
   
    	// Calcul de toutes les solutions
    	System.out.println("\n\n*** Autres solutions *** (non affiché)");        
        while(model.getSolver().solve()) {    	
            //System.out.println("Sol "+ model.getSolver().getSolutionCount()+"\n"+model);
	    }
	    
 
        
        // Affichage de l'ensemble des caractéristiques de résolution
      	System.out.println("\n\n*** Bilan ***");        
        model.getSolver().printStatistics();
	}
}

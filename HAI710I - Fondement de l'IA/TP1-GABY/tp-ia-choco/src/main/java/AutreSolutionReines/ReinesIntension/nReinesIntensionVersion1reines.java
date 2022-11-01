package AutreSolutionReines.ReinesIntension;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;
//import org.chocosolver.solver.constraints.extension.Tuples;


public class nReinesIntensionVersion1reines{

	public static void main(String[] args) {
		
		// Création du modele
		Model model = new Model("nReine");
		
		
		// Création des variables
		int nb_reine = 1;
		int nb_cases = 1;
		IntVar [] reines = model.intVarArray("Ri", nb_reine, 1, nb_cases);	//on considere ici les reines placer de la case 1 � la 25eme
		
		
        
        /************************************************************************
         *                                                                      *
         *    Compléter en ajoutant les contraintes modélisant les contraintes  *
         *                                                                      *
         ************************************************************************/

		
		//Contraintes d'�galit� :
		model.allDifferent(new IntVar[]{reines[0]}).post();
		
		//Contraintes pour que Ri soit � la ligne i
		model.arithm(reines[0], ">=", 1).post();
		model.arithm(reines[0], "<=", 1).post();
		
		
		
		
	
		
		
		
		
		
		
        // Affichage du réseau de contraintes créé
        System.out.println("*** Réseau Initial ***");
        System.out.println(model);
        

        // Calcul de la première solution
        if(model.getSolver().solve()) {
        	System.out.println("\n\n*** Première solution ***");        
        	System.out.println(model);
        }

        
   
    	// Calcul de toutes les solutions
    	System.out.println("\n\n*** Autres solutions ***");        
        while(model.getSolver().solve()) {    	
            System.out.println("Sol "+ model.getSolver().getSolutionCount()+"\n"+model);
	    }
	    
 
        
        // Affichage de l'ensemble des caractéristiques de résolution
      	System.out.println("\n\n*** Bilan ***");        
        model.getSolver().printStatistics();
	}
}

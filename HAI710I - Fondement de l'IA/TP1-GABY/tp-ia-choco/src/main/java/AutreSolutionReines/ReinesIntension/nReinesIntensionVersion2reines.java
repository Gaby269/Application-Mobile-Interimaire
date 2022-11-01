package AutreSolutionReines.ReinesIntension;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;
//import org.chocosolver.solver.constraints.extension.Tuples;


public class nReinesIntensionVersion2reines{

	public static void main(String[] args) {
		
		// Création du modele
		Model model = new Model("nReine");
		
		
		// Création des variables
		int nb_reine = 2;
		int nb_cases = 4;
		IntVar [] reines = model.intVarArray("Ri", nb_reine, 1, nb_cases);	//on considere ici les reines placer de la case 1 � la 25eme
		
		
        
        /************************************************************************
         *                                                                      *
         *    Compléter en ajoutant les contraintes modélisant les contraintes  *
         *                                                                      *
         ************************************************************************/

		
		//Contraintes d'�galit� :
		model.allDifferent(new IntVar[]{reines[0],reines[1]}).post();
		
		//Contraintes pour que Ri soit � la ligne i
		model.arithm(reines[0], ">=", 1).post();
		model.arithm(reines[0], "<=", 2).post();
		
		model.arithm(reines[1], ">=", 3).post();
		model.arithm(reines[1], "<=", 4).post();
		
		
		
		
		
		
		//Contraintes de colonnes
		for (int i=0; i<2; i++) {
			for (int j=i+1; j<2; j++) {
				for (int k=2; k<3; k=k+2) {
					model.arithm(reines[j], "!=", reines[i], "+", k).post();
				}
			}
		}
		
		
		
		//contraintes diago
		//a droite
		for (int i=0; i<2; i++) {
			int k=3;
			for (int j=i+1; j<2; j++) {
				model.arithm(reines[j], "!=", reines[i], "+", k).post();
				k+=3;
			}
		}
		//a gauche
		for (int i=0; i<2; i++) {
			int k=1;
			for (int j=i+1; j<2; j++) {
				model.arithm(reines[j], "!=", reines[i], "+", k).post();
				k+=1;
			}
		}
	
		
		
		
		
		
		
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

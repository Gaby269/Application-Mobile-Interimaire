package AutreSolutionReines.ReinesIntension;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;
//import org.chocosolver.solver.constraints.extension.Tuples;


public class nReinesIntensionVersion12reines{

	public static void main(String[] args) {
		
		// Création du modele
		Model model = new Model("nReine");
		
		
		// Création des variables
		int nb_reine = 12;
		int nb_cases = 144;
		IntVar [] reines = model.intVarArray("Ri", nb_reine, 1, nb_cases);	//on considere ici les reines placer de la case 1 � la 25eme
		
		
        
        /************************************************************************
         *                                                                      *
         *    Compléter en ajoutant les contraintes modélisant les contraintes  *
         *                                                                      *
         ************************************************************************/

		
		//Contraintes d'�galit� :
		model.allDifferent(new IntVar[]{reines[0],reines[1],reines[2],reines[3],reines[4],reines[5],reines[6],reines[7],reines[8],reines[9],reines[10],reines[11]}).post();
		
		//Contraintes pour que Ri soit � la ligne i
		model.arithm(reines[0], ">=", 1).post();
		model.arithm(reines[0], "<=", 12).post();
		
		model.arithm(reines[1], ">=", 13).post();
		model.arithm(reines[1], "<=", 24).post();
		
		model.arithm(reines[2], ">=", 25).post();
		model.arithm(reines[2], "<=", 36).post();
		
		model.arithm(reines[3], ">=", 37).post();
		model.arithm(reines[3], "<=", 48).post();
		
		model.arithm(reines[4], ">=", 49).post();
		model.arithm(reines[4], "<=", 60).post();
		
		model.arithm(reines[5], ">=", 61).post();
		model.arithm(reines[5], "<=", 72).post();
		
		model.arithm(reines[6], ">=", 73).post();
		model.arithm(reines[6], "<=", 84).post();
		
		model.arithm(reines[7], ">=", 85).post();
		model.arithm(reines[7], "<=", 96).post();
		
		model.arithm(reines[8], ">=", 97).post();
		model.arithm(reines[8], "<=", 108).post();
		
		model.arithm(reines[9], ">=", 109).post();
		model.arithm(reines[9], "<=", 120).post();
		
		model.arithm(reines[10], ">=", 121).post();
		model.arithm(reines[10], "<=", 132).post();
		
		model.arithm(reines[11], ">=", 133).post();
		model.arithm(reines[11], "<=", 144).post();
		
		
		
		
		
		//Contraintes de colonnes
		for (int i=0; i<12; i++) {
			for (int j=i+1; j<12; j++) {
				for (int k=12; k<133; k=k+12) {
					model.arithm(reines[j], "!=", reines[i], "+", k).post();
				}
			}
		}
		
		
		
		//contraintes diago
		//a droite
		for (int i=0; i<12; i++) {
			int k=13;
			for (int j=i+1; j<12; j++) {
				model.arithm(reines[j], "!=", reines[i], "+", k).post();
				k+=13;
			}
		}
		//a gauche
		for (int i=0; i<12; i++) {
			int k=11;
			for (int j=i+1; j<12; j++) {
				model.arithm(reines[j], "!=", reines[i], "+", k).post();
				k+=11;
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
    	System.out.println("\n\n*** Autres solutions ***");        
        while(model.getSolver().solve()) {    	
            System.out.println("Sol "+ model.getSolver().getSolutionCount()+"\n"+model);
	    }
	    
 
        
        // Affichage de l'ensemble des caractéristiques de résolution
      	System.out.println("\n\n*** Bilan ***");        
        model.getSolver().printStatistics();
	}
}

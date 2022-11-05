package AutreSolutionReines.ReinesIntension;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;
//import org.chocosolver.solver.constraints.extension.Tuples;


public class nReinesIntensionVersion8reines{

	public static void main(String[] args) {
		
		// Création du modele
		Model model = new Model("nReine");
		
		
		// Création des variables
		int nbReine = 8;
		int nbCases = 64;
		IntVar [] reines = model.intVarArray("Ri", nbReine, 1, nbCases);	//on considere ici les reines placer de la case 1 � la 25eme
		
		
        
        /************************************************************************
         *                                                                      *
         *    Compléter en ajoutant les contraintes modélisant les contraintes  *
         *                                                                      *
         ************************************************************************/

		
		//Contraintes d'�galit� :
		model.allDifferent(new IntVar[]{reines[0],reines[1],reines[2],reines[3],reines[4]}).post();
		
		//Contraintes pour que Ri soit � la ligne i
		model.arithm(reines[0], ">=", 1).post();
		model.arithm(reines[0], "<=", 8).post();
		
		model.arithm(reines[1], ">=", 9).post();
		model.arithm(reines[1], "<=", 16).post();
		
		model.arithm(reines[2], ">=", 17).post();
		model.arithm(reines[2], "<=", 24).post();
		
		model.arithm(reines[3], ">=", 25).post();
		model.arithm(reines[3], "<=", 32).post();
		
		model.arithm(reines[4], ">=", 33).post();
		model.arithm(reines[4], "<=", 40).post();
		
		model.arithm(reines[5], ">=", 41).post();
		model.arithm(reines[5], "<=", 48).post();
		
		model.arithm(reines[6], ">=", 49).post();
		model.arithm(reines[6], "<=", 56).post();
		
		model.arithm(reines[7], ">=", 57).post();
		model.arithm(reines[7], "<=", 64).post();
		
		
		
		
		
		
		//Contraintes de colonnes
		for (int i=0; i<8; i++) {
			for (int j=i+1; j<8; j++) {
				for (int k=8; k<57; k=k+8) {
					model.arithm(reines[j], "!=", reines[i], "+", k).post();
				}
			}
		}
		
		
		
		//contraintes diago
		//a droite
		for (int i=0; i<8; i++) {
			int k=9;		//on doit parcourir à droite 9 cases pour avoir la diago en  bas à droite
			int t=7;		//on doit parcourir 7 cases pour aller a la case en bas à gauche
			for (int j=i+1; j<8; j++) {
				model.arithm(reines[j], "!=", reines[i], "+", k).post();
				model.arithm(reines[j], "!=", reines[i], "+", t).post();
				k+=9;
				t+=7;
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

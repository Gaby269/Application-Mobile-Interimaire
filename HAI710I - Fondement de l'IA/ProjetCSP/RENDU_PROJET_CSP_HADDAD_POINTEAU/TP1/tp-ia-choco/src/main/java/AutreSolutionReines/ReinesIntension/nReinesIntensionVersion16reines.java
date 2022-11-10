package AutreSolutionReines.ReinesIntension;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;
//import org.chocosolver.solver.constraints.extension.Tuples;


public class nReinesIntensionVersion16reines{

	public static void main(String[] args) {
		
		// Création du modele
		Model model = new Model("nReine");
		
		
		// Création des variables
		int nbReine = 16;
		int nbCases = 256;
		IntVar [] reines = model.intVarArray("Ri", nbReine, 1, nbCases);	//on considere ici les reines placer de la case 1 � la 25eme
		
		
        
        /************************************************************************
         *                                                                      *
         *    Compléter en ajoutant les contraintes modélisant les contraintes  *
         *                                                                      *
         ************************************************************************/

		
		//Contraintes d'�galit� :
		model.allDifferent(new IntVar[]{reines[0],reines[1],reines[2],reines[3],reines[4],reines[5],
										reines[6],reines[7],reines[8],reines[9],reines[10],reines[11],
										reines[12],reines[13],reines[14],reines[15]}).post();
		
		//Contraintes pour que Ri soit � la ligne i
		model.arithm(reines[0], ">=", 1).post();
		model.arithm(reines[0], "<=", 16).post();
		
		model.arithm(reines[1], ">=", 17).post();
		model.arithm(reines[1], "<=", 32).post();
		
		model.arithm(reines[2], ">=", 33).post();
		model.arithm(reines[2], "<=", 48).post();
		
		model.arithm(reines[3], ">=", 49).post();
		model.arithm(reines[3], "<=", 64).post();
		
		model.arithm(reines[4], ">=", 65).post();
		model.arithm(reines[4], "<=", 80).post();
		
		model.arithm(reines[5], ">=", 81).post();
		model.arithm(reines[5], "<=", 96).post();
		
		model.arithm(reines[6], ">=", 97).post();
		model.arithm(reines[6], "<=", 112).post();
		
		model.arithm(reines[7], ">=", 113).post();
		model.arithm(reines[7], "<=", 128).post();
		
		model.arithm(reines[8], ">=", 129).post();
		model.arithm(reines[8], "<=", 144).post();
		
		model.arithm(reines[9], ">=", 145).post();
		model.arithm(reines[9], "<=", 160).post();
		
		model.arithm(reines[10], ">=", 161).post();
		model.arithm(reines[10], "<=", 176).post();
		
		model.arithm(reines[11], ">=", 177).post();
		model.arithm(reines[11], "<=", 192).post();
		
		model.arithm(reines[12], ">=", 193).post();
		model.arithm(reines[12], "<=", 208).post();
		
		model.arithm(reines[13], ">=", 209).post();
		model.arithm(reines[13], "<=", 224).post();
		
		model.arithm(reines[14], ">=", 225).post();
		model.arithm(reines[14], "<=", 240).post();
		
		model.arithm(reines[15], ">=", 241).post();
		model.arithm(reines[15], "<=", 256).post();
		
		
		
		
		
		
		//Contraintes de colonnes
		for (int i=0; i<16; i++) {
			for (int j=i+1; j<16; j++) {
				for (int k=16; k<21; k=k+16) {
					model.arithm(reines[j], "!=", reines[i], "+", k).post();
				}
			}
		}
		
		
		
		//contraintes diago
		//a droite
		for (int i=0; i<16; i++) {
			int k=17;
			for (int j=i+1; j<16; j++) {
				model.arithm(reines[j], "!=", reines[i], "+", k).post();
				k+=17;
			}
		}
		//a gauche
		for (int i=0; i<16; i++) {
			int k=15;
			for (int j=i+1; j<16; j++) {
				model.arithm(reines[j], "!=", reines[i], "+", k).post();
				k+=15;
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

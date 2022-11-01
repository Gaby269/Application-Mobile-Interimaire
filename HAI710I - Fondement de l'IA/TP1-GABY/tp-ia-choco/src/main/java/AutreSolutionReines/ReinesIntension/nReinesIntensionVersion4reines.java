package AutreSolutionReines.ReinesIntension;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;
//import org.chocosolver.solver.constraints.extension.Tuples;


public class nReinesIntensionVersion4reines{

	public static void main(String[] args) {
		
		// Création du modele
		Model model = new Model("nReine");
		
		
		// Création des variables
		int nb_reine = 4;
		int nb_cases = 16;
		IntVar [] reines = model.intVarArray("Ri", nb_reine, 1, nb_cases);	//on considere ici les reines placer de la case 1 � la 25eme
		
		
        
        /************************************************************************
         *                                                                      *
         *    Compléter en ajoutant les contraintes modélisant les contraintes  *
         *                                                                      *
         ************************************************************************/

		
		//Contraintes d'�galit� :
		model.allDifferent(new IntVar[]{reines[0],reines[1],reines[2],reines[3]}).post();
		
		//Contraintes pour que Ri soit � la ligne i
		model.arithm(reines[0], ">=", 1).post();
		model.arithm(reines[0], "<=", 4).post();
		
		model.arithm(reines[1], ">=", 5).post();
		model.arithm(reines[1], "<=", 8).post();
		
		model.arithm(reines[2], ">=", 9).post();
		model.arithm(reines[2], "<=", 12).post();
		
		model.arithm(reines[3], ">=", 13).post();
		model.arithm(reines[3], "<=", 16).post();
		
		
		
		
		
		
		//Contraintes de colonnes
		for (int i=0; i<4; i++) {
			for (int j=i+1; j<4; j++) {
				for (int k=4; k<13; k=k+4) {
					model.arithm(reines[j], "!=", reines[i], "+", k).post();
				}
			}
		}
		
		
		
		//contraintes diago
		//a droite
		for (int i=0; i<4; i++) {
			int k=5;
			int t=3;
			for (int j=i+1; j<4; j++) {
				model.arithm(reines[j], "!=", reines[i], "+", k).post();
				model.arithm(reines[j], "!=", reines[i], "+", t).post();
				k+=5;
				t+=3;
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

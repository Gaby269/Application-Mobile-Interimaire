package BonneSolutionReines.ReinesIntension;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;
//import org.chocosolver.solver.constraints.extension.Tuples;


public class nReinesIntension5reines{

	public static void main(String[] args) {
		
		// Création du modele
		Model model = new Model("5Reine");
		
		
		// Création des variables
		int nbReine = 5;
		int nbCases = 5;
		//on considere ici les reines � la ligne i placer dans les colonnes numerot� de 1 � 5
		IntVar [] reines = model.intVarArray("Ri", nbReine, 1, nbCases);	
		
		
        
        /************************************************************************
         *                                                                      *
         *    Compléter en ajoutant les contraintes modélisant les contraintes  *
         *                                                                      *
         ************************************************************************/

		
		//Contraintes d'egalite : les reines ne doivent pas �tre sur la m�me colonne donc qu'elle soit �gal
		model.allDifferent(new IntVar[]{reines[0],reines[1],reines[2],reines[3],reines[4]}).post();
		
		
		
		//contraintes diago
		//a droite
		for (int i=0; i<5; i++) {
			for (int j=i+1; j<5; j++) {
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
    	System.out.println("\n\n*** Autres solutions ***");        
        while(model.getSolver().solve()) {    	
            System.out.println("Sol "+ model.getSolver().getSolutionCount()+"\n"+model);
	    }
	    
 
        
        // Affichage de l'ensemble des caractéristiques de résolution
      	System.out.println("\n\n*** Bilan ***");        
        model.getSolver().printStatistics();
	}
}

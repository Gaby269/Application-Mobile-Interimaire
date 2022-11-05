package BonneSolutionReines.ReinesExtension;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.constraints.extension.Tuples;


public class nReinesExtension {

	public static void main(String[] args) {
		
		// Creation du modele
		Model model = new Model("nReine");
		
		
		// Creation des variables
		int nbReine = 5;
		int nbColonnes = 5;
		//Ri a i comme ligne et peut se deplacer de 1 a� 5 en colonne 
		//(on suppose donc que la reine � la ligne i est obligatoirement � la ligne i, sans mettre de contrainte dessus)
		IntVar [] reines = model.intVarArray("Ri", nbReine, 1, nbColonnes);	
		
		
		//COLONNES : 
		
		// Cr�ation des contraintes egalites et meme ligne
        int [][] tEq = new int[][] {{1,1},{2,2},{3,3},{4,4},{5,5}};
        Tuples tuplesInterdits = new Tuples(tEq,false);		// création de Tuples de valeurs interdits
        
        
        //DIAGONALES : 
        
        //Pas de diago pour reine cote a cote
        int [][] tEqDiag = new int[][] {{1,2},{2,3},{3,4},{4,5},{2,1},{3,2},{4,3},{5,4}};
        Tuples tuplesInterditsDiag = new Tuples(tEqDiag,false);		// création de Tuples de valeurs interdits
        
        //Pas de diago pour reine d'interval 2
        int [][] tEqDiag2 = new int[][] {{1,3},{2,4},{3,5},{3,1},{4,2},{5,3}};
        Tuples tuplesInterditsDiag2 = new Tuples(tEqDiag2,false);		// création de Tuples de valeurs interdits
        
        //Pas de diago pour reine d'interval 3
        int [][] tEqDiag3 = new int[][] {{1,4},{2,5},{4,1},{5,2}};
        Tuples tuplesInterditsDiag3 = new Tuples(tEqDiag3,false);		// création de Tuples de valeurs interdits
        
        //Pas de diago pour reine d'interval 4
        int [][] tEqDiag4 = new int[][] {{1,5},{5,1}};
        Tuples tuplesInterditsDiag4 = new Tuples(tEqDiag4,false);		// création de Tuples de valeurs interdits
        
        
        
        /************************************
         *                                  *                                    
         *    Modelisation des contraintes  *
         *                                  *                                    
         ************************************/

        //COLONNES : 
        
        //Par de reines sur les memes colonnes
        for (int i=0; i<5; i++) {
        	for (int j=i+1; j<5; j++) {
        		model.table(new IntVar[]{reines[i],reines[j]}, tuplesInterdits).post();
        	}
        }
        
        
        //DIAGONALES : 
        
        //Pas de diago pour les proches
        model.table(new IntVar[]{reines[0],reines[1]}, tuplesInterditsDiag).post();
        model.table(new IntVar[]{reines[1],reines[2]}, tuplesInterditsDiag).post();
        model.table(new IntVar[]{reines[2],reines[3]}, tuplesInterditsDiag).post();
        model.table(new IntVar[]{reines[3],reines[4]}, tuplesInterditsDiag).post();
        
        //Pas de diago avec un interval de 2
        model.table(new IntVar[]{reines[0],reines[2]}, tuplesInterditsDiag2).post();
        model.table(new IntVar[]{reines[1],reines[3]}, tuplesInterditsDiag2).post();
        model.table(new IntVar[]{reines[2],reines[4]}, tuplesInterditsDiag2).post();
        
        //Pas de diago avec un interval de 3
        model.table(new IntVar[]{reines[0],reines[3]}, tuplesInterditsDiag3).post();
        model.table(new IntVar[]{reines[1],reines[4]}, tuplesInterditsDiag3).post();
        
      //Pas de diago avec un interval de 4
        model.table(new IntVar[]{reines[0],reines[4]}, tuplesInterditsDiag4).post();
        
        
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

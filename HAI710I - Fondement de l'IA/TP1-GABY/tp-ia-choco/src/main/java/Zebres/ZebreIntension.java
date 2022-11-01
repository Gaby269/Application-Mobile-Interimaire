package Zebres;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.constraints.extension.Tuples;

public class ZebreIntension {

	public static void main(String[] args) {
		
		// Cr�ation du modele
		Model model = new Model("Zebre");
		
		
		// Cr�ation des variables
		IntVar blu = model.intVar("Blue", 1, 5);	// blu est une variable enti�re dont le nom est "Blue" est le domaine [1,5]
		IntVar gre = model.intVar("Green", 1, 5); 
		IntVar ivo = model.intVar("Ivory", 1, 5);         
		IntVar red = model.intVar("Red", 1, 5);         
		IntVar yel = model.intVar("Yellow", 1, 5);   
		
		IntVar eng = model.intVar("English", 1, 5);         
		IntVar jap = model.intVar("Japanese", 1, 5);    
		IntVar nor = model.intVar("Norwegian", 1, 5);         
		IntVar spa = model.intVar("Spanish", 1, 5);         
		IntVar ukr = model.intVar("Ukrainian", 1, 5);         
		
		IntVar cof = model.intVar("Coffee", 1, 5); 
		IntVar mil = model.intVar("Milk", 1, 5);        
		IntVar ora = model.intVar("Orange Juice", 1, 5);         
		IntVar tea = model.intVar("Tea", 1, 5);         
		IntVar wat = model.intVar("Water", 1, 5);         
		
	    IntVar dog = model.intVar("Dog", 1, 5);         
	    IntVar fox = model.intVar("Fox", 1, 5);         
	    IntVar hor = model.intVar("Horse", 1, 5);         
	    IntVar sna = model.intVar("Snail", 1, 5);         
	    IntVar zeb = model.intVar("Zebra", 1, 5);         
	    
	    IntVar che = model.intVar("Chesterfield", 1, 5);         
	    IntVar koo = model.intVar("Kool", 1, 5);         
	    IntVar luc = model.intVar("Lucky Strike", 1, 5);         
	    IntVar old = model.intVar("Old Gold", 1, 5);         
	    IntVar par = model.intVar("Parliament", 1, 5);         

	    

	    //Contraintes d'�galit� :
	    
        model.allDifferent(new IntVar[]{blu, gre, ivo, red, yel}).post();
        model.allDifferent(new IntVar[]{eng, jap, nor, spa, ukr}).post();
        model.allDifferent(new IntVar[]{cof, mil, ora, tea, wat}).post();
        model.allDifferent(new IntVar[]{dog, fox, hor, sna, zeb}).post();
        model.allDifferent(new IntVar[]{che, koo, luc, old, par}).post();
        
        /************************************************************************
         *                                                                      *
         * Compl�ter en ajoutant les contraintes mod�lisant les phrases 2 � 15  *
         *                                                                      *
         ************************************************************************/
        
        //PHRASE 2 : anglais vie dans la maison rouge
        model.arithm(eng, "=", red).post();
        
        //PHRASE 3 : espagnol a le chien
        model.arithm(spa, "=", dog).post();
        
        //PHRASE 4 : caf� dans la maison verte
        model.arithm(cof, "=", gre).post();
        
        //PHRASE 5 : ukranien boit du tea
        model.arithm(ukr, "=", tea).post();
        
        //PHRASE 6 : maison verte juste � la droite de la maison ivoirienne
        model.arithm(gre, "=", ivo, "+", 1).post();
        
        
        //PHRASE 7 : le old gold a le snails
        model.arithm(old, "=", sna).post();
        
        //PHRASE 8 : kools smocke dans maison jaune
        model.arithm(koo, "=", yel).post();
        
        //PHRASE 9 : lait dans la maison du milieu
        model.arithm(mil, "=", 3).post();//modif le domaine
        
        //PHRASE 10 : le norvegien fit dans la premi�re maison
        model.arithm(nor, "=", 1).post();//modif le domaine
        
        //PHRASE 11 : chesterfileds vie dans la maison apres ou avant la maison avec le fox
        model.distance(fox, che, "=", 1).post();
        
        //PHRASE 12 : kools smook dans la maison apres la ou ya le cheval
        model.distance(koo, hor, "=", 1).post();
        
        //PHRASE 13 : lucky strike boit du jus dorange
        model.arithm(luc, "=", ora).post();
        
        //PHRASE 14 : japon smocke parliaments
        model.arithm(jap, "=", par).post();
        
        //PHRASE 15 : norvegien vie � cot� la maison bleu
        model.distance(nor, blu, "=", 1).post();
        
        
        
        
        
        
        // Affichage du r�seau de contraintes cr��
        System.out.println("*** R�seau Initial ***");
        System.out.println(model);
        

        // Calcul de la premi�re solution
        if(model.getSolver().solve()) {
        	System.out.println("\n\n*** Premi�re solution ***");        
        	System.out.println(model);
        }

        
   
    	// Calcul de toutes les solutions
    	System.out.println("\n\n*** Autres solutions ***");        
        while(model.getSolver().solve()) {    	
            System.out.println("Sol "+ model.getSolver().getSolutionCount()+"\n"+model);
	    }
	    
 
        
        // Affichage de l'ensemble des caract�ristiques de r�solution
      	System.out.println("\n\n*** Bilan ***");        
        model.getSolver().printStatistics();
	}
}

// solveur
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.extension.Tuples;
import org.chocosolver.solver.variables.IntVar;
// Ecriture de fichiers
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
// time
 import java.util.Scanner; // Scanner pour les entrées

public class Expe {

		//code donnée
	private static Model lireReseau(BufferedReader in) throws Exception{
			Model model = new Model("Expe");
			// System.out.println(in.readLine());
			int nbVariables = Integer.parseInt(in.readLine());				// le nombre de variables
			int tailleDom = Integer.parseInt(in.readLine());				// la valeur max des domaines
			IntVar []var = model.intVarArray("x",nbVariables,0,tailleDom-1); 	
			int nbConstraints = Integer.parseInt(in.readLine());			// le nombre de contraintes binaires		
			for(int k=1;k<=nbConstraints;k++) { 
				String chaine[] = in.readLine().split(";");
				IntVar portee[] = new IntVar[]{var[Integer.parseInt(chaine[0])],var[Integer.parseInt(chaine[1])]}; 
				int nbTuples = Integer.parseInt(in.readLine());				// le nombre de tuples		
				Tuples tuples = new Tuples(new int[][]{},true);
				for(int nb=1;nb<=nbTuples;nb++) { 
					chaine = in.readLine().split(";");
					int t[] = new int[]{Integer.parseInt(chaine[0]), Integer.parseInt(chaine[1])};
					tuples.add(t);
				}
				model.table(portee,tuples).post();	
			}
			in.readLine();
			return model;
	}	
		
			//ajout
	public static void main(String[] args) throws Exception{
		int nbRes = 10;								// nombre de reseau fixer pcq pas pu recup
		int tailleDom = 17;							// on fixe le domaine pour ne pas ouvrir le fichier
	    FileWriter fichier_resultats; 				// fichier qu'on va ecrire les reusltat
	    
		//Donner le nom du fichier
		System.out.println("Veuillez entrer le nom du benchmark dans le dossier /reseaux :");	//je demande a ce qu'on entre une solution
		Scanner clavier = new Scanner(System.in);												//creation du scanneur
		String bench = clavier.nextLine();
		clavier.close();
		
	    fichier_resultats = new FileWriter("../resultats/result_"+bench+".csv",false); 	// écriture dans le fichier .CSV en ecrasant le fichier d'avant
		fichier_resultats.write("Durete;% solutions;temps moyen (s)\n"); 				// en-tête du fichier
		
		// parsing des réseaux
		String path = "../reseaux/"+bench+"/";			// ouverture du fichier
		File reseaux = new File(path); 					// fichiers des reseaux
		File listeFichiers[] = reseaux.listFiles(); 	// liste des fichiers
		int nbDiff = listeFichiers.length; 				// nb de fichiers
		
		//BOUCLE DES FICHIERS	nommé csp_nb.txt
		for (int i=0; i<nbDiff; i++) { 					// pour chaque fichier
			String fic = listeFichiers[i].getName();
			BufferedReader readFile = new BufferedReader(new FileReader(path+fic));
			System.out.println("\n" + fic + " :\n");
			
			int nbSoluce = 0; 		// nombre de reseaux avec au moins une solution
			int nbTO = 0;			// nombre de reseaux qui time out
			double tempsMoyen = 0;	// temps moyen d'avoir une solution ou non (sans time out)
			
			//BOUCLE RESEAU 	par fichier il y en a 10
			for(int nb=1 ; nb<=nbRes; nb++) { 			// pour chaque reseau
				Model model=lireReseau(readFile); 		// création du modèle
				Solver solver = model.getSolver(); 		// initialisation du solver
				solver.limitTime("30s"); 		// set du Time Out (TO) à 30s
				
				System.out.println("Résolution du réseau n°"+nb); 	// indication visuelle de l'avancée du benchmark
				long startTime = System.nanoTime();					// calcul de lheure de départ

				if (solver.solve()) { 							 	// si le modèle à au moins une solution
					tempsMoyen += System.nanoTime() - startTime; 	// calcul du temps d'exécution
					System.out.println("-> Solution trouvée :)\n");
					nbSoluce++;										// augmente le nombre de reseau qui a au moins une solution
				}
				else if (solver.isStopCriterionMet()){				// si le solver time out
					System.out.println("-> Time out !\n");		// affichage 
					nbTO++; 										// incrémentation du compteur de Timeouts
					//on n'ajoute pas le temps moyen car  on ne prend pas en compte les soler qui ont TO
				}
				else {												// sinon pas de solution donc pas d'affichage
					tempsMoyen += System.nanoTime() - startTime; 	// calcul du temps d'exécution
					System.out.println("-> Pas de solution :(\n");// affichage
				}
			}//fin reseau
			
			//ECRITURE DANS LE FICHIER
				//nombre de tuples
		int nbTuples = Integer.parseInt((fic.split("\\.")[0]).split("\\_")[1]);											 //recuperation du nb de tuples autorisés dans le titre du fichiers
				//Dureté
			double durete = (double)Math.round(100 * (double)(tailleDom*tailleDom - nbTuples) / (tailleDom*tailleDom)) / 100;			//dureté donnée dant le fichier (d²-t)/d²
				//Pourcentage
		double pourcentage = (nbTO==nbRes)? -1 : Math.round(100*nbSoluce/(nbRes-nbTO));													//calcul du pourcentage * si tout le monde a time out alors pas de pourcentage (-1) * sinon on recalcule le nombre de reseau qui ont pas timeout, on arondi à deux chiffres après la virgule
				//Temps moyen
			double tempsMoy = (nbTO==nbRes)? -1 : (double)Math.round(1000 * ((double)tempsMoyen/(nbRes-nbTO)) / 1000000000) / 1000;		//calcul du temps moyen de trouver une solution ou non en seconde, on arondi à trois chiffres après la virgule
				//Ecriture
			fichier_resultats.write(durete + ";" + pourcentage + "%;" + tempsMoy + "\n");												//ecriture dans le fichier

			// après on pourra tracer la courbe dans Excel pour obtenir la courbe du pourcentage de solution en fonction de la dureté et le temps moyen pour trouver s'il y a ou non une solutions
			
		} // fin du parsing de tous les fichiers
		
		fichier_resultats.close(); // fermeture du FileWriter
		
		return;	
	}
	
}

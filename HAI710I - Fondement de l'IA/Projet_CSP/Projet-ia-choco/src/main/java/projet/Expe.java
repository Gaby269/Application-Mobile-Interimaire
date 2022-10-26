package projet;

import java.io.BufferedReader;
import java.io.FileReader;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.extension.Tuples;
import org.chocosolver.solver.variables.IntVar;

public class Expe {

	private static Model lireReseau(BufferedReader in) throws Exception{
			Model model = new Model("Expe");
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
		
			
	public static void main(String[] args) throws Exception{
		String ficName1 = "bench.txt";
		String ficName2 = "benchSatisf.txt";
		String ficName3 = "benchInsat.txt";
		int nbRes=3;
		BufferedReader readFile1 = new BufferedReader(new FileReader(ficName1));
		BufferedReader readFile2 = new BufferedReader(new FileReader(ficName2));
		BufferedReader readFile3 = new BufferedReader(new FileReader(ficName3));
		for(int nb=1 ; nb<=nbRes; nb++) {
			Model model1=lireReseau(readFile1);
			if(model1==null) {
				System.out.println("Problème de lecture de fichier 1 !\n");
				return;
			}
			System.out.println("Réseau lu "+nb+" du fichier 1 :\n"+model1+"\n\n");
			
			Model model2=lireReseau(readFile2);
			if(model2==null) {
				System.out.println("Problème de lecture de fichier 2 !\n");
				return;
			}
			System.out.println("Réseau lu "+nb+" du fichier 2 :\n"+model2+"\n\n");
			
			Model model3=lireReseau(readFile3);
			if(model3==null) {
				System.out.println("Problème de lecture de fichier 3 !\n");
				return;
			}
			System.out.println("Réseau lu "+nb+" du fichier 3 :\n"+model3+"\n\n");
		}
		return;	
	}
	
}

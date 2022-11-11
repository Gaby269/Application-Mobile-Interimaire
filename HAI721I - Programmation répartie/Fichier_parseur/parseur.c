#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define TAILLE_MAX 1000

int main(int argc, char *argv[]) {
  
  //GESTION DES DONN2ES
  if (argc != 2) {
    printf("\n[UTILISATION] : ./parseur chemin_fichier\n\n");
    exit(1);
  }

  char *nom_fichier = argv[1];
  FILE *fichier;

  // OUVERTURE EN LECTURE
  fichier = fopen(nom_fichier, "r");
  if (fichier == NULL) {
    printf("\nImpossible d'ouvrir le fichier '%s'\n", nom_fichier);
    exit(2);
  }

  
  //DONNEES
  char ligne[TAILLE_MAX];
  int nb_sommets = 0;
  int nb_aretes = 0;


  
  // PREMIERE LECTURE
  printf("\nPREMIERE LECTURE DU FICHIER !\n");

  int i;
  while (fgets(ligne, TAILLE_MAX, fichier) != NULL) {
    // Si la ligne commence par "c" c'est un commentaire on l'ignore
    if (ligne[0] == 'c') {
      continue;
    }
    // Si la ligne commence par "p" c'est la ligne qui contient le nombre de
    // sommets et de aretes
    if (ligne[0] == 'p') {
      i = 0;
      char *tmp = strtok(ligne, " ");
      while (tmp != NULL) {
        if (i == 2) {
          nb_sommets = atoi(tmp);
        }
        if (i == 3) {
          nb_aretes = atoi(tmp);
        }
        tmp = strtok(NULL, " ");
        i++;
      }
      i = 0;
      continue;
    }
  }
  // on crée donc le tableu d'aretes
  int aretes[nb_aretes][2];
  // AFFICHAGE
  printf("\nNombre de sommets : %d\n", nb_sommets);
  printf("Nombre d'aretes : %d\n\n", nb_aretes);

  
  // RECOMMENCE
  rewind(fichier);      //relecture du fichier au début

  
  // RELECTURE DU FICHIER
  printf("\nDEUXIEME LECTURE DU FICHIER !");
  while (fgets(ligne, TAILLE_MAX, fichier) != NULL) {
    // Si la ligne commence par "c" c'est un commentaire on l'ignore
    if (ligne[0] == 'c') {
      continue;
    }
    // Si la ligne commence par "p" on continue car on la traité avant
    if (ligne[0] == 'p') {
      continue;
    }

    // Si la ligne commence par "e" on récupère les aretes
    if (ligne[0] == 'e') {
      char *tmp = strtok(ligne, " ");     //recuperation de la ligne
      int j = 0;                          //indice pour savoir au quel mot on est
      while (tmp != NULL) {              //tant que la ligne n'est pas fini
        if (j == 1) {                    //si je suis au deuxieme mot 
          aretes[i][0] = atoi(tmp);      //je recupere le noeud
        }
        if (j == 2) {                    //si je suis au deuxieme mot
          aretes[i][1] = atoi(tmp);      //je recupere le noeud
        }
        //printf("tmp: %s\n", tmp);        //affichage du mot
        tmp = strtok(NULL, " ");          //rend null le mot courant
        j++;                            //on incremente pour apsser au mot suivant
      }
      i++;                              //indice pour le tableau d'arete
      continue;
    }
  }

  //AFFICHAGE
  printf("\n\nListe des arretes :\n\n");

  // AFFICHAGE DES ARRETES
  for(int k=0; k<nb_aretes; k++){
    printf("%d -> %d\n", aretes[k][0], aretes[k][1]);
  }

  // FERMETURE DU FICHIER
  fclose(fichier);
  return 0;
}
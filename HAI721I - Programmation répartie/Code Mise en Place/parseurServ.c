#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define TAILLE_MAX 1000

//CREATION D'une STRUCTURE DES ARRETS
struct aretes{
  int noeud1;
  int noeud2;
};

//CREATION STRUCTURE POUR LES NOMBRE IMPORTANTS
struct info_nb{
  int nb_sommets;
  int nb_aretes;
};


//FONCTION QUI RECUPER LA S>TrUCUTRE DES NOMBRE
struct info_nb nbAreteNbNoeud(char * nom_fichier){

  FILE * fichier;
  
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
  struct info_nb info_nb;

  //LECTURE
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

  fclose(fichier);        //Fermeture du fichier

  //donnée a mettre dans le tableau
  info_nb.nb_sommets = nb_sommets;
  info_nb.nb_aretes = nb_aretes;

  return info_nb;
  
}


void Arretes (char * nom_fichier, int nb_sommets, int nb_aretes, struct aretes * aretes){

  FILE * fichier;
  
  // OUVERTURE EN LECTURE
  fichier = fopen(nom_fichier, "r");
  if (fichier == NULL) {
    printf("\nImpossible d'ouvrir le fichier '%s'\n", nom_fichier);
    exit(2);
  }

  //DONNEES
  char ligne[TAILLE_MAX];
  
  
  int i;
  
  while (fgets(ligne, TAILLE_MAX, fichier) != NULL) {
    // Si la ligne commence par "c" c'est un commentaire on l'ignore
    if (ligne[0] == 'c') {
      continue;
    }
    // Si la ligne commence par "p" on continue car on la traité avant
    if (ligne[0] == 'p') {
      i=0;
      continue;
    }

    // Si la ligne commence par "e" on récupère les aretes
    if (ligne[0] == 'e') {
      char *tmp = strtok(ligne, " ");     //recuperation de la ligne
      int j = 0;                          //indice pour savoir au quel mot on est
      while (tmp != NULL) {              //tant que la ligne n'est pas fini
        if (j == 1) {                    //si je suis au deuxieme mot 
          aretes[i].noeud1 = atoi(tmp);  //donne les info du noeuds 1
          
        }
        if (j == 2) {                    //si je suis au deuxieme mot
          aretes[i].noeud2 = atoi(tmp);            //je recupere le noeud2
        }
        //printf("tmp: %s\n", tmp);        //affichage du mot
        tmp = strtok(NULL, " ");          //rend null le mot courant
        //printf("\n- tmp = %s ", tmp);
        j++;                            //on incremente pour apsser au mot suivant
      }
      i++;                              //indice pour le tableau d'arete
      continue;
    }
  }
  
  
}
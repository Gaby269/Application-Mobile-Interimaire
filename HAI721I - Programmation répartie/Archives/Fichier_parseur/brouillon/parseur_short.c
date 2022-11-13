#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define TAILLE_MAX 1000

int main(int argc, char *argv[]) {
  if (argc != 2) {
    printf("\n[UTILISATION] : ./parseur chemin_fichier\n");
    exit(1);
  }

  char *nom_fichier = argv[1];
  FILE *fichier;

  // Ouverture en lecture
  fichier = fopen(nom_fichier, "r");
  if (fichier == NULL) {
    printf("\nImpossible d'ouvrir le fichier '%s'\n", nom_fichier);
    exit(2);
  }

  char ligne[TAILLE_MAX];
  int nb_sommets = 0;
  int nb_aretes = 0;

  // On lit chaque ligne du fichier
  while (fgets(ligne, TAILLE_MAX, fichier) != NULL) {
    // Si la ligne commence par "c" c'est un commentaire on l'ignore
    if (ligne[0] == 'c') {
      continue;
    }
    // Si la ligne commence par "p" c'est la ligne qui contient le nombre de
    // sommets et de aretes
    if (ligne[0] == 'p') {
      int i = 0;
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
      break;
    }
  } // on a les bonnes valeurs du nb de noeud et arêtes
  
  int i = 0;
  int aretes[nb_aretes][2];
  while (fgets(ligne, TAILLE_MAX, fichier) != NULL) {
    // Si la ligne commence par "e" on récupère les aretes
    if (ligne[0] == 'e') {
      char *tmp = strtok(ligne, " ");
      int j = 0;
      int tmpNb1 = 0;
      int tmpNb2 = 0;
      while (tmp != NULL) {
        if (j == 1) {
          aretes[i][0] = atoi(tmp);
        }
        if (j == 2) {
          aretes[i][1] = atoi(tmp);
        }
        printf("tmp: %s\n", tmp);
        tmp = strtok(NULL, " ");
        j++;
      }
      i++;
      continue;
    }
  }
  printf("\nNombre de sommets : %d\n", nb_sommets);
  printf("Nombre d'aretes : %d\n", nb_aretes);
  printf("Liste des aretes :\n");

  // On affiche toutes les aretes
  int k = 0;
  while (k < nb_aretes) {
    printf("%d -> %d\n", aretes[k][0], aretes[k][1]);
    k++;
  }

  // On ferme le fichier
  fclose(fichier);
  return 0;
}
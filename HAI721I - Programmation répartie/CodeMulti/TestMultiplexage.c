#include <stdio.h>
#include <stdlib.h>

#define MAX_VERTICES 100
#define MAX_COLORS 5

int numVertices;
int colors[MAX_VERTICES];
int graph[MAX_VERTICES][MAX_VERTICES];


int* getNeighborColors(int vertex) {
  static int neighborColors[MAX_VERTICES];
  int numNeighbors = 0;
  for (int i = 0; i < numVertices; i++) {
    if (graph[vertex][i]) {
      neighborColors[numNeighbors++] = colors[i];
    }
  }
  return neighborColors;
}

int main() {
  // Initialize the graph and colors array
  // ...

  while (1) {
    int changes = 0;
    for (int i = 0; i < numVertices; i++) {
      int* neighborColors = getNeighborColors(i);
      int newColor;
      for (newColor = 1; newColor <= MAX_COLORS; newColor++) {
        int found = 0;
        for (int j = 0; j < MAX_VERTICES; j++) {
          if (neighborColors[j] == newColor) {
            found = 1;
            break;
          }
        }
        if (!found) {
          break;
        }
      }
      if (colors[i] != newColor) {
        colors[i] = newColor;
      }
      if (changes == 0) {
        break;
      }
    }
  }
  return 0;
}


/*
Pour chaque sommet du graphe, nous utilisons la fonction de multiplexage "getNeighborColors" pour obtenir les couleurs de ses voisins. Nous parcourons ensuite les couleurs possibles et sélectionnons la première couleur qui n'est pas utilisée par les voisins. Si aucune couleur n'est disponible, nous utilisons la couleur la plus proche de celle du sommet actuel qui n'est pas utilisée par ses voisins.

Enfin, nous mettons à jour le tableau de couleurs pour refléter la nouvelle couleur du sommet actuel et comptons le nombre de changements de couleur effectués lors de cette itération. Si aucun changement n'a été effectué, cela signifie que tous les sommets ont une couleur unique et nous pouvons arrêter l'algorithme. Sinon, nous répétons les étapes 2 et 3 jusqu'à ce que tous les sommets aient une couleur unique.*/
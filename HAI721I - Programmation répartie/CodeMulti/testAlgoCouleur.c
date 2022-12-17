#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>

#define MAX_VERTICES 100
#define MAX_COLORS 100

int num_vertices;
int num_edges;

int graph[MAX_VERTICES][MAX_VERTICES];
int colors[MAX_VERTICES];

int my_vertex;
int my_color;

void send_color(int vertex, int color) {
  int sockfd;
  struct sockaddr_in addr;
  sockfd = socket(AF_INET, SOCK_STREAM, 0);
  memset(&addr, 0, sizeof(addr));
  addr.sin_family = AF_INET;
  addr.sin_addr.s_addr = inet_addr("127.0.0.1");
  addr.sin_port = htons(vertex);
  connect(sockfd, (struct sockaddr *)&addr, sizeof(addr));
  write(sockfd, &color, sizeof(color));
  close(sockfd);
}

void receive_color(int *vertex, int *color) {
  int sockfd;
  struct sockaddr_in addr;
  socklen_t addrlen = sizeof(addr);
  sockfd = socket(AF_INET, SOCK_STREAM, 0);
  memset(&addr, 0, sizeof(addr));
  addr.sin_family = AF_INET;
  addr.sin_addr.s_addr = INADDR_ANY;
  addr.sin_port = htons(my_vertex);
  bind(sockfd, (struct sockaddr *)&addr, sizeof(addr));
  listen(sockfd, 5);
  int clientfd = accept(sockfd, (struct sockaddr *)&addr, &addrlen);
  *vertex = ntohs(addr.sin_port);
  read(clientfd, color, sizeof(*color));
  close(clientfd);
  close(sockfd);
}


int has_color(int vertex, int color) {
  int i;
  for (i = 0; i < num_vertices; i++)
    if (graph[vertex][i] && colors[i] == color)
      return 1;
  return 0;
}

void choose_color() {
  int i;
  int old_color = my_color;
  for (i = 1; i <= num_vertices; i++) {
    if (!has_color(my_vertex, i)) {
      my_color = i;
      return;
    }
  }
  if (old_color != my_color) {
    // Envoyez votre nouvelle couleur à tous vos voisins
    for (i = 0; i < num_vertices; i++) {
      if (graph[my_vertex][i]) {
        send_color(i, my_color);
      }
    }
  }
}


int main() {
  // Lisez les informations sur votre sommet et votre voisinage
  // ...

  // Choisissez une couleur pour ce sommet
  choose_color();

  // Envoyez votre couleur à tous vos voisins
  int i;
  for (i = 0; i < num_vertices; i++) {
    if (graph[my_vertex][i]) {
      send_color(i, my_color);
    }
  }

  // Recevez les couleurs de vos voisins et mettez à jour votre tableau de couleurs
  int vertex, color;
  for (i = 0; i < num_vertices; i++) {
    if (graph[my_vertex][i]) {
      receive_color(&vertex, &color);
      colors[vertex] = color;
    }
  }

  // Choisissez à nouveau une couleur pour votre sommet en prenant en compte les couleurs de vos voisins
  choose_color();

  printf("Vertex %d has color %d\n", my_vertex, my_color);

  return 0;
}

/* 
Ce code utilise les sockets TCP pour envoyer et recevoir les couleurs. Chaque processus écoute sur un port unique correspondant à son numéro de sommet, et se connecte aux ports de ses voisins pour envoyer ses couleurs.

Une fois que tous les processus ont échangé leurs couleurs avec leurs voisins, chaque processus peut choisir à nouveau une couleur optimale en prenant en compte les couleurs de ses voisins, comme indiqué dans la fonction "choose_color".

Il est important de noter que cette approche nécessite que chaque processus connaisse ses voisins et leurs numéros de sommet, et qu'elle nécessite également l'utilisation de sockets pour la communication entre les processus. Elle peut être utilisée lorsque chaque processus ne connaît pas toutes les arêtes du graphe, mais connaît seulement ses voisins directs.
*/
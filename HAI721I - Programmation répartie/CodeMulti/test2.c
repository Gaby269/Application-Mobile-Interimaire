#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <string.h>

#define MAX_NEIGHBORS 10
#define BUFFER_SIZE 1024

int main(int argc, char *argv[]) {
    int sockfd, newsockfd, portno;
    socklen_t clilen;
    char buffer[BUFFER_SIZE];
    struct sockaddr_in serv_addr, cli_addr;
    int n;
    int num_neighbors;
    int neighbor_fds[MAX_NEIGHBORS];

    // Vérifie que le programme a été appelé avec le bon nombre d'arguments
    if (argc < 2) {
        fprintf(stderr, "Usage: %s port\n", argv[0]);
        exit(1);
    }

    // Crée un socket
    sockfd = socket(AF_INET, SOCK_STREAM, 0);
    if (sockfd < 0) {
        perror("ERROR opening socket");
        exit(1);
    }

    // Initialise l'adresse du serveur
    bzero((char *) &serv_addr, sizeof(serv_addr));
    portno = atoi(argv[1]);
    serv_addr.sin_family = AF_INET;
    serv_addr.sin_addr.s_addr = INADDR_ANY;
    serv_addr.sin_port = htons(portno);

    // Lie le socket à l'adresse du serveur
    if (bind(sockfd, (struct sockaddr *) &serv_addr, sizeof(serv_addr)) < 0) {
        perror("ERROR on binding");
        exit(1);
    }

    // Met le socket en écoute pour les connexions entrantes
    listen(sockfd, 5);
    clilen = sizeof(cli_addr);

    // Accepte une connexion entrante et crée un nouveau socket pour la communication
    newsockfd = accept(sockfd, (struct sockaddr *) &cli_addr, &clilen);
    if (newsockfd < 0) {
        perror("ERROR on accept");
        exit(1);
    }

    // Reçoit le nombre de voisins du serveur
    bzero(buffer, BUFFER_SIZE);
    n = read(newsockfd, buffer, BUFFER_SIZE);
    if (n < 0) {
        perror("ERROR reading from socket");
        exit(1);
    }
    num_neighbors = atoi(buffer);

    // Reçoit les descripteurs de fichier des voisins à qui le noeud doit se connecter
    for (int i = 0; i < num_neighbors; i++) {
        bzero(buffer, BUFFER_SIZE);
        n = read(newsockfd, buffer, BUFFER_SIZE);
        if (n < 0) {
            perror("ERROR reading from socket");
            exit(1);
        }
        neighbor_fds[i] = atoi(buffer);
    }

    // Se connecte aux voisins à qui le noeud doit se connecter
    for (int i = 0; i < num_neighbors; i++) {
        int neighbor_fd = neighbor_fds[i];
        if (connect(neighbor_fd, (struct sockaddr *) &cli_addr, sizeof(cli_addr)) < 0) {
            perror("ERROR connecting to neighbor");
            exit(1);
        }
    }

    // Met le socket en écoute pour les connexions entrantes des voisins
    listen(sockfd, 5);

    // Attend les connexions entrantes des voisins
    while (1) {
        newsockfd = accept(sockfd, (struct sockaddr *) &cli_addr, &clilen);
        if (newsockfd < 0) {
            perror("ERROR on accept");
            exit(1);
        }
    }

    return 0;
}
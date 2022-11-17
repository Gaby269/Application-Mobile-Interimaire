#include <stdio.h> 
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netdb.h>
#include <stdlib.h>
#include <arpa/inet.h>
#include <string.h>
#include <pthread.h>


#define ERREUR -1
#define FERMETURE 0
#define THREAD 0

///////////////////////
/* Programme serveur */
///////////////////////

//////////////////////
// FONCTION SENDTCP //
//////////////////////

int sendTCP (int sock, void* msg, int sizeMsg){

   //VARIABLE : 
   int rest_octet = sizeMsg; //taille du message en octet
   
   while(rest_octet != 0){ //tant quil me reste des octets a recevoir

      int res = send(sock, msg + (sizeMsg - rest_octet), rest_octet, 0); //je tente d'envoyer mon message

      //GESTION ERREUR
      if (res <= 0){ 
         return res; 
      } 
      rest_octet = rest_octet - res;
      
   }
   return sizeMsg;
}


//////////////////////
// FONCTION RECVTCP //
//////////////////////

int recvTCP (int sock, void* msg, int sizeMsg){
      //VARIABLE : 
   int rest_octet = sizeMsg; //taille du message en octet
   
   while(rest_octet != 0){ //tant quil me reste des octets a recevoir

      int res = recv(sock, msg + (sizeMsg - rest_octet), rest_octet, 0); //je tente de recevoir un message

      //GESTION ERREUR
      if (res <= 0){ 
         return res; 
      } 

      rest_octet = rest_octet - res;
      
   }
   return sizeMsg;
}




//////////////////////////
// PARAMETRES DU THREAD //
//////////////////////////

struct paramsFT {

  // structure pour regrouper les param�tres d'un thread. 
  int idConnexion;                  // un identifiant de connexion au serveur, de 1 � N (N le nombre total de theads secondaires
  int sockClientPort;               //  stocke le port du client associé au thread
  struct in_addr sockClientAdr;     // stocke l'adresse du client associé au thread

};


//////////////////////////////////////
// FONCTION ASSOCIE A CHAQUE THREAD //
//////////////////////////////////////

void * client (void * p){     //un thread représente un client

  //GESTION PARAMETRE DE LA STRUTURE
  struct paramsFT * args = (struct paramsFT *) p;
  pthread_t threadCourant = pthread_self();

  //AFFICHAGE DU CLIENT PAR LE THREAD QUIL UTILISE ET SES INFORMATIONS
  printf("[THREAD] : %lu, processus %d\n",threadCourant,getpid());
  printf("[SERVEUR] Le client connecté est %s:%i.\n", inet_ntoa(args->sockClientAdr), ntohs(args->sockClientPort));    


  //PREMIER TRAVAIL : reception par le serveur des messages envoyé par le client

    //RECEPTION TAILLE MESSAGE
      int messageTaille=0;                        //taille du message
      int res_taille_message = recv(args->idConnexion, &messageTaille, sizeof(int), 0);  //recevoir le message de la taille

      //GESTION DES ERREURS
      if (res_taille_message == ERREUR) {
        perror("\n[SERVEUR] : Erreur lors de la reception de la taille du message ");
      }
      else{ 
        if (res_taille_message == FERMETURE) {
          perror("\n[SERVEUR] : Abandon de la socket cliente 1 ");
        }
        else{ //siil n'y a aucun problème 

          //SUCCES
          printf("\n\n[SERVEUR] : J'ai recu la taille, elle est de %i octets", messageTaille);
          
    //FIN RECEPTION TAILLE MESSAGE

    //RECEPTION DU MESSAGE COMPLET

          char messageClient[4000];              //declare le message qui va etre stocké

          //RECEVOIR LE MESSAGE
          int res_messageRecu = recv(args->idConnexion, messageClient, messageTaille, 0); //je recoit jusqua la taille du message le message stocker à l'adresse messageClient

          //GESTION DES ERREURS/FERMETURE
          if (res_messageRecu == ERREUR) {
            perror("\n[SERVEUR] : Erreur lors de la reception du messsage ");
            pthread_exit(NULL);
          }
          else{ 
            if (res_messageRecu == FERMETURE) {
              perror("\n[SERVEUR] : Abandon de la socket serveur 2 ");
              pthread_exit(NULL);
            }
            else{ 
              
              //SUCCES
              printf("\n[SERVEUR] : Reception du message réussi !");
            }
          } 
            
    //FIN RECPETION MESSAGE

        }
    //FIN ETAPE5 : RECEPTION

      } 
  pthread_exit(NULL); // fortement recommand�.

}



// MAIN

int main(int argc, char *argv[]) {

  //ETAPE 0 : GESTION DES PARAMETRES

    if (argc != 3){
        printf("[UTILISATION] : %s port_serveur nbClient\n", argv[0]);
        exit(1);
    }

    char * port_serveur = argv[1];              //je stocke le port du serveur
    //int nbClient = atoi(argv[2]);             //je stocke le nombre de client qu'on veut

  //FIN DE GESTION PARAM



  /* ETAPE 1 : CREATION DE LA SOCKET EN TCP */  

    int dSServeur = socket(PF_INET, SOCK_STREAM, 0); //SOCK_STREAM pour le protocole TCP

      //GESTION ERREUR
      if (dSServeur == ERREUR){
          perror("\n[SERVEUR] : problème à la creation socket :");
          exit(2); 
      }

    //SUCCES
    printf("\n[SERVEUR] : Creation de la socket réussie");
   
  //FIN DE LA CREATION DE AL SOCKET





  /* ETAPE 2 : NOMMER SOCKET SERVEUR */

    struct sockaddr_in adrServeur ;
    adrServeur.sin_family = AF_INET ;                     //IPv4 : famille AF_INET
    adrServeur.sin_addr.s_addr = INADDR_ANY;              //Attache la socket àtoutes les interfaces réseaux locales : toutes les adresses de la station
    adrServeur.sin_port = htons((atoi(port_serveur))) ;   // on doit convertir la chaine de caractère en nombre


    int res_bind = bind(dSServeur,                        // descripteur de socket
                    (struct sockaddr*)&adrServeur,         // pointeur vers l'adresse
                    sizeof(adrServeur)) ;                  // longueur de l'adresse

        //GESTION ERREUR
        if (res_bind == ERREUR) {
          perror("[SERVEUR] : Erreur lors du nommage de la socket : ");
          close(dSServeur);
          exit(3); // je choisis ici d'arrêter le programme car le reste
          // dependent du nommage meme si il peut le faire automatiquement.
        }

    //SUCCES
    printf("\n\n[SERVEUR] : Nommage réussi, adresse + port %i:%i", adrServeur.sin_addr.s_addr, adrServeur.sin_port);

  //FIN DU NOMMAGE



  /* ETAPE 3 : PASSER LA SOCKET EN ECOUTE */

    int nbmaxAttente = 10;                             //on doit avoir un nb max
    int res_listen = listen(dSServeur, nbmaxAttente);  //met en ecoute au max pour 10 clients

        //GESTION ERREUR
        if (res_listen == ERREUR) {
          perror("\n[SERVEUR] : Erreur lors de la mise en ecoute de la socket ");
          close(dSServeur);
          exit(4); // je choisis ici d'arrêter le programme car le reste
          // dependent du nommage meme si il peut le faire automatiquement.
        }

    //SUCCES
    printf("\n\n[SERVEUR] : Mise en écoute de la socket réussi, nombre maximal de demande de connexion : %i",  nbmaxAttente);


    //SUSPENDRE LEXE AVANT ACCEPT
        printf("\n\n[SERVEUR] : Entrez un caractère apres avoir analyser le comportement du client : ");  //on demmande au client de entrez un message
        fflush(stdout);
        fgetc( stdin );
    //FIN MISE EN ATTENTE

  //FIN DE LA MISE EN ECOUTE



    


  /* ETAPE 4 : ACCEPTER UNE DEMANDE DE CONNECTION */

    //VARAIBLES :
    char adresseIPClient[INET_ADDRSTRLEN];                    //stokage de l'adresse IP
    struct sockaddr_in adrClient;                             //adresse cliente 
    socklen_t lgAdr_Client = sizeof(struct sockaddr_in);      //sa taille
    pthread_t threads[3];
    int i=0;

    while (1){

      //ACCEPTATION

        int res_dSClient = accept(dSServeur, (struct sockaddr *) &adrClient, &lgAdr_Client); //nouvelle socket cliente

          //GESTION ERREUR
          if (res_dSClient == ERREUR) {
              perror("\n[SERVEUR] : Erreur lors de l'acceptation de connexion ");
              close(dSServeur);
              exit(5);          //si le client se deconnecte on peut continuer le programme alors il faudra faire un elese de tout ce qui a apres
          }

        //SUCCES
        inet_ntop(AF_INET, &adrClient.sin_addr, adresseIPClient, INET_ADDRSTRLEN);                                                //on donne a al varaible ladresse pour pouvoir la réutiliser en l'affichant
        printf("\n\n[SERVEUR] : Acceptation de la demande de connexion par le client réussi, son addresse %s", adresseIPClient); //affiche que c'est réussi

      //FIN ACCEPTATION
          

    //CREATION DU THREAD POUR LE CLIENT

      i++;  //on a un client en plus
      struct paramsFT * args2 = (struct paramsFT*)malloc(sizeof(struct paramsFT)); //aloue de la memoire pour les arguments
      args2->idConnexion = res_dSClient;                     //on donne lidée de connexion c'est a dire la socket du nouveau client
      args2->sockClientAdr = adrClient.sin_addr;                 //on lui donne son adresse et son port
      args2->sockClientPort = adrClient.sin_port;

      if (pthread_create(&threads[i], NULL, client, args2) != 0){  //on cree le thread pour le client
        perror("Erreur creation thread");
        exit(1);
      }

    //FIN CRERATION THREAD

  //FIN ETAPE 4 : ACCEPTER DEMANDE


    }//fin while


    
  close( dSServeur);
  printf("[SERVEUR] : C'est fini\n");
  
  return 0;
}

// Auteur : Gabrielle POINTEAU
// Date : 05/11/2022

#include <netinet/in.h>
#include <stdio.h>
#include <sys/types.h>
#include <netdb.h>
#include <arpa/inet.h>
#include <sys/socket.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>


//REQUETE POSSIBLE QU'ON A MIS EN PLACE
#define ADR_PROC 1              //adresse du processus courant
#define ATTRIB_NUM 2            //atribution du numero pour chaque processus
#define ADR_VOISIN 3            //adresse du voisin
#define ELECTION 4              //election
#define TAILLE_RESEAU 5         //a taille du reseau
#define DESTRUCTION_ANNEAU 6    //destruction du graphe


//SYSTEME ERREUR OU FERMETURE
#define ERREUR -1               //erreur
#define FERMETURE 0             //Fermeture



/* Programme client */


////////////////////////////////////////
// STRUCTURE POUR ENVOIE INFORMATIONS //
//////////////////////////////////////// 

/// @brief Structure qui répertorie le descripteur et l'adresse du processus
struct procAnneau {
    int indiceProc;                    //indice du site dans le graphe
    int dSProc;
    struct sockaddr_in adrProc;
};




/// @brief Structure des inforamtions avec la requete les inforamtions que l'on a besoin selon la requete et l'adresse du processus 
struct infos_procGraphe {
    int requete;                    //requete qu'on veut
    int info1;                      //information 1 qu'on a besoin
    int info2;                      //informations 2 qu'on a besoin
    struct sockaddr_in adrProc;     //adresse du processus dont on parle
};



//////////////////////
// FONCTION SENDTCP //
//////////////////////
/// @brief Fonction qui recoit un message par buffer
/// @param sock descripteur de lenvoie
/// @param informations_proc message recu
/// @param sizeinformations_proc taille du message a recu
/// @return resultat de la reception qui est la taille du message recu
int sendTCP (int sock, void* informations_proc, int sizeinformations_proc){

   //VARIABLE : 
   int rest_lu = sizeinformations_proc;                  //on commence a sizeinformations_proc
   int res;
   int lu;
   
   while(rest_lu > 0){         //tant quil me reste des octets a recevoir

      res = send(sock, informations_proc + (sizeinformations_proc - rest_lu), rest_lu, 0); //on tente d'envoyer mon message

      //GESTION ERREUR
      if (res <= 0){ 
         return res; 
      }  

      rest_lu = rest_lu - res;     //on rajoute res pour savoir combien on en a lu
      lu+=res;
   }
   return lu;
}




/////////////////////////////
// FONCTION SENDCompletTCP //
/////////////////////////////
/// @brief Fonction qui envoie la taille puis le message
/// @param sock descripteur pour envoie
/// @param informations_proc message a envoyer
/// @param sizeinformations_proc taille du message
void sendCompletTCP(int sock, void* informations_proc, int sizeinformations_proc){

    //PREMIER APPEL POUR LA TAILLE                                                //creation d'une variable qui recupere la taille du message
    int res_premier_appel = sendTCP(sock, &sizeinformations_proc, sizeof(int));     //on envoie la taille du message
    
        //GESTION DES ERREURS
        if (res_premier_appel == ERREUR) {
            perror("\n[ERREUR] : Erreur lors de l'envoie de la taille du message : ");
            close(sock);
            exit(1);          // on choisis ici d'arrêter le programme car le reste
        }
        if (res_premier_appel == FERMETURE) {
            perror("\n[ERREUR] : Abandon de la socket principale : ");
            close(sock);
            exit(1);          // on choisis ici d'arrêter le programme car le reste
        }

   //DEUXIEME APPEL POUR LE MESSAGE
   int res_deuxieme_appel = sendTCP(sock, informations_proc, sizeinformations_proc);     //on envoie la taille du message
   
        //GESTION DES ERREURS
        if (res_deuxieme_appel == ERREUR) {
            perror("\n[ERREUR] : Erreur lors de l'envoie du message : ");
            close(sock);
            exit(1);          // on choisis ici d'arrêter le programme cr le reste depend de cet envoie
        }
        if (res_deuxieme_appel == FERMETURE) {
            perror("\n[ERREUR] : Abandon de la socket principale : ");
            close(sock);
            exit(1);          // on choisis ici d'arrêter le programme car le reste depend de cet envoie
        }

}








//////////////////////
// FONCTION RECVTCP //
//////////////////////
/// @brief Fonction qui recoit un message par buffer
/// @param sock descripteur de lenvoie
/// @param informations_proc message recu
/// @param sizeinformations_proc taille du message a recu
/// @return resultat de la reception qui est la taille du message recu
int recvTCP (int sock, void* informations_proc, int sizeinformations_proc){
   
   //VARIABLE : 
   int rest_recv = sizeinformations_proc;                  //on commence a 0
   int res;
   int recu;
   
   while(rest_recv > 0){         //tant quil me reste des octets a recevoir

      res = send(sock, informations_proc + (sizeinformations_proc - rest_recv), rest_recv, 0); //on tente d'envoyer mon message

      //GESTION ERREUR
      if (res <= 0){ 
         return res; 
      }  

      rest_recv = rest_recv - res;     //on rajoute res pour savoir combien on en a lu
      recu +=res;
   }
   return recu;
}




/////////////////////////////
// FONCTION RECVCOMPLETTCP //
/////////////////////////////
/// @brief Fonction qui recoit la taille puis le message
/// @param sock descripteur de lenvoie
/// @param informations_proc message recu
/// @param sizeinformations_proc taille du message a recu
/// @return resultat de la reception qui est la taille du message recu
void recvCompletTCP(int sock, void* informations_proc, int sizeinformations_proc){

   //PREMIER APPEL POUR LA TAILLE
   int taille_informations_proc = 1;                                                     //creation d'une variable qui recupere la taille du message
   int res_premier_appel = recvTCP(sock, &taille_informations_proc, sizeof(int));        //on recoit la taille du message
   
      //GESTION DES ERREURS
      if (res_premier_appel == ERREUR) {
         perror("\n[ERREUR] : Erreur lors de la reception de la taille du message : ");
         close(sock);
         exit(1);          // on choisis ici d'arrêter le programme car le reste
      }
      if (res_premier_appel == FERMETURE) {
         perror("\n[ERREUR] : Abandon de la socket principale : ");
         close(sock);
         exit(1);          // on choisis ici d'arrêter le programme car le reste
      }

   //VERIFICATION DES TAILLES
   if (taille_informations_proc > sizeinformations_proc){
      perror("[ERREUR] La taille du message est trop grande par rapport a celle attendu");
      exit(1);
   }

   //DEUXIEME APPEL POUR LE MESSAGE
   int res_deuxieme_appel = recvTCP(sock, informations_proc, sizeinformations_proc);     //on recoit la taille du message
   
      //GESTION DES ERREURS
      if (res_deuxieme_appel == ERREUR) {
         perror("\n[ERREUR] : Erreur lors de la reception du message : ");
         close(sock);
         exit(1);          // on choisis ici d'arrêter le programme car le reste
      }
      if (res_deuxieme_appel == FERMETURE) {
         perror("\n[ERREUR] : Abandon de la socket principale : ");
         close(sock);
         exit(1);          // on choisis ici d'arrêter le programme car le reste
      }

}




/////////////////////
// CREATION SOCKET //
/////////////////////
/// @brief Fonction qui crée une socket
/// @return descripteur de la socket 
int creationSocket (){

    int option = 1;
    int dS = socket(PF_INET, SOCK_STREAM, 0);  //on crée la socket enTCP
    setsockopt(dS, SOL_SOCKET, SO_REUSEADDR, &option, sizeof(option));

        //GESTION DES ERREUR
        if (dS == ERREUR){
            perror("[ERREUR] Problème lors de la création de la socket : ");
            close(dS);  //on ferme la socket
            exit(1);          //on sort du programme
        }

    printf("\n[SERVEUR] Création de la socket réussie \n");
    return dS;        //on retourne le descripteur
}



//////////////////////////
// NOMMAGE DE LA SOCKET //
//////////////////////////
/// @brief Fonction qui nomme une scoket
/// @param dS descripteur de la sokcet à nommer
/// @param port port de la socket à nommer
/// @return retourner l'adresse de la socket
struct sockaddr_in nommageSocket(int dS, char * port){

    struct sockaddr_in adrSocket ;
    adrSocket.sin_family = AF_INET ;                            //IPv4 : famille AF_INET
    adrSocket.sin_addr.s_addr = INADDR_ANY;                     //Attache la socket àtoutes les interfaces réseaux locales : toutes les adresses de la station
    adrSocket.sin_port = htons((short) atoi(port)) ;            // on doit convertir la chaine de caractère en nombre

    printf("\n[SERVEUR] Le port est %hu\n", htons(adrSocket.sin_port));      //affichage du port

    int res_bind_client = bind(dS,                                  // descripteur de socket
                                (struct sockaddr*)&adrSocket,       // pointeur vers l'adresse
                                sizeof(adrSocket)) ;                // longueur de l'adresse


      //GESTION ERREUR
      if (res_bind_client == ERREUR) {
         perror("\n\n[ERREUR] lors du nommage de la socket : ");
         close(dS);
         exit(1); // on choisis ici d'arrêter le programme car le reste
         // dependent du nommage meme si il peut le faire automatiquement.
      }

   return adrSocket;

}





/////////////////////////
// SE METTRE EN ECOUTE //
/////////////////////////
/// @brief Fonction qui met en ecoute une socket
/// @param dS descripteur de la socket qui se met en ecoute
/// @param nbMaxAttente nombre maximum d'attente possible
void ecouter(int nbProc, int dS){
   
    int nbmaxAttente = nbProc;                               //on doit avoir un nb max qui est le nombre de processus du graphe
    int res_listen = listen(dS, nbmaxAttente);               //met en ecoute au max pour 10 clients

        //GESTION ERREUR
        if (res_listen == ERREUR) {
            perror("\n\n[ERREUR] : Erreur lors de la mise en ecoute de la socket : ");
            close(dS);
            exit(1); // on choisis ici d'arrêter le programme car le reste
            // dependent du nommage meme si il peut le faire automatiquement.
        }
}






////////////////////////////
// ACCEPTER UNE CONNEXION //
////////////////////////////
/// @brief Fonction qui accepte une socket
/// @param dS descripteur de la socket qui va accepter
/// @param adr adresse du client qui vient detre accepter
/// @return entier qui est le descripteur de la socket accepter
int accepter(int dS, struct sockaddr_in* adr){

    socklen_t lgAdr = sizeof(struct sockaddr_in);   // sa taille

    int res_dS = accept(dS, (struct sockaddr *) &adr, &lgAdr); //nouvelle socket cliente

        //GESTION ERREUR
        if (res_dS == ERREUR) {
            perror("\n\n[SERVEUR] : Erreur lors de l'acceptation de connexion : ");
            close(dS);
            exit(1); 
        }

   return res_dS;    //retourne l'adresse du client qu'on vient d'accepter

}



/*

/// @brief Fonction qui detruit un processus dans du graphe
/// @param descripteur descripteur du processus qu'on veut supprimer
/// @param set 
/// @param currentMax maximum courant des indices
/// @param maxdS maximum des descripteurs
/// @param procAnneaux liste des processus qui font partie du graphe
void deconnexionSousAnneau(
    int descripteur, 
    fd_set* set,
    int* currentMax,
    int* maxDescripteur,
    struct sousAnneau sousAnneaux[]
) {
    
    //FERMETURE DE LA SOCKET VOULU
    FD_CLR(descripteur, set);
    printf("\n[SERVEUR] Un sous-anneau s'est déconnecté\n");                          //Affichage
    close(descripteur);                                                             //on ferme la socket du descripteur
    
    //RETRAINT D'UN PROCESSUS DE L LISTE EN RECalVULANT LE MAX
    
    //DONNEES
    int i;                                                                      //on fixe un index
    int max = procAnneaux[0].dSProc;                                            //on veut trouver le maxium des descripteur
    
    //PARCOURT DE LA LISTE
    for (i=0; i<currentMax; i++) {                                              //on parcourt les processus dans la liste
        if (procAnneaux[i].dSProc == descripteur) break;                        //si on arrive au descripteur qu'on veut supprimer on arrete et on a l'index
        if (max < procAnneaux[i].dSProc) max = procAnneaux[i].dSProc;           //on modifie le maximum des descripteur
    }
    if (i == currentMax) return ERREUR;                                             //si l'indexe est la taille du tableau le descripteur n'existe pas on s'arrete

    //MODIFICATION DE LA LISTE
    while (i < currentMax-1) {                                                  //tant que l'indexe est plus petit que la taille -1 
        procAnneaux[i] = procAnneaux[i+1];                                      //on change le processus à l'index par l'index+1
        if (max < procAnneaux[i].dSProc) max = procAnneaux[i].dSProc;           //on recalcul le max
        i++;                                                                    //et on parcourt la liste pour modifier tous les éléments du tableau
    }

    //ATTRIBUTION MAX
    int newMax = max;                                                               //recalculage du max
    if (newMax == ERREUR) {                                                             //si erreur alors le processus n'est pas dans la liste
        printf("\n[SERVEUR] Problème: le processus n'est aps dans la liste'\n");
    } else {                                                                        //sinon
        currentMax--;                                                            //on decremente le maximum courant
        maxdS = newMax;                                                            //et on change le maximum
    }
}


*/




/// @brief Foncion main 
/// @param argc nombre d'argument
/// @param argv liste des argumenets
/// @return entier
int main(int argc, char *argv[])
{

    // ETAPE 1 : GESTION PARAMETRES
    if (argc != 3){
        printf("\n[UTILISATION] : %s port_serveur nombre_proc_anneaux\n", argv[0]);
        exit(1);
    }

    int nbProcAnneaux = atoi(argv[2]);
    char* portServeur = argv[1];


    // ETAPE 2 : CREATION SOCKET SERVEUR
    int dSServeur = creationSocket();                  //creation 

    //ETAPE 3 : NOMMER SOCKET
    nommageSocket(dSServeur, portServeur);            //nommer la socket avec son port

    //ETAPE 4 : MISE SOUS ECOUTE
    int nbMax = 100;                                //on fixe le nombre de processus maximum pour attente 
    ecouter(nbMax, dSServeur);                    //on met en ecoute la socket serveur


    //ETAPE 5 : DECLARATION DE DONNEES UTILS
    fd_set set, settmp;
    int dSClient;                                   //on decare la socket cliente    
    FD_ZERO(&set);
    FD_SET(dSServeur, &set);
    int maxdS = dSServeur;                          //on fixe le maximum des descripteur en commancant par la socket serveur
    struct sockaddr_in sockClient;                  //on declare la socket cliente
    int nbMaxAnneau = 100;                          //on fixe le nombre maximum d'anneau possible
    struct procAnneau procAnneaux[nbMaxAnneau];     //on declare un tableau de tous les processus qui sont dans du graphe
    int currentMaxAnneau = 0;                       //on declare le maximum des anneau courant 


    while (1) {                                                  //pk un while ?    
        //MULTIPLEXAGE
        settmp = set;
        if (select(maxdS+1, &settmp, NULL, NULL, NULL) == -1) {
            printf("\n[SERVEUR] Problème lors du select : ");  
            continue;
        }
        

        for (int df=2; df <= maxdS; df++) {          //on parcourt les descripteur possible en commencant par 2 car il n'y en a pas de 0 ou 1
            if (!FD_ISSET(df, &settmp)) {
                continue;
            }

            if (df == dSServeur) {                  //si le descripteur est la socket du serveur

                //ETAPE 6 : ACCEPTATION DU CLIENT
                dSClient = accepter(dSServeur, &sockClient);        //on accepte le client qui demande


                //ETAPE 7 : RECPETION DES INFORMATIONS DU CLIENT
                struct infos_procGraphe informations_proc;                                                      //structure qui va recuperer les informations qu'un client a envoyer
    
                //RECPETION DES INFORMATIONS
                recvCompletTCP(dSClient, &informations_proc, sizeof(struct infos_procGraphe));                  //reception des informations
                sockClient = informations_proc.adrProc;                                                         //recuperation de l'adrresse recu


                //ETAPE 8 : ATTRIBUTION DES INFORMATIONS POUR LES PROCESSUS
                
                //RECUPERATION DE LADRESSE
                char adrClientRecu[INET_ADDRSTRLEN];
                inet_ntop(AF_INET, &sockClient.sin_addr, adrClientRecu, INET_ADDRSTRLEN);
                printf("\n[SERVEUR] Connexion d'un sous-anneau d'adresse %s\n", adrClientRecu);       //affichage

                FD_SET(dSClient, &settmp);

                //MODIFIATION MAX DESCRIPTEUR
                if (maxdS < dSClient) maxdS = dSClient;

                //MODIFICATION DE LA SSTRUCTURE
                procAnneaux[currentMaxAnneau].dSProc = dSClient;        //on attribut un descripteur
                procAnneaux[currentMaxAnneau].adrProc = sockClient;        //on attribut une adresse

                //CREATION DES INFORAMTIOSN DUN CLIENT
                struct infos_procGraphe attribution;                               //on declare l'attibution d'un numero
                attribution.requete = ATTRIB_NUM;               //on donne la requete
                attribution.info1 = currentMaxAnneau;                  //on donne l'information principale qu'on a besoin ici le maximum courant
                currentMaxAnneau++;                                          //on incremente le maximum courant pour continuer l'attribution

                //ENVOIE DE LATTRIBUTION DU NUMERO D'ANNEAU AU CLIENT
                sendCompletTCP(dSClient, &attribution, sizeof(struct infos_procGraphe));  //on envoie l'attribution à un processus

                
                //ETAPE 9 : AFFICHAGE DES PROCESSUS CONNECTE                    
                char adrClientEnv[INET_ADDRSTRLEN];                                    //on va stocker l'adresse du sous anneau dedans
                printf("\n***********************************\n");                  //afficher une ligne
                printf("\n[SERVEUR] \033[4mListe des sous-anneaux:.\033[0m\n");

                //BOUCLE POUR PARCOURIR LA TAILLE DES SOUS ANNEAU EXIStant                          
                for (int i=0; i<currentMaxAnneau; i++) { 

                    //RECUPERATION DES ADRESSES ET PORT
                    inet_ntop(AF_INET, &procAnneaux[i].adrProc.sin_addr, adrClientEnv, INET_ADDRSTRLEN);   //recuperation de l'adresse dans le tableau pour le processus i
                    int portClient = htons(procAnneaux[i].adrProc.sin_port);

                    //AFFICHAGE
                    printf("\n[SERVEUR] Le processus n°%i de descripteur %i : %s:%i\n", i, procAnneaux[i].dSProc, adrClientEnv, portClient);
                }
                printf("\n***********************************\n");
                
                continue;
            }
        }

        if (currentMaxAnneau == nbProcAnneaux) {                                            //si le nombre de processus courant est égal au nombre de processus donnée
            printf("\n[SERVEUR] Tous les anneaux sont connectés, envoi des adresses\n");      //affichage
            
            //ETAPE 10 : ENVOIE DES INFORMATIONS AU PROCESSUS

            //VERIFICATION DE PLUS DE 3 PROCESSUS
            if (currentMaxAnneau < 3) {           //verification que le nombre de processus est moins de 3
                printf("\n[SERVEUR] Impossible de créer un anneau avec moins de trois sous-anneaux\n");       //aors c'est pas possible
                exit(1);
            }

            //DONNEES
            struct infos_procGraphe informations_proc;                                      //on declare la structure qui dit ce qu'on veut
            informations_proc.requete = ADR_VOISIN;                           //on donne la requete ADR_VOISIN
            
            //BOUCLE QUI PARCOURT LA LISTE DES INFORMATIONS DES PROCESSUS DE LANNEAU -1
            for (int i=0; i<currentMaxAnneau-1; i++) {                        //on parcours le tableau des informations des processus
                
                informations_proc.adrProc = procAnneaux[i+1].adrProc;            //donne à la structure l'adresse de la liste du processus i+1
                
                //RECUPERATION DE L'ADRESSE ET PORT
                char adrClient[INET_ADDRSTRLEN];
                inet_ntop(AF_INET, &informations_proc.adrProc.sin_addr, adrClient, INET_ADDRSTRLEN);
                int portClient = htons(informations_proc.adrProc.sin_port);

                //ENVOIE DES INFORMATIONS 
                sendCompletTCP(procAnneaux[i].dSProc, &informations_proc, sizeof(struct infos_procGraphe));     //on envoie les inforamtions ici adresse du voisin
                printf("\n[SERVEUR] Envoie de l'adresse: %s:%i\n", adrClient, portClient);
            }
            //DERNIER PROCESSUS
            informations_proc.adrProc = procAnneaux[0].adrProc;          //donne l'adresse du premier processus

            //RECUPERATION DE LADRESSE ET PORT
            char adrClient[INET_ADDRSTRLEN];
            inet_ntop(AF_INET, &informations_proc.adrProc.sin_addr, adrClient, INET_ADDRSTRLEN);
            int portClient = htons(informations_proc.adrProc.sin_port);

            //ENVOIE DE LADRESSE DU DERNIER PROCESSUS
            printf("\n[SERVEUR] Envoi de l'adresse: %s:%i\n", adrClient, portClient);
            sendCompletTCP(procAnneaux[currentMaxAnneau-1].dSProc, &informations_proc, sizeof(struct infos_procGraphe));


            //ETAPE 11 : FERMETURE DE LA SOCKET SERVEUR
            if (close(dSServeur) == -1) {                                                   //on peut donc fermer la socket du serveur car on a fait notre boulot
                printf("\n[SERVEUR] Problème lors de la fermeture du descripteur : ");
            }
            printf("\n[SERVEUR] Au revoir.\n");
            exit(0);
        }
    }
}




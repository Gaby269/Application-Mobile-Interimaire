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
#include <unistd.h>
#include "parseur.c"
#include "structures.c"
#include "fonctions_tcp.c"

// Mode Debug pour les affichages
#define DEBUG 2

//REQUETE POSSIBLE QU'ON A MIS EN PLACE
#define ADR_PROC 1              //adresse du processus courant
#define ATTRIB_NUM 2            //atribution du numero pour chaque processus
#define ADR_VOISINS 3           //adresse du voisin
#define ELECTION 4              //election
#define NB_VOISIN 5             //on donne le nb de voisins


//SYSTEME ERREUR OU FERMETURE
#define TRUE 1
#define ERREUR -1               //erreur
#define FERMETURE 0             //Fermeture
#define NOEUDS_MAX 100          //on fixe le nombre de noeud maximum qu'il peut y avoir dans un graphe



/* Programme Noeud */







/// @brief Foncion main 
/// @param argc nombre d'argument
/// @param argv liste des argumenets
/// @return entier
int main(int argc, char *argv[]) {

    // ETAPE 1 : GESTION PARAMETRES
    if (argc != 3) {
        printf("\n[UTILISATION] : %s port_serveur fichier_graphe\n\n", argv[0]);
        exit(1);
    }

    char* portServeur = argv[1];        //port du serveur
    char *nom_fichier = argv[2];        //nom du fichier ou recuperer la structure du graphe


    //ETAPE 1.25 : REUPERATION DES INFO DU GRAPHE
    struct info_nb nB;                  //on declare une structure 
    nB = nbAreteNbNoeud(nom_fichier);  //on recupere les nombre important
    int nb_sommets = nB.nb_sommets;    //on recupere le nombre de sommets
    int nb_aretes = nB.nb_aretes;      //et le nombre d'aretes

    // AFFICHAGE
    if (DEBUG > 0) { 
        printf("\nNombre de sommets : %d\n", nb_sommets);
        printf("Nombre d'aretes : %d", nb_aretes);
    }


    //appel de la fonction pour recuperer les arretes
    struct aretes *ListeAretes = (struct aretes*) malloc (nb_aretes *sizeof(struct aretes));  //on alloue de la memoire pour la liste des aretes
    Arretes(nom_fichier, nb_sommets, nb_aretes, ListeAretes);    //et on recuperer cette liste directement dans ListeAretes

    //AFFICHAGE
  if (DEBUG > 1) { 
    printf("\n\nListe des arretes :\n\n");
   }

    // AFFICHAGE DES ARRETES
    for(int k=0; k<nb_aretes; k++){
        printf("%d -> %d\n", ListeAretes[k].noeud1, ListeAretes[k].noeud2);
    }

    //on a donc dans le tableau ListeAretes la liste des aretes on aurait pu les trier en disant à l'indice i c'est le noeud i est il est relié avec tous les elements de sont tableau


    // ETAPE 1.5 : LECTURE DU TABLEAU 
      //tableau des nb de voisin
    struct nbVois nbVoisin[nb_sommets];               //tableau du nombre de voisin de chaque sommets
      //initialisation
    for (int i=0; i<nb_sommets; i++){ 
        nbVoisin[i].nbVoisinDemande = 0;
        nbVoisin[i].nbVoisinTotal = 0;
      }
      //parcours et ajout des voisins
    for (int a=0; a<nb_aretes; a++){
        //données
      int noeud1 = ListeAretes[a].noeud1;
      int noeud2 = ListeAretes[a].noeud2;             //si l'indice du noeud est present dans l'un des deux noeuds de l'arete
        //augmentation
      nbVoisin[noeud1-1].nbVoisinTotal++;        //on augmente pour le noeud 1   
      nbVoisin[noeud2-1].nbVoisinTotal++;        //on aumgnete pour le neoud2
      nbVoisin[noeud1-1].nbVoisinDemande++;      //on augmente le nombre de voisin a qui ont va demandé
    }
      //affichage
    printf("\n[SERVEUR] \033[4mTableau des nb de voisins :\033[0m\n");
    for (int i=0; i<nb_sommets; i++){
        printf("Le noeuds %d a %d voisins au total et %d voisin a demander\n", i+1, nbVoisin[i].nbVoisinTotal, nbVoisin[i].nbVoisinDemande);
    }



    // ETAPE 2 : CREATION SOCKET SERVEUR
    int dSServeur = creationSocket();                  //creation 
    //AFFICHAGE
    printf("\n[SERVEUR] Création de la socket réussie\n");
    printf("[SERVEUR] Le descripteur est %d \n", dSServeur);


    //ETAPE 3 : NOMMER SOCKET
    struct sockaddr_in adrServeur = nommageSocket(dSServeur, portServeur);              //nommer la socket avec son port
    //AFFICHAGE
        //adresse
    char adrServeurAff[INET_ADDRSTRLEN];
    inet_ntop(AF_INET, &adrServeur.sin_addr, adrServeurAff, INET_ADDRSTRLEN);
        //port
    int portServeurAff = htons(adrServeur.sin_port);
        //affichage
    printf("\n[SERVEUR] Le port est %d\n", portServeurAff);                //affichage du port
    printf("[SERVEUR] L'adresse est %s\n", adrServeurAff);                //affichage de l'adresse


    //ETAPE 4 : MISE SOUS ECOUTE
    int nbMaxEcoute = NOEUDS_MAX;                                //on fixe le nombre de processus maximum pour attente 
    ecouter(dSServeur, nbMaxEcoute);                      //on met en ecoute la socket serveur
    //AFFICHAGE
    printf("\n[SERVEUR] La mise en ecoute de la socket du serveur réussi !\n");


    // ETAPE 4 : MISE EN PLACE DES DONNES
        //donnees util
    int dSNoeud;                                                                //declaration du descripteur
    //int nbMaxdS = dSServeur;                                                  //maximum des descripteurs
    int num;                                                     //on declare le nombre de Noeud courant
    //int nbMaxNoeud = NOEUDS_MAX;                                              //on declare un maximum de noeuds possibles
    struct infos_Graphe *procGraphe = (struct infos_Graphe*) malloc(nb_sommets * sizeof(struct infos_Graphe*));         //on declare un tableau de structure pour les informations des Noeuds connecté au sevreur
    struct sockaddr_in sockNoeud;                                               //on declare la socket du Noeud
    socklen_t lgAdr;                                                            //taille de l'adresse
    


    //BOUCLE POUR RECEVOIR LES CLIENT

     for (num=1; num<=nb_sommets; num++) {

        //ETAPE 4 : ACCEPTATION DU NOEUD
            //acceptation
        dSNoeud = accept(dSServeur, (struct sockaddr*)&sockNoeud, &lgAdr);          //on accepte le Noeud qui demande

        //AFFICHAGE
            //affichage
        printf("\n[SERVEUR] Connexion d'un Noeud de descripteur %d\n", dSNoeud);
        if (num < 2){
            printf("[SERVEUR] %d Noeud est connecté au serveur\n", num);       //affichage du nombre de Noeud connecté
        }
        else printf("[SERVEUR] %d Noeuds sont connectés au serveur\n", num);       //affichage du nombre de Noeud connecté


        //ETAPE 5 : RECEPTION DES INFORMATIONS DU Noeud
            //donnees        
        struct infos_Graphe info_proc;                      // structure qui va recuperer les informations qu'un Noeud a envoyer
            //reception
        recvCompletTCP(dSNoeud, &info_proc, sizeof(struct infos_Graphe));       // reception des informations dans info_proc
            //modification des donnees
        sockNoeud = info_proc.adrProc;                                          // donner a sockNoeud l'adresse recu dans info_proc
        int indice_proc = info_proc.indice-1;                                     //donne l'indice
        //MODIFICATION DES INFORMATIONS
        procGraphe[indice_proc].indice = indice_proc+1;                         //on attribut l'indice du noeud
        procGraphe[indice_proc].descripteur = dSNoeud;                          //on attribut le descripteur
        procGraphe[indice_proc].adrProc = sockNoeud;                            //on attribut l' adresse

        //AFFICHAGE
            //adresse
        char adrProcAff[INET_ADDRSTRLEN];             //on va stocker l'adresse du sous anneau dedans
        inet_ntop(AF_INET, &sockNoeud.sin_addr, adrProcAff, INET_ADDRSTRLEN);     //adresse du Noeud    
            //port
        int portProcAff = htons(sockNoeud.sin_port);                                 //port du Noeud
            //Affichage
        
        printf("\n[SERVEUR] \033[4mLe processus a comme informations après réception :\033[0m\n");
        printf("\n       Adresse du processus : %s\n       Port : %d", adrProcAff, portProcAff);
        printf("\n       Indice du noeud dans le graphe : %d\n       Descripteur de la socket du processus : %d\n\n", indice_proc+1, procGraphe[indice_proc].descripteur);
         

        //ENVOI DU NOMBRE DE VOISIN A CHAQUE NOEUDS
          //descripteur de socket courant
        int dS_courant = procGraphe[indice_proc].descripteur;                  //indice courant
          //envoi
        sendCompletTCP(dS_courant, &nbVoisin[indice_proc], sizeof(struct nbVois));
         //AFFC+ICHAGE
         printf("\n[SERVEUR] \033[4mInformations envoyé :\033[0m\n");
         printf("\n      [SERVEUR] Nombre de voisin total = %d\n", nbVoisin[indice_proc].nbVoisinTotal);
         printf("      [SERVEUR] Nombre de voisin de demande = %d\n", nbVoisin[indice_proc].nbVoisinDemande);
       
        printf("\n***********************************\n");
      } //fin de la premieère connexion avec tous les noeuds


      //UNE FOIS QUE TOUS LES NOEUDS SONT LA
      printf("\n[SERVEUR] Tous les Noeuds sont connectés !\n\n***********************************\n");

      //ETAPE ~6 : AFFICHAGE DE LA LISTE DES NOEUDS CONNECTE
          //affichage
      printf("\n[SERVEUR] \033[4mListe des noeuds connecté(s):\033[0m\n");
          //adresse
      char adrNoeudCoAff[INET_ADDRSTRLEN];                                       //on va stocker l'adresse du sous anneau dedans
          //parcourt des noeuds connectés au serveur                        
      for (int i=0; i<num-1; i++) {     //on commence a un car les indice commence a 1
              //recuperation adresse
          inet_ntop(AF_INET, &procGraphe[i].adrProc.sin_addr, adrNoeudCoAff, INET_ADDRSTRLEN);     //adresse du Noeud    
              //port
          int portNoeudCoAff = htons(procGraphe[i].adrProc.sin_port);                                 //port du Noeud
          printf("\n      Noeud d'indice %d de descripteur %i : %s:%i\n", i, procGraphe[i].descripteur, adrNoeudCoAff, portNoeudCoAff);
      }


      // MISE EN ATTENTE 
      char d ;
      printf("\n\n[SERVEUR %d] : Entrez un caractère après l'envoie des informations : ", dSServeur);  //on demmande au client de entrez un message
      scanf("%c", &d);
   //FIN MISE EN ATTENTE
        
      //ETAPE 7: ENVOIE DES INFORMATIONS AU PROCESSUS VOISINS
          //donnees

      /*
      struct infos_Graphe info_proc;             //on declare la structure qui dit ce qu'on veut
      info_proc.requete = ADR_VOISINS;           //on donne la requete ADR_VOISIN
          //boucle
      for (int i=0; i<num; i++) {     //pour chaque noeuds

          //structure contenant les voisins a envoyer
          struct infos_Graphe info_voisin[procGraphe[i].nbVoisin];   

          for (int v=0; v<procGraphe[i].nbVoisin; v++){              //parcourt des voisins v est donc 

              int indice_proc = procGraphe[i].voisins[v];                 //dns le tableau voisins du noeud i 
              
              info_proc.descripteur = procGraphe[indice_proc].descripteur;        //Recuperation du descripteur du voisins
              info_proc.indice = procGraphe[indice_proc].indice;                  //recuperation de l'indice du voisin
              info_proc.adrProc = procGraphe[indice_proc].adrProc;                //donne à la structure l'adresse de la liste du processus i+1

              info_voisin[indice_proc] = info_proc;                              //donne alors la structure
          }
              //envoie des inforamtions
          sendCompletTCP(info_proc.descripteur, info_voisin, sizeof(struct infos_Graphe));     //on envoie les inforamtions ici adresse des voisins
          
          
          //ETAPE 8 : AFFICHAGE DE LA LISTE DES NOEUDS VOISINS
              //adresse
          char adrNoeudAff[INET_ADDRSTRLEN];
          inet_ntop(AF_INET, &info_proc.adrProc.sin_addr, adrNoeudAff, INET_ADDRSTRLEN);
              //port
          int portNoeudAff = htons(info_proc.adrProc.sin_port); 
              //affichage
          printf("\n[SERVEUR] Le Noeud n°%i de descripteur %i a pour adresse et port : %s:%i\n", i, procGraphe[i].descripteur, adrNoeudAff, portNoeudAff);
          printf("\n***********************************\n");
          printf("\n[SERVEUR] \033[4mListe des adresses des voisins envoyés:\033[0m\n\n");
              //adresse
          char adrNoeudVois[INET_ADDRSTRLEN];
              //parcourt des noeuds connectés au serveur                        
          for (int j=0; j<procGraphe[i].nbVoisin; j++) { 
                  //recup adresse
              inet_ntop(AF_INET, &info_proc.adrProc.sin_addr, adrNoeudVois, INET_ADDRSTRLEN);
                  //port
              int portNoeudVois = htons(info_proc.adrProc.sin_port);   
                  //affichage               
              printf("Noeud voisin n°%i de descripteur %i : %s::%i\n", j, procGraphe[j].descripteur, adrNoeudVois, portNoeudVois);
          }
          printf("\n***********************************\n");
      }//fin du for des noeuds
*/

      //ETAPE 8 : FERMETURE DE LA SOCKET SERVEUR CAR PLUS BESOIN
          //fermeture
      close(dSServeur);
          //affichage
      printf("\n[SERVEUR] Je peux m'en aller !\n");
          //on sort du programme
      exit(0);
      
      
    
}//fin du main


















/*
    //ETAPE 5 : DECLARATION DE DONNEES UTILS
    fd_set set, settmp;
    int dSNoeud;                                   //on decare la socket Noeud    
    FD_ZERO(&set);
    FD_SET(dSServeur, &set);
    int maxdS = dSServeur;                          //on fixe le maximum des descripteur en commancant par la socket serveur
    struct sockaddr_in sockNoeud;                  //on declare la socket Noeud
    int nbMaxAnneau = NOEUDS_MAX;                          //on fixe le nombre maximum d'anneau possible
    struct procGraphe procGraphe[nbMaxAnneau];     //on declare un tableau de tous les processus qui sont dans du graphe
    int num = 0;                       //on declare le maximum des anneau courant 


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

                //ETAPE 6 : ACCEPTATION DU Noeud
                dSNoeud = accepter(dSServeur, &sockNoeud);        //on accepte le Noeud qui demande


                //ETAPE 7 : RECPETION DES INFORMATIONS DU Noeud
                struct infos_Graphe info_proc;                                                     //structure qui va recuperer les informations qu'un Noeud a envoyer
    
                //RECPETION DES INFORMATIONS
                recvCompletTCP(dSNoeud, &info_proc, sizeof(struct infos_Graphe));                  //reception des informations
                sockNoeud = info_proc.adrProc;                                                         //recuperation de l'adrresse recu


                //ETAPE 8 : ATTRIBUTION DES INFORMATIONS POUR LES PROCESSUS
                
                //RECUPERATION DE LADRESSE
                char adrNoeudRecu[INET_ADDRSTRLEN];
                inet_ntop(AF_INET, &sockNoeud.sin_addr, adrNoeudRecu, INET_ADDRSTRLEN);
                printf("\n[SERVEUR] Connexion d'un sous-anneau d'adresse %s\n", adrNoeudRecu);       //affichage

                FD_SET(dSNoeud, &settmp);

                //MODIFIATION MAX DESCRIPTEUR
                if (maxdS < dSNoeud) maxdS = dSNoeud;

                //MODIFICATION DE LA SSTRUCTURE
                procGraphe[num].dSProc = dSNoeud;             //on attribut un descripteur
                procGraphe[num].adrProc = sockNoeud;          //on attribut une adresse

                //CREATION DES INFORAMTIOSN DUN Noeud
                struct infos_Graphe attribution;        //on declare l'attibution d'un numero
                attribution.requete = ATTRIB_NUM;           //on donne la requete
                attribution.info1 = num;       //on donne l'information principale qu'on a besoin ici le maximum courant
                num++;                         //on incremente le maximum courant pour continuer l'attribution

                //ENVOIE DE LATTRIBUTION DU NUMERO D'ANNEAU AU Noeud
                sendCompletTCP(dSNoeud, &attribution, sizeof(struct infos_Graphe));  //on envoie l'attribution à un processus

                
                //ETAPE 9 : AFFICHAGE DES PROCESSUS CONNECTE                    
                char adrNoeudEnv[INET_ADDRSTRLEN];                                       //on va stocker l'adresse du sous anneau dedans
                printf("\n***********************************\n");                      //afficher une ligne
                printf("\n[SERVEUR] \033[4mListe des sous-anneaux:.\033[0m\n");

                //BOUCLE POUR PARCOURIR LA TAILLE DES SOUS ANNEAU EXIStant                          
                for (int i=0; i<num; i++) { 

                    //RECUPERATION DES ADRESSES ET PORT
                    inet_ntop(AF_INET, &procGraphe[i].adrProc.sin_addr, adrNoeudEnv, INET_ADDRSTRLEN);   //recuperation de l'adresse dans le tableau pour le processus i
                    int portNoeud = htons(procGraphe[i].adrProc.sin_port);

                    //AFFICHAGE
                    printf("\n[SERVEUR] Le processus n°%i de descripteur %i : %s:%i\n", i, procGraphe[i].dSProc, adrNoeudEnv, portNoeud);
                }
                printf("\n***********************************\n");
                
                continue;
            }
        }//fin du for

        if (num == nbprocGraphe) {                                            //si le nombre de processus courant est égal au nombre de processus donnée
            printf("\n[SERVEUR] Tous les anneaux sont connectés, envoi des adresses\n");      //affichage
            
            //ETAPE 10 : ENVOIE DES INFORMATIONS AU PROCESSUS

            //VERIFICATION DE PLUS DE 3 PROCESSUS
            if (num < 3) {           //verification que le nombre de processus est moins de 3
                printf("\n[SERVEUR] Impossible de créer un anneau avec moins de trois sous-anneaux\n");       //aors c'est pas possible
                exit(1);
            }

            //DONNEES
            struct infos_Graphe info_proc;                                      //on declare la structure qui dit ce qu'on veut
            info_proc.requete = ADR_VOISIN;                           //on donne la requete ADR_VOISIN
            
            //BOUCLE QUI PARCOURT LA LISTE DES INFORMATIONS DES PROCESSUS DE LANNEAU -1
            for (int i=0; i<num-1; i++) {                        //on parcours le tableau des informations des processus
                
                info_proc.adrProc = procGraphe[i+1].adrProc;            //donne à la structure l'adresse de la liste du processus i+1
                
                //RECUPERATION DE L'ADRESSE ET PORT
                char adrNoeud[INET_ADDRSTRLEN];
                inet_ntop(AF_INET, &info_proc.adrProc.sin_addr, adrNoeud, INET_ADDRSTRLEN);
                int portNoeud = htons(info_proc.adrProc.sin_port);

                //ENVOIE DES INFORMATIONS 
                sendCompletTCP(procGraphe[i].dSProc, &info_proc, sizeof(struct infos_Graphe));     //on envoie les inforamtions ici adresse du voisin
                printf("\n[SERVEUR] Envoie de l'adresse: %s:%i\n", adrNoeud, portNoeud);
            }
            //DERNIER PROCESSUS
            info_proc.adrProc = procGraphe[0].adrProc;          //donne l'adresse du premier processus

            //RECUPERATION DE LADRESSE ET PORT
            char adrNoeud[INET_ADDRSTRLEN];
            inet_ntop(AF_INET, &info_proc.adrProc.sin_addr, adrNoeud, INET_ADDRSTRLEN);
            int portNoeud = htons(info_proc.adrProc.sin_port);

            //ENVOIE DE LADRESSE DU DERNIER PROCESSUS
            printf("\n[SERVEUR] Envoi de l'adresse: %s:%i\n", adrNoeud, portNoeud);
            sendCompletTCP(procGraphe[num-1].dSProc, &info_proc, sizeof(struct infos_Graphe));


            //ETAPE 11 : FERMETURE DE LA SOCKET SERVEUR
            if (close(dSServeur) == -1) {                                                   //on peut donc fermer la socket du serveur car on a fait notre boulot
                printf("\n[SERVEUR] Problème lors de la fermeture du descripteur : ");
            }
            printf("\n[SERVEUR] Au revoir.\n");
            exit(0);
        }
    }
}
*/



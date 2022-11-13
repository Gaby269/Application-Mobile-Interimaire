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
#include "parseur.c"
#include "structures.c"
#include "fonctions_tcp.c"


//REQUETE POSSIBLE QU'ON A MIS EN PLACE
#define ADR_PROC 1              //adresse du processus courant
#define ATTRIB_NUM 2            //atribution du numero pour chaque processus
#define ADR_VOISINS 3           //adresse du voisin
#define ELECTION 4              //election
#define NB_VOISIN 5             //on donne le nb de voisins


//SYSTEME ERREUR OU FERMETURE
#define TRUE 1
#define FALSE 0
#define ERREUR -1
#define FERMETURE 0

#define TAILLE_MAX_STOCK 100        //maximum de stockage
#define NOEUDS_MAX 100          //on fixe le nombre de noeud maximum qu'il peut y avoir dans un graphe





/////////////////////////
// PROGRAME PRINCIPAL  //
/////////////////////////
/// @brief Fonction main
/// @param argc nb d'argument
/// @param argv liste des arguments
/// @return entier
int main(int argc, char *argv[]) {

    //ETAPE 1 : GESTION DES INFORMATIONS
    if (argc != 5){
        printf("\n[UTILISATION] %s ip_serveur port_serveur port_noeud indice_proc\n\n", argv[0]);
        exit(1);
    }

    char* adresseIP = argv[1];          //adresse ip du serveur
    char* port_serveur = argv[2];       //port du serveur
    char* port_noeud = argv[3];         //port du noeud
    int indice_proc = atoi(argv[4]);    //indice du client

    printf("\n************************************************\n************************************************\n");
    printf("\n[PROCESSUS %d] \033[4mInforamtions données en paramètres :\033[0m\n", indice_proc);
    printf("\n       Adresse du serveur : %s\n       Port : %d", adresseIP, atoi(port_serveur));
    printf("\n       Port du noeud : %d", atoi(port_noeud));
    printf("\n       Indice du processus : %d\n", indice_proc);


    //ETAPE 2 : CREATION DE LA SOCKET QUI DISCUTE AVEC SERVEUR
    int dSProcCS = creationSocket();
    //AFFICHAGE
    //printf("\n[PROCESSUS] \033[4mSocket du noeud :\033[0m\n");
    printf("\n\n[PROCESSUS %d] Création de la socket réussie\n", indice_proc);
    printf("[PROCESSUS %d] Le descripteur du noeud est %d \n", indice_proc, dSProcCS);


    //ETAPE 3 : DESIGNATION DE LA SOCKET SERVEUR
    struct sockaddr_in sockServ = designationSocket(adresseIP, port_serveur);
    //AFFICHAGE
        //addresse
    char adrServ[INET_ADDRSTRLEN];
    inet_ntop(AF_INET, &sockServ.sin_addr, adrServ, INET_ADDRSTRLEN);
        //port
    int portServ = htons((short) sockServ.sin_port);
    printf("\n[PROCESSUS %d] Designation de la socket du serveur réussi\n", indice_proc);
    printf("[PROCESSUS %d] Le serveur a donc pour IP %s:%d\n", indice_proc, adrServ, portServ);


    //ETAPE 4 : DEMANDE DE CONNECTION DE LA SOCKET A L'ADRESSE
    connexion(dSProcCS, &sockServ);
    //AFFICHAGE
    printf("\n[PROCESSUS %d] Connexion au serveur réussi !\n", indice_proc);


    //ETAPE 5 : CREATION de LA SOCKET PRECEDENT
    int dSProcJGraphe = creationSocket();
    //AFFICHAGE
    //printf("\n[PROCESSUS] \033[4mSocket du noeud :\033[0m\n");
    printf("\n[PROCESSUS %d] Création de la socket réussie\n", indice_proc);
    printf("[PROCESSUS %d] Le descripteur du noeud est %d \n", indice_proc, dSProcJGraphe);



    //ETAPE 6 : NOMMAGE DE LA SOCKET
    struct sockaddr_in sockProcJ = nommageSocket(dSProcJGraphe, port_noeud);
    //AFFICHAGE
        //adresse
    char adrProcJ[INET_ADDRSTRLEN];
    inet_ntop(AF_INET, &sockProcJ.sin_addr, adrProcJ, INET_ADDRSTRLEN);
        //port
    int portProcJ = htons((short) sockProcJ.sin_port);
        //affichage
    printf("\n[PROCESSUS %d] Nommage de la socket du noeud réussi\n", indice_proc);
    printf("[PROCESSUS %d] Les informations du noeud par la socket de descripteur %d : %s::%i\n", indice_proc, dSProcJGraphe, adrProcJ, portProcJ);



    //ETAPE 7 : MISE SOUS ECOUTE DE LA SOCKET
    int nbMaxAttente = NOEUDS_MAX;
    ecouter(dSProcJGraphe, nbMaxAttente);
        //AFFICHAGE
    printf("\n[PROCESSUS %d] La mise en ecoute de la socket du noeud précédent réussi !\n", indice_proc);



    //ETAPE 8 : ENVOIE DES INFORMATIONS AU SERVEUR
        //inforamtions du noeud
    struct infos_Graphe informations_proc;      //declare la structure qu'on va envoyer au serveur
    informations_proc.requete = ADR_PROC;           //requete d'une adresse
    informations_proc.indice = indice_proc;         //donne l'indice du processus
    informations_proc.descripteur = dSProcJGraphe;  //donne le descripteur
    informations_proc.adrProc = sockProcJ;          //donner l'adresse de la socket
        //envoie
    sendCompletTCP(dSProcCS, &informations_proc, sizeof(struct infos_Graphe));
    
    //AFFICHAGE
    printf("\n[PROCESSUS %d] Envoi des inforamtions réussi !\n", indice_proc);
    printf("[PROCESSUS %d] \033[4mEnvoie des inforamtions suivantes :\033[0m\n", indice_proc);
    printf("\n       Adresse du processus : %s\n       Port : %d", adrProcJ, portProcJ);
    printf("\n       Indice du noeud : %d\n       Descripteur de la socket du processus : %d\n\n", indice_proc, dSProcJGraphe);


  
    //ETAPE 10 : RECEPTION DES INFORAMTIONS DES PROCESSUS AVEC QUI DISCUTER
    struct nbVois nbVoisin;
    recvCompletTCP(dSProcCS, &nbVoisin, sizeof(struct nbVois));  //on reutilise la structure informations_proc pour la recepetion
    printf("\n[PROCESSUS %d] Reception du nombre de voisin réussi\n", indice_proc);
    printf("[PROCESSUS %d] Le nombre de voisin totale est donc %d et le nombre de voisin a qui il doit demandé une connexion est %d", indice_proc, nbVoisin.nbVoisinTotal, nbVoisin.nbVoisinDemande);


    //ETAPE 11 : CHANGEMENT DE DONNEES
    int numeroSousGraphe = informations_proc.info1;                   //le numero du sous Graphe prend la valeur de l'information 1 du message recu   
    printf("\n[PROCESSUS %d] J'ai reçu mon numéro de sous Graphe", indice_proc);
/*


    //ETAPE 12 : CREATION DE L'ADRESSE DU PROCESSUS SUIVANT
    fd_set set, settmp;
    FD_ZERO(&set);                              //on reinitialise 
    FD_SET(dSProcCS, &set);                     //ajout de la socket serveur
    FD_SET(dSProcJGraphe, &set);                //ajout de la socket preedente
    struct sockaddr_in sockProcK;               //structure de son adresse
    //socklen_t lgAdr;                          //taille de l'adresse


    //ETAPE 13 : CALCUL DU MAXIMUM DES DESCRIPTEUR
    int maxdS = dSProcCS;                                 //declaration max des descripteur
    if (dSProcJGraphe > dSProcCS) maxdS = dSProcJGraphe;    //calcul du maximum par rapport au processus precedent
    

    //ETAEP 14 : MODIFICATION DES DONNEES
    int dSProcKGraphe = ERREUR;                                     //descripteur du processus suivant pour l'instant c'est une erreur

    int connecteAuPrecedent = FALSE;                                //declare un boolean qui dit si la connexion est ok ou non
    int taille_reseau = -1;                                         //on declare la taille du reseau
    int plus_grand = FALSE;                                         //on declare un boolean qui dit si le processus est le chef ou non

    struct infos_Graphe stockage[TAILLE_MAX_STOCK];                 //declaration du tableau des informations avec les reuetes
    int indiceStockage = 0;                                         //et on declare l'indice du stockage


    while (TRUE) {

        //SI LA SOCKET DU PROCESSUS PROCHAIN EXISTE ET QUE L'INDICE OU STOCKER LES MESSAGES EST PLUS GRAND QUE 0
        if (indiceStockage > 0 && dSProcKGraphe != ERREUR) {    

            //ETAPE 15 : ENVOIE DES MESSAGES SOCKER DANS LE TABLEAU DES INFORMATIONS

                //GESTION ERREUR SI INDICE EST A 0
                if (indiceStockage == 0) {


                    //FEREMTURE DU DESCRIPTEUR PROCHAIN
                    close(dSProcKGraphe);

                    //FERMETURE DU DESCRIPTEUR PRECEDENT
                    close(dSProcJGraphe);

                    FD_CLR(dSProcKGraphe, &set);  //supression du processus suivant
                    FD_CLR(dSProcJGraphe, &set);  //suppression du processus precedent

                    //AFFICHAGE
                    printf("\n[PROCESSUS] Destruction des processus\n");
                    close(dSProcCS);
                    exit(1);                    //on sort du programme
                }

            //ENVOIE DU NOMBRE DE MESSAGE A ENVOYER
            printf("\n[PROCESSUS] Envoi du numéro des messages stockés");
            sendCompletTCP(dSProcCS, &indiceStockage, sizeof(int));

            //ENVOIE DE TOUS LES MESSAGES
            for (int i=0; i<indiceStockage; i++) {
                printf("\n[PROCESSUS] Envoi d'un message");
                sendCompletTCP(dSProcCS, &stockage[i], sizeof(struct infos_Graphe));
            }

            //UNE FOIS QU'ON A TOUT ENVOYER ON RECOMMENCE EN METTANT l'INDICE A 0
            indiceStockage = 0;
            printf("\n[PROCESSUS] Tableau de message envoyé");
        }

        
        //ETAPE 16 : slectionner le maximum des données dans settmp
        settmp = set;
        if (select(maxdS+1, &settmp, NULL, NULL, NULL) == ERREUR) {
            printf("\n[PROCESSUS] Problème lors du select");
            continue;
        }
        

        //BOUCLE POUR PARCOURIR TOUTES LES SOCKETS POSSIBLES
        for (int df=2; df <= maxdS; df++) {
            
            if (!FD_ISSET(df, &settmp)) {
                continue;
            }

            //SI C'EST LA SOCKET QUI DISCUTE AVEC LE SERVEUR
            if (df == dSProcCS) {

                //DONNEES
                struct infos_Graphe informations_proc;

                // ETAPE 17 : RECPETION DU MESSAGE DE LA PART DU SERVEUR QUI CONTIENT DONC l'ADRESSE DU PROCESSUS SUIVANT
                recvCompletTCP(dSProcCS, &informations_proc, sizeof(struct infos_Graphe));
                //ON PEUT MAINTENANT ARRETER LA CONNECTION AVEC LE SERVEUR
                printf("\n[PROCESSUS] Deconnexion du serveur");

                // ETAPE 18 : FERMETURE DE LA SOCKET
                if (close(dSProcCS) == -1) {
                    printf("\n[PROCESSUS] Problème lors de la fermeture du descripteur");
                }
                FD_CLR(dSProcCS, &set);        //supression de la socet serveur

                //SI LA REQUETE EST BIEN ADRESSE DU VOISINS
                if (informations_proc.requete == ADR_VOISIN) {

                    // ETAPE 19 : STOCKAGE DE L'ADRESSE
                    sockProcK = informations_proc.adrProc;             //l'adresse de la socket suivante prend la valeur de l'adresse du message
                    
                    //RECUPERATION ADRESSE ET PORT
                    char adresse[INET_ADDRSTRLEN];
                    inet_ntop(AF_INET, &sockProcK.sin_addr, adresse, INET_ADDRSTRLEN);
                    int port = htons(sockProcK.sin_port);
                    
                    //AFFICHAGE
                    printf("\n[PROCESSUS] Tentative de se connecter à l'Graphe suivant: %s:%i\n", adresse, port);


                    //ETAPE 20 : CREATION DE LA SOCKET DU PROCESSUS SUIVANT
                    dSProcKGraphe = creationSocket();

                    //ETAPE 21 : CONNECTION AVEC LADRESSE DU PROCESSU PROCHAIN
                    connexion(dSProcKGraphe, &sockProcK);       //connexion du descripteur du processus k avec une adresse recu

                    //AFFICHAGE
                    printf("\n[PROCESSUS] Connexion au prochain sous-Graphe réussie");


                    // ETAPE 22 : LANCER L'ELECTION POUR SAVOIR QUI COMMENCE
                    
                    //DONNEEES  
                    struct infos_Graphe informations_proc;                            //declaration d'une structure contenant un message correspondant à une requete
                    informations_proc.requete = ELECTION;                                 //la requete est une election
                    informations_proc.info1 = numeroSousGraphe;                           //l'information 1 est le numero du processus de l'Graphe
                    informations_proc.info2 = 1;                                          //l'information 2 est un entier 1

                    //STOCKAGE DANS LE TABLEAU stockage

                        //GESTION ERREUR DE DEPASSEMENT
                        if (indiceStockage == TAILLE_MAX_STOCK) {        //taille trop grande tableau pas assez grande
                            printf("\n[PROCESSUS] Problème, dépassement du stockage maximum de messages");
                            close(dSProcCS);
                            exit(1);
                        }

                    stockage[indiceStockage] = informations_proc;            //stocke le message à l'indice donnée en parametre dans la liste des messages
                    indiceStockage++;                                        //on incremente l'indice pour le mesages suviant
                                    
                    break;
                }
                //sinon auncune requete
                else {
                    printf("\n[PROCESSUS] Tout autre requete n'est pas possible pour cette étape");
                    break;
                }
            }


            //SI SOCKET EST LE PROCESSUS PRECEDENT
            if (df == dSProcJGraphe) {

                //SI LA CONNEION NEST PAS ENCORE EFFECTUE
                if (!connecteAuPrecedent) {

                    // ETAPE 17 : ACCEPTATION DE LA SOCKET QUI DEMANDE LA CONNECTION
                    dSProcJGraphe = accepter(dSProcJGraphe, sockProcJ);

                    //RECUPERATION DES ADRESSE
                    char adresse[INET_ADDRSTRLEN];
                    inet_ntop(AF_INET, &sockProcJ.sin_addr, adresse, INET_ADDRSTRLEN);

                    //AFFICHAGE
                    printf("\n[PROCESSUS] Connecté à l'Graphe précédent: %s", adresse);
                    FD_SET(dSProcJGraphe, &set);

                    //RECLACULATION DU MAX
                    if (maxdS < dSProcJGraphe) maxdS = dSProcJGraphe;
                    connecteAuPrecedent = TRUE;         //la connexion est effectué
                    continue;

                }
                //SI CONNEXION EST FAITE
                else{       //if (connecteAuPrecedent)

                    //ETAPE 18 : RECEPTION DES MESSAGES DU PROCESSUS PRECEDENT

                    //DONNEES
                    struct infos_Graphe informations_proc;      //messages qui comportera une requete et des inforamtions
                    int nbReception;                                //nombre de recpetion 
                    int numeroProvenance;                           //numero du processus donnée dans informations
                    int calculDeTaille;                             //calcul de la taille 

                    //RECEPTION DU NOMBRE DE RECEPTION A RECEVOIR
                    recvCompletTCP(dSProcJGraphe, &nbReception, sizeof(int));

                    //BOUCLE POUR RECEVOIR AUTANT DE MESSAGE QUE PREVU
                    for (int i=0; i<nbReception; i++) {

                        //RECEVOIR LE MESSAGE 
                        recvCompletTCP(dSProcJGraphe, &informations_proc, sizeof(struct infos_Graphe));
                        
                        //SELON LA REQUETE
                        switch (informations_proc.requete) {
                            case ELECTION:                              //si c'est une election
                                numeroProvenance = informations_proc.info1;           //le numero de provenance prend la valeur du numero de processus
                                calculDeTaille = informations_proc.info2;             //le calcul prend la valeur 1 pour l'instant
                                printf("\n[PROCESSUS] J'ai reçu un message d'élection du P°%i, le calcul de taille en cours est %i",numeroProvenance, calculDeTaille);

                                //SI MON NUMERO EST PLUS GRAND QUE LE NUMERO DE PROVENANCE JE NE DOIS RIEN ENVOYER
                                if (numeroProvenance < numeroSousGraphe) {
                                    printf("\n[PROCESSUS] Je suis un meilleur candidat, je ne renvoie pas");
                                } 
                                //SINON SI LES DEUX NUMEROS SONT EGAUX ALORS CEST MON NUMERO
                                else if (numeroProvenance == numeroSousGraphe) {
                                    printf("\n[PROCESSUS] J'ai reçu mon propre message, je suis donc l'élu, je renvoie la taille du réseau à tous");
                                    //MODIFICATION DONNEES
                                    plus_grand = TRUE;             //je deviens le chef car je viens de recevoir mon propre nuemro donc ca a été le plus grand pour tous
                                    taille_reseau = calculDeTaille;   //la taille du reseau est alors le calcul de la taille
                                    informations_proc.info1 = calculDeTaille;         //la nouvel 1 information c'est également le calcul de la taille
                                    informations_proc.requete = TAILLE_RESEAU;        //et on change la requete qui est la taille du reseau
                                    //STOCKAGE DANS LE TABLEAU stockage
                    
                                        //GESTION ERREUR DE DEPASSEMENT
                                        if (indiceStockage == TAILLE_MAX_STOCK) {        //taille trop grande tableau pas assez grande
                                            printf("\n[PROCESSUS] Problème, dépassement du stockage maximum de messages");
                                            close(dSProcCS);
                                            close(dSProcJGraphe);
                                            exit(1);
                                        }

                                    stockage[indiceStockage] = informations_proc;            //stocke le message à l'indice donnée en parametre dans la liste des messages
                                    indiceStockage++;                                        //on incremente l'indice pour le mesages suviant

                                } 
                                //SINON MON NUMERO EST PLUS PETIT DONC ON ENVOIE LE NUMERO PLUS grAND
                                else {
                                    printf("\n[PROCESSUS] C'est un meilleur candidat que moi, je renvoie");
                                    informations_proc.info2++;                          //on incremente l'information 2 qui correspond la taille du reseau
                                                                               
                                    //STOCKAGE DANS LE TABLEAU stockage
                    
                                        //GESTION ERREUR DE DEPASSEMENT
                                        if (indiceStockage == TAILLE_MAX_STOCK) {           //taille trop grande tableau pas assez grande
                                            printf("\n[PROCESSUS] Problème, dépassement du stockage maximum de messages");
                                            close(dSProcCS);
                                            close(dSProcJGraphe);
                                            exit(1);
                                        }

                                    stockage[indiceStockage] = informations_proc;            //stocke le message à l'indice donnée en parametre dans la liste des messages
                                    indiceStockage++;                                        //on incremente l'indice pour le mesages suviant

                                }
                                break;                                  //on a pas d'autre conditions
                            case TAILLE_RESEAU:                         //si c'est une taille du reseau
                                //SI JE SUIS LE CHEF
                                if (plus_grand) {                  //si je suis le chef alors la taille du reseau est connu de tous
                                    printf("\n[PROCESSUS] Le message de taille du reseau a bien fait le tour de l'Graphe");
                                }
                                //SINON JE SUIS PAS LE CHEF 
                                else {
                                    taille_reseau = informations_proc.info1;                              //la taille du reseau prend la valeur de la premiere informations
                                    printf("\n[PROCESSUS] La taille du reseau est: %i",taille_reseau);         //affichage
                                    //STOCKAGE DANS LE TABLEAU stockage
                    
                                        //GESTION ERREUR DE DEPASSEMENT
                                        if (indiceStockage == TAILLE_MAX_STOCK) {        //taille trop grande tableau pas assez grande
                                            printf("\n[PROCESSUS] Problème, dépassement du stockage maximum de messages");
                                            close(dSProcCS);
                                            close(dSProcJGraphe);
                                            exit(1);
                                        }

                                    stockage[indiceStockage] = informations_proc;            //stocke le message à l'indice donnée en parametre dans la liste des messages
                                    indiceStockage++;                                        //on incremente l'indice pour le mesages suviant

                                }
                                break;
                            default:                //sinon ca ne correspond pas a de resquete
                                printf("\n[PROCESSUS] Problème: requête non reconnue");
                                break;
                        }
                    }
                    //FIN ETAPE 18

                } 
            }
        }
    }
    */
}

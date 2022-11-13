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
#include "fonctions.c"



  /* Programme noeuds.c */

  
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
    printf("\n[NOEUD %d] \033[4mInforamtions données en paramètres :\033[0m\n", indice_proc);
    printf("\n       Adresse du serveur : %s\n       Port : %d", adresseIP, atoi(port_serveur));
    printf("\n       Port du noeud : %d", atoi(port_noeud));
    printf("\n       Indice du processus : %d\n", indice_proc);


    //ETAPE 2 : CREATION DE LA SOCKET QUI DISCUTE AVEC SERVEUR
    int dSProcCS = creationSocket();
    //AFFICHAGE
    //printf("\n[NOEUD] \033[4mSocket du noeud :\033[0m\n");
    printf("\n\n[NOEUD %d] Création de la socket réussie\n", indice_proc);
    printf("[NOEUD %d] Le descripteur du noeud est %d \n", indice_proc, dSProcCS);


    //aparté pour construire la socket en ecoute
  
    //ETAPE 3 : DESIGNATION DE LA SOCKET SERVEUR
    struct sockaddr_in sockServ = designationSocket(adresseIP, port_serveur);
    //AFFICHAGE
        //addresse
    char adrServ[INET_ADDRSTRLEN];
    inet_ntop(AF_INET, &sockServ.sin_addr, adrServ, INET_ADDRSTRLEN);
        //port
    int portServ = htons((short) sockServ.sin_port);
    printf("\n[NOEUD %d] Designation de la socket du serveur réussi\n", indice_proc);
    printf("[NOEUD %d] Le serveur a donc pour IP %s:%d\n", indice_proc, adrServ, portServ);


    //ETAPE 4 : DEMANDE DE CONNECTION DE LA SOCKET A L'ADRESSE
    connexion(dSProcCS, &sockServ);
    //AFFICHAGE
    printf("\n[NOEUD %d] Connexion au serveur réussi !\n", indice_proc);


    //ETAPE 5 : CREATION de LA SOCKET ARETE
    int dSProcArete = creationSocket();
    //AFFICHAGE
    //printf("\n[NOEUD] \033[4mSocket du noeud :\033[0m\n");
    printf("\n[NOEUD %d] Création de la socket réussie\n", indice_proc);
    printf("[NOEUD %d] Le descripteur du noeud est %d \n", indice_proc, dSProcArete);



    //ETAPE 6 : NOMMAGE DE LA SOCKET
    struct sockaddr_in sockArete = nommageSocket(dSProcArete, port_noeud);
    //AFFICHAGE
        //adresse
    char adrArete[INET_ADDRSTRLEN];
    inet_ntop(AF_INET, &sockArete.sin_addr, adrArete, INET_ADDRSTRLEN);
        //port
    int portArete = htons((short) sockArete.sin_port);
        //affichage
    printf("\n[NOEUD %d] Nommage de la socket du noeud réussi\n", indice_proc);
    printf("[NOEUD %d] Les informations du noeud par la socket de descripteur %d : %s:%i\n", indice_proc, dSProcArete, adrArete, portArete);



    //ETAPE 7 : MISE SOUS ECOUTE DE LA SOCKET
    int nbMaxAttente = NOEUDS_MAX;
    ecouter(dSProcArete, nbMaxAttente);
        //AFFICHAGE
    printf("\n[NOEUD %d] La mise en ecoute de la socket !\n", indice_proc);


    //on renvient a la socket serveur
  
    //ETAPE 8 : ENVOIE DES INFORMATIONS AU SERVEUR
        //inforamtions du noeud
    struct infos_Graphe informations_proc;      		//declare la structure qu'on va envoyer au serveur
    informations_proc.requete = ADR_PROC;           //requete d'une adresse
    informations_proc.numero = indice_proc;         //donne l'indice du processus
    informations_proc.descripteur = dSProcArete;  	//donne le descripteur
    informations_proc.adrProc = sockArete;          //donner l'adresse de la socket
        //envoie
    sendCompletTCP(dSProcCS, &informations_proc, sizeof(struct infos_Graphe));
    
    //AFFICHAGE
    printf("\n[NOEUD %d] Envoi des inforamtions réussi !\n", indice_proc);
    printf("[NOEUD %d] \033[4mEnvoie des inforamtions suivantes :\033[0m\n", indice_proc);
    printf("\n       Adresse du processus : %s\n       Port : %d", adrArete, portArete);
    printf("\n       Numéro du noeud : %d\n       Descripteur de la socket du processus : %d\n\n", indice_proc, dSProcArete);


  
    //ETAPE 10 : RECEPTION DU NB VOISIN
    struct nbVois nbVoisin;
    recvCompletTCP(dSProcCS, &nbVoisin, sizeof(struct nbVois));  //on reutilise la structure informations_proc pour la recepetion
		int nbVoisinTotal = nbVoisin.nbVoisinTotal;
		int nbVoisinDemande = nbVoisin.nbVoisinDemande;
    int nbVoisinAttente = nbVoisinTotal - nbVoisinDemande;
		//AFFICHAGE
    printf("\n[NOEUD %d] Reception du nombre de voisin réussi\n", indice_proc);
    printf("	Nombre de voisin total : %d\n", nbVoisinTotal);
		printf("	Nombre de voisin auquels envoyer une demande de connexion : %d\n\n", nbVoisinDemande);

	

	
    //ETAPE 11 : RECEPTIONS DES INFORAMTIONS DES VOISINS A QUI TU DEMANDE UNE CONNEXION
    	//DONNÉES tous les voisins auxquels on doit se connecter
    struct infos_Graphe* info_voisins = (struct infos_Graphe*) malloc(nbVoisinDemande * sizeof(struct infos_Graphe)); //tableau des informations des voisins du noeud courant
		//BOUCLE de autant de voisin que tu a  
    for (int v=0; v<nbVoisin.nbVoisinDemande; v++) {
        	//données
				struct infos_Graphe info_voisin_courant;      //structure du voisin courant
					//reception
		    recvCompletTCP(dSProcCS, &info_voisin_courant, sizeof(struct infos_Graphe));
					//ajout dans le tableau
				info_voisins[v] = info_voisin_courant;
				//AFFICHAGE
				if (DEBUG > 2) {
						printf("[NOEUD %d] -> connexion à %d\n", indice_proc, info_voisin_courant.numero);	
				}
		}

		

			//ETAPE 8 : FERMETURE DE LA SOCKET SERVEUR CAR PLUS BESOIN
          //fermeture
      close(dSProcCS);
          //affichage
			if (DEBUG > 2) {printf("\n[NOEUD %d] Je peux m'en aller !\n", indice_proc);}
      
      


				

		//ETAPE 12 : revenir sur la socket d'ecoute elle sera notre serveur pour les autres clients dSProcArete
	
				//ACCEPTER LES CONNEXION
	
		// ETAPE 4 : MISE EN PLACE DES DONNES
      //donnees util
    int dSVoisinEntrant;                                                        //declaration du descripteur
    //int nbMaxdS = dSProcArete;                                                  //maximum des descripteurs
    //int nbMaxNoeud = NOEUDS_MAX;                                              //on declare un maximum de noeuds possibles
    //struct infos_Graphe *procVoisinEcoute = (struct infos_Graphe*) malloc(nbVoisinAttente * sizeof(struct infos_Graphe));         //on declare un tableau de structure pour les informations des Noeuds connecté au sevreur
    struct sockaddr_in sockVoisinAttente;                                               //on declare la socket du Noeud
    socklen_t lgAdr;                                                            //taille de l'adresse
    


    //BOUCLE POUR RECEVOIR LES CLIENT
     for (int numVoisin=1; numVoisin<=nbVoisinAttente; numVoisin++) {

        //ETAPE 4 : ACCEPTATION DU NOEUD
            //acceptation
        dSVoisinEntrant = accept(dSProcArete, (struct sockaddr*)&sockVoisinAttente, &lgAdr);          //on accepte le Noeud qui demande

        //AFFICHAGE
            //affichage
        printf("\n[NOEUD %d] Connexion d'un Noeud de descripteur %d\n", indice_proc, dSVoisinEntrant);
       if (DEBUG > 3) { 
			 		if (numVoisin < 2) {printf("[NOEUD %d] %d Noeud est connecté au noeud en ecoute", indice_proc, numVoisin);}  //affichage du nombre de Noeud connecté
	        else {printf("[NOEUD %d] %d Noeuds sont connectés au noeud en ecoute", indice_proc, numVoisin);}       //affichage du nombre de Noeud connecté
				}

        //ETAPE 5 : RECEPTION DES INFORMATIONS DDES VOISINS ENTRANT
            //donnees        
        int numero_Voisin;                      // entier qui va etre le numero du noeud qui se connecte
            //reception
        recvCompletTCP(dSVoisinEntrant, &numero_Voisin, sizeof(int));       // reception de l'entier dans numero_Voisin
        
        //AFFICHAGE
        printf("\n[NOEUD %d] Le noeud %d est notre voisin\n", indice_proc, numero_Voisin);

        //ENVOIE ACCUSE DE RECEPTION
        /* int dS_courant = procVoisin[indice_proc].descripteur;                  //indice courant
          //envoi
        sendCompletTCP(dS_courant, &nbVoisin[indice_proc], sizeof(struct nbVois));
					//AFFICHAGE
			 	printf("\n[NOEUD %d] Informations envoyées \n", indice_proc);
       */
        printf("\n***********************************\n");
    } //fin de la premieère connexion avec tous les noeuds


	
			//ETAPE 13 : je cree une socket pour demander une connection
	
				//DEMANDER LES CONNEXION

			// ETAPE 4 : MISE EN PLACE DES DONNES
    	  //donnees util
	    //int nbMaxdS = dSProcArete;                                                  //maximum des descripteurs
	    //int nbMaxNoeud = NOEUDS_MAX;                                              //on declare un maximum de noeuds possibles
	    //struct infos_Graphe*procVoisinDemande = (struct infos_Graphe*) malloc(nbVoisinDemande * sizeof(struct infos_Graphe)); 
	    int dSVoisinDemande;
	
	
	    //BOUCLE POUR DEMANDER AU CLIENT
     for (int numVoisin=0; numVoisin<nbVoisinDemande; numVoisin++) {

				 //ETAPE 2 : CREATION DE LA SOCKET QUI DISCUTE AVEC PROCESSU VOISIN
	    	dSVoisinDemande = creationSocket();
	    	//AFFICHAGE
		    //printf("\n[NOEUD] \033[4mSocket du noeud :\033[0m\n");
		    printf("\n\n[NOEUD %d] Création de la socket réussie\n", indice_proc);
		    printf("[NOEUD %d] Le descripteur du noeud est %d \n", indice_proc, dSVoisinDemande);
		
		
		    //aparté pour construire la socket en ecoute
		  
		    //ETAPE 3 : DESIGNATION DE LA SOCKET SERVEUR
		    struct sockaddr_in sockVoisinDemande = info_voisins[numVoisin].adrProc;
		    //AFFICHAGE
			 	if (DEBUG > 2) {
		        //addresse
		    	char adrDem[INET_ADDRSTRLEN];
			    inet_ntop(AF_INET, &sockVoisinDemande.sin_addr, adrDem, INET_ADDRSTRLEN);
			        //port
			    int portDem = htons((short) sockVoisinDemande.sin_port);
			    printf("\n[NOEUD %d] Designation de la socket du voisin réussi\n", indice_proc);
			    printf("[NOEUD %d] Le voisin a donc pour IP %s:%d\n", indice_proc, adrDem, portDem);
				}
		
		    //ETAPE 4 : DEMANDE DE CONNECTION DE LA SOCKET A L'ADRESSE
		    connexion(dSVoisinDemande, &sockVoisinDemande);
		    //AFFICHAGE
		    printf("\n[NOEUD %d] Connexion au voisin %d réussie !\n", indice_proc, info_voisins[numVoisin].numero);


		 		//ETAPE 5 : Envoie d'un message je suis moi
				 sendCompletTCP(dSVoisinDemande, &indice_proc, sizeof(int));
				 printf("[NOEUD %d] Envoi de mon indice\n", indice_proc);

			}

	
      
		sleep(10);

			//ETAPE 8 : FERMETURE DE LA SOCKET CLIENTE AR PLUS BESOIN
          //fermeture
      close(dSVoisinDemande);
			close(dSVoisinEntrant);
          //affichage
			if (DEBUG > 2) {printf("\n[NOEUD %d] Je peux m'en aller !\n", indice_proc);}
      
      







} // Fin du main








	
  //ETAPE 12 : CONNEXION AUX AUTRES VOISINS
/*
    //ETAPE 11 : CHANGEMENT DE DONNEES
    int numeroSousGraphe = informations_proc.info1;  //le numero du sous Graphe prend la valeur de l'information 1 du message recu
    printf("\n[NOEUD %d] J'ai reçu mon numéro de sous Graphe", indice_proc);


    //ETAPE 12 : CREATION DE L'ADRESSE DU NOEUD SUIVANT
    fd_set set, settmp;
    FD_ZERO(&set);                              //on reinitialise 
    FD_SET(dSProcCS, &set);                     //ajout de la socket serveur
    FD_SET(dSProcArete, &set);                //ajout de la socket preedente
    struct sockaddr_in sockProcK;               //structure de son adresse
    //socklen_t lgAdr;                          //taille de l'adresse


    //ETAPE 13 : CALCUL DU MAXIMUM DES DESCRIPTEUR
    int maxdS = dSProcCS;                                 //declaration max des descripteur
    if (dSProcArete > dSProcCS) maxdS = dSProcArete;    //calcul du maximum par rapport au processus precedent
    

    //ETAEP 14 : MODIFICATION DES DONNEES
    int dSProcKGraphe = ERREUR;                                     //descripteur du processus suivant pour l'instant c'est une erreur

    int connecteAuPrecedent = FALSE;                                //declare un boolean qui dit si la connexion est ok ou non
    int taille_reseau = -1;                                         //on declare la taille du reseau
    int plus_grand = FALSE;                                         //on declare un boolean qui dit si le processus est le chef ou non

    struct infos_Graphe stockage[TAILLE_MAX_STOCK];                 //declaration du tableau des informations avec les reuetes
    int indiceStockage = 0;                                         //et on declare l'indice du stockage


    while (TRUE) {

        //SI LA SOCKET DU NOEUD PROCHAIN EXISTE ET QUE L'INDICE OU STOCKER LES MESSAGES EST PLUS GRAND QUE 0
        if (indiceStockage > 0 && dSProcKGraphe != ERREUR) {    

            //ETAPE 15 : ENVOIE DES MESSAGES SOCKER DANS LE TABLEAU DES INFORMATIONS

                //GESTION ERREUR SI INDICE EST A 0
                if (indiceStockage == 0) {


                    //FEREMTURE DU DESCRIPTEUR PROCHAIN
                    close(dSProcKGraphe);

                    //FERMETURE DU DESCRIPTEUR PRECEDENT
                    close(dSProcArete);

                    FD_CLR(dSProcKGraphe, &set);  //supression du processus suivant
                    FD_CLR(dSProcArete, &set);  //suppression du processus precedent

                    //AFFICHAGE
                    printf("\n[NOEUD] Destruction des processus\n");
                    close(dSProcCS);
                    exit(1);                    //on sort du programme
                }

            //ENVOIE DU NOMBRE DE MESSAGE A ENVOYER
            printf("\n[NOEUD] Envoi du numéro des messages stockés");
            sendCompletTCP(dSProcCS, &indiceStockage, sizeof(int));

            //ENVOIE DE TOUS LES MESSAGES
            for (int i=0; i<indiceStockage; i++) {
                printf("\n[NOEUD] Envoi d'un message");
                sendCompletTCP(dSProcCS, &stockage[i], sizeof(struct infos_Graphe));
            }

            //UNE FOIS QU'ON A TOUT ENVOYER ON RECOMMENCE EN METTANT l'INDICE A 0
            indiceStockage = 0;
            printf("\n[NOEUD] Tableau de message envoyé");
        }

        
        //ETAPE 16 : slectionner le maximum des données dans settmp
        settmp = set;
        if (select(maxdS+1, &settmp, NULL, NULL, NULL) == ERREUR) {
            printf("\n[NOEUD] Problème lors du select");
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

                // ETAPE 17 : RECPETION DU MESSAGE DE LA PART DU SERVEUR QUI CONTIENT DONC l'ADRESSE DU NOEUD SUIVANT
                recvCompletTCP(dSProcCS, &informations_proc, sizeof(struct infos_Graphe));
                //ON PEUT MAINTENANT ARRETER LA CONNECTION AVEC LE SERVEUR
                printf("\n[NOEUD] Deconnexion du serveur");

                // ETAPE 18 : FERMETURE DE LA SOCKET
                if (close(dSProcCS) == -1) {
                    printf("\n[NOEUD] Problème lors de la fermeture du descripteur");
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
                    printf("\n[NOEUD] Tentative de se connecter à l'Graphe suivant: %s:%i\n", adresse, port);


                    //ETAPE 20 : CREATION DE LA SOCKET DU NOEUD SUIVANT
                    dSProcKGraphe = creationSocket();

                    //ETAPE 21 : CONNECTION AVEC LADRESSE DU PROCESSU PROCHAIN
                    connexion(dSProcKGraphe, &sockProcK);       //connexion du descripteur du processus k avec une adresse recu

                    //AFFICHAGE
                    printf("\n[NOEUD] Connexion au prochain sous-Graphe réussie");


                    // ETAPE 22 : LANCER L'ELECTION POUR SAVOIR QUI COMMENCE
                    
                    //DONNEEES  
                    struct infos_Graphe informations_proc;                            //declaration d'une structure contenant un message correspondant à une requete
                    informations_proc.requete = ELECTION;                                 //la requete est une election
                    informations_proc.info1 = numeroSousGraphe;                           //l'information 1 est le numero du processus de l'Graphe
                    informations_proc.info2 = 1;                                          //l'information 2 est un entier 1

                    //STOCKAGE DANS LE TABLEAU stockage

                        //GESTION ERREUR DE DEPASSEMENT
                        if (indiceStockage == TAILLE_MAX_STOCK) {        //taille trop grande tableau pas assez grande
                            printf("\n[NOEUD] Problème, dépassement du stockage maximum de messages");
                            close(dSProcCS);
                            exit(1);
                        }

                    stockage[indiceStockage] = informations_proc;            //stocke le message à l'indice donnée en parametre dans la liste des messages
                    indiceStockage++;                                        //on incremente l'indice pour le mesages suviant
                                    
                    break;
                }
                //sinon auncune requete
                else {
                    printf("\n[NOEUD] Tout autre requete n'est pas possible pour cette étape");
                    break;
                }
            }


            //SI SOCKET EST LE NOEUD PRECEDENT
            if (df == dSProcArete) {

                //SI LA CONNEION NEST PAS ENCORE EFFECTUE
                if (!connecteAuPrecedent) {

                    // ETAPE 17 : ACCEPTATION DE LA SOCKET QUI DEMANDE LA CONNECTION
                    dSProcArete = accepter(dSProcArete, sockArete);

                    //RECUPERATION DES ADRESSE
                    char adresse[INET_ADDRSTRLEN];
                    inet_ntop(AF_INET, &sockArete.sin_addr, adresse, INET_ADDRSTRLEN);

                    //AFFICHAGE
                    printf("\n[NOEUD] Connecté à l'Graphe précédent: %s", adresse);
                    FD_SET(dSProcArete, &set);

                    //RECLACULATION DU MAX
                    if (maxdS < dSProcArete) maxdS = dSProcArete;
                    connecteAuPrecedent = TRUE;         //la connexion est effectué
                    continue;

                }
                //SI CONNEXION EST FAITE
                else{       //if (connecteAuPrecedent)

                    //ETAPE 18 : RECEPTION DES MESSAGES DU NOEUD PRECEDENT

                    //DONNEES
                    struct infos_Graphe informations_proc;      //messages qui comportera une requete et des inforamtions
                    int nbReception;                                //nombre de recpetion 
                    int numeroProvenance;                           //numero du processus donnée dans informations
                    int calculDeTaille;                             //calcul de la taille 

                    //RECEPTION DU NOMBRE DE RECEPTION A RECEVOIR
                    recvCompletTCP(dSProcArete, &nbReception, sizeof(int));

                    //BOUCLE POUR RECEVOIR AUTANT DE MESSAGE QUE PREVU
                    for (int i=0; i<nbReception; i++) {

                        //RECEVOIR LE MESSAGE 
                        recvCompletTCP(dSProcArete, &informations_proc, sizeof(struct infos_Graphe));
                        
                        //SELON LA REQUETE
                        switch (informations_proc.requete) {
                            case ELECTION:                              //si c'est une election
                                numeroProvenance = informations_proc.info1;           //le numero de provenance prend la valeur du numero de processus
                                calculDeTaille = informations_proc.info2;             //le calcul prend la valeur 1 pour l'instant
                                printf("\n[NOEUD] J'ai reçu un message d'élection du P°%i, le calcul de taille en cours est %i",numeroProvenance, calculDeTaille);

                                //SI MON NUMERO EST PLUS GRAND QUE LE NUMERO DE PROVENANCE JE NE DOIS RIEN ENVOYER
                                if (numeroProvenance < numeroSousGraphe) {
                                    printf("\n[NOEUD] Je suis un meilleur candidat, je ne renvoie pas");
                                } 
                                //SINON SI LES DEUX NUMEROS SONT EGAUX ALORS CEST MON NUMERO
                                else if (numeroProvenance == numeroSousGraphe) {
                                    printf("\n[NOEUD] J'ai reçu mon propre message, je suis donc l'élu, je renvoie la taille du réseau à tous");
                                    //MODIFICATION DONNEES
                                    plus_grand = TRUE;             //je deviens le chef car je viens de recevoir mon propre nuemro donc ca a été le plus grand pour tous
                                    taille_reseau = calculDeTaille;   //la taille du reseau est alors le calcul de la taille
                                    informations_proc.info1 = calculDeTaille;         //la nouvel 1 information c'est également le calcul de la taille
                                    informations_proc.requete = TAILLE_RESEAU;        //et on change la requete qui est la taille du reseau
                                    //STOCKAGE DANS LE TABLEAU stockage
                    
                                        //GESTION ERREUR DE DEPASSEMENT
                                        if (indiceStockage == TAILLE_MAX_STOCK) {        //taille trop grande tableau pas assez grande
                                            printf("\n[NOEUD] Problème, dépassement du stockage maximum de messages");
                                            close(dSProcCS);
                                            close(dSProcArete);
                                            exit(1);
                                        }

                                    stockage[indiceStockage] = informations_proc;            //stocke le message à l'indice donnée en parametre dans la liste des messages
                                    indiceStockage++;                                        //on incremente l'indice pour le mesages suviant

                                } 
                                //SINON MON NUMERO EST PLUS PETIT DONC ON ENVOIE LE NUMERO PLUS grAND
                                else {
                                    printf("\n[NOEUD] C'est un meilleur candidat que moi, je renvoie");
                                    informations_proc.info2++;                          //on incremente l'information 2 qui correspond la taille du reseau
                                                                               
                                    //STOCKAGE DANS LE TABLEAU stockage
                    
                                        //GESTION ERREUR DE DEPASSEMENT
                                        if (indiceStockage == TAILLE_MAX_STOCK) {           //taille trop grande tableau pas assez grande
                                            printf("\n[NOEUD] Problème, dépassement du stockage maximum de messages");
                                            close(dSProcCS);
                                            close(dSProcArete);
                                            exit(1);
                                        }

                                    stockage[indiceStockage] = informations_proc;            //stocke le message à l'indice donnée en parametre dans la liste des messages
                                    indiceStockage++;                                        //on incremente l'indice pour le mesages suviant

                                }
                                break;                                  //on a pas d'autre conditions
                            case TAILLE_RESEAU:                         //si c'est une taille du reseau
                                //SI JE SUIS LE CHEF
                                if (plus_grand) {                  //si je suis le chef alors la taille du reseau est connu de tous
                                    printf("\n[NOEUD] Le message de taille du reseau a bien fait le tour de l'Graphe");
                                }
                                //SINON JE SUIS PAS LE CHEF 
                                else {
                                    taille_reseau = informations_proc.info1;                              //la taille du reseau prend la valeur de la premiere informations
                                    printf("\n[NOEUD] La taille du reseau est: %i",taille_reseau);         //affichage
                                    //STOCKAGE DANS LE TABLEAU stockage
                    
                                        //GESTION ERREUR DE DEPASSEMENT
                                        if (indiceStockage == TAILLE_MAX_STOCK) {        //taille trop grande tableau pas assez grande
                                            printf("\n[NOEUD] Problème, dépassement du stockage maximum de messages");
                                            close(dSProcCS);
                                            close(dSProcArete);
                                            exit(1);
                                        }

                                    stockage[indiceStockage] = informations_proc;            //stocke le message à l'indice donnée en parametre dans la liste des messages
                                    indiceStockage++;                                        //on incremente l'indice pour le mesages suviant

                                }
                                break;
                            default:                //sinon ca ne correspond pas a de resquete
                                printf("\n[NOEUD] Problème: requête non reconnue");
                                break;
                        }
                    }
                    //FIN ETAPE 18

                } 
            }
        }
    }
    */

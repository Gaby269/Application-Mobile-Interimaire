#include "fonctions.c"



void * AcceptationDuNoeud (void * p){     //un thread représente un voisin qui va nous contacter

    //GESTION PARAMETRE DE LA STRUTURE
        //arguments
    struct paramsNoeud * args = (struct paramsNoeud *) p;
				//infoVoisin
		int dSVois =  args->dSVoisin;                 //descripteur du noedu
		int indice_vois = args->indice_vois;					//indice du voisins
    		//infomoi
		int numeroMoi = args->numero_proc;					//recup du numero de moi
				//thread
    //pthread_t threadCourant = pthread_self();                    //identifiant du threas

		//AFFICHAGE
		printColorPlus(numeroMoi, "ACCEPTATION");printf("du %d-ème voisin\n", indice_vois);

		//RECEPTION DES INFORMATIONS DDES VOISINS ENTRANT
		int numero_Voisin;                                         // entier qui va etre le numero du noeud qui se connecte
				//reception
		recvCompletTCP(dSVois, &numero_Voisin, sizeof(int));       // reception de l'entier dans numero_Voisin
		
		//AFFICHAGE
		printColorPlus(numeroMoi, "NOEUD");printf("%d est mon voisin\n", numero_Voisin);

		args->numero_vois = numero_Voisin;

		//ENVOIE ACCUSE DE RECEPTION
		/* int dS_courant = procVoisin[numero_noeud-1].descripteur;                  //indice courant
			//envoi
		sendCompletTCP(dS_courant, &nbVoisin[numero_noeud-1], sizeof(struct nbVois));
		//AFFICHAGE
		prrintColor(numero_noeud);printf("\n");printf("Informations envoyées \n", numero_noeud);
       */

		pthread_exit(NULL); // fortement recommandé.

	}



void * ConnexionAuNoeud (void * p){    //un thread représente un voisin que l'on va contacter

    //GESTION PARAMETRE DE LA STRUTURE
        //arguments
    struct paramsNoeud * args = (struct paramsNoeud *) p;
				//infoVoisin
		int dSVois =  args->dSVoisin;                //descripteur du noeud
		int indice_vois = args->indice_vois;				 //indice du voisins sortant
		int numero_vois = args->numero_vois;				//numero voisin
    		//infomoi
		int numeroMoi = args->numero_proc;					//recup du numero de moi

		//AFFICHAGE
		printColorPlus(numeroMoi, "DEMANDE");printf("de connexion au %d-ème voisin réussie !\n", indice_vois);
	
		//ETAPE 11 : ENVOIE D'UN MESSAGE JE SUIS MOI
		sendCompletTCP(dSVois, &numeroMoi, sizeof(int));
				//affichage
		printColorPlus(numeroMoi, "ENVOIE");printf("de mon indice à %d\n", numero_vois);

		pthread_exit(NULL); // fortement recommand�.

	}





/////////////////////////
//   PROGRAME NOEUDS   //
/////////////////////////
int main(int argc, char *argv[]) {

    //GESTION DES INFORMATIONS
    if (argc != 5){
        printf("\n[UTILISATION] %s ip_serveur port_serveur port_noeud numero_noeud\n\n", argv[0]);
        exit(1);
    }

    char* adresseIP = argv[1];          //adresse ip du serveur
    char* port_serveur = argv[2];       //port du serveur
    char* port_noeud = argv[3];         //port du noeud
    int numero_noeud = atoi(argv[4]);    //indice du client


    //AFFICHAGE DES DONNEES EN PARAMETRES
    printColorNoeud("\n************************************************\n------------------- NOEUD ", numero_noeud, " -------------------\n************************************************\n\n");
    if (DEBUG > 3){
        printColor(numero_noeud);printf("\033[4mInforamtions données en paramètres :\033[0m\n");
        printf("\n       Adresse du serveur : %s\n       Port : %d", adresseIP, atoi(port_serveur));
        printf("\n       Port du noeud : %d", atoi(port_noeud));
        printf("\n       Indice du processus : %d", numero_noeud);
    }


    //ETAPE 1 : CREATION DE LA SOCKET QUI DISCUTE AVEC SERVEUR
    int dSProcCS = creationSocket();
        //affichage
    printColor(numero_noeud);printf("Création de la socket pour serveur réussie !\n");
    printColor(numero_noeud);printf("Le descripteur du noeud est %d \n\n", dSProcCS);

  
    //ETAPE 2 : DESIGNATION DE LA SOCKET SERVEUR
    struct sockaddr_in sockServ = designationSocket(adresseIP, port_serveur);
    //AFFICHAGE
        //addresse
    char adrServ[INET_ADDRSTRLEN];
    inet_ntop(AF_INET, &sockServ.sin_addr, adrServ, INET_ADDRSTRLEN);
        //port
    int portServ = htons((short) sockServ.sin_port);
    printColor(numero_noeud);printf("Designation de la socket du serveur réussi ! \n");
    printColor(numero_noeud);printf("Le serveur a donc pour IP %s:%d\n\n", adrServ, portServ);


    //ETAPE 3 : DEMANDE DE CONNEXION AU SERVEUR
    connexion(dSProcCS, &sockServ);
    //AFFICHAGE
    printColor(numero_noeud);printf("Connexion au serveur réussi !\n\n");




    //Creation d'une autre socket qui va jouer le rôle de serveur pour ses voisins qui demande une connection

    //ETAPE 4 : CREATION de LA SOCKET ARETE
    int dSProcArete = creationSocket();
    //AFFICHAGE
    //printf("\n[NOEUD] \033[4mSocket du noeud :\033[0m\n");
    printColor(numero_noeud);printf("Création de la socket d'écoute réussi !\n");
    printColor(numero_noeud);printf("Le descripteur du noeud est %d \n\n", dSProcArete);

    //NOMMAGE DE LA SOCKET
    struct sockaddr_in sockArete = nommageSocket(dSProcArete, port_noeud);
    //AFFICHAGE
        //adresse
    char adrArete[INET_ADDRSTRLEN];
    inet_ntop(AF_INET, &sockArete.sin_addr, adrArete, INET_ADDRSTRLEN);
        //port
    int portArete = htons((short) sockArete.sin_port);
        //affichage
    printColor(numero_noeud);printf("Informations du noeud (socket) de descripteur %d : %s:%i\n\n", dSProcArete, adrArete, portArete);


    //ETAPE 5 : MISE SOUS ECOUTE DE LA SOCKET
    int nbMaxAttente = NOEUDS_MAX;
    ecouter(dSProcArete, nbMaxAttente);
        //AFFICHAGE
    printColor(numero_noeud);printf("La mise en ecoute de la socket réussi !\n\n");

    // On vient de finir de mettre en ecoute une socket qui va avoir le rôle de serveur pour ses voisins qui demande une connection




    //on renvient alors a la socket qui discute avec le serveur
  
    //ETAPE 6 : ENVOIE DES INFORMATIONS AU SERVEUR
        //inforamtions du noeud
    struct infos_Graphe informations_proc;      	//declare la structure qu'on va envoyer au serveur
    informations_proc.numero = numero_noeud;         //donne l'indice du processus
    informations_proc.descripteur = dSProcArete;  	//donne le descripteur
    informations_proc.adrProc = sockArete;          //donner l'adresse de la socket
        //envoie
    sendCompletTCP(dSProcCS, &informations_proc, sizeof(struct infos_Graphe));
        //affichage
    printColor(numero_noeud);printf("Envoi des inforamtions réussi au serveur !\n");
    printColor(numero_noeud);printf("\033[4mEnvoie des informations suivantes :\033[0m\n");
    printf("\n       Adresse du processus : %s\n       Port : %d", adrArete, portArete);
    printf("\n       Numéro du noeud : %d\n       Descripteur de la socket du processus : %d\n\n\n", numero_noeud, dSProcArete);


  
    //ETAPE 7 : RECEPTION DU NB VOISIN
    struct nbVois nbVoisin;
        //reception
    recvCompletTCP(dSProcCS, &nbVoisin, sizeof(struct nbVois));  //on reutilise la structure informations_proc pour la recepetion
        //données
    int nbVoisinTotal = nbVoisin.nbVoisinTotal;
    int nbVoisinDemande = nbVoisin.nbVoisinDemande;
    int nbVoisinAttente = nbVoisinTotal - nbVoisinDemande;
		//affichage
    printColor(numero_noeud);printf("Reception du nombre de voisin réussi ! \n");
    printf("	Nombre de voisin total : %d\n", nbVoisinTotal);
		printf("	Nombre de voisin auquels envoyer une demande de connexion : %d\n\n", nbVoisinDemande);   //ajout d'une delimitation

		printf("\n************************************************\n************************************************\n\n\n");   

	
    //ETAPE 8 : RECEPTIONS DES INFORAMTIONS DES VOISINS A QUI TU DEMANDE UNE CONNEXION
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
            //affichage
        if (DEBUG > 2) {
            printColorPlus(numero_noeud, "RECEPTION");printf("-> connexion à %d\n", info_voisin_courant.numero);	
        }
    }

		

    //FERMETURE DE LA SOCKET QUI DISCUTE AVEC LE SERVEUR CAR PLUS BESOIN
        //fermeture
    close(dSProcCS);
        //affichage
    if (DEBUG > 2) {
        //printf("\n");
        printColorPlus(numero_noeud, "FERMETURE");
        printf("Je me déconnecte du serveur !\n");
    }
    
    
    //Fin de la communication entre le client et le serveur


				

    //On revient maintenant sur la socket qu'on a mise en écoute au début du fichier nommé dSProcArete   

        //////////////////////////
        // DEMANDE DE CONNEXION //
        //////////////////////////

    //ETAPE 9 : ENVOI DES DEMANDES DE CONNEXION
        // donnée utile
		pthread_t threadsCo[nb_sommets];                                              //creation d'un tableau de thread
  	for (int i=0; i<nb_sommets; i++){
        threadsCo[i] = 0;
    }
		int dSVoisinDemande;
	  //arguments pour threads
    struct paramsInter1 * argsCo = (struct paramsInter1*)malloc(sizeof(struct paramsInter1)); //aloue de la memoire pour les arguments


		//BOUCLE POUR DEMANDER AU CLIENT
    for (int numVoisin=1; numVoisin<=nbVoisinDemande; numVoisin++) {

        //CREATION DE LA SOCKET QUI DISCUTE AVEC PROCESSU VOISIN
        dSVoisinDemande = creationSocket();
            //affichage
        printColorPlus(numero_noeud, "CREATION");printf("de la socket qui demande réussi !\n");
        printColorPlus(numero_noeud, "DESCRIPTEUR");printf("du noeud créé est %d\n", dSVoisinDemande);
    

        //DESIGNATION DE LA SOCKET SERVEUR
        struct sockaddr_in sockVoisinDemande = info_voisins[numVoisin-1].adrProc;                 //recuperation du voisin qui veut demander la connection dans le tableau des voisins car il n'y a que les noeuds qui doivent demander une connection
        //AFFICHAGE
				printColorPlus(numero_noeud, "DESIGNATION");printf("de la socket du voisin réussi\n");

			
        //ETAPE 10 : DEMANDE DE CONNEXION DE LA SOCKET A L'ADRESSE
        connexion(dSVoisinDemande, &sockVoisinDemande);

			    //ARGS
        argsCo->numero_proc = numero_noeud;
				argsCo->numero_vois = info_voisins[numVoisin-1].numero;
			 	argsCo->indice_vois = numVoisin-1;
			 	argsCo->dSVoisin = dSVoisinDemande;

			
				//AFFICHAGE
						//addresse
				char adrDem[INET_ADDRSTRLEN];
				inet_ntop(AF_INET, &sockVoisinDemande.sin_addr, adrDem, INET_ADDRSTRLEN);
						//port
				int portDem = htons((short) sockVoisinDemande.sin_port);
						//affichage
				printColorPlus(numero_noeud, "ADRESSE");printf("du voisin a qui je demande est %s:%d\n", adrDem, portDem);
				printColorPlus(numero_noeud, "CREATION THREAD");printf("pour le noeud d'indice %d\n", numero_noeud, numVoisin-1);
				//CREATION DU THREAD POUR LE CLIENT QUI SE CONNECTE
			 	creationThread(&threadsCo[numVoisin-1], argsCo, ConnexionAuNoeud);    //creation du thread

	}//fin des demandes



        /////////////////////////////
        // ACCEPTER LES CONNEXIONS //
        /////////////////////////////

    //MISE EN PLACE DES DONNES
        //donnees util
    int dSVoisinEntrant;                                                        //declaration du descripteur
    struct sockaddr_in sockVoisinAccept;                                       //on declare la socket du Noeud
    socklen_t lgAdr;                                                            //taille de l'adresse
	  pthread_t threadsAcc[nb_sommets];                                              //creation d'un tableau de thread
    for (int i=0; i<nb_sommets; i++){
        threadsAcc[i] = 0;
    }
	  //arguments pour threads
    struct paramsInter1 * argsAcc = (struct paramsInter1*)malloc(sizeof(struct paramsInter1)); //aloue de la memoire pour les arguments

    //BOUCLE POUR RECEVOIR LES CLIENT
     for (int numVoisin=1; numVoisin<=nbVoisinAttente; numVoisin++) {

        //ETAPE 12 : ACCEPTATION DU NOEUD
            //acceptation
        dSVoisinEntrant = accept(dSProcArete, (struct sockaddr*)&sockVoisinAccept, &lgAdr);          //on accepte le Noeud qui demande

        	//ARGS
        argsAcc->numero_proc = numero_noeud;
			 	argsAcc->indice_vois = numVoisin-1;
			 	argsAcc->dSVoisin = dSVoisinEntrant;
			 
        
			 	//AFFICHAGE
			 		//adresse
				char adrAcc[INET_ADDRSTRLEN];
				inet_ntop(AF_INET, &sockVoisinAccept.sin_addr, adrAcc, INET_ADDRSTRLEN);
						//port
				int portAcc = htons((short) sockVoisinAccept.sin_port);
						//affichage
				printColorPlus(numero_noeud, "ADRESSE");printf("du voisin que tu accept est %s:%d\n", adrAcc, portAcc);
			 	printColorPlus(numero_noeud, "CREATION THREAD");printf("pour le noeud d'indice %d\n", numero_noeud, numVoisin-1);
       	//CREATION DU THREAD POUR LE CLIENT
			 	creationThread(&threadsAcc[numVoisin-1], argsAcc, AcceptationDuNoeud);    //creation du thread

    } //fin des accept


	
	
    sleep(5);

    //FERMETURE DE LA SOCKET CLIENTE CAR PLUS BESOIN
        //fermeture
    close(dSVoisinDemande);
    close(dSVoisinEntrant);
        //affichage
    printColorPlus(numero_noeud, "FERMETURE");printf("Je peux m'en aller !\n");


} // Fin du main


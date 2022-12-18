#include "fonctions.c"
#include <math.h>



/////////////////////////
//   PROGRAME NOEUDS   //
/////////////////////////
int main(int argc, char *argv[]) {

    //GESTION DES INFORMATIONS DONEES EN PARAMETRES
    if (argc != 5){
        printf("\n[UTILISATION] %s ip_serveur port_serveur port_noeud numero_noeud\n\n", argv[0]);
        exit(1);
    }

    char* adresseIP = argv[1];          	//adresse ip du serveur
    char* port_serveur = argv[2];       	//port du serveur
    char* port_noeud = argv[3];         	//port du noeud
    int numero_noeud = atoi(argv[4]);    	//numero du client


    printColorNoeud("\n************************************************\n------------------- NOEUD ", numero_noeud, " -------------------\n************************************************\n\n");
	printColor(numero_noeud);printf("\033[4mInforamtions données en paramètres :\033[0m\n");
	printf("\n       Adresse du serveur : %s\n       Port : %d", adresseIP, atoi(port_serveur));
	printf("\n       Port du noeud : %d", atoi(port_noeud));
	printf("\n       Indice du processus : %d\n\n", numero_noeud);
	

//A - CONSTRUCTION D’UN SOMMET

    //ETAPE 1 : CREATION DE LA SOCKET QUI DISCUTE AVEC SERVEUR
    int dSProcServ = creationSocket();
    
    printColor(numero_noeud);printf("Création de la socket pour serveur réussie !\n");
    printColor(numero_noeud);printf("Le descripteur du noeud est %d \n\n", dSProcServ);

  
    //ETAPE 2 : DESIGNATION DE LA SOCKET SERVEUR
    struct sockaddr_in sockServ = designationSocket(adresseIP, port_serveur);
    char adrServ[INET_ADDRSTRLEN];
    inet_ntop(AF_INET, &sockServ.sin_addr, adrServ, INET_ADDRSTRLEN);
    int portServ = htons((short) sockServ.sin_port);
    printColor(numero_noeud);printf("Designation de la socket du serveur réussi ! \n");
    printColor(numero_noeud);printf("Le serveur a donc pour adresse %s:%d\n\n", adrServ, portServ);


    //ETAPE 3 : DEMANDE DE CONNEXION AU SERVEUR
    connexion(dSProcServ, &sockServ);
    
    printColor(numero_noeud);printf("Connexion au serveur réussi !\n\n");


	
//B - CONSTRUCTION DE LA SOCKET DE RECEPTION : Creation d'une autre socket qui va jouer le rôle de serveur pour ses voisins qui demande une connexion

    //ETAPE 4 : GESTION DE LA SOCKET QUI JOUE LE RÔLE DE SERVEUR POUR LES AUTRES NOEUDS

		//a) creation de la socket
    int dSVoisinAttente = creationSocket();
    
    printColor(numero_noeud);printf("Création de la socket d'écoute réussi !\n");
    printColor(numero_noeud);printf("Le descripteur du noeud est %d \n\n", dSVoisinAttente);

    	//b) nommage de la socket
    struct sockaddr_in sockArete = nommageSocket(dSVoisinAttente, port_noeud);
    char adrArete[INET_ADDRSTRLEN];
    inet_ntop(AF_INET, &sockArete.sin_addr, adrArete, INET_ADDRSTRLEN);
    int portArete = htons((short) sockArete.sin_port);
    
    printColor(numero_noeud);printf("Informations du noeud (socket) de descripteur %d : %s:%i\n\n", dSVoisinAttente, adrArete, portArete);


    //ETAPE 5 : MISE SOUS ECOUTE DE LA SOCKET
    int nbMaxAttente = NOEUDS_MAX;
    ecouter(dSVoisinAttente, nbMaxAttente);
        //AFFICHAGE
    printColor(numero_noeud);printf("Mise en ecoute de la socket réussie !\n\n");

    //On vient de finir de mettre en ecoute une socket qui va avoir le rôle de serveur pour ses voisins qui demande une connexion


//C - ENVOI LES INFORMATIONS AU SERVEUR

    //ETAPE 6 : ENVOIE DES INFORMATIONS AU SERVEUR
        //informations du noeud
    struct infos_Graphe informations_proc;      		//declare la structure qu'on va envoyer au serveur
    informations_proc.numero = numero_noeud;        	//donne l'indice du processus
    informations_proc.descripteur = dSVoisinAttente;  		//donne le descripteur
    informations_proc.adrProc = sockArete;          	//donner l'adresse de la socket
        //envoie des informations
    sendCompletTCP(dSProcServ, &informations_proc, sizeof(struct infos_Graphe));
    
    printColor(numero_noeud);printf("Envoi des inforamtions réussi au serveur !\n");
    printColor(numero_noeud);printf("\033[4mEnvoie des informations suivantes :\033[0m\n");
    printf("\n       Adresse du processus : %s\n       Port : %d", adrArete, portArete);
    printf("\n       Numéro du noeud : %d\n       Descripteur de la socket du processus : %d\n\n\n", numero_noeud, dSVoisinAttente);


    //DEFINITION des variables de multiplexage
    fd_set tabScrut;
    fd_set tabScrutTmp;
	FD_ZERO(&tabScrut);					//initialisation à 0 des booléens de scrutation
	FD_SET(dSProcServ, &tabScrut);		//ajout de la socket qui discute avec le serveur
	FD_SET(dSVoisinAttente, &tabScrut);		//ajout de la socket de l'arrete 
	int maxDs = dSProcServ;				//nb de socket a scrutter

	int dSVoisinEntrant = 0;			//descripteur
	int dSVoisinDemande = 0;			//descripteur

	int nbVoisinsConnectes = 0;			//nb de voisins accepté
	int nbVoisinTotal = 1;
	int nbTotalNoeuds = 0;
	int nbVoisinDemande, nbVoisinAttente;		   
	
    while(nbVoisinsConnectes < nbVoisinTotal) {		//tant qu'on a pas accepté tous les voisins)) {
		
        tabScrutTmp = tabScrut;
		//demande de scruter le tableau pour maxDs sockets
        if (select(maxDs+1, &tabScrutTmp, NULL, NULL, NULL) == -1) {	
            printColor(numero_noeud);printf("Problème lors du select\n");
        }


		for (int df = 2; df < maxDs+1; df++) {		//on parcours le tableau de scrutation
			
			if (!FD_ISSET(df, &tabScrutTmp)) {	    //on cherche le descripteur qui a produit un evenement
                continue;
            }

            if (df == dSProcServ) {                                            //si un evenement se produit sur la socket serveur
				
				//RECEPTION du nombre de voisins
                struct nbVois nbVoisin;
                recvCompletTCP(dSProcServ, &nbVoisin, sizeof(struct nbVois));  //on reutilise la structure informations_proc pour la reception

				nbTotalNoeuds = nbVoisin.nbNoeuds;						   //nombre de noeuds total dans le graphe
                nbVoisinTotal = nbVoisin.nbVoisinTotal;					   //nombre de voisins total
                nbVoisinDemande = nbVoisin.nbVoisinDemande;				   //nombre de voisin a qui je dois demander une connexion
                nbVoisinAttente = nbVoisinTotal - nbVoisinDemande;		   //nombre de voisin que je dois attendre
            			
                printColor(numero_noeud);printf("Reception du nombre de voisin réussi ! \n");
                printf("	Nombre de voisin total : %d\n", nbVoisinTotal);
                if (nbVoisinDemande > 0) {
            	    printf("	Nombre de voisin auquels envoyer une demande de connexion : %d\n", nbVoisinDemande);
                }
				if (nbVoisinAttente > 0) {
            	    printf("	Nombre de voisin qu'on doit accepter : %d\n", nbVoisinAttente);
                }

				//RECEPTION de l'ordre
                int ordre;
                recvCompletTCP(dSProcServ, &ordre, sizeof(int));
                printf("	Priorité du sommet : %d\n\n", ordre);
                
            	printf("\n************************************************"); 
            	printf("\n************************************************\n\n\n"); 


				//ETAPE 8 : RECEPTIONS DES INFORMATIONS DES VOISINS QUE JE DOIS CONTACTER
					
					//a) données de tous les voisins auxquels je dois me connecter
                    //tableau des informations de tous mes voisins
				struct infos_Graphe* info_voisins = (struct infos_Graphe*) malloc(nbVoisinDemande * sizeof(struct infos_Graphe)); 

				if (nbVoisinDemande != 0){
						//b) parcours du nombre de voisin a qui je demande
	                for (int vois = 0; vois < nbVoisinDemande; vois++) {
	                	struct infos_Graphe info_voisin_courant;     	//structure du voisin courant
	                	recvCompletTCP(dSProcServ, &info_voisin_courant, sizeof(struct infos_Graphe));
	                	info_voisins[vois] = info_voisin_courant;			//on ajoute ces informations dans le tableau prevu a cet effet
	                	printColorPlus(numero_noeud, "RECEPTION");printf("-> je dois me connecter à %d\n", info_voisin_courant.numero);	
	                }
				
					
					//ETAPE 10 : DEMANDE DE CONNEXION AUX VOISINS
		            for (int v = 0; v < nbVoisinDemande; v++) {
						//a) Création de la socket qui discute avec le processus voisin
		                dSVoisinDemande = creationSocket();
	                    
	    	            printColorPlus(numero_noeud, "CREATION");printf("de la socket qui demande réussi !\n");
	    	            printColorPlus(numero_noeud, "DESCRIPTEUR");printf("du noeud créé est %d\n", dSVoisinDemande);
	    	        
	    	              	//b) designation de la socket du voisin
	    	            struct sockaddr_in sockVoisin = info_voisins[v].adrProc;	//recuperation des informations du voisin en fonction de son indice 
	    	            printColorPlus(numero_noeud, "DESIGNATION");printf("de la socket du voisin réussie\n");
	    	              
	    					//c) demande de connexion
	                        //connexion du descripteur qu'on vient de créer et la socket du voisin courant
	    	            connexion(dSVoisinDemande, &sockVoisin);	
	    	            printColorPlus(numero_noeud, "CONNEXION");printf("d'un voisin réussi !\n");
						info_voisins[v].descripteur = dSVoisinDemande;
						
	                    	//d) affichage
			            char adrDem[INET_ADDRSTRLEN];
	    		        inet_ntop(AF_INET, &sockVoisin.sin_addr, adrDem, INET_ADDRSTRLEN);
	    		        int portDem = htons((short) sockVoisin.sin_port); 
	                    
	    		        printColorPlus(numero_noeud, "ADRESSE");printf("du voisin a qui je demande est %s:%d\n", adrDem, portDem);
	    		        printColorPlus(numero_noeud, "DEMANDE");printf("de connexion au %d-ème voisin réussie !\n", v+1);
				        nbVoisinsConnectes++;					//on incrémente le nombre de voisins acceptés    
	                    
	                }
				}

					//c) Fermeture de la socket qui discute avec le serveur
				FD_CLR(dSProcServ, &tabScrut);
                maxDs = MAX(dSProcServ, dSVoisinDemande);
                close(dSProcServ);
				printColorPlus(numero_noeud, "FERMETURE");printf("Je me déconnecte du serveur !\n");

				//liberation de la mémoire
				free(info_voisins); 
				
            }
                
            else {	//je recoit une connexion d'un voisin
            	
				//ETAPE 12 : ACCEPTATION DU NOEUD DONC RECEPTION DES CONNEXIONS

				    //données de adresse
    			struct sockaddr_in sockVoisinAccept;          //on declare la socket du Noeud
    			socklen_t lgAdr;                              //taille de l'adresse
						
	                //a) acceptation
	            dSVoisinEntrant = accept(dSVoisinAttente, (struct sockaddr*)&sockVoisinAccept, &lgAdr);     //on accepte le Noeud qui demande
				
					//GESTION ERREUR
					if (dSVoisinEntrant == ERREUR) {
							perror("\n\n[ERREUR] lors de l'accept d'un voisin : ");
							close(dSProcServ);
							exit(1); //on arrête le programme
					}
							
					//affichage
	            printColorPlus(numero_noeud, "ACCEPTATION");printf("d'un voisin réussi !\n");
	
				//ETAPE 13 AJOUT DE LA NOUVELLE SOCKET DANS tabScrut
				printColorPlus(numero_noeud, "AJOUT");printf("de la nouvelle socket a tabScrut\n");
				FD_SET(dSVoisinEntrant, &tabScrut);		//on ajoute la socket acceptée dans les socket à scruter
				maxDs = MAX(maxDs, dSVoisinEntrant);	//on réajuste le max
				nbVoisinsConnectes++;					//on incrémente le nombre de voisins acceptés


        		/*
		        //RECEPTION DES INFORMATIONS DES VOISINS ENTRANT POUR PLUS TARD
		        int numero_Voisin;                                         // entier qui va etre le numero du noeud qui se connecte
		            //reception
		        recvCompletTCP(dSVois, &numero_Voisin, sizeof(int));       // reception de l'entier dans numero_Voisin
		
		        //AFFICHAGE
		        printColorPlus(numeroMoi, "RECEPTION");printf("du noeud %d qui est mon voisin\n", numero_Voisin);
		
		        args->VoisinCourant->numero = numero_Voisin;
				*/
            } //fin du else
            
		} //fin du for 
        
    } //fin du while
    

    //FERMETURE DE LA SOCKET CLIENTE QUI ECOUTE ET DES SOCKET QUI ACCEPTENT ET QUI SE CONNECTENT
    printColorPlus(numero_noeud, "FERMETURE");printf("Je peux m'en aller !\n");
    sleep(5);
    close(dSVoisinDemande);
    close(dSVoisinEntrant);

	return 0;
	
} //Fin du main
#include "fonctions.c"
#include <math.h>



void* Coloration(void* p){
	//recuperation des arguments du thread
    struct paramsColoration* args = (struct paramsColoration*) p;

	int numeroMoi = args->numero;					      //indice pour le thread
	int ordre = args->ordre;							  //ordre du processus courant
	int nbVoisins = args->nbVoisins;					  //ordre du processus courant
	int* couleurVoisins = args->couleurVoisins;			  //tableau des couleurs des voisins
    struct infos_Graphe *Voisins = args->VoisinsCourant;  //tableau de structure des informations des voisins
	
	printf("coucou je suis la %d::%d;;%d\n", numeroMoi, ordre, nbVoisins ); 
	printf("jai %d VOISINS\n", nbVoisins);
	//couleur du noeud au depart
	int couleur = 1;

	int i = 0;
	while (i < nbVoisins) {						//parcourt du tableau des voisins
		if (couleurVoisins[i] == couleur) {		//si c'est la meme couleurs on change de couleur
			couleur++;							//incrementation
			printf("j'incremente la couleur\n");
			i = 0;								//on passe i a 0 car fini pour cette couleur
		}
		i++;									//sinon on continue
	}

    printColorPlus(numeroMoi, "COLORE");printf("avec la couleur %d\n", couleur);

	//ENVOI A TOUS MES VOISINS MA COULEUR 

	//préparation à l'envoi du message
	struct messages message;
	message.requete = COULEUR;		//un de mes voisins s'est colorié
	message.numI = ordre;
	message.message = couleur;
	printf("message : %d::%d::%d\n", message.requete , message.numI, message.message);

	for (int i = 0; i < nbVoisins; i++) {
		printf("c'est le moemnt de partager que je suis la star, %d\n", Voisins[i].descripteur);
		//J'envoie à mes voisins <COULEUR, ordre_i, couleur_i> 
		int dSVoisin = Voisins[i].descripteur;
		printf("message : %d::%d::%d::%d\n", Voisins[i].descripteur, message.requete , message.numI, message.message);
		int s = sendCompletTCP(dSVoisin, &message, sizeof(struct messages), message.numI);
		    //GESTION DES ERREURS
			if (s == ERREUR) {
				printf("Je vais avoir une erreur sur lenvoie de ma couleur au noeud %d\n", message.numI);
				perror("\n[ERREUR] : Erreur lors de l'envoie du message ");
				pthread_exit(NULL);
				exit(1);
			}
			else if (s == FERMETURE) {
				printf("Je vais avoir mon amis qui sen va sans moi au noeud %d\n", message.numI);
				perror("\n[ERREUR] : Abandon de la socket principale dans le l'envoie");
				pthread_exit(NULL);
				exit(1); 
			}
		printColorPlus(numeroMoi, "ENVOIE");printf("de ma couleur %d au noeud %d (resultat : %d)\n", couleur, dSVoisin, s);
        
	}
	printColorPlus(numeroMoi, "ENVOIE");printf("de ma couleur %d a tout le monde\n", couleur);
    pthread_exit(NULL);
}


/*
void* ThreadEnvoiBroadcast(void* p) {
}
*/





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

    //On vient de finir de mettre en ecoute une socket qui va avoir le rôle de serveur pour ses voisins qui demandent une connexion


//C - ENVOI LES INFORMATIONS AU SERVEUR

    //ETAPE 6 : ENVOIE DES INFORMATIONS AU SERVEUR
        //informations du noeud
    struct infos_Graphe informations_noeud;      		//structure qu'on va envoyer au serveur
    informations_noeud.numero = numero_noeud;        	//indice du processus
    informations_noeud.descripteur = dSVoisinAttente;  	//le descripteur
    informations_noeud.adrProc = sockArete;          	//adresse de la socket
    
    int s = sendCompletTCP(dSProcServ, &informations_noeud, sizeof(struct infos_Graphe), numero_noeud);
			//GESTION DES ERREURS
		if (s == ERREUR) {
			printf("Je vais aovir un probleme sur lenvoie au serveur des infos au noeud %d\n", numero_noeud);
			perror("\n[ERREUR] : Erreur lors de l'envoie du message ");
			exit(1);
		}
		else if (s == FERMETURE) {
			printf("Mon ami le serveur va sen aller c'est trop triste au noeud %d\n", numero_noeud);
			perror("\n[ERREUR] : Abandon de la socket principale dans le l'envoie");
			exit(1); 
		}
    
    printColor(numero_noeud);printf("Envoi des inforamtions réussi au serveur !\n");
    printColor(numero_noeud);printf("\033[4mEnvoie des informations suivantes :\033[0m\n");
    printf("\n       Adresse du processus : %s\n       Port : %d", adrArete, portArete);
    printf("\n       Numéro du noeud : %d\n       Descripteur de la socket du processus : %d\n\n\n", numero_noeud, dSVoisinAttente);


    //DEFINITION des variables de multiplexage
    fd_set tabScrut;
    fd_set tabScrutTmp;
	FD_ZERO(&tabScrut);					//initialisation à 0 des booléens de scrutation
	FD_SET(dSProcServ, &tabScrut);		//ajout de la socket qui discute avec le serveur
	FD_SET(dSVoisinAttente, &tabScrut);	//ajout de la socket de l'arête 
	int maxDs = dSProcServ;				//nb de socket a scrutter

	int dSVoisinEntrant = 0;			//descripteur
	int dSVoisinDemande = 0;			//descripteur

	int nbVoisinTotal = 1;				//car on est connecté à au moins un sommet (graphe connexe)
	int nbTotalNoeuds = 0;
	int nbVoisinDemande, nbVoisinAttente;		   
	int nbVoisinsConnectes = 0;			//nb de voisins auquel on s'est connecté
	int nbVoisinsAcceptes = 0;			//nb de voisins que l'on a accepté

	int ordre;							//ordre de priorité du sommet
    struct infos_Graphe* info_voisins;
	
    while(nbVoisinsConnectes+nbVoisinsAcceptes < nbVoisinTotal) {		//tant qu'on a pas accepté tous les voisins)) {
        tabScrutTmp = tabScrut;
		//demande de scruter le tableau pour maxDs sockets
        if (select(maxDs+1, &tabScrutTmp, NULL, NULL, NULL) == -1) {	
            printColor(numero_noeud);printf("Problème lors du select\n");
        }


		for (int df = 2; df < maxDs+1; df++) {		//on parcours le tableau de scrutation
			
			if (!FD_ISSET(df, &tabScrutTmp)) {	    //on cherche le descripteur qui a produit un evenement
                continue;
            }

            else if (df == dSProcServ) {                                            //si un evenement se produit sur la socket serveur
				
				//RECEPTION du nombre de voisins
                struct nbVois nbVoisin;
                int s = recvCompletTCP(dSProcServ, &nbVoisin, sizeof(struct nbVois), numero_noeud);
						//GESTION DES ERREURS
					if (s == ERREUR) {
						printf("Je vais avoir un probleme sur la reception des info au voisin %d\n", numero_noeud);
						perror("\n[ERREUR] : Erreur lors de la reception du message ");
						exit(1);
					}
					else if (s == FERMETURE) {
						printf("Mon ami va s'en aller sur la reception des infos au voisin %d\n", numero_noeud);
						perror("\n[ERREUR] : Abandon de la socket principale dans le la reception");
						exit(1); 
					} 

				nbTotalNoeuds = nbVoisin.nbNoeuds;						   //nombre de noeuds total dans le graphe
                nbVoisinTotal = nbVoisin.nbVoisinTotal;					   //nombre de voisins total
                nbVoisinDemande = nbVoisin.nbVoisinDemande;				   //nombre de voisin a qui je dois demander une connexion
                nbVoisinAttente = nbVoisinTotal - nbVoisinDemande;		   //nombre de voisin que je dois attendre
            			
                printColor(numero_noeud);printf("Reception du nombre de voisin réussi ! (recv %d)\n", s);
                printf("	Nombre de voisin total : %d\n", nbVoisinTotal);
                if (nbVoisinDemande > 0) {
            	    printf("	Nombre de voisin%s auxquels se connecter : %d\n", nbVoisinDemande>1?"s":"", nbVoisinDemande);
                }
				if (nbVoisinAttente > 0) {
            	    printf("	Nombre de voisin%s que l'on doit accepter : %d\n", nbVoisinAttente>1?"s":"", nbVoisinAttente);
                }

				//RECEPTION de l'ordre de priorité du sommet
                int sa = recvCompletTCP(dSProcServ, &ordre, sizeof(int), numero_noeud);
						//GESTION DES ERREURS
					if (sa == ERREUR) {
						printf("Je vais avoir un probleme sur la reception des info au voisin %d\n", numero_noeud);
						perror("\n[ERREUR] : Erreur lors de la reception du message ");
						exit(1);
					}
					else if (sa == FERMETURE) {
						printf("Mon ami va s'en aller sur la reception des infos au voisin %d\n", numero_noeud);
						perror("\n[ERREUR] : Abandon de la socket principale dans le la reception");
						exit(1); 
					}
                printf("	Priorité du sommet : %d (recv %d)\n\n", ordre, sa);
                
            	printf("\n************************************************"); 
            	printf("\n************************************************\n\n\n"); 


				//ETAPE 8 : RECEPTIONS DES INFORMATIONS DES VOISINS QUE JE DOIS CONTACTER
					
					//a) données de tous les voisins auxquels je dois me connecter
                    //tableau des informations de tous mes voisins
				info_voisins = (struct infos_Graphe*) malloc(nbVoisinTotal * sizeof(struct infos_Graphe)); 

				if (nbVoisinDemande != 0) {
						//b) parcours du nombre de voisin a qui je demande
	                for (int vois = 0; vois < nbVoisinDemande; vois++) {
	                	struct infos_Graphe info_voisin_courant;     	//structure du voisin courant
	                	int s = recvCompletTCP(dSProcServ, &info_voisin_courant, sizeof(struct infos_Graphe), numero_noeud);
								//GESTION DES ERREURS
							if (s == ERREUR) {
								printf("Je vais avoir un probleme sur la reception des info au voisin %d\n", numero_noeud);
								perror("\n[ERREUR] : Erreur lors de la reception du message ");
								exit(1);
							}
							else if (s == FERMETURE) {
								printf("Mon ami va s'en aller sur la reception des infos au voisin %d\n", numero_noeud);
								perror("\n[ERREUR] : Abandon de la socket principale dans le la reception");
								exit(1); 
							}

	                	info_voisins[vois] = info_voisin_courant;			//on ajoute ces informations dans le tableau prevu a cet effet
	                	printColorPlus(numero_noeud, "RECEPTION");printf("-> je dois me connecter à %d (recv %d)\n", info_voisin_courant.numero, s);	
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

                        //Envoie de nos infos au voisin
                        int s = sendCompletTCP(dSVoisinDemande, &informations_noeud, sizeof(struct infos_Graphe), numero_noeud);
								//GESTION DES ERREURS
							if (s == ERREUR) {
								printf("Je vais avoir un probleme sur lenvoie des info au voisin %d\n", numero_noeud);
								perror("\n[ERREUR] : Erreur lors de l'envoie du message ");
								exit(1);
							}
							else if (s == FERMETURE) {
								printf("Mon ami va s'en aller sur lenvoie des infos au voisin %d\n", numero_noeud);
								perror("\n[ERREUR] : Abandon de la socket principale dans le l'envoie");
								exit(1); 
							}

						//Ajout de la nouvelle socket dans tabScrut
						FD_SET(dSVoisinDemande, &tabScrut);		//on ajoute la socket acceptée dans les socket à scruter
						maxDs = MAX(maxDs, dSVoisinDemande);	//on réajuste le max
	                }
				}

					//c) Fermeture de la socket qui discute avec le serveur
				FD_CLR(dSProcServ, &tabScrut);
				if (maxDs == dSProcServ){		//si ct le max on decremente sinon on a pas besoin
					maxDs--;
				}
                close(dSProcServ);
				printColorPlus(numero_noeud, "FERMETURE");printf("Je me déconnecte du serveur !\n");
				
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

                //Reception des infos entrant du voisin pour plus tard
                struct infos_Graphe info_voisin_courant;     	    //structure du voisin courant
                int s = recvCompletTCP(dSVoisinEntrant, &info_voisin_courant, sizeof(struct infos_Graphe), numero_noeud);
						//GESTION DES ERREURS
					if (s == ERREUR) {
						printf("Je vais avoir un probleme sur la reception des info au voisin %d\n", numero_noeud);
						perror("\n[ERREUR] : Erreur lors de la reception du message ");
						exit(1);
					}
					else if (s == FERMETURE) {
						printf("Mon ami va s'en aller sur la reception des infos au voisin %d\n", numero_noeud);
						perror("\n[ERREUR] : Abandon de la socket principale dans le la reception");
						exit(1); 
					}
                info_voisin_courant.descripteur = dSVoisinEntrant;								//on met a jour le descripteur du voisin
				printColorPlus(numero_noeud, "RECEPTION");printf("des infos du voisin %d descripteur%d (recv %d)\n",  info_voisin_courant.numero, info_voisin_courant.descripteur, s);
				info_voisins[nbVoisinDemande+nbVoisinsAcceptes] = info_voisin_courant;			//on ajoute les infos du voisin dans le tableau des noeuds qui se connectent a nous
	
				//ETAPE 13 AJOUT DE LA NOUVELLE SOCKET DANS tabScrut
				FD_SET(dSVoisinEntrant, &tabScrut);		//on ajoute la socket acceptée dans les socket à scruter
				maxDs = MAX(maxDs, dSVoisinEntrant);	//on réajuste le max
				printColorPlus(numero_noeud, "AJOUT");printf("de la nouvelle socket a tabScrut %d\n", dSVoisinEntrant);

				nbVoisinsAcceptes++;					//on incrémente le nombre de voisins acceptés

            } //fin du else
            
        printColor(numero_noeud);printf("voisins connectés : %d/%d\n", nbVoisinsAcceptes+nbVoisinsConnectes, nbVoisinTotal);
		} //fin du for 
        
    } //fin du while

sleep(5);		//ajout d'un sleep pour attendre que ils soit reelemnt tous connectés entre eux et on ne peut  pas faire de entrer clavier car il faudrait en faire pour autant de noeuds quon a

    ////////////////    
    //  PARTIE 2  //
    ////////////////
    
	int couleurMax = 1;			//couleur max utilisée jusque là
	int nbVoisinsColores = 0;	//nombre de voisins colorés
    int dernierFini = 0;        //quel est le dernier noeud à s'être colorié

	int* couleurVoisins = (int*)malloc(nbVoisinTotal * sizeof(int));   	//structure où l'on va stocker les couleurs de nos voisins
	for (int i = 0; i<nbVoisinTotal; i++) {							//initialisation
		couleurVoisins[i] = 0;
	}


	struct paramsColoration infos_Coloration;					//paramaetre pour le thread
	infos_Coloration.numero = numero_noeud;						//indice pour le thread
	infos_Coloration.ordre = ordre;								//ordre du processus courant
	infos_Coloration.nbVoisins = nbVoisinTotal;					//nombre de voisins
    printf("Je suis le noeud %d et j'ai un ordre de %d\n", numero_noeud, ordre);
    
	//je suis le premier il commence directement
    if (ordre == 1) {    
		printf("Ouiiiiiiiiiiiiiiii c'est moi %d\n", numero_noeud);    
		infos_Coloration.couleurVoisins = couleurVoisins;			//tableau des couleurs
		infos_Coloration.VoisinsCourant = info_voisins;   			//structure des informations du voisins
		for (int i=0; i<nbVoisinTotal; i++){
			printf("avant couleursVoisins %i : %d\n", i, couleurVoisins[i]);
		}
		pthread_t threadColoration = 0;

		int res_create = pthread_create(&threadColoration, NULL, Coloration, &infos_Coloration); //je me colorie
        	//GESTION ERREUR
        if (res_create == ERREUR){
            perror("[ERREUR] lors de la creation du thread de coloration : ");
            exit(1);
        }
		printColorPlus(numero_noeud, "CREATION THREAD\n");
        dernierFini = 1;
    }

    
	//Boucle pour le colorage, on sort lorsqu'on a colorié tous les noeuds
	while(dernierFini < nbTotalNoeuds) {
		//printf("dernierFini == nbTotalNoeuds = %d::%d::%d\n", dernierFini, nbTotalNoeuds, numero_noeud);

		//TABLEAU DE SCRUTATION EN MULTIPLEXAGE
        tabScrutTmp = tabScrut;
        int res = select(maxDs+1, &tabScrutTmp, NULL, NULL, NULL);
        if (res == ERREUR) {	
            printColor(numero_noeud);printf("Problème lors du select\n");
            exit(1);
        }

		for (int df = 2; df < maxDs+1; df++) {		//on parcours le tableau de scrutation
			
			if (FD_ISSET(df, &tabScrutTmp)) {		
				struct messages msg;
				int s = recvCompletTCP(df, &msg, sizeof(struct messages), numero_noeud);		//on recoit un message
						//GESTION DES ERREURS
					if (s == ERREUR) {
						printf("Je vais avoir un probleme sur la reception des info au voisin %d\n", numero_noeud);
						perror("\n[ERREUR] : Erreur lors de la reception du message ");
						FD_CLR(df, &tabScrut);
						exit(1);
					}
					else if (s == FERMETURE) {
						printf("Mon ami va s'en aller sur la reception des infos au voisin %d\n", numero_noeud);
						perror("\n[ERREUR] : Abandon de la socket principale dans le la reception");
						FD_CLR(df, &tabScrut);
						exit(1); 
					}
				int type_i = msg.requete;

				if ((type_i == COULEUR) || (type_i == BROADCAST)) {               //si le message est de type COULEUR ou BROADCAST
						//données du message
					int ordre_i = msg.numI;
					int couleur_i = msg.message;
					if (type_i == 1){
						printColorPlus(numero_noeud, "RECEPTION");printf("du message de type BROAD, d'ordre %d et de couleur %d (recv %d)\n", ordre_i, couleur_i, s);
					}
					else{
						printColorPlus(numero_noeud, "RECEPTION");printf("du message de type COL, d'ordre %d et de couleur %d (recv %d)\n",ordre_i, couleur_i, s);
					}
                    
					//si je n'étais pas au courant que ce noeud était colorié
					if (dernierFini < ordre_i) {

						dernierFini = ordre_i;                     			//pas besoin de max car on sait qu'on est < au dernier fini
						couleurMax = MAX(couleurMax, couleur_i);  			//on met à jour la couleur max utilisée jusque là
						
						if (type_i == COULEUR) {                   			//COULEUR signifie que le message vient d'un voisin
							couleurVoisins[nbVoisinsColores] = couleur_i;   //je met à jour mon tableau des couleurs
							nbVoisinsColores++;							    //incrémente le nombre de voisins colorés
							//on change le type de message seulement si c'est COULEUR
							msg.requete = BROADCAST;                   		//on défini le type du message en le modifiant en BRODCAST
						}

						//J'envoie à mes voisins <BROADCAST, ordre_i, couleur_i>
						for (int i = 0; i < nbVoisinTotal; i++) {
							int dSVoisin = info_voisins[i].descripteur;
							int s = sendCompletTCP(dSVoisin, &msg, sizeof(struct messages), numero_noeud);	//envoie le meme message en changeant le type
									//GESTION DES ERREURS
								if (s == ERREUR) {
									printf("Je vais a voir une erreur sur le brodcast %d\n", numero_noeud);
									perror("\n[ERREUR] : Erreur lors de l'envoie du message ");
									exit(1);
								}
								else if (s == FERMETURE) {
									printf("Mon ami va s'en aller sur le brodcast %d\n", numero_noeud);
									perror("\n[ERREUR] : Abandon de la socket principale dans le l'envoie");
									exit(1); 
								}
							printColorPlus(numero_noeud, "BROADCAST");printf("a numero %d de couleur %d d'ordre %d (send %d)\n", info_voisins[i].numero, msg.message, msg.numI, s);
						}
						printColorPlus(numero_noeud, "BROADCAST");printf("fait a tout le monde du noeud d'ordre %d\n", msg.numI);

						//si je suis le suivant je me colorie pcq le precedent l'a deja fait
						if (ordre_i+1 == ordre) {

                            printColorPlus(numero_noeud, "COLORATION");printf("c'est à moi de me colorier\n");

							infos_Coloration.couleurVoisins = couleurVoisins;	//tableau des couleurs
							infos_Coloration.VoisinsCourant = info_voisins;   			//structure des informations du voisins
							//for (int i = 0; i<nbVoisinTotal; i++) {							//initialisation
							//	printf("couleurs actuel %d\n", [i]);
							//}
							pthread_t threadColoration = 0;
							int res_create = pthread_create(&threadColoration, NULL, Coloration, &infos_Coloration); //je me colorie
							//GESTION DES ERREURS
							if (res_create == ERREUR){
								perror("[ERREUR] lors de la creation du thread de coloration : ");
								exit(1);
							}
							printColorPlus(numero_noeud, "CREATION THREAD\n");
						}
					} //fin du if (message pas reçu)
					else{
						printColorPlus(numero_noeud, "PAS DE BROADCAST");printf("car je suis déjà à jour avec %d\n", dernierFini);
					}
				} //fin du if (type message)
            } //fin du if (évenement)
		} //fin du for (scrutage)
	} //fin du while
	if (dernierFini == nbTotalNoeuds){
		printColorPlus(numero_noeud, "FINITOO");printf("on est arrivé\n");
	}



    printColorPlus(numero_noeud, "TERMINÉ");printf("Je sais que le graphe est %d-coloriable !\n",couleurMax);
    //FERMETURE DE LA SOCKET CLIENTE QUI ECOUTE ET DES SOCKET QUI ACCEPTENT ET QUI SE CONNECTENT
	sleep(10);
    printColorPlus(numero_noeud, "FERMETURE");printf("Je peux m'en aller !\n");

    
    close(dSVoisinDemande);
    close(dSVoisinEntrant);

    //liberation de la mémoire
    free(info_voisins); 

	return 0;
	
} //Fin du main
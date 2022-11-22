# Projet de Coloration de graphe HADDAD POINTEAU

**Lancement automatique**

*Lancer le serveur* : ``python3 serveur.py port_serveur fichier_graphe``

*Utiliser le programme noeuds.py pour lancer autant de noeuds qu'il y en a dans le graphe *: ``python3 noeuds.py ip_serveur port_serveur fichier_graphe``


**Exemple**

*lancement du serveur dans le terminal 1* : ``python3 serveur.py 1025 graphe/graphe_test.txt``

*lancement des noeuds dans le terminal 2* : ``python3 noeuds.py 127.0.0.1 1025 graphe/graphe_test.txt``
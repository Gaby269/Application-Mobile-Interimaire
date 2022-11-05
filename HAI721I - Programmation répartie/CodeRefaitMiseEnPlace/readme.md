# Anneau de processus

**Installation**

```mkdir obj; mkdir bin; make```

**Lancement**

*Lancer le serveur* : ``bin/serveur port_serveur nb_noeud_graphe``  
*Lancer autant de noeuds que spécifié*: ``bin/noeud ip_serveur port_serveur port_noeud_graphe``


**Disclaimer**

Le serveur ainsi que les sous-anneaux utilisent le multiplexage: ce n'est pas nécessaire. 

**Algo d'élection (déterminer qui sera le chef de la communauté de l'anneau) et de calcul de taille**  
var Stockage[]  
Boucle principale de Pi:  
--- **Si** il y a des messages dans stockage, et que je suis connecté à l'adresse de Pi+1:
------ Je les envoie tous  
--- **Si** je reçois une adresse:  
------ je m'y connecte  
------ j'envoie un message de type "je suis Pi, message n°1"  
--- **Si** je reçois un message de type "je suis Px, message n°y":  
------ **Si** je suis connecté au noeud suivant:  
---------- **Si** x < i (mon numéro), je ne renvoie pas: ce n'est pas le chef c'est sûr  
---------- **Si** x > i, c'est peut-être lui le chef, je renvoie "je suis Px, message n°y+1"  
---------- **Si** x == i, je viens de recevoir mon propre message: je suis le chef puisque mon message n'a pas été éliminé, y est la taille de l'anneau  
-------------- Je renvoie un message de type "la taille de l'anneau est y"  
------ **Sinon** (je ne suis pas connecté au noeud suivant):  
--------- Je stocke le message dans ma var Stockage  
--- **Si** je reçois un message de type "la taille de l'anneau est y", le message est parlant  
#programme qui nous sert a lancer le programme noeuds autant de fois que l'on a besoin

import os, sys

if (len(sys.argv) < 2):
  print(f"[UTILISATION] :\npython3 {sys.argv[0]} port_serveur fichier_graphe")
  exit(1)

#os.system("python3 cleaner.py")
  
path = sys.argv[2]

fichier = open(path, "r")
contenu = fichier.readline()

while (contenu[0] == 'c'):        # on saute les commentaires
    contenu = fichier.readline()
  
p, edge, noeuds, aretes = contenu.split() # on récupère la première ligne avec les informations du graphe
fichier.close()

print(f"{noeuds} noeuds, {aretes} arêtes")

# information sur le graphe
noeuds = int(noeuds)
aretes = int(aretes)

#information pour changer la couleur pour chaque noeuds
couleur = 0

# Information de connexions
ip_serveur = "0.0.0.0"
port_serveur = int(sys.argv[1])
print("IP serveur :",ip_serveur+":"+str(port_serveur))

# Lancement des noeuds
for i in range(1, noeuds):
    cmd = ["./bin/noeuds", ip_serveur, str(port_serveur), str(port_serveur+i), str(i), "&"]
    os.system(" ".join(cmd))

cmd = ["./bin/noeuds", ip_serveur, str(port_serveur), str(port_serveur+noeuds), str(noeuds)]
os.system(" ".join(cmd))
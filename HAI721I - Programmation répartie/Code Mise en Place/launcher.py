#programme qui nous sert a lancer le programme noeuds autant de fois que l'on a besoin

import os, sys

if (len(sys.argv) < 2):
  print(f"[UTILISATION] :\npython3 {sys.argv[0]} port_serveur fichier_graphe");
  exit(1);
  
path = sys.argv[2]
fichier = open(path, "r")
contenu = fichier.readline()

while (contenu[0] == 'c'): # on saute les commentaires
    contenu = fichier.readline()
  
p, edge, noeuds, arretes = contenu.split()

fichier.close()

print(noeuds, "noeuds")
print(arretes, "arretes")

# information sur le graphe
noeuds = int(noeuds)
arretes = int(arretes)

# Information de connexions
ip_serveur = "0.0.0.0"
port_serveur = int(sys.argv[1])
print("IP serveur :",ip_serveur+":"+str(port_serveur))

# Lancement du serveur
#

# Lancement des noeuds
for i in range(1, 1+noeuds):
    cmd = ["./bin/noeuds", ip_serveur, str(port_serveur), str(port_serveur+i), str(i), "&"]
    os.system(" ".join(cmd))

# bon grosso merdo voilà
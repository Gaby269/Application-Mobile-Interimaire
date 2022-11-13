#programme qui nous sert a lancer le programme noeuds autant de fois que l'on a besoin

import os

fichier = open("./graphe/graphe_test.txt", "r")
contenu = fichier.readline()
while (contenu[0] == 'c'): 
  contenu = fichier.readline()
p, edge, noeuds, arretes = contenu.split()

print(noeuds, "noeuds")
print(arretes, "arretes")

noeuds = int(noeuds)
arretes = int(arretes)

for i in range(noeuds):
    os.system("./noeud " + str(i) + " &")

# bon grosso merdo voilà
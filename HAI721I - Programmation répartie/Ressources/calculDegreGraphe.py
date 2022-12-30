import os, sys

if (len(sys.argv) != 2):
  print(f"[UTILISATION] :\npython3 {sys.argv[0]} nom_fichier")
  exit(1)

path = sys.argv[1]								
fichier = open(path, "r")						
contenu = fichier.readline()					
while (contenu[0] == 'c'):        				
    contenu = fichier.readline()				

p, edge, nbNoeuds, nbAretes = contenu.split() 	
print(f"{nbNoeuds} noeuds, {nbAretes} arêtes")	

nbNoeuds = int(nbNoeuds)		
nbAretes = int(nbAretes)

noeuds = [0 for _ in range(nbNoeuds)]
aretes = []

for i in range(nbAretes):
    line = fichier.readline()
    if line.strip() != "":
        e, noeud1, noeud2 = line.split()
        aretes.append([noeud1, noeud2])
fichier.close()		

# Calcul du degré de chaque noeud
for arete in aretes:
    noeuds[int(arete[0])-1] += 1
    noeuds[int(arete[1])-1] += 1

# Affichage du degré de chaque noeud
# for i in range(nbNoeuds):
#     print(f"Le noeud {i+1} a un degré de {noeuds[i]}")

# Degré maximum
print(f"Le degré maximum est {max(noeuds)}")

# degré moyen
print(f"Le degré moyen est {sum(noeuds)/nbNoeuds}")
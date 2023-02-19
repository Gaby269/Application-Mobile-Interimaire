import rdflib
from parser import parser
from parser import spo_formater
from parser import levenshtein_similarity
from parser import normalized_levenshtein_similarity
from parser import jaro_similarity
from ttl_printer import pttl

nbTests = 10

# #Charger les graphes
# g1 = rdflib.Graph()
# g2 = rdflib.Graph()
# #Parsing des fichiers
# g1.parse("source.ttl", format="turtle")
# g2.parse("target.ttl", format="turtle")

g1 = parser("source.ttl")
g2 = parser("target.ttl")

print("Parsing terminé !\n\n")

#tests
cpt = 0
for key, value in g1.items():
  # print(f"\n\nclef : {key}\nvaleur : {value}")
  pttl(key, value)
  cpt += 1
  if cpt > nbTests: break

print("Affichage terminé !\n\n")


#######################
# Test des similarite #
#######################

print(levenshtein_similarity("aacbfkzoefieifef", "ajejfejodadaef"))
print(normalized_levenshtein_similarity("aacbfkzoefieifef", "ajejfejodadaef"))



#####################################################
# Comparaison pour les fichiers qui sont identiques #
#####################################################

# Initialiser la liste des triplets
triplets = []


#Transformer en liste d triplet
liste_g1 = spo_formater(g1)
liste_g2 = spo_formater(g2)


cpt = 0
# Boucle sur toutes les entités du graphe source
for sujet, predicat, objet in liste_g1:
	  print(f"sujet : {sujet}\npredicat : {predicat}\nobjet : {objet}\n\n")
	  cpt += 1
	  if cpt > nbTests: break
	  # Trouver toutes les entités correspondantes dans le graphe cible
	  query = "SELECT ?predicat ?objet WHERE { <%sujet> ?predicat ?objet }"% (sujet)  #creation de la requete
	  results = liste_g2.query(query)  #application de la requete sur g2

# Afficher les résultats de la requête
for resultat in results:
    print(resultat)




	
for result in results:
# Ajouter un triplet owl:sameAs si une correspondance est trouvée
# ce sont donc des objets dont un prédicat (avec sujet correspondant) est identique dans g1 et g2
	triplets.append((objet, "owl:sameAs", result[0]))





	  
# Afficher les triplets
f = open("results.txt", "w", encoding='utf8')
print("Les triplets owl:sameAs sont :")
for triplet in triplets:
  #print(triplet,"\n")
  f.write(triplet)
  f.write("\n")

f.close()

alternative = g1 & g2  # liste de triplettes identiques
print(alternative)




"""

import rdflib
import difflib

def apply_similarity_measure(g1, g2, prop, measure):
    # Initialiser la liste des triplets
    triplets = []
    
    # Boucle sur toutes les entités du graphe source
    for s, p, o in g1:
        if p == prop:
            # Trouver toutes les entités correspondantes dans le graphe cible
            query = "SELECT ?o WHERE { ?s <%s> ?o }" % prop
            results = g2.query(query)
            for result in results:
                # Appliquer la mesure de similarité pour trouver les correspondances
                if measure == "Jaccard":
                    sim = difflib.SequenceMatcher(None, str(o), str(result[0])).ratio()
                elif measure == "Levenshtein":
                    sim = difflib.Levenshtein.ratio(str(o), str(result[0]))
                if sim > 0.8:
                    # Ajouter un triplet owl:sameAs si une correspondance est trouvée
                    triplets.append((s, "owl:sameAs", result[0]))
    return triplets




# Demander à l'utilisateur de choisir les propriétés à comparer
properties = input("Entrez les propriétés à comparer (séparées par des virgules) : ").split(",")

# Demander à l'utilisateur de choisir les mesures de similarité
measures = input("Entrez les mesures de similarité (séparées par des virgules) : ").split(",")

# Demander à l'utilisateur de choisir le seuil de similarité
threshold = float(input("Entrez le seuil de similarité : "))

# Initialiser la liste des triplets
triplets = []

# Boucle sur les propriétés et les mesures choisies par l'utilisateur
for prop in properties:
  for measure in measures:
    # Appliquer la mesure de similarité entre les valeurs de la propriété dans les deux graphes
    result = apply_similarity_measure(g1, g2, prop, measure)

    # Si le résultat est supérieur ou égal au seuil de similarité, ajouter un triplet owl:sameAs
    if result >= threshold:
      triplets.append((g1_entity, "owl:sameAs", g2_entity))

# Afficher les triplets
print("Les triplets owl:sameAs sont :")
for triplet in triplets:
  print(triplet)
"""

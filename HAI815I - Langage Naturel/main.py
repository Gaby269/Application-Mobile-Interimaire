import requests
import json
import os
from inferences import deduction
from parser import rechercheRESEAU, formaterDico


os.system("python inferences.py")
input("lancement ...")

# Demander à l'utilisateur de rentrer une phrase
mot1 = input("Entrez un premier mot : ").strip()
relation = input("Entrez une relation : ").strip()
mot2 = input("Entrez un second mot : ").strip()
print("\nLe phrase que vous avez entré est :", mot1, relation, mot2)

# Exploiter la phrase
phr = [mot1, relation, mot2]
#phr = phrase.split(" ")
print("\n\n", phr)

# Recherche des phr en totalité, et que les relations sortantes et entrante
rechercheRESEAU(phr, True, False, False, 0)

formaterDico(phr)

deduction(mot1, relation, mot2)

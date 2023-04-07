import requests
import json
import os
from inferences import deduction
from parser import rechercheDUMP, rechercheASK, formaterDico
from utilities import *


#os.system("python inferences.py")
#input("lancement ...")
# print(traductionToChiffre("1"))
# print(traductionToChiffre("est"))
# print(traductionToChiffre("peut-être"))
# print(traductionToRelation("9"))
# rechercheDUMP(["ailes"], overwrite=True)

# Demander à l'utilisateur de rentrer une phrase
mot1 = input("Entrez un premier mot : ").strip()
relation = input("Entrez une relation (verbe infinitif) : ").strip()
mot2 = input("Entrez un second mot : ").strip()
print("\nLa phrase que vous avez entrée est :", mot1, relation, mot2)

    
relation = traductionFrancaisToChiffre(relation)
phr = [mot1, relation, mot2]
deduction(mot1, relation, mot2)

# Recherche des phr en totalité, et que les relations sortantes et entrante
#rechercheDUMP([mot1, mot2], 0)

#formaterDico(phr)

#deduction(mot1, relation, mot2)

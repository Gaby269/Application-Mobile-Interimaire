# NETTOYAGE LE TERMINAL ET LES PORTS ET LANCEMENT DU SERVEUR
import os, sys

#GESTION DES PARAMETRES
if (len(sys.argv) != 3):
  print(f"[UTILISATION] :\npython3 {sys.argv[0]} nom_fichier port_serveur")
  exit(1)

	
# NETOYAGE DES PROCESSUS
try:
  os.system("python3 cleaner.py")								      #utilisation de cleaner pour fermer les ports
  print("Netoyage des processus effectué")						#afficahge
except:
  print("Processus non nétoyés (processus blocant)")


	
# COMPILATION
try: #essaie
  os.system("mkdir bin")
  os.system("mkdir obj")
  os.system("clear")
except:
  print("Dossiers prêts")

os.system("make")


# INFORMATIONS DE CONNEXION
fichier_graphe = sys.argv[1]
port_serveur = int(sys.argv[2])


# Lancement du serveur
cmd = ["./bin/serveur", str(port_serveur), fichier_graphe]		# lancement du serveur dans le terminal
os.system(" ".join(cmd))
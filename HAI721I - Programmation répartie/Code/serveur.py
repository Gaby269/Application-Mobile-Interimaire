import os, sys

if (len(sys.argv) < 2):
  print(f"[UTILISATION] :\npython3 {sys.argv[0]} port_serveur fichier_graphe")
  exit(1)

# Netoyage des processus
os.system("python3 cleaner.py")

# Compilation
os.system("mkdir bin")
os.system("mkdir obj")
os.system("make")

# Information de connexions
port_serveur = int(sys.argv[1])
fichier_graphe = sys.argv[2]

# Lancement du serveur
cmd = ["./bin/serveur", str(port_serveur), fichier_graphe]
os.system(" ".join(cmd))
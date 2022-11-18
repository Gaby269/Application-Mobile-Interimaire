import os
import time
from subprocess import Popen, PIPE

res = Popen(["ps", "aux"], stdout=PIPE)
res = ''.join(map(chr, (res.communicate())[0]))

for r in res.split('\n'):
    if (("bin/client" in r) or ("bin/serverMultiplex" in r)):
        num = r.split(' ')
        for n in num:
            if len(n) > 0 and n[0] in '0123456789': #si c'est un nombre
                os.system("kill " + n)
                break

print("Netoyage des processus effectué")
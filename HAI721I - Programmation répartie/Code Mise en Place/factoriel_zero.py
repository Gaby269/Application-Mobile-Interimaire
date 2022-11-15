n = int(input("n : "))
cpt = 0
i = 1
while (n//5**i) > 0:
    cpt += (n//5**i)
    i += 1
print(str(n)+"! à",cpt,"zéros")
########################################
#~ définitions
########################################

BIN=bin/noeuds  bin/serveur

SRCS0=noeuds.c
SRCS1=serveur.c
SRCS2=parseur.c
SRCS3=structures.c
SRCS4=fonctions.c

default: $(BIN)

########################################
#~ regles pour l'executable
########################################

obj/%.o: %.c
	gcc -Wall -Iinclude -c $< -o $@

bin/noeuds: $(SRCS0:%.c=obj/%.o)
	gcc -o $@ $+ -lpthread

bin/serveur: $(SRCS1:%.c=obj/%.o)
	gcc -o $@ $+ -lpthread

clean:
	rm -f $(BIN) obj/*.o *~

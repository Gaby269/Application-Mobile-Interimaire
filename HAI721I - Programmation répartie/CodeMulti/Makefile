########################################
#~ définitions
########################################

BIN=bin/noeuds  bin/serveur

SRCS3=structures.c
SRCS2=parseur.c
SRCS4=fonctions.c
SRCS0=noeuds.c
SRCS1=serveur.c

default: $(BIN)

########################################
#~ regles pour l'executable
########################################

obj/%.o: %.c
	gcc -Wall -Iinclude -c $< -o $@

bin/noeuds: $(SRCS0:%.c=obj/%.o)
	gcc -o $@ $+ -lpthread -lm

bin/serveur: $(SRCS1:%.c=obj/%.o)
	gcc -o $@ $+ -lpthread -lm

clean:
	rm -f $(BIN) obj/*.o *~

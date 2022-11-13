########################################
#~ définitions
########################################

BIN=bin/processus  bin/serveur

SRCS0=processus.c
SRCS1=serveur.c

default: $(BIN)

########################################
#~ regles pour l'executable
########################################

obj/%.o: %.c
	gcc -Wall -Iinclude -c $< -o $@

bin/processus: $(SRCS0:%.c=obj/%.o)
	gcc -o $@ $+

bin/serveur: $(SRCS1:%.c=obj/%.o)
	gcc -o $@ $+

clean:
	rm -f $(BIN) obj/*.o *~

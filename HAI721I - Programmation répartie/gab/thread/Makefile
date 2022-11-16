########################################
#~ définitions
########################################

BIN=bin/thread  bin/rdv  bin/serveur  bin/image

SRCS0=thread_simple.c
SRCS1=rdv_thread.c
SRCS2=serveur_thread.c
SRCS3=image_thread.c

default: $(BIN)

########################################
#~ regles pour l'executable
########################################

obj/%.o: %.c
	gcc -Wall -Iinclude -c $< -o $@

bin/thread: $(SRCS0:%.c=obj/%.o)
	gcc -o $@ $+ obj/calculC.o -lpthread

bin/rdv: $(SRCS1:%.c=obj/%.o)
	gcc -o $@ $+ obj/calculC.o -lpthread

bin/serveur: $(SRCS2:%.c=obj/%.o)
	gcc -o $@ $+ obj/calculC.o -lpthread

bin/image: $(SRCS3:%.c=obj/%.o)
	gcc -o $@ $+ obj/calculC.o -lpthread

clean:
	rm -f $(BIN) obj/*.o *~

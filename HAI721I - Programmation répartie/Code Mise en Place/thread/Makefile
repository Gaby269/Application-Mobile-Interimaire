########################################
#~ définitions
########################################

BIN=bin/thread

SRCS0=thread_simple.c

default: $(BIN)

########################################
#~ regles pour l'executable
########################################

obj/%.o: %.c
	gcc -Wall -Iinclude -c $< -o $@

bin/thread: $(SRCS0:%.c=obj/%.o)
	gcc -o $@ $+ obj/calculC.o -lpthread

clean:
	rm -f $(BIN) obj/*.o *~

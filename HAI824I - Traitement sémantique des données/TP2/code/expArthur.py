import rdflib
from parser import parser
from ttl_printer import pttl


g1 = parser("source.ttl")
g2 = parser("target.ttl")

print(g1[0])
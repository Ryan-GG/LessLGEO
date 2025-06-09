0 FILE test.part
0 Description: test.part
0 Name:
0 Author:
0 BFC CERTIFY CCW

line type, meta command, id1, id2, transformation matrix, x,y,z , x & y (half stud size), nothing
matters besides 10:4 which indicates the stud type i guess
0 PE_CONN 3 23 1.000000 0.000000 0.000000 0.000000 1.000000 0.000000 0.000000 0.000000 1.000000
-20.000000 0.000000 0.000000 2 2 3:1,0:4,3:1,0:4,10:4,0:4,3:1,0:4,3:1
0 NOFILE

so we can just do a map on the ids connect to other ids, then they have to match stud type to anti
stud
the cubees in the corners can be calc from ( origin + ( lateral * half stud ) ), and fuck the other
types

we will have two maps, one of id1<id2<stud(+),antistud(-)> & studType( normal, ring, pin,
whatever )<antiStud Type( normal, pin hole etc)
The we can say for all parts in thee model, get all the part connections, for every connection
intersecting, look up if the connections have
matching stud and antistud, and matching stud type and anti stud type

Going to use the rules design pattern to implement rules and filtering / matching

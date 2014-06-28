PKU\_distrystem
==============

Test procedure
--------------
Run naming;
Run s1;
Run client;
---
mkdir 1/1.1/1.1.1
mkdir 1/1.2
touch 1/1.2/a.txt
touch 1/1.1/1.1.1/b.txt
mkdir 2/2.1
touch 2/c.txt

write 2/c.txt -sdata "Test 1."
write 2/c.txt -sdata -p 5 -sdata "2"
write 2/c.txt -sdata -okk


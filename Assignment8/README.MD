Read Me

Family Tree Prolog Implementation


Overview :

This project illustrates the use of Prolog to model  family tree. First the basic facts were defined, the rules were used to infer more complex relationships.

The main purpose of this project is to learn how Prolog's logical inference system can be applied to represent and query knowledge i.e. family relationships for this project.


Files :

family_tree.pl : prolog file with facts and rules.

ReadMe.md : Documentation of the project. 


Setup :

Install SWI-Prolog from the official website

Load the family_tree.pl file in the Prolog console:

?- [family_tree].

Once the file is loaded, you can start querying.



Facts : 

Gender Facts

    Male: john, paul, mike, jake

    Female: mary, susan, lisa, anna

Parent Facts

    parent(john, paul)

    parent(mary, paul)

    parent(john, susan)

    parent(mary, susan)

    parent(paul, mike)

    parent(paul, lisa)

    parent(susan, jake)

    parent(susan, anna)


Rules :

Grandparent:
A grandparent is defined as someone who is a parent of a parent:
grandparent(X, Y) :- parent(X, Z), parent(Z, Y).

Sibling:
Two people are siblings if they share at least one parent:
sibling(X, Y) :- parent(P, X), parent(P, Y), X \= Y.

Cousin:
Two people are cousins if their parents are siblings:
cousin(X, Y) :- parent(P1, X), parent(P2, Y), sibling(P1, P2), X \= Y.

Child:
A child is someone whose parent is known:
child(X, Y) :- parent(Y, X).

Descendant:
A descendant is someone who is a child or further down the line:
descendant(X, Y) :- parent(Y, X).
descendant(X, Y) :- parent(Y, Z), descendant(X, Z).




Sample Queries and Expected Output :


1. Find all males in the family:

?- male(X).

X = john ;
X = paul ;
X = mike ;
X = jake.


2. Find all females in the family:

?- female(X).

X = mary ;
X = susan ;
X = lisa ;
X = anna.


3. Find all children of Paul:

?- child(X, paul).

X = mike ;
X = lisa.

4. Is Jake a sibling of Anna?

?- sibling(jake, anna).
true.


5. Who are the grandparents of Mike?
?- grandparent(X, mike).

X = john ;
X = mary.

Find all cousins of Mike:
?- cousin(X, mike).

X = susan ;
X = jake ;
X = anna.


7. Is Mary a grandparent of Lisa?
?- grandparent(mary, lisa).

true.

8. Find all descendants of John:
?- descendant(X, john).

X = paul ;
X = susan ;
X = mike ;
X = lisa ;
X = jake ;
X = anna.

9. Find all siblings of Susan:

?- sibling(susan, X).

X = paul.

10. Who are the children of Susan?
?- child(X, susan).

X = jake ;
X = anna.



Conclusion

This project demonstrated the use of Prolog's logical reasoning capabilities to model a family tree.

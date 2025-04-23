% Gender facts
male(john).
female(mary).
male(paul).
female(susan).
male(mike).
female(lisa).
male(jake).
female(anna).

% Parent facts
parent(john, paul).
parent(mary, paul).
parent(john, susan).
parent(mary, susan).
parent(paul, mike).
parent(paul, lisa).
parent(susan, jake).
parent(susan, anna).

% Rules
grandparent(X, Y) :- parent(X, Z), parent(Z, Y).

sibling(X, Y) :- parent(P, X), parent(P, Y), X \= Y.

cousin(X, Y) :- parent(P1, X), parent(P2, Y), sibling(P1, P2), X \= Y.

child(X, Y) :- parent(Y, X).

descendant(X, Y) :- parent(Y, X).
descendant(X, Y) :- parent(Y, Z), descendant(X, Z).
    

%%*************************************%%
%% Demo file illustrating some facts   %%
%% and rules in Prolog                 %%
%%*************************************%%

%%*************************************%%
%% FACTS                               %%
%%*************************************%%                        
speed(ford, 100).
speed(chevy, 105).
speed(dodge, 95).
speed(volvo, 80).

time(ford, 20).
time(chevy, 21).
time(dodge, 23).
time(volvo, 24).

%%*************************************%%
%% RULES                               %%
%%*************************************%%
distance(X,Y) :-
	speed(X, Speed),
	time(X,Time),
	Y is Speed * Time.
